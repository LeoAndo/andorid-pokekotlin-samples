package com.example.pokekotlinapisample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import me.sargunvohra.lib.pokekotlin.client.PokeApi
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import com.bumptech.glide.Glide
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editText = findViewById<EditText>(R.id.editTextPokemonId)
        val imageView = findViewById<ImageView>(R.id.imagePokemon)
        val textView = findViewById<TextView>(R.id.txtPokemonIdWithName)
        findViewById<View>(R.id.buttonSearchPokemon).setOnClickListener {
            Thread {
                val pokeApi: PokeApi = PokeApiClient()
                kotlin.runCatching {
                    pokeApi.getPokemon(editText.text.toString().toInt())
                }.onSuccess { pokemon ->
                    runOnUiThread {
                        textView.text = "${pokemon.id} : ${pokemon.name}"
                        Glide.with(this@MainActivity).load(pokemon.sprites.backDefault)
                            .into(imageView)
                    }
                }.onFailure { throwable ->
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "error: " + throwable.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.start()
        }
    }
}