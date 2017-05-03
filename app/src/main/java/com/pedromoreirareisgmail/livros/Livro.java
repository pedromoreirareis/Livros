package com.pedromoreirareisgmail.livros;

public class Livro {
    private String mTitulo;
    private String mAutor;
    private String mDescricao;
    private String mTipo; // Livro ou revista
    private int mQuantPag;

    /**
     * Dados referentes a um livro ou revista pesquisado na API Google Livros
     *
     * @param titulo    nome do livro ou revista
     * @param autor     nome do autor ou autores do livro ou revista
     * @param descricao pequeno texto com infromações sobre o livro ou revista
     * @param tipo      especifica se é um livro ou revista
     * @param quantPag  mostra a quantidade pagina que o livro ou revista tem
     */
    public Livro(String titulo, String autor, String descricao, String tipo, int quantPag) {
        mTitulo = titulo;
        mAutor = autor;
        mDescricao = descricao;
        mTipo = tipo;
        mQuantPag = quantPag;
    }

    public String getmTitulo() {
        return mTitulo;
    }

    public String getmAutor() {
        return mAutor;
    }

    public String getmDescricao() {
        return mDescricao;
    }

    public String getmTipo() {
        return mTipo;
    }

    public int getmQuantPag() {
        return mQuantPag;
    }
}
