package com.example.daniele.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MigratedOpenHelper extends SQLiteOpenHelper {

    static final String MIGRATION_NAME_PATTERN = "\\d+_\\w+\\.sql";
    private static final SQLiteDatabase.CursorFactory DEFAULT_FACTORY = null;

    private final AssetManager assetManager;
    private final String migrationsDir;

    public static MigratedOpenHelper open(Context context, String migrationsDir, String name) {
        AssetManager assetManager = context.getAssets();
        int version = lastMigrationNumber(assetManager, migrationsDir);
        return new MigratedOpenHelper(context, name, version, assetManager, migrationsDir);
    }

    private static int lastMigrationNumber(AssetManager assetManager, String migrationsDir) {
        List<String> filenames = migrationsFilenames(assetManager, migrationsDir);

        if (filenames.isEmpty()) {
            throw MigrationLoadException.noMigrationsFound(migrationsDir);
        }

        Collections.sort(filenames, Collections.<String>reverseOrder());

        return migrationNumberFor(filenames.get(0));
    }

    private static boolean isMigration(String filename) {
        return filename.matches(MIGRATION_NAME_PATTERN);
    }

    private static int migrationNumberFor(String filename) {
        String migrationNumber = filename.split("_")[0];
        return Integer.parseInt(migrationNumber);
    }

    private static List<String> migrationsFilenames(AssetManager assetManager, String migrationsDir) {
        try {
            String[] candidates = assetManager.list(migrationsDir);
            List<String> filenames = new ArrayList<>(candidates.length);
            for (String candidate : candidates) {
                if (isMigration(candidate)) {
                    filenames.add(candidate);
                }
            }

            return filenames;
        } catch (IOException cause) {
            throw MigrationLoadException.failedToListMigrations(migrationsDir, cause);
        }
    }

    private MigratedOpenHelper(Context context, String name, int version, AssetManager assetManager, String migrationsDir) {
        super(context, name, DEFAULT_FACTORY, version);
        this.assetManager = assetManager;
        this.migrationsDir = migrationsDir;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        List<String> filenames = migrationsFilenames(assetManager, migrationsDir);
        Collections.sort(filenames);

        sqLiteDatabase.beginTransaction();

        for (String filename : filenames) {
            sqLiteDatabase.execSQL(getMigrationSql(filename));
        }

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        List<String> filenames = migrationsFilenames(assetManager, migrationsDir);
        Collections.sort(filenames);

        sqLiteDatabase.beginTransaction();

        for (String filename : filenames) {
            if (oldVersion >= migrationNumberFor(filename)) {
                continue;
            }

            sqLiteDatabase.execSQL(getMigrationSql(filename));
        }

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    private String getMigrationSql(String migration) {
        InputStream in;
        try {
            in = assetManager.open(String.format("%s/%s", migrationsDir, migration));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            StringBuilder migrationBuffer = new StringBuilder();
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                migrationBuffer.append(line);
            }
            return migrationBuffer.toString();
        } catch (IOException cause) {
            throw MigrationLoadException.unableToReadMigration(migration, cause);
        }
    }

    public static class MigrationLoadException extends RuntimeException {

        static MigrationLoadException failedToListMigrations(String path, IOException cause) {
            return new MigrationLoadException("Failed to list migrations at " + path, cause);
        }

        static MigrationLoadException noMigrationsFound(String path) {
            return new MigrationLoadException("No migrations found in the given path" + path);
        }

        static MigrationLoadException unableToReadMigration(String filename, IOException cause) {
            return new MigrationLoadException("Unable to read migration" + filename, cause);
        }

        public MigrationLoadException(String message, IOException e) {
            super(message, e);
        }

        public MigrationLoadException(String message) {
            super(message);
        }
    }
}
