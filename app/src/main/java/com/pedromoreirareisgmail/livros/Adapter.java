package com.pedromoreirareisgmail.livros;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedromoreirareisgmail.livros.databinding.ItensBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends ArrayAdapter<Livro> {
    private static final String TAG = Adapter.class.getSimpleName();
    private ItensBinding mBinding;

    public Adapter(@NonNull Context context, @NonNull List<Livro> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MyViewHolder holder;


        // Verifica se existe uma View Disponivel
        if (convertView == null) {

            // Infla uma nova View
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.itens, parent, false);

            // Coloca a View inflada pelo Binding no convertView
            convertView = mBinding.getRoot();

            // Atribui ao ViewHolder a gerencia de referencias dentro da View Inflada
            holder = new MyViewHolder(convertView);

            // Deixa uma nova View criada para ser utilizada e gerenciada pelo ViewHolder
            convertView.setTag(holder);

        } else {

            // Reutiliza uma View se tiver disponivel
            holder = (MyViewHolder) convertView.getTag();
        }

        // Pega a posição do objeto atual na lista de livro
        Livro itemAtual = getItem(position);

        // Verifica se existe a posição atual em Livro
        if (itemAtual != null) {

            // Atribui valor do objeto atual na view
            holder.tvTitulo.setText(itemAtual.getmTitulo());
            holder.tvAutor.setText(itemAtual.getmAutor());
            holder.tvPag.setText(itemAtual.getmPag());
            holder.tvTipo.setText(itemAtual.getmTipo());
            holder.tvInfo.setText(itemAtual.getmInfo());
            String linkImagem = (itemAtual.getmImagem());

            //Verifica se não exite um link
            if (linkImagem.equals("sem_imagem")) {

                // Não existindo coloca a imagem "Sem Imagem"
                holder.ivCapa.setImageResource(R.drawable.sem_imagem);

            } else {

                // Utiliza a Lib Picasso para fazer Download e Cache da Imagem
                Picasso.with(getContext()).load(linkImagem).into(holder.ivCapa);
            }
        }

        return convertView;
    }

    /**
     * Criando a classe ViewHolder
     */
    private class MyViewHolder {
        TextView tvTitulo;
        TextView tvAutor;
        TextView tvPag;
        TextView tvTipo;
        TextView tvInfo;
        ImageView ivCapa;
        private String urlLink;
        private Object bitmap;

        // Contrutor ViewHolder recebendo as referencia da View
        private MyViewHolder(View view) {
            tvTitulo = mBinding.tvTitulo;
            tvAutor = mBinding.tvAutor;
            tvPag = mBinding.tvPagina;
            tvTipo = mBinding.tvTipo;
            tvInfo = mBinding.tvInformacao;
            ivCapa = mBinding.ivCapa;
        }
    }
}
