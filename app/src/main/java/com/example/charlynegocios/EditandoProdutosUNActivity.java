package com.example.charlynegocios;

import static com.example.charlynegocios.MainActivity.storage;

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

import com.example.charlynegocios.adapter.Adapter;
import com.example.charlynegocios.model.Produto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class EditandoProdutosUNActivity extends AppCompatActivity {
    private Button btEditar;
    private Uri uriFoto;
    private ImageView imgFoto;
    private EditText textNome, textPreco;
    private StorageReference storageReference = MainActivity.storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editando_produtos_unactivity);

        Bundle dados = getIntent().getExtras();
        Produto produto = (Produto) dados.getSerializable("Produto");

        inicializarComponentes();

        textNome.setText(produto.getNome());
        textPreco.setText(String.valueOf(produto.getPreco()).replace(".",","));

        Picasso.get().load(produto.getUriFoto()).into(imgFoto);



        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionaFoto();
            }
        });


        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProduto(produto);
            }
        });


    }


    private void createProduto(Produto produto) {
        String nome = textNome.getText().toString().trim();
        String preco = textPreco.getText().toString().trim();
        preco = preco.replace(",", ".");
        String UID = java.util.UUID.randomUUID().toString();

        if (nome.equals("") || preco.equals("")) {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        } else {

            Produto produtoAtualizado = new Produto(UID, nome, Double.parseDouble(preco), null);
            atualizarProduto(produto, produtoAtualizado);
            Toast.makeText(getApplicationContext(), "Produto Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
            Log.i("Produto: ", produtoAtualizado.toString());
            finish();

        }


    }


    private void selecionaFoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0);

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

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void inicializarComponentes() {

        imgFoto = findViewById(R.id.imageFotoEditarProduto);
        textNome = findViewById(R.id.editTextNomeProdutoUniversoNaturalEditar);
        textPreco = findViewById(R.id.editTextPrecoProdutoEditarUniversoNatural);
        btEditar = findViewById(R.id.btEditarProdutosUniversoNatural);
    }


    private void atualizarProduto(Produto produto, Produto produtoAtualizado) {
        Query query;

        query = MainActivity.myRef.child("Produto").orderByChild("uid").equalTo(produto.getUID());


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot obj : snapshot.getChildren()) {
                    obj.getRef().removeValue();
                    if(imgFoto == null){
                    StorageReference gsReference = storage.getReferenceFromUrl(produto.getUriFoto());
                    gsReference.delete();}

                    upload(produto, produtoAtualizado);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void upload(Produto produto, Produto produtoAtualizado) {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/produtos/" + filename);
        if(uriFoto!=null){
            ref.putFile(uriFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            produtoAtualizado.setUriFoto(uri.toString());
                            MainActivity.myRef.child("Produto").child(produto.getUID()).setValue(produtoAtualizado);
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

        else {
            produtoAtualizado.setUriFoto(produto.getUriFoto());
            MainActivity.myRef.child("Produto").child(produto.getUID()).setValue(produtoAtualizado);

        }


    }
}