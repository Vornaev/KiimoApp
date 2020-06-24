package org.kiimo.me.models.delivery


import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class CalculateDeliveryResponse(
    @SerializedName("basePrice")
    val basePrice: Int = 0,
    @SerializedName("bruttoAmount")
    val bruttoAmount: Double = 0.0,
    @SerializedName("currencyId")
    val currencyId: String = "",
    @SerializedName("kilometers")
    val kilometers: Double = 0.0,
    @SerializedName("minutes")
    val minutes: Double = 0.0,
    @SerializedName("nettoAmount")
    val nettoAmount: String = "",
    @SerializedName("pricePerKm")
    val pricePerKm: Int = 0,
    @SerializedName("pricePerMin")
    val pricePerMin: Double = 0.0
) {
    fun getBruttoPrice(): String {
        return this.bruttoAmount.roundToInt().toString()
    }
}