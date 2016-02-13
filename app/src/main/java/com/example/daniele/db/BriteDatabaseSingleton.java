package com.example.daniele.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

public class BriteDatabaseSingleton {

    private static final Object LOCK = new Object();
    private static BriteDatabase db;

    public static BriteDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (db == null) {
                Context appContext = context.getApplicationContext();

                SqlBrite sqlBrite = SqlBrite.create();
                db = sqlBrite.wrapDatabaseHelper(getSqLiteOpenHelper(appContext));
            }
        }

        return db;
    }

    private static SQLiteOpenHelper getSqLiteOpenHelper(Context context) {
        return DbOpenHelper.newInstance(context);
    }

    private BriteDatabaseSingleton() {

    }
}
