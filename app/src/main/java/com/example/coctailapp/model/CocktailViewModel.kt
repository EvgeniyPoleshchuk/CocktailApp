package com.example.coctailapp.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coctailapp.ApiService.cocktailService
import com.example.coctailapp.screen.QueryParam
import kotlinx.coroutines.launch
import java.lang.Exception

class CocktailViewModel : ViewModel() {
    private val _cocktailState = mutableStateOf(CocktailState())
    val cocktailState: State<CocktailState> = _cocktailState


    fun fetchData() {
        viewModelScope.launch {
            try {
                val response = cocktailService.getCocktail(QueryParam.name, QueryParam.ingredients)
                _cocktailState.value = _cocktailState.value.copy(
                    list = response,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _cocktailState.value = _cocktailState.value.copy(
                    loading = false,
                    error = "Error fetching cocktail ${e.message}"
                )
            }
        }
    }


    data class CocktailState(
        val loading: Boolean = true,
        val list: List<CocktailItem> = emptyList(),
        var error: String? = null
    )
}
