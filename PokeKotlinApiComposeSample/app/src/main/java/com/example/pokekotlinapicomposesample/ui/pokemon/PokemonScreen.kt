package com.example.pokekotlinapicomposesample.ui.pokemon

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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

@Composable
fun PokemonScreen(
    viewModel: PokemonViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    var pokemonIdText by remember { mutableStateOf("") }
    var isEnableButton by remember { mutableStateOf(false) }
    // val snackBarMessage by viewModel.snackBarMessage.collectAsState(initial = null)
    var errorMessage: String? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = errorMessage, block = {
        Log.d("PokemonScreen", "LaunchedEffect snackBarMessage $errorMessage")
        errorMessage?.let { scaffoldState.snackbarHostState.showSnackbar(it) }
    })

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(content = { Text(text = "ComposeSample") })
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
                onClickSearchButton = { viewModel.searchPokemon(pokemonIdText) },
                onError = { errorMessage = it },
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
    onError: (String) -> Unit,
) {
    Log.d("PokemonScreen", "PokemonContent uiState: $uiState")
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
            UiState.Initial -> {}
            is UiState.Error -> {
                Text(
                    text = uiState.errorMessage,
                    modifier = Modifier.wrapContentSize(),
                )
                onError(uiState.errorMessage)
                Log.d("PokemonScreen", "UiState Error: ${uiState.errorMessage}")
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
            onError = {},
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
            onError = {},
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
            onError = {},
        )
    }
}