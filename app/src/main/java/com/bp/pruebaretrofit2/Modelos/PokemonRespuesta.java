package com.bp.pruebaretrofit2.Modelos;

import java.util.ArrayList;

/**
 * Clase que controla las respuestas.
 */

public class PokemonRespuesta {

    //Lista de tipo pokemon que contendrá todos los resultados.
    private ArrayList<Pokemon> results;

    public ArrayList<Pokemon> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }
}
