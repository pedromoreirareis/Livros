package com.pedromoreirareisgmail.livros;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class LivroLoader extends AsyncTaskLoader<List<Livro>> {

    private String mUrl;

    public LivroLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public List<Livro> loadInBackground() {

        if(mUrl == null ){
            return null;
        }
        return Utils.ComecarASyncTask(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
