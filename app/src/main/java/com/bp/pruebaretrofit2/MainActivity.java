package com.bp.pruebaretrofit2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bp.pruebaretrofit2.Api.ApiService;
import com.bp.pruebaretrofit2.Modelos.Pokemon;
import com.bp.pruebaretrofit2.Modelos.PokemonRespuesta;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";

    private Retrofit retrofit;

    private RecyclerView recyclerView;
    private ListaPokemonAdapter listaPokemonAdapter;

    //Valor que representa el limite para cargar los siguientes 20 pokemon.
    private int offset;

    private boolean aptoParaCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instancia del RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //Instancia del adapter.
        listaPokemonAdapter = new ListaPokemonAdapter(this);

        recyclerView.setAdapter(listaPokemonAdapter);
        recyclerView.setHasFixedSize(true);

        //Se emplea el GridLayoutManager para mostrar los pokemon en forma de matriz de 3 columnas.
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        /* Cargará los próximos pokemon cuando se haga scroll al final de la lista. Los muestra de
        20 en 20. */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //Detecta si se hace scroll hacia abajo y si es el final.
                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (aptoParaCargar) { //Si es el final...
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, " Llegamos al final.");

                            aptoParaCargar = false;
                            offset += 20; //... carga los proximos 20 pokemon.
                            obtenerDatos(offset); //y se vuelve a consultar a la Api.
                        }
                    }
                }
            }
        });

        //Instancia Retrofit.
        retrofit = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/api/v2/") //URL base.
                .addConverterFactory(GsonConverterFactory.create()) //Convierte las respuestas en objetos.
                .build();

        aptoParaCargar = true;
        offset = 0;
        obtenerDatos(offset);
    }

    /* Este método es el encargado de recuperar los datos. */
    private void obtenerDatos(int offset) {

        //Instancia de la interfaz ApiService.
        ApiService service = retrofit.create(ApiService.class);
        //Obtiene la lista usando el método de la interfaz y lo guarda.
        Call<PokemonRespuesta> pokemonRespuestaCall = service.obtenerListaPokemon(20, offset);

        /*El método enqueue permite realizar llamadas asincronas y recuperar su resultado
        en un Callback. */
        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {

            /* Si se recibe respuesta. */
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                aptoParaCargar = true;
                //Aunque se obtenga respuesta se comprueba si es correcta.
                if (response.isSuccessful()) {

                    //Se obtiene la información de la respuesta con el método body().
                    PokemonRespuesta pokemonRespuesta = response.body();
                    //Se obtiene la lista de pokemonRespuesta y se almacena.
                    ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();
                    /* Se pasa la lista al método del adapter, que añadirá la lista a la que hay
                    * sin reemplazarla. */
                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);

                } else {
                    Log.e(TAG, " onResponse: " + response.errorBody());
                }
            }
            /* Si no se recibe respuesta. */
            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                aptoParaCargar = true;
                Log.e(TAG, " onFailure: " + t.getMessage());
            }
        });
    }
}
