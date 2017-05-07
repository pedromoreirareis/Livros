package com.pedromoreirareisgmail.livros;

public class Livro {
    private String mTitulo;
    private String mAutor;
    private String mImagem;
    private String mTipo;
    private String mPag;
    private String mInfo;


    /**
     * Dados referentes a um livro ou revista pesquisado na API Google Livros
     *
     * @param titulo nome do livro ou revista
     * @param autor  nome do autor ou autores do livro ou da revista
     * @param imagem link da imagem da capa do livro ou revista
     * @param tipo   indetifica se é u livro ou uma revista
     * @param pag    quantidade de paginas do livro ou revista
     * @param info   pequeno texto com informações sobre o livro ou revista
     */
    public Livro(String titulo, String autor, String imagem, String tipo,
                 String pag, String info) {
        mTitulo = titulo;
        mAutor = autor;
        mImagem = imagem;
        mTipo = tipo;
        mPag = pag;
        mInfo = info;
    }

    public String getmTitulo() {
        return mTitulo;
    }

    public String getmAutor() {
        return mAutor;
    }

    public String getmImagem() {
        return mImagem;
    }

    public String getmTipo() {
        return mTipo;
    }

    public String getmPag() {
        return mPag;
    }

    public String getmInfo() {
        return mInfo;
    }

}
