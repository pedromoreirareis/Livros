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

        MyViewHolder holder;


        // Verifica se existe uma View Disponivel
        if (convertView == null) {

            // Infla uma nova View
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.itens, parent, false);
            convertView = mBinding.getRoot();

            holder = new MyViewHolder(convertView);

            // Deixa uma nova View criada para ser utilizada
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

                // Se existir, primeiro coloca a Imagem "Sem Imagem"
                holder.ivCapa.setImageResource(R.drawable.sem_imagem);

                // O holder urlLink recebe o link da imagem
                holder.urlLink = linkImagem;

                // Inicia o Downoad da imagem - Utilizando ASyncTask
                new DownloadImagemTask().execute(holder);
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

    /**
     * ASyncTask para download da imagens da capa do livro
     */
    private class DownloadImagemTask extends AsyncTask<MyViewHolder, Void, MyViewHolder> {


        @Override
        protected MyViewHolder doInBackground(MyViewHolder... url) {

            // Cria um novo holder e recebe
            // a url da imagem a ser baixada
            MyViewHolder holder = url[0];

            try {

                // A partir da url String cria um Objeto URL
                URL imagemURL = new URL(holder.urlLink);

                // Objeto bitmap -
                // BitmapFactory - Cria um objeto Bitmap, monta os Stream decodificado
                // decodeStream  - decodifica Stream, para colocar no Bitmap
                // openStream    - Abre uma conexao com a url e retorna os Stream para leitura
                holder.bitmap = BitmapFactory.decodeStream(imagemURL.openStream());

            } catch (IOException e) {

                Log.e(TAG, "Erro a baixar imagem", e);

                // Se tiver alguns erro o bitmap recebe null
                holder.bitmap = null;
            }


            return holder;
        }

        @Override
        protected void onPostExecute(MyViewHolder resultado) {

            // erifica se retornou diferente de null
            if (resultado.bitmap != null) {

                // Se sim coloca a imagem no ImageView
                resultado.ivCapa.setImageBitmap((Bitmap) resultado.bitmap);
            }
        }
    }

}
