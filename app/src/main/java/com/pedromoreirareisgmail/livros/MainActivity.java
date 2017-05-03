package com.pedromoreirareisgmail.livros;

import android.app.LoaderManager;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.pedromoreirareisgmail.livros.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Livro>> {

    private static final String URL_BASE = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final int LOADER_ID = 1;
    private String urlRequisicao = null;
    private EditText metPesquisa;
    private Adapter mAdapter;

    private View.OnClickListener clickPesquisa = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String urlParametros = "&projection=full&maxResults=40&orderBy=relevance";

            String urlPesquisa = metPesquisa.getText().toString().trim().replaceAll("\\s+", "+");

            urlRequisicao = URL_BASE + urlPesquisa + urlParametros;

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, MainActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // faz referencia as views no Layout principal - container
        metPesquisa = mBinding.etPesquisa;
        ImageView mivPesquisa = mBinding.ivPesquisa;
        ListView listView = mBinding.list;

        // Cria uma instancia do Adapter com uma lista de livro vazia
        mAdapter = new Adapter(this, new ArrayList<Livro>());
        listView.setAdapter(mAdapter);


        mivPesquisa.setOnClickListener(clickPesquisa);
    }

    @Override
    public Loader<List<Livro>> onCreateLoader(int id, Bundle args) {
        return new LivroLoader(this, urlRequisicao);
    }

    @Override
    public void onLoadFinished(Loader<List<Livro>> loader, List<Livro> livros) {
        mAdapter.clear();
        if (livros != null && !livros.isEmpty()) {
            mAdapter.addAll(livros);

        }

    }

    @Override
    public void onLoaderReset(Loader<List<Livro>> loader) {
        mAdapter.clear();
    }
}
