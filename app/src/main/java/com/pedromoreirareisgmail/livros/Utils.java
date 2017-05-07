package com.pedromoreirareisgmail.livros;


import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class Utils {
    private static final String TAG = Utils.class.getName();

    // Construtor
    public Utils() {
    }

    // Começa o processo de download
    public static List<Livro> ComecarASyncTask(String urlString) {

        // Pega a urlString e cria a um Objeto URL
        URL urlRequisicao = createURL(urlString);

        // String que recebera os dados retornado da pesquisa
        String dadosJSON = "";

        try {
            // Inicia a requisição
            dadosJSON = makeHttpRequest(urlRequisicao);
        } catch (IOException e) {
            Log.e(TAG, "Erro na solicitação de dados", e);
        }

        // Extrai os dados do Objeto JSON
        return extrairJSON(dadosJSON);
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
            connection.setConnectTimeout(5000);

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

    /**
     * Recebe uma string em formato JSON e faz a extração dos dados solicitados
     *
     * @param dadosJSON string ainda em formato JSON
     * @return uma lista com objetos composto com informações dos livros
     */
    private static List<Livro> extrairJSON(String dadosJSON) {

        if (TextUtils.isEmpty(dadosJSON)) {
            return null;
        }

        // Cria uma lista de livros vazia
        List<Livro> livros = new ArrayList<>();

        try {

            // Pega os dados JSON  em formato String
            // e coloca em um objeto para tratamento
            JSONObject root = new JSONObject(dadosJSON);

            // Verifica a quantidade de itens que tem a resposta JSON
            // Se for zero, retorna a lista vazia
            if (root.getInt("totalItems") == 0) {
                return livros;
            }

            // No objeto JSON, Verifica o Array principal, "items"
            JSONArray arrayItems = root.getJSONArray("items");

            // Dentro do Array "items", verifica todos os objetos pertencente
            // a esse a Array para capturar os dados


            for (int i = 0; i < arrayItems.length(); i++) {

                // Dentro do Array "Items" passa por todos os objetos "raiz"
                // nesse objeto é que iremos capturar os dados
                JSONObject livroAtual = arrayItems.getJSONObject(i);

                // Aqui encontraremos a informações sobre o livro
                JSONObject volumeInfo = livroAtual.getJSONObject("volumeInfo");

                // Inicializando as variáveis
                String title;
                String authors;
                String pageCount;
                String printType;
                String thumbnail;
                String textSnippet;

                // Titulo do livro ou resvita
                if (volumeInfo.has("title")) {
                    title = volumeInfo.getString("title");
                } else {
                    title = "Titulo não disponível...";
                }

                // Esse Array contem o autor ou autores do livro ou revista
                if (volumeInfo.has("authors")) {
                    JSONArray arrayAuthors = volumeInfo.getJSONArray("authors");

                    // Formatar os dados do Array de autor(es) separando os autores com virgula
                    authors = formatarAutor(arrayAuthors);
                } else {
                    authors = "Autor não identificado...";
                }

                // Quantidade de paginas
                if (volumeInfo.has("pageCount")) {
                    pageCount = volumeInfo.getString("pageCount") + " páginas";
                } else {
                    pageCount = " --";
                }

                // Indentifica se é um livro ou uma revista
                if (volumeInfo.has("printType")) {
                    printType = formatarTipo(volumeInfo.getString("printType"));
                } else {
                    printType = " --";
                }

                if (volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    if (imageLinks.has("thumbnail")) {
                        thumbnail = imageLinks.getString("thumbnail");
                    } else {
                        thumbnail = "sem_imagem";
                    }
                } else {
                    thumbnail = "sem_imagem";
                }

                // Pequeno texto com informação do livro ou revista
                if (livroAtual.has("searchInfo")) {
                    JSONObject searchInfo = livroAtual.getJSONObject("searchInfo");
                    if (searchInfo.has("textSnippet")) {
                        textSnippet = Html.fromHtml(searchInfo.getString("textSnippet")).toString();
                    } else {
                        textSnippet = "Não há descrição disponível...";
                    }
                } else {
                    textSnippet = "Não há descrição disponível...";
                }

                livros.add(new Livro(title, authors, thumbnail, printType, pageCount, textSnippet));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Erro ao capturar dados JSON ", e);
        }

        return livros;
    }

    /**
     * Formata a lista de autores, separando o nomes por virgula
     *
     * @param arrayAutor lista de autores ainda en um array
     * @return string com nomes dos autoures
     * @throws JSONException se der algum erro faz o tratamento no metodo extrairJSON
     */
    private static String formatarAutor(JSONArray arrayAutor) throws JSONException {

        String listaAutores = null;

        //Se não tiver autor retorna null
        if (arrayAutor.length() == 0) {
            return null;
        }

        // Verifica se tem um ou mais autores
        for (int j = 0; j < arrayAutor.length(); j++) {

            if (j == 0) {
                // Se tiver um unico autor pega o nome desse autor
                listaAutores = arrayAutor.getString(0);
            } else {
                // caso tenha mais de um autor coloca em uma string
                // um embaixo do outro
                listaAutores += ", " + arrayAutor.getString(j);
            }
        }
        return listaAutores;
    }

    private static String formatarTipo(String tipo) {

        if (tipo.equals("BOOK")) {
            return "livro";
        } else {
            return "revista";
        }
    }
}
