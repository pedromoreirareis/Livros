package com.pedromoreirareisgmail.livros;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;


public class LivroLoader extends AsyncTaskLoader<List<Livro>> {

    private String mUrl;

    public LivroLoader(Context context, String url) {
        super(context);
        Log.v("LivroLoader: ", " construtor");
        mUrl = url;
    }

    // Inicia o Loader
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // Iniciar o download se tiver uma url valida
    @Override
    public List<Livro> loadInBackground() {

        // Se a url for nula
        if (mUrl == null) {
            return null;
        }

        // Se tiver uma url válida
        return Utils.comecarASyncTask(mUrl);
    }


}
