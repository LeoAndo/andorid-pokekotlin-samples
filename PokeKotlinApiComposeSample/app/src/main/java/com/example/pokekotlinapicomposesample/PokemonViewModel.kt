package com.example.pokekotlinapicomposesample

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

import me.sargunvohra.lib.pokekotlin.client.PokeApi

class PokemonViewModel : ViewModel() {
    var uiState by mutableStateOf<UiState>(UiState.Initial)
        private set
//    private val _snackBarMessage = MutableSharedFlow<String>()
//    val snackBarMessage: SharedFlow<String> = _snackBarMessage

    fun searchPokemon(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val pokeApi: PokeApi = PokeApiClient()
                pokeApi.getPokemon(id.toInt())
            }.onSuccess {
                uiState =
                    UiState.PokemonData(id = it.id, name = it.name, url = it.sprites.backDefault)
            }.onFailure {
                uiState = UiState.Error(it.localizedMessage ?: "error!")
                // _snackBarMessage.emit(it.localizedMessage ?: "error!")
            }
        }
    }
}

sealed interface UiState {
    object Initial : UiState
    data class PokemonData(val id: Int, val name: String, val url: String?) : UiState
    data class Error(val errorMessage: String) : UiState
}