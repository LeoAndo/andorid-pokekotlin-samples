package com.example.pokekotlinapicomposesample

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.pokekotlinapicomposesample.ui.theme.PokeKotlinApiComposeSampleTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<PokemonViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeKotlinApiComposeSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PokemonScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun PokemonScreen(viewModel: PokemonViewModel) {
    var pokemonIdText by remember { mutableStateOf("") }
    var isEnableButton by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                content = { Text(text = "ComposeSample") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            PokemonContent(
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
                        snackbarHostState.showSnackbar(it)
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
    uiState: UiState,
    pokemonIdText: String,
    isEnableButton: Boolean,
    onValueChange: (String) -> Unit,
    onClickSearchButton: () -> Unit,
    showSnackBarMessage: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(12.dp),
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
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    "dark theme: Error",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
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
    uiMode = UI_MODE_NIGHT_NO,
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
    uiMode = UI_MODE_NIGHT_NO,
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