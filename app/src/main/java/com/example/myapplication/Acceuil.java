package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Acceuil extends AppCompatActivity {
    private TextView tvusername;
    private Button btnajout, btnaff, btnLogout;
    public static ArrayList<Contact> data = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acceuil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        tvusername = findViewById(R.id.txt_acc);
        btnajout = findViewById(R.id.btnajout_acc);
        btnaff = findViewById(R.id.btnaff_acc);
        btnLogout = findViewById(R.id.btnLogout);

        btnajout.setOnClickListener(view -> {
            Intent i = new Intent(Acceuil.this, Ajout.class);
            startActivity(i);
        });

        btnaff.setOnClickListener(view -> {
            Intent i = new Intent(Acceuil.this, Affichage.class);
            startActivity(i);
        });

        btnLogout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_LOGGED_IN, false);
            editor.apply();

            Intent i = new Intent(Acceuil.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        Intent x = this.getIntent();
        Bundle b = x.getExtras();
        if (b != null) {
            String u = b.getString("User");
            tvusername.setText("Bienvenue " + u);
        }
    }
}
