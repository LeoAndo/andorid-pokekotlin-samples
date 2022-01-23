package com.example.pokekotlinapisample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = findViewById(R.id.editTextPokemonId);
        ImageView imageView = findViewById(R.id.imagePokemon);
        TextView textView = findViewById(R.id.txtPokemonIdWithName);

        findViewById(R.id.buttonSearchPokemon).setOnClickListener(v -> {
            new Thread(() -> {
                PokeApi pokeApi = new PokeApiClient();
                try {
                    Pokemon pokemon = pokeApi.getPokemon(Integer.parseInt(editText.getText().toString()));
                    runOnUiThread(() -> {
                        textView.setText(pokemon.getId() + " : " + pokemon.getName());
                        Glide.with(MainActivity.this).load(pokemon.getSprites().component1()).into(imageView);
                    });
                } catch (Throwable throwable) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "error: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });
    }
}