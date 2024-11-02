package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class ContactManager {
    private SQLiteDatabase db;
    private final ContactHelper helper;

    public ContactManager(Context con) {
        helper = new ContactHelper(con);
    }

    public void ouvrir() {
        if (db == null || !db.isOpen()) {
            db = helper.getWritableDatabase();
        }
    }

    public long ajout(String nom, String prenom, String numero) {
        long result = -1;
        ouvrir();

        ContentValues values = new ContentValues();
        values.put(ContactHelper.COL_NOM, nom);
        values.put(ContactHelper.COL_PREN, prenom);
        values.put(ContactHelper.COL_NUM, numero);

        try {
            result = db.insert(ContactHelper.TABLE_CONTACT, null, values);
            if (result == -1) {
                Log.d("ContactManager", "Failed to insert contact: " + nom);
            } else {
                Log.d("ContactManager", "Contact added successfully: " + nom);
            }
        } catch (Exception e) {
            Log.e("ContactManager", "Error adding contact: " + e.getMessage());
        }

        return result;
    }

    public ArrayList<Contact> getAllContact() {
        ArrayList<Contact> contacts = new ArrayList<>();
        ouvrir();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ContactHelper.TABLE_CONTACT, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(ContactHelper.COL_ID));
                @SuppressLint("Range") String nom = cursor.getString(cursor.getColumnIndex(ContactHelper.COL_NOM));
                @SuppressLint("Range") String prenom = cursor.getString(cursor.getColumnIndex(ContactHelper.COL_PREN));
                @SuppressLint("Range") String numero = cursor.getString(cursor.getColumnIndex(ContactHelper.COL_NUM));
                contacts.add(new Contact(id, prenom, nom, numero));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contacts;
    }

    // Modification de la méthode deleteContact pour utiliser l'ID
    public boolean deleteContact(int id) {
        ouvrir();
        int rowsDeleted = db.delete(ContactHelper.TABLE_CONTACT, ContactHelper.COL_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    // Modification de la méthode updateContact pour utiliser l'ID
    public int updateContact(int id, String nouveauNom, String nouveauPrenom, String nouveauNum) {
        ouvrir();
        ContentValues values = new ContentValues();
        values.put(ContactHelper.COL_NOM, nouveauNom);
        values.put(ContactHelper.COL_PREN, nouveauPrenom);
        values.put(ContactHelper.COL_NUM, nouveauNum);

        return db.update(ContactHelper.TABLE_CONTACT, values, ContactHelper.COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void fermer() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
