package com.example.pokekotlinapicomposesample.ui.pokemon

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.pokekotlinapicomposesample.PokemonViewModel
import com.example.pokekotlinapicomposesample.UiState
import com.example.pokekotlinapicomposesample.extention.mainContentPadding
import com.example.pokekotlinapicomposesample.ui.theme.PokeKotlinApiComposeSampleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PokemonScreen(
    viewModel: PokemonViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    var pokemonIdText by remember { mutableStateOf("") }
    var isEnableButton by remember { mutableStateOf(false) }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                content = { Text(text = "ComposeSample") }
            )
        },
        content = { innerPadding ->
            PokemonContent(
                modifier = Modifier.mainContentPadding(innerPadding),
                uiState = viewModel.uiState,
                pokemonIdText = pokemonIdText,
                isEnableButton = isEnableButton,
                onValueChange = {
                    pokemonIdText = it
                    isEnableButton = it.isNotEmpty()
                },
                onClickSearchButton = {
                    viewModel.searchPokemon(pokemonIdText)
                },
                showSnackBarMessage = {
                    // show snackbar as a suspend function
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(it)
                        viewModel.showSnackBarCompleted()
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PokemonContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    pokemonIdText: String,
    isEnableButton: Boolean,
    onValueChange: (String) -> Unit,
    onClickSearchButton: () -> Unit,
    showSnackBarMessage: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(modifier = Modifier.fillMaxWidth(),
            value = pokemonIdText,
            onValueChange = onValueChange,
            label = {
                Text(text = "input here Pokemon ID...")
            })
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickSearchButton,
            enabled = isEnableButton
        ) {
            Text(text = "Search!!!")
        }

        when (uiState) {
            UiState.Initial, UiState.ShowSnackBarCompleted -> {}
            is UiState.Error -> {
                showSnackBarMessage(uiState.errorMessage)
            }
            is UiState.PokemonData -> {
                Image(
                    painter = rememberImagePainter(
                        data = uiState.url,
                        builder = {
                            crossfade(true)
                            placeholder(drawableResId = android.R.drawable.btn_star)
                        }
                    ),
                    contentDescription = "Pokemon Image",
                    modifier = Modifier.size(300.dp),
                    contentScale = ContentScale.Crop,
                )
                Text(text = "${uiState.id} : ${uiState.name}")
            }
        }
    }
}

@Preview(
    "default: Error",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    "dark theme: Error",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
@Composable
fun PokemonContent_Preview_Error() {
    PokeKotlinApiComposeSampleTheme {
        PokemonContent(
            pokemonIdText = "",
            isEnableButton = false,
            onValueChange = {},
            uiState = UiState.Error("error!!!!"),
            onClickSearchButton = {},
            showSnackBarMessage = {}
        )
    }
}

@Preview(
    "default: Not Empty",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Composable
fun PokemonContent_Preview_NotEmpty() {
    PokeKotlinApiComposeSampleTheme {
        PokemonContent(
            pokemonIdText = "abc",
            isEnableButton = true,
            onValueChange = {},
            uiState = UiState.PokemonData(id = 1, name = "abc", url = null),
            onClickSearchButton = {},
            showSnackBarMessage = {}
        )
    }
}

@Preview(
    "default: Empty",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Composable
fun PokemonContent_Preview_Empty() {
    PokeKotlinApiComposeSampleTheme {
        PokemonContent(
            pokemonIdText = "",
            isEnableButton = false,
            onValueChange = {},
            uiState = UiState.Initial,
            onClickSearchButton = {},
            showSnackBarMessage = {}
        )
    }
}