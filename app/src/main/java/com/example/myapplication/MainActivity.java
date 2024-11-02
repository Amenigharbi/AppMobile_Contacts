package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText ednom, edmdp;
    private Button btnval, btnQte;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            startActivity(new Intent(MainActivity.this, Acceuil.class));
            finish();
        }

        ednom = findViewById(R.id.ednom_auth);
        edmdp = findViewById(R.id.edmp_auth);
        btnval = findViewById(R.id.Valider);
        btnQte = findViewById(R.id.btnQut);
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox);

        btnQte.setOnClickListener(view -> finish());

        btnval.setOnClickListener(view -> {
            String nom = ednom.getText().toString();
            String mdp = edmdp.getText().toString();
            if (nom.equalsIgnoreCase("Ameni") && mdp.equals("000")) {
                if (rememberMeCheckbox.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(IS_LOGGED_IN, true);
                    editor.apply();
                }

                Intent i = new Intent(MainActivity.this, Acceuil.class);
                i.putExtra("User", nom);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Valeur non valide", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
