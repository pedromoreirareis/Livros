package com.pedromoreirareisgmail.livros;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pedromoreirareisgmail.livros.databinding.ItensBinding;

import java.util.List;

public class Adapter extends ArrayAdapter<Livro> {

    public Adapter(@NonNull Context context, @NonNull List<Livro> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItensBinding mBinding;

        /**
         * Se não tiver uma View inflada então infla e guarda para reutilizar
         * Se tiver view inflada então reutiliza
         */
        if (convertView == null) {
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.itens, parent, false);
            convertView = mBinding.getRoot();
            convertView.setTag(mBinding);
        } else {
            mBinding = (ItensBinding) convertView.getTag();
        }

        // Pega a posição do objeto atual na lista de livro
        Livro itemAtual = getItem(position);


        // Faz referencia as View no Layout de itens
        TextView tvTitulo = mBinding.tvTitulo;
        TextView tvAutor = mBinding.tvAutor;

        // Atribui valor do objeto atual na view
        tvTitulo.setText(itemAtual != null ? itemAtual.getmTitulo() : null);

        tvAutor.setText(itemAtual != null ? itemAtual.getmAutor() : null);

        return convertView;
    }
}
