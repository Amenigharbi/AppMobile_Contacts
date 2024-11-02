package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ContactHelper extends SQLiteOpenHelper {
    public static final String TABLE_CONTACT = "Contact";
    public static final String COL_ID = "Id"; // Ajout de la colonne Id
    public static final String COL_PREN = "Prenom";
    public static final String COL_NOM = "Nom";
    public static final String COL_NUM = "Numero";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACT + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Déclaration de la clé primaire
            COL_PREN + " TEXT NOT NULL, " +
            COL_NOM + " TEXT NOT NULL, " +
            COL_NUM + " TEXT NOT NULL)";

    public ContactHelper(@Nullable Context context) {
        super(context, "mabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(sqLiteDatabase);
    }
}
