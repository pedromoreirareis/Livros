package com.pedromoreirareisgmail.livros;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedromoreirareisgmail.livros.databinding.ItensBinding;

import java.io.IOException;
import java.net.URL;
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

        ViewHolder holder;


        // Verifica se existe uma View Disponivel
        if (convertView == null) {

            // Infla uma nova View
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.itens, parent, false);
            convertView = mBinding.getRoot();

            holder = new ViewHolder(convertView);

            // Deixa uma nova View criada para ser utilizada
            convertView.setTag(holder);
        } else {

            // Reutiliza uma View se tiver disponivel
            holder = (ViewHolder) convertView.getTag();
        }

        // Pega a posição do objeto atual na lista de livro
        Livro itemAtual = getItem(position);


        // Atribui valor do objeto atual na view
        holder.tvTitulo.setText(itemAtual != null ? itemAtual.getmTitulo() : null);
        holder.tvAutor.setText(itemAtual != null ? itemAtual.getmAutor() : null);
        holder.tvPag.setText(itemAtual != null ? itemAtual.getmPag() : null);
        holder.tvTipo.setText(itemAtual != null ? itemAtual.getmTipo() : null);
        holder.tvInfo.setText(itemAtual != null ? itemAtual.getmInfo() : null);

        // Salva o link da imagem em uma string
        String linkImagem = (itemAtual != null ? itemAtual.getmImagem() : null);


        //Verifica se exite um link
        if (linkImagem.equals("sem_imagem")) {

            holder.ivCapa.setImageResource(R.drawable.sem_imagem);
        } else {
            // Utiliza a biblioteca Picasso para baixar a imagem e colocar no ImagemView
            //Picasso.with(getContext()).load(linkImagem).into(holder.ivCapa);

            holder.ivCapa.setImageResource(R.drawable.sem_imagem);

            holder.urlLink = linkImagem;

            new DownloadImagemTask().execute(holder);

        }


        return convertView;
    }

    private class ViewHolder {
        TextView tvTitulo;
        TextView tvAutor;
        TextView tvPag;
        TextView tvTipo;
        TextView tvInfo;
        ImageView ivCapa;
        private String urlLink;
        private Object bitmap;


        private ViewHolder(View view) {
            tvTitulo = mBinding.tvTitulo;
            tvAutor = mBinding.tvAutor;
            tvPag = mBinding.tvPagina;
            tvTipo = mBinding.tvTipo;
            tvInfo = mBinding.tvInformacao;
            ivCapa = mBinding.ivCapa;
        }
    }

    private class DownloadImagemTask extends AsyncTask<ViewHolder, Void, ViewHolder> {


        @Override
        protected ViewHolder doInBackground(ViewHolder... url) {

            ViewHolder holder = url[0];

            try {
                URL imagemURL = new URL(holder.urlLink);
                holder.bitmap = BitmapFactory.decodeStream(imagemURL.openStream());

            } catch (IOException e) {

                Log.e(TAG, "Erro a baixar imagem", e);
                holder.bitmap = null;
            }


            return holder;
        }

        @Override
        protected void onPostExecute(ViewHolder resultado) {
            if (resultado.bitmap == null) {
                resultado.ivCapa.setImageResource(R.drawable.sem_imagem);
            } else {
                resultado.ivCapa.setImageBitmap((Bitmap) resultado.bitmap);
            }
        }
    }

}
