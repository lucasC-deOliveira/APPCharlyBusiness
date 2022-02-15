package com.example.charlynegocios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.charlynegocios.adapter.Adapter;
import com.example.charlynegocios.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PesquisarProdutosActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutos;
    private List<Produto> produtos;
    private Adapter adapter;
    private EditText nomeProduto;
    private Button btPesquisar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_produtos);

        produtos = new ArrayList<>();

        adapter = new Adapter(produtos, getApplicationContext());

        nomeProduto = findViewById(R.id.EditTextPesquisarProduto);

        String nome = nomeProduto.getText().toString().trim();

        recyclerProdutos = findViewById(R.id.recyclerListarProdutos);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerProdutos.setLayoutManager(layoutManager);

        recyclerProdutos.setAdapter(adapter);

        btPesquisar = findViewById(R.id.btPesquisarProdutos);

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesquisarProduto(adapter, nome);
            }
        });






    }


    private void pesquisarProduto(Adapter adapter, String nome) {

        Query query;
        query = MainActivity.myRef.child("Produto");




        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.removeAll(produtos);
                for (DataSnapshot obj : snapshot.getChildren()) {
                    Produto produtoRecuperado = obj.getValue(Produto.class);

                    if(produtoRecuperado.getNome().equals(nome)){
                        produtos.add(produtoRecuperado);
                    }

                    if(nome.equals("")){
                        produtos.add(produtoRecuperado);
                    }

                    adapter.notifyDataSetChanged();
                    //Log.i("ProdutoDebug", produtos.toString());
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

}