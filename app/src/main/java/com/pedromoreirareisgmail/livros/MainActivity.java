package com.pedromoreirareisgmail.livros;

import android.app.LoaderManager;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pedromoreirareisgmail.livros.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Livro>> {


    private static final int LOADER_ID = 1;
    private ActivityMainBinding mBinding;
    private TextView mTvMensagem;
    private String mUrlRequisicao = null;
    private EditText mEtPesquisa;
    private Adapter mAdapter;
    private Boolean mPesquisou = false;

    // Click na Imagem
    private final View.OnClickListener clickPesquisa = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pesquisarLivro();
        }
    };

    // Click na ação search do teclado
    private TextView.OnEditorActionListener tecladoPesquisa = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                // Chama o metodo pesquisa livros
                pesquisarLivro();
                return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Esconde barra de progresso
        semProgresso();

        // faz referencia as views no Layout principal - container
        mEtPesquisa = mBinding.etPesquisa;
        mTvMensagem = mBinding.tvMsg;
        Button butPesquisa = mBinding.butPesquisa;
        ListView listView = mBinding.list;

        // se não tiver nada no ListView mostra esse TextView
        listView.setEmptyView(mTvMensagem);

        // Cria uma instancia do Adapter com uma lista de livro vazia
        mAdapter = new Adapter(this, new ArrayList<Livro>());

        // Coloca os dados do adapter no listview
        listView.setAdapter(mAdapter);

        // Ao clicar da imagem pesquisa chama evento de click
        butPesquisa.setOnClickListener(clickPesquisa);

        // Ao clicar icone pesquisa no teclado
        mEtPesquisa.setOnEditorActionListener(tecladoPesquisa);

        // Cria o Loader
        getLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);

        // Verifica se tem internet
        if (!temInternet()) {
            semIternet();
        } else {
            mTvMensagem.setVisibility(View.GONE);
        }
    }

    /**
     * Cria o Loader - ao criar lodaer mUrlRequisicao é null
     *
     * @return um Lista de livros
     */
    @Override
    public Loader<List<Livro>> onCreateLoader(int id, Bundle args) {
        return new LivroLoader(this, mUrlRequisicao);
    }

    /**
     * Após finalizar a pesquisa na internet
     *
     * @param livros resultados da pesquisa iniciada em onCreateLoader
     */
    @Override
    public void onLoadFinished(Loader<List<Livro>> loader, List<Livro> livros) {

        // Esconde barra de progresso
        View progresso = mBinding.pbProgresso;
        progresso.setVisibility(View.GONE);

        mAdapter.clear();
        if (livros != null && !livros.isEmpty()) {
            mAdapter.addAll(livros);
        } else {
            if (mPesquisou) {
                mTvMensagem.setText(R.string.msg_sem_retorno);
                mPesquisou = false;
            }
        }
    }

    /**
     * Limpa o adapter
     */
    @Override
    public void onLoaderReset(Loader<List<Livro>> loader) {
        mAdapter.clear();
    }

    /**
     * Faz o processo de pesquisa, captura a entrada do usuario e inicia o Loader
     */
    private void pesquisarLivro() {

        // Metodo de entrada - pega servico sistema - tipo de entrada
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // Esconde o teclado virtual, se curso estiver no TextView mEtPesquisa
        imm.hideSoftInputFromWindow(mEtPesquisa.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        // Limpa a mUrlRequisicao
        mUrlRequisicao = "";

        // Limpa utlPesquisa
        String urlPesquisa;

        // Pega texto edit, retira espaços inicio e fim e transforma em string
        urlPesquisa = mEtPesquisa.getText().toString().trim();

        // Se o edit (urlPesquisa) não estiver vazia ou não for nula
        if (!urlPesquisa.isEmpty()) {

            // Limpa o edit
            mEtPesquisa.setText("");

            // Tira o foco do edit
            mEtPesquisa.clearFocus();

            // Limpa o Adapater / ListView
            mAdapter.clear();

            // Onde tiver espaço(os) substitui por +
            urlPesquisa = urlPesquisa.replaceAll("\\s+", "+");

            // Verifica se tem internet
            if (temInternet()) {
                // Junta URL_BASE + conteudo digita, já formatado + parametros de pesquisa
                mUrlRequisicao = urlPesquisa;

                // reiniciar o loader para uma nova pesquisa
                getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);

                // Deixa o progressBar visivel
                View progresso = mBinding.pbProgresso;
                progresso.setVisibility(View.VISIBLE);

                // Deixa o mTvMensagem escondido
                mTvMensagem.setVisibility(View.GONE);

                // Foi passada uma url e iniciou pesquisa
                mPesquisou = true;
            } else {

                semIternet();
            }

        } else {
            Toast.makeText(MainActivity.this, R.string.digite, Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean temInternet() {

        // cria Gerenciador de estado da conexao com internet (Wi-fi, GPRS, UMTS, etc)
        // Obter servico do sistema do tipo CONNECTIVITY_SERVICE
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // cria NetworkiInfo - informações da rede - verifica se tem uma rede ativa
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Se internet estiver conectada e objeto não for nulo return true
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void semIternet() {
        // Deixa o progressBar visivel
        View progresso = mBinding.pbProgresso;
        progresso.setVisibility(View.GONE);

        // Deixa o mTvMensagem visivel e mostra que tem internet
        mTvMensagem.setText(R.string.msg_sem_internet);
        mTvMensagem.setVisibility(View.VISIBLE);
    }

    private void semProgresso() {
        // Esconde barra de progresso
        View progresso = mBinding.pbProgresso;
        progresso.setVisibility(View.GONE);
    }
}
