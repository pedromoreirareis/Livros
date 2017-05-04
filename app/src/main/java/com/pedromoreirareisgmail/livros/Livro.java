package com.pedromoreirareisgmail.livros;

public class Livro {
    private String mTitulo;
    private String mAutor;

    /**
     * Dados referentes a um livro ou revista pesquisado na API Google Livros
     *
     * @param titulo    nome do livro ou revista
     * @param autor     nome do autor ou autores do livro ou revista
     */
    public Livro(String titulo, String autor) {
        mTitulo = titulo;
        mAutor = autor;
    }

    public String getmTitulo() {
        return mTitulo;
    }

    public String getmAutor() {
        return mAutor;
    }

}
