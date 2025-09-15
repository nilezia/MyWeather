package com.nilezia.myweather.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nilezia.myweather.domain.model.DirectLocationUi
import com.nilezia.myweather.ui.viewmodel.WeatherViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun OtherWeatherScreen(viewModel: WeatherViewModel = hiltViewModel(), onSelect: (DirectLocationUi) -> Unit = {}, navController: NavHostController?=null) {
    val uiState by viewModel.uiState.collectAsState()
    Column(Modifier.fillMaxSize()) {
        SearchBarExample() {
            viewModel.getWeatherByCityName(it)

        }
        LazyColumn {
            items(uiState.listLocationUi?.size ?: 0) {
                uiState.listLocationUi?.get(it)?.let { data->
                    CardLocationSearch(data) {
                        onSelect.invoke(data)
                        navController?.popBackStack()
                        Log.d("TAG", "OtherWeatherScreen: $it")

                    }
                }
            }
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
fun SearchBarExample(onSearch: (String) -> Unit = {}) {
    var searchText by remember { mutableStateOf("") }

    // ใช้ LaunchedEffect + debounce
    LaunchedEffect(searchText) {
        snapshotFlow { searchText }
            .debounce(1000)
            .distinctUntilChanged()
            .collect { text ->
                if (text.isNotBlank()) {
                    onSearch(text)
                }
            }
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        OutlinedTextField(
            shape = RoundedCornerShape(16.dp),
            value = searchText,
            onValueChange = { newValue ->
                searchText = newValue
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                disabledIndicatorColor = Color.White,
                errorIndicatorColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
            )
        )

        // Display search results based on 'searchText'
        // For example: LazyColumn with filtered items
        Text(text = "Current search query: $searchText", modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun CardLocationSearch(directLocationUi: DirectLocationUi, onClick: (DirectLocationUi) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(directLocationUi) } // ตรงนี้สำคัญ
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = directLocationUi.name)
            Text(text = directLocationUi.country)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun OtherWeatherScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF90CAF9),
                        Color(0xFF2196F3),
                    )
                )
            )
    ) {
        OtherWeatherScreen()

    }
}