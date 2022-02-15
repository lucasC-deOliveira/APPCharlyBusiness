package com.example.charlynegocios.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.charlynegocios.EditandoProdutosUNActivity;
import com.example.charlynegocios.MainActivity;
import com.example.charlynegocios.R;
import com.example.charlynegocios.model.Produto;
import com.example.charlynegocios.repository.ProdutoRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.squareup.picasso.Picasso;


import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

//o recyclerView recicla as visualizações ao rolar a tela

//passar viewholder
//mesmo ao rolar o scroll
public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Produto> lista;
    private Context context;
    private Boolean isLoading = false;
    private ProdutoRepository produtoRepository;



    public Adapter(List<Produto> lista, Context context){
    this.lista = lista;
    this.context=context;
    produtoRepository = new ProdutoRepository(context);
    }


    public void setLoading(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    @NonNull
    @Override
    //metodo para criar as primeiras view
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //passar adapter de um layout de item para ser exibido
        //converter item xml para view
        View item_lista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_item_lista_produtos_melhorado, parent, false);


        return new MyViewHolder(item_lista);
    }

    //metodo que exibe os itens
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //definindo objeto para recuperar os dados
        Produto produto = lista.get(position);
        String preco = String.valueOf((produto.getPreco()));
        preco= preco.replace(".",",");

        if(isLoading){
            holder.container.setAlpha(0);
        }





        //definindo valores
       holder.nome.setText(produto.getNome());
       holder.preco.setText("R$ "+String.format(preco, "%.2f"));
       StorageReference ref= MainActivity.storage.getReference();
        //Glide.with(context).load(produto.getUriFoto()).into(holder.fotoProduto);
        Picasso.get().load(produto.getUriFoto()).resize(200,200).centerCrop().into(holder.fotoProduto);








    }

    //metodo que define quantidade de itens que serão exibidos
    @Override
    public int getItemCount() {
        return lista.size();
    }

    //inner class
    public class MyViewHolder extends RecyclerView.ViewHolder{
        //criar atributos que vao ser exibidos
        TextView nome, preco;
        ImageView fotoProduto;
        ImageButton edit, remove;
        LinearLayout container, pading;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //recuperar componentes de tela
            nome = itemView.findViewById(R.id.textNomeProduto3);
            preco = itemView.findViewById(R.id.textPrecoProduto3);
            fotoProduto = itemView.findViewById(R.id.imageProdutoAdapter3);
            edit = itemView.findViewById(R.id.imageButtonEditAdapter3);
            container = itemView.findViewById(R.id.container3);
            remove = itemView.findViewById(R.id.imageButtonRemoveAdapter3);
            pading = itemView.findViewById(R.id.pading);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Editando Item", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, EditandoProdutosUNActivity.class);
                    intent.putExtra("Produto", lista.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            remove.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    produtoRepository.removerProduto(lista.get(getAdapterPosition()));
                   // Toast.makeText(context, "Produto Removido", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }
    }
}
