package com.pedromoreirareisgmail.livros;

import android.app.LoaderManager;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pedromoreirareisgmail.livros.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Livro>> {

    private static final String URL_BASE = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String URL_PARAMETROS = "&projection=full&maxResults=40&orderBy=relevance";
    private static final int LOADER_ID = 1;
    private String mUrlRequisicao = "";
    private EditText metPesquisa;
    private final View.OnClickListener clickPesquisa = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pesquisarLivro();
        }
    };
    private Adapter mAdapter;

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

        // Ao clicar da imagem pesquisa chama evento de click
        mivPesquisa.setOnClickListener(clickPesquisa);
        metPesquisa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    pesquisarLivro();
                    return true;
                }

                return false;
            }
        });

        getLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);

    }


    @Override
    public Loader<List<Livro>> onCreateLoader(int id, Bundle args) {
        return new LivroLoader(this, mUrlRequisicao);
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

    private void pesquisarLivro() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(metPesquisa.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        mUrlRequisicao = "";

        //urlPesquisa = metPesquisa.getText().toString().trim().replaceAll("\\s+", "+");

        String urlPesquisa = "";
        // Pega o texto do edit, retira espaço no inicio e fim, se tiver
        // Substitui "\\s+" -espaço ou espaços em branco- pelo sina "+"
        // que é o solicitado pela API
        urlPesquisa = metPesquisa.getText().toString().trim();
        metPesquisa.setText("");
        metPesquisa.clearFocus();

        if (urlPesquisa != null & !urlPesquisa.isEmpty()) {
            urlPesquisa = urlPesquisa.replaceAll("\\s+", "+");

            // Junta a url base de requisição
            // o conteudo a ser pesquisado
            // os paramentro de pesquisa em uma String
            mUrlRequisicao = URL_BASE + urlPesquisa + URL_PARAMETROS;

            // Prepara o carregador para iniciar
            // re-conectar ao Loader ja criado no caso alteração da Activity
            // Inicializa o Loader Chmando onCreateLoader
            getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);

        } else {
            Toast.makeText(MainActivity.this, "Digite uma pesquisa.", Toast.LENGTH_SHORT).show();
        }
    }

}
