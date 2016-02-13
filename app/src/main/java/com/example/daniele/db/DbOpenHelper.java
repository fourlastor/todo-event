package com.example.daniele.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSource;
import okio.Okio;

public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String TODO_DB = "todo.db";
    public static final int VERSION = 1;

    private final AssetManager assetManager;

    public static DbOpenHelper newInstance(Context context) {
        AssetManager assetManager = context.getAssets();

        return new DbOpenHelper(context, assetManager);
    }

    private DbOpenHelper(Context context, AssetManager assetManager) {
        super(context, TODO_DB, null, VERSION);
        this.assetManager = assetManager;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createEventTable());
    }

    private String createEventTable() {
        return getMigration("1_create_event_table.sql");
    }

    private String getMigration(String migration) {
        InputStream in;
        try {
            in = assetManager.open("migrations/" + migration);
            BufferedSource migrationSource = Okio.buffer(Okio.source(in));
            return migrationSource.readUtf8();
        } catch (IOException e) {
            throw new MigrationLoadException(migration, e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private class MigrationLoadException extends RuntimeException {
        public MigrationLoadException(String migration, IOException e) {
            super("Unable to load migration" + migration, e);
        }
    }
}
