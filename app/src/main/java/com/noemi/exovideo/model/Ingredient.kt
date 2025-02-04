package com.noemi.exovideo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Ingredient(

    val quantity: String,
    val measure: String,
    val ingredient: String
) : Parcelable {
    override fun toString(): String {
        return "$quantity $measure $ingredient"
    }
}
