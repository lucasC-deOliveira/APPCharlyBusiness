package com.example.charlynegocios;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.charlynegocios.model.Produto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UniversoCadastrarProdutosActivity extends AppCompatActivity {
    private Button btFoto, btCadastrar;
    private Uri uriFoto;

    private ImageView imgFoto;
    private EditText textNome, textPreco;
    private StorageReference storageReference = MainActivity.storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universo_cadastrar_produtos);

        inicializarComponentes();


        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionaFoto();
            }
        });


        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProduto();
            }
        });
    }

    private void selecionaFoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0);

    }

    private void createProduto() {
        String nome = textNome.getText().toString().trim();
        String preco = textPreco.getText().toString().trim();
        preco = preco.replace(",", ".");
        preco = preco.replaceAll(" ", "");
        String UID = java.util.UUID.randomUUID().toString();

        if (nome.equals("") || preco.equals("")) {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Produto produto = new Produto(UID, nome, Double.parseDouble(preco), null);
                upload(produto);
                Toast.makeText(getApplicationContext(), "Produto Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();
                Log.i("Produto: ", produto.toString());
                finish();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (data != null) {
                uriFoto = data.getData();

                Bitmap bitmap;
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriFoto);
                    imgFoto.setImageBitmap(bitmap);
                    btFoto.setAlpha(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void inicializarComponentes() {
        btFoto = findViewById(R.id.buttonFotoCadasatroUniversoNatural);
        imgFoto = findViewById(R.id.imageFoto);
        textNome = findViewById(R.id.editTextNomeProdutoUniversoNaturalCadastro);
        textPreco = findViewById(R.id.editTextPrecoProdutoCadastroUniversoNatural);
        btCadastrar = findViewById(R.id.btCadastrarProdutosUniversoNatural);
    }

    private void upload(Produto produto) {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/produtos/" + filename);
        ref.putFile(uriFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        produto.setUriFoto(uri.toString());
                        MainActivity.myRef.child("Produto").child(produto.getUID()).setValue(produto);
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


}