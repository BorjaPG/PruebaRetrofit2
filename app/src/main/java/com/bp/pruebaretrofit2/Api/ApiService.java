package com.bp.pruebaretrofit2.Api;

import com.bp.pruebaretrofit2.Modelos.PokemonRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz que contiene los métodos necesarios para gestionar las peticiones.
 */

public interface ApiService {

    @GET("pokemon") //Obtiene información de "http://pokeapi.co/api/v2/pokemon".
    /* obtenerListaPokemon obtiene por parámetros los elementos de la URL indicada. */
    Call<PokemonRespuesta> obtenerListaPokemon(@Query("limit") int limit, @Query("offset") int offset);
}
