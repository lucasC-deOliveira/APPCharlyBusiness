package com.example.charlynegocios.repository;

import static com.example.charlynegocios.MainActivity.storage;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.charlynegocios.MainActivity;
import com.example.charlynegocios.adapter.Adapter;
import com.example.charlynegocios.model.Produto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    private Context contexto;


    public ProdutoRepository(Context contexto){
        this.contexto=contexto;
    }

    public void removerProduto(Produto produto) {
        Query query;
        query = MainActivity.myRef.child("Produto").orderByChild("produto");

        StorageReference storageRef = storage.getReference();


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot obj : snapshot.getChildren()) {
                    Produto produtoRecuperado = obj.getValue(Produto.class);
                    if (produtoRecuperado.getUID() == produto.getUID()) {
                        obj.getRef().removeValue();

                        StorageReference gsReference = storage.getReferenceFromUrl(produto.getUriFoto());
                        gsReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(contexto, "Produto Removido Com Sucesso!", Toast.LENGTH_LONG).show();
                            }
                        });




                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
