package com.pedromoreirareisgmail.livros;


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

    public Utils() {
    }

    public static List<Livro> ComecarASyncTask(String urlString) {

        URL urlRequisicao = createURL(urlString);

        String dadosJSON = "";

        try {
            dadosJSON = makeHttpRequest(urlRequisicao);
        } catch (IOException e) {
            Log.e(TAG,"Erro na solicitação de dados",e);
        }

        return extrairJSON(dadosJSON);
    }

    private static URL createURL(String preUrl) {
        URL url = null;

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
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                dadosJSON = leiaInputStream(inputStream);
            } else {
               Log.e(TAG,"Erro na requisição de dados, Código:  " + connection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(TAG,"Erro ao fazer a requisição de dados ou converter os dados JSON",e);
        }finally {
            if(connection != null){
                connection.disconnect();
            }

            if(inputStream != null){
                inputStream.close();
            }
        }

        return dadosJSON;
    }

    private static String leiaInputStream(InputStream inputStream) throws IOException {

        StringBuilder saida = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);

            String linha = reader.readLine();

            while (linha != null) {
                saida.append(linha);
                linha = reader.readLine();
            }

        }


        return saida.toString();
    }

    private static List<Livro> extrairJSON(String dadosJSON) {


        // Cria uma lista de livros
        List<Livro> livros = new ArrayList<>();

        try {

            // Pega os dados JSON e coloca em um objeto para tratamento
            JSONObject root = new JSONObject(dadosJSON);

            // No objeto JSON, Verifica o Array principal, items
            JSONArray arrayItems = root.getJSONArray("items");

            // Dentro do Array items, verifica todos os objetos pertencente
            // a esse a Array para capturar os dados
            for (int i = 0; i < arrayItems.length(); i++) {

                // Dentro do Array pega cada objeto
                JSONObject livroAtual = arrayItems.getJSONObject(i);

                // Esse objeto contem as informações sobre o livro
                JSONObject volumeInfo = livroAtual.getJSONObject("volumeInfo");

                // Titulo do livro
                String title = volumeInfo.getString("title");

                // Esse Array contem o autor ou autores do livro
                JSONArray arrayAuthors = volumeInfo.getJSONArray("authors");

                //TODO: JSON Array Imagem e fazer Download

                // Formatar os dados do Array de autor(es) colocando os
                // autores separados por virgula
                String authors = formatarAutor(arrayAuthors);

                // Pega as descrição do livro, um pequeno texto com as
                // principais informaçõe do livro
                String description = volumeInfo.getString("description");

                // Informa a quantidade de paginas do livro ou revista
                int pageCount = volumeInfo.getInt("pageCount");

                // Informa se é um livro ou uma revista
                String printType = formataTipo(volumeInfo.getString("printType"));


                livros.add(new Livro(title, authors, description, printType, pageCount));
            }


        } catch (JSONException e) {
            Log.e(TAG, "Erro ao capturar dados JSON", e);
        }


        return livros;
    }

    private static String formatarAutor(JSONArray arrayAutor) throws JSONException {
        String listaAutores = null;

        for (int j = 0; j < arrayAutor.length(); j++) {

            if (j == 0) {
                // Se tiver um unico autor pega o nome desse autor
                listaAutores = arrayAutor.getString(0);
            } else {
                // caso tenha mais de um autor coloca em uma string
                // um embaixo do outro
                listaAutores = listaAutores + "\n" + arrayAutor.getString(j);
            }

        }
        return listaAutores;
    }

    private static String formataTipo(String type) {
        String tipo = null;
        switch (type) {
            case "BOOK":
                tipo = "Livro";
                break;
            case "MAGAZINE":
                tipo = "Revista";
                break;
        }
        return tipo;
    }
}
