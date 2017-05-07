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
import java.util.Objects;

public class Adapter extends ArrayAdapter<Livro> {

    public Adapter(@NonNull Context context, @NonNull List<Livro> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItensBinding mBinding;

        // Verifica se existe uma View Disponivel
        if (convertView == null) {

            // Infla uma nova View
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.itens, parent, false);
            convertView = mBinding.getRoot();

            // Deixa uma nova View criada para ser utilizada
            convertView.setTag(mBinding);
        } else {

            // Reutiliza uma View se tiver disponivel
            mBinding = (ItensBinding) convertView.getTag();
        }

        // Pega a posição do objeto atual na lista de livro
        Livro itemAtual = getItem(position);


        // Faz referencia as View no Layout de itens
        TextView tvTitulo = mBinding.tvTitulo;
        TextView tvAutor = mBinding.tvAutor;
        TextView tvPag = mBinding.tvPagina;
        TextView tvTipo = mBinding.tvTipo;
        TextView tvInfo = mBinding.tvInformacao;
        ImageView ivCapa = mBinding.ivCapa;

        // Atribui valor do objeto atual na view
        tvTitulo.setText(itemAtual != null ? itemAtual.getmTitulo() : null);
        tvAutor.setText(itemAtual != null ? itemAtual.getmAutor() : null);
        tvPag.setText(itemAtual != null ? itemAtual.getmPag() : null);
        tvTipo.setText(itemAtual != null ? itemAtual.getmTipo() : null);
        tvInfo.setText(itemAtual != null ? itemAtual.getmInfo() : null);

        // Salva o link da imagem em uma string
        String linkImagem = (itemAtual != null ? itemAtual.getmImagem() : null);

        // Verifica se exite um link
        if (Objects.equals(linkImagem, "sem_imagem")) {

            ivCapa.setImageResource(R.drawable.sem_imagem);
        } else {
            // Utiliza a biblioteca Picasso para baixar a imagem e colocar no ImagemView
            Picasso.with(getContext()).load(linkImagem).into(ivCapa);
        }

        return convertView;
    }

}
