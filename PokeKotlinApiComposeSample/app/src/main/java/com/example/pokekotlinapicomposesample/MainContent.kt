package com.example.pokekotlinapicomposesample

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.pokekotlinapicomposesample.ui.pokemon.PokemonScreen
import com.example.pokekotlinapicomposesample.ui.theme.PokeKotlinApiComposeSampleTheme

@Composable
fun MainContent() {
    PokeKotlinApiComposeSampleTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            PokemonScreen()
        }
    }
}