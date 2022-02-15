package com.example.charlynegocios;

import static com.example.charlynegocios.MainActivity.storage;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.charlynegocios.Util.CriaArquivo;
import com.example.charlynegocios.Util.RecyclerItemClickListener;
import com.example.charlynegocios.adapter.Adapter;
import com.example.charlynegocios.model.Produto;
import com.example.charlynegocios.repository.ProdutoRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UniversoNaturalListarProdutosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private List<Produto> produtos = new ArrayList<>();
    private List<Produto> produtos;
    private ImageButton btPNG, btPDF;
    private Adapter adapter;
    private ProdutoRepository produtoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universo_natural_listar_produtos);






        //codigo teste
        produtos = new ArrayList<Produto>();
        adapter= new Adapter(produtos, this);

        produtoRepository = new ProdutoRepository(getApplicationContext());




        //codigo antigo

        recyclerView = findViewById(R.id.recyclerViewProdutos);

        //linear
       RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);


        //grid layout
       /*GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1
        );*/

        recyclerView.setLayoutManager(layoutManager);
        //definir tamanho fixo
        recyclerView.setHasFixedSize(true);

        //definir um divisor de item
       /*DividerItemDecoration divisor = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        divisor.setDrawable(getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(divisor);*/

        //passar o adaptador
        recyclerView.setAdapter(adapter);

        btPNG = findViewById(R.id.buttonPNG);

        btPNG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvarArquivo("PNG", "Lista de Produtos");
            }
        });

        btPDF = findViewById(R.id.buttonPDF);

        btPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvarArquivo("PDF", "Lista de Produtos");
            }
        });





        pesquisarProduto(adapter);



        // adicionar evento de click
        /*
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                LinearLayout container = view.findViewById(R.id.container);
                                if (container.getAlpha() == 0) {
                                    container.setAlpha(1);
                                } else {
                                    container.setAlpha(0);
                                }

                                ImageButton edit, remove;

                                edit = findViewById(R.id.imageButtonEditAdapter);
                                remove = findViewById(R.id.imageButtonRemoveAdapter);

                                remove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(view.getContext(), "Removendo Item", Toast.LENGTH_LONG).show();
                                        removerProduto(produtos.get(position));
                                    }
                                });

                                edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(view.getContext(), "Editando Item", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), EditandoProdutosUNActivity.class);
                                        intent.putExtra("Produto", produtos.get(position));
                                        startActivity(intent);
                                        finish();
                                    }
                                });


                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        })
        );*/


    }





    private Bitmap fotoDoRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();

        Bitmap bitmapPronto = null;

        if (adapter != null) {
            //recebendo tamanho da lista
            int tamanhoDaLista = adapter.getItemCount();
            int altura = 0;
            int alturaVolatil = 0;
            final int tamanhoMaximoDoArquivo = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int tamanhoDoCache = tamanhoMaximoDoArquivo / 8;
            LruCache<String, Bitmap> bitmapCache = new LruCache<>(tamanhoDoCache);
            Paint paint = new Paint();

            for (int x = 0; x < tamanhoDaLista; x++) {

                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(x));
                adapter.onBindViewHolder(holder, x);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                //passando dimensões do arquivo
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());

                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();

                Bitmap cacheDoBitmap = holder.itemView.getDrawingCache();
                if (cacheDoBitmap != null) {
                    bitmapCache.put(String.valueOf(x), cacheDoBitmap);

                }

                altura += holder.itemView.getMeasuredHeight();


            }
            bitmapPronto = Bitmap.createBitmap(view.getMeasuredWidth(), altura, Bitmap.Config.ARGB_8888);

            Canvas pagina = new Canvas(bitmapPronto);
            pagina.drawColor(Color.BLACK);


            for (int x = 0; x < tamanhoDaLista; x++) {

                Bitmap bitmap = bitmapCache.get(String.valueOf(x));
                pagina.drawBitmap(bitmap, 0, alturaVolatil, paint);
                alturaVolatil += bitmap.getHeight();
                bitmap.recycle();

            }


        }


        return bitmapPronto;
    }

    private void SalvarArquivo(String tipo, String nomeArquivo) {
        adapter.setLoading(true);
        File pasta = new File(Environment.getExternalStorageDirectory() + "/Download");

        Bitmap bitmapRecycler = fotoDoRecyclerView(recyclerView);

        CriaArquivo arquivo = new CriaArquivo(pasta, getApplicationContext());
        if (tipo.equals("PNG")) {
            String criando = arquivo.SalvarPNG(bitmapRecycler, nomeArquivo);

            if (criando.equals("Sucesso")) {
                Toast.makeText(getApplicationContext(), "Arquivo " + tipo + " Criado com Sucesso", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), criando, Toast.LENGTH_LONG).show();
            }
        }

        if (tipo.equals("PDF")) {
            String criando = arquivo.SalvarPDF(bitmapRecycler, nomeArquivo);

            if (criando.equals("Sucesso")) {
                Toast.makeText(getApplicationContext(), "Arquivo " + tipo + " Criado com Sucesso", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), criando, Toast.LENGTH_LONG).show();
            }
        }
        adapter.setLoading(false);
    }

    private void pesquisarProduto(Adapter adapter) {

        Query query;

        query = MainActivity.myRef.child("Produto").orderByChild("produto");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.removeAll(produtos);
                for (DataSnapshot obj : snapshot.getChildren()) {
                    Produto produtoRecuperado = obj.getValue(Produto.class);
                    produtos.add(produtoRecuperado);
                    adapter.notifyDataSetChanged();
                    //Log.i("ProdutoDebug", produtos.toString());
                }
                Toast.makeText(getApplicationContext(),"Listando "+ produtos.toArray().length + " produtos", Toast.LENGTH_SHORT).show();

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






    @Override
    public void onBackPressed(){
      finish();
    }

}