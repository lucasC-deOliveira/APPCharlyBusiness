package com.example.charlynegocios;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UniversoNaturalActivity extends AppCompatActivity {

    private Button btCadastrar, btListar, btRealizar, btPesuisarProdutos;
    private static final int CREATEPDF = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universo_natural);

        iniciarComponentes();

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UniversoCadastrarProdutosActivity.class);
                startActivity(intent);
            }
        });

        btListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UniversoNaturalListarProdutosActivity.class);
                startActivity(intent);
            }
        });

        btPesuisarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), PesquisarProdutosActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "To funcionando" ,Toast.LENGTH_LONG).show();
            }
        });

    }
        private void iniciarComponentes(){
            btCadastrar = findViewById(R.id.buttonUniversoNaturalCadastrarProdutos);
            btListar = findViewById(R.id.buttonUniversoNaturalListarProdutos);
            btRealizar = findViewById(R.id.buttonUniversoNaturalRealizarVenda);
            btPesuisarProdutos = findViewById(R.id.buttonUniversoNaturalPesquisarProdutos1);
        }

}
