package com.example.pokekotlinapicomposesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import com.example.pokekotlinapicomposesample.ui.pokemon.PokemonScreen
import com.example.pokekotlinapicomposesample.ui.theme.PokeKotlinApiComposeSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeKotlinApiComposeSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PokemonScreen()
                }
            }
        }
    }
}