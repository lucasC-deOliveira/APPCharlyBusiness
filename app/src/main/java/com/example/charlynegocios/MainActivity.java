package com.example.charlynegocios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {
    private Button buttonUniversoNatural, buttonCharlyImoveis;
    public static FirebaseDatabase dataBase;
    public static DatabaseReference myRef;
    public static FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iniciarFirebase();

        buttonUniversoNatural = findViewById(R.id.MainUniversoNaturalButton);
        buttonCharlyImoveis = findViewById(R.id.MainCharlyImoveisButton);

        buttonUniversoNatural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), UniversoNaturalActivity.class);
                startActivity(intent);
            }
        });


        buttonCharlyImoveis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), CharlyImoveisActivity.class);
                startActivity(intent);
            }
        });


    }

    private void iniciarFirebase(){
        dataBase = FirebaseDatabase.getInstance();
        myRef = dataBase.getReference();
    }
}