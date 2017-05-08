package com.pedromoreirareisgmail.livros;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;


public class Utils {
    private static final String TAG = Utils.class.getName();
    private static final String URL_BASE = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String URL_PARAMETROS = "&projection=full&maxResults=40&orderBy=relevance";

    // Construtor
    public Utils() {
    }

    // Começa o processo de download
    public static List<Livro> comecarASyncTask(String urlString) {

        // Junta URL_BASE + conteudo digita, já formatado + parametros de pesquisa
        String urlCompleta = URL_BASE + urlString + URL_PARAMETROS;

        // Pega a urlString e cria a um Objeto URL
        URL urlRequisicao = createURL(urlCompleta);

        // String que recebera os dados retornado da pesquisa
        String dadosJSON = "";

        try {
            // Inicia a requisição
            dadosJSON = makeHttpRequest(urlRequisicao);
        } catch (IOException e) {
            Log.e(TAG, "Erro na solicitação de dados", e);
        }

        // Extrai os dados do Objeto JSON
        return Extrair.extrairJSON(dadosJSON);
    }

    private static URL createURL(String preUrl) {
        URL url = null;

        // Cria o Objeto URL
        try {
            url = new URL(preUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Erro ao criar a URL", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String dadosJSON = "";

        if (url == null) {
            return dadosJSON;
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {

            // Inicia a conexao com a internet
            connection = (HttpURLConnection) url.openConnection();

            // Paramentro tempo para conexao
            connection.setConnectTimeout(10000);

            // Tempo de leitura
            connection.setReadTimeout(10000);

            // Metodo de requisição
            connection.setRequestMethod("GET");

            // Conecta com internet
            connection.connect();

            // Verifica se a conexao com internet deu certo
            if (connection.getResponseCode() == 200) {

                // Recebe os dados em formato de inputStream (bytes)
                inputStream = connection.getInputStream();

                // Envia os dados em formato inputStream para ser transformado em strings
                dadosJSON = leiaInputStream(inputStream);
            } else {
                Log.e(TAG, "Erro na requisição de dados, Código:  " + connection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(TAG, "Erro ao fazer a requisição de dados ou converter os dados JSON", e);
        } finally {

            // Disconecta da internet
            if (connection != null) {
                connection.disconnect();
            }

            // Fecha a entrada de inputStream
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return dadosJSON;
    }

    private static String leiaInputStream(InputStream inputStream) throws IOException {

        // cria uma string que recebe novos dados sem criar uma nova instancia
        StringBuilder saida = new StringBuilder();

        // Se a inputStream não estiver nula
        if (inputStream != null) {

            // faz a leitura do inputStream e transforma de inputStream em string
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            // Coloca os dados lido em um buffer(cache) aumentando o tempo de resposta
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // pega uma linha no buffer
            String linha = reader.readLine();

            // enquanto tiver dados na linha
            while (linha != null) {

                // Pega a string da linha e coloca na stringBuilder
                saida.append(linha);

                // Pega uma nova linha no buffer
                linha = reader.readLine();
            }

        }
        return saida.toString();
    }
}
