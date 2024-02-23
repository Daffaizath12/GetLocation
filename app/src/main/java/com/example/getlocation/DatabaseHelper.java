package com.example.getlocation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Users.db";
    public static final int DATABASE_VERSION = 2; // Ganti versi database jika melakukan perubahan skema

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USER_TABLE = "CREATE TABLE users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "address TEXT, "
                + "phone TEXT, "
                + "email TEXT);"; // Menambahkan kolom baru untuk alamat, nomor telepon, dan email

        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Jika Anda ingin menambahkan perubahan skema di masa depan, Anda bisa melakukan itu di sini.
        // Namun, untuk sekarang, kita akan melakukan drop dan re-create tabel jika ada upgrade.
        if (oldVersion < 2) {
            // Versi 2 menambahkan kolom baru
            db.execSQL("ALTER TABLE users ADD COLUMN address TEXT");
            db.execSQL("ALTER TABLE users ADD COLUMN phone TEXT");
            db.execSQL("ALTER TABLE users ADD COLUMN email TEXT");
        } else {
            // Untuk versi lainnya, kita akan melakukan drop dan re-create tabel
            String SQL_DELETE_USER_TABLE = "DROP TABLE IF EXISTS users";
            db.execSQL(SQL_DELETE_USER_TABLE);
            onCreate(db);
        }
    }
}

