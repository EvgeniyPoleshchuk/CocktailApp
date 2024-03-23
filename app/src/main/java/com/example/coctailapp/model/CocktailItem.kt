package com.example.coctailapp.model


import com.google.gson.annotations.SerializedName

data class CocktailItem(
    @SerializedName("ingredients")
    val ingredients: List<String>,
    @SerializedName("instructions")
    val instructions: String,
    @SerializedName("name")
    val name: String
)

data class QueryParam(var name: String,var ingredients: String)

class IngredientList() {
    var ingredientList = mutableListOf(
        "Vodka",
        "Tequila",
        "Rum",
        "Gin",
        "Beer",
        "Brandy",
        "Liqueur",
        "Whiskey",
        "Bourbon"
    )

    fun addIngredient(value: String) {
        ingredientList.add(value)
    }

}


