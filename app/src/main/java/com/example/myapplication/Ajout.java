package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Ajout extends AppCompatActivity {

    private EditText ednom, edpren, pass;
    private Button btnaj, btnqte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajout);

        // Setting up insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        ednom = findViewById(R.id.NomAj);
        edpren = findViewById(R.id.PrenomAj);
        pass = findViewById(R.id.PassAj);
        btnaj = findViewById(R.id.ajt_btn);
        btnqte = findViewById(R.id.qte_ajt);

        // Set click listener to the quantity button
        btnqte.setOnClickListener(view -> finish());

        // Set click listener to the add button
        btnaj.setOnClickListener(view -> {
            String nom = ednom.getText().toString().trim();
            String pren = edpren.getText().toString().trim();
            String passw = pass.getText().toString().trim();

            // Validate inputs
            if (validateInputs(nom, pren, passw)) {
                // Create an instance of ContactManager to add to the database
                ContactManager manager = new ContactManager(Ajout.this);
                manager.ouvrir(); // Open the database
                manager.ajout(nom, pren, passw); // Add the contact to the database
                manager.fermer(); // Close the database after insertion

                // Show success message
                Toast.makeText(Ajout.this, "Contact ajouté avec succès", Toast.LENGTH_SHORT).show();

                // Optionally, you can clear the fields after submission
                clearFields();
            }
        });
    }

    // Input validation method
    private boolean validateInputs(String nom, String pren, String passw) {
        if (nom.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un nom", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pren.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un prénom", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passw.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un mot de passe", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Method to clear input fields
    private void clearFields() {
        ednom.setText("");
        edpren.setText("");
        pass.setText("");
    }
}
