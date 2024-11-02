package com.example.myapplication;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Affichage extends AppCompatActivity {

    private RecyclerView rv;
    private MyContactRecycleAdapter ad;
    private ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage); // Assurez-vous que cela correspond au nom de votre fichier XML

        rv = findViewById(R.id.rv);
        SearchView searchView = findViewById(R.id.search_view);

        // Récupérer les contacts de votre gestionnaire
        ContactManager manager = new ContactManager(Affichage.this);
        manager.ouvrir();
        contacts = manager.getAllContact();
        manager.fermer();

        ad = new MyContactRecycleAdapter(Affichage.this, contacts);
        rv.setAdapter(ad);
        rv.setLayoutManager(new GridLayoutManager(this, 1));

        // Écouteur pour le SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Pas besoin de gérer ici
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ad.getFilter().filter(newText); // Filtre les contacts en fonction de la recherche
                return true;
            }
        });
    }
}
