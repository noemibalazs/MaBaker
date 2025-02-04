package com.noemi.exovideo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Recipe(

    val id: Int = 0,
    val name: String = "",
    val ingredients: List<Ingredient> = listOf(),
    val steps: List<Step> = listOf(),
    val servings: Int = 0,
    val image: String = ""
) : Parcelable
