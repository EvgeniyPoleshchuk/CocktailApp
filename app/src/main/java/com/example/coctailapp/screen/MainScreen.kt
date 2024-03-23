package com.example.coctailapp.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coctailapp.R
import com.example.coctailapp.model.CocktailItem

import com.example.coctailapp.model.CocktailViewModel
import com.example.coctailapp.model.IngredientList
import com.example.coctailapp.model.QueryParam
import com.example.coctailapp.ui.theme.Yellow30
import java.util.Locale

var QueryParam: QueryParam = QueryParam("","")


@Composable
fun MainScreen() {
    val cocktailViewModel: CocktailViewModel = viewModel()
    val viewState by cocktailViewModel.cocktailState
    var textSearch by remember { mutableStateOf("") }
    val onSearch = remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    Image(
        painter = painterResource(R.drawable.thumbnail), "",
        modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End
    ) {

        Row(
            modifier = Modifier
                .padding(top = 30.dp, start = 10.dp, end = 10.dp)
                .background(Yellow30),
        ) {
            OutlinedTextField(
                value = textSearch,
                onValueChange = { textSearch = it },
                singleLine = true,
                trailingIcon = {
                    if (textSearch.isNotEmpty()) {
                        IconButton(onClick = { textSearch = "" }) {
                            Icon(Icons.Filled.Close, "")
                        }
                    }
                },
                modifier = Modifier.background(Color.White)
            )
            Spacer(modifier = Modifier.padding(end = 10.dp))
            IconButton(
                onClick = {
                    QueryParam.ingredients =""
                    QueryParam.name = textSearch.lowercase(Locale.getDefault())
                    cocktailViewModel.fetchData()
                    onSearch.value = true
                }, modifier = Modifier
                    .padding(top = 5.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_baseline_search_24), "",
                    modifier = Modifier.size(100.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(3.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Filter", fontSize = 20.sp, color = Color.White,
                modifier = Modifier.clickable {
                    isVisible = !isVisible
                })
            Image(painter = painterResource(R.drawable.baseline_density_medium_24), "")
        }
        Spacer(modifier = Modifier.padding(3.dp))
        FilterVisibility(isVisible,cocktailViewModel,onSearch)
        Spacer(modifier = Modifier.padding(5.dp))
        if (onSearch.value) {
            CocktailData(viewState)
        }
    }


}


@Composable
fun CocktailItems(cocktails: List<CocktailItem>) {
    LazyColumn() {
        items(cocktails.size) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
                    .alpha(0.9f),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Column(
                    modifier = Modifier.padding(start = 7.dp),
                ) {
                    Row() {
                        Text("Name : ", fontWeight = FontWeight.Bold)
                        Text(cocktails[it].name, fontFamily = FontFamily.Monospace)
                    }
                    Row() {
                        Text("Ingredients : ", fontWeight = FontWeight.Bold)
                        Column() {
                            cocktails[it].ingredients.forEach {
                                Text(text = it)
                            }
                        }
                    }
                    Row() {
                        Text("Instructions : ", fontWeight = FontWeight.Bold)
                        Text(cocktails[it].instructions)
                    }
                }
            }
        }
    }
}

@Composable
fun CocktailData(viewState: CocktailViewModel.CocktailState) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            viewState.error != null || viewState.list.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.outline_search_off_24), "",
                        modifier = Modifier.size(300.dp)
                    )
                    Text("Not Found", fontSize = 50.sp, color = Color.Red)
                }
            }

            else -> {
                CocktailItems(viewState.list)
            }
        }
    }


}

@Composable
fun FilterVisibility(
    isVisibility: Boolean,
    cocktailViewModel: CocktailViewModel,
    onSearch: MutableState<Boolean>
) {
    if (isVisibility) {
        LazyRow() {
            items(IngredientList().ingredientList) { item ->
                Button(modifier = Modifier
                    .size(100.dp, 30.dp)
                    .padding(end = 3.dp),
                    colors = ButtonDefaults.buttonColors(Yellow30),
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    border = BorderStroke(2.dp, Color.White),
                    onClick = {
                        onSearch.value = true
                        QueryParam.ingredients = item
                        cocktailViewModel.fetchData()
                    }) {
                    Text(item, fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CocktailListPreview() {
    MainScreen()
}