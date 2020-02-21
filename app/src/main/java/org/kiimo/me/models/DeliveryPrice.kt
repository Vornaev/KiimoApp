package org.kiimo.me.models

data class DeliveryPrice(
    val bruttoAmount: String?,
    val nettoAmount: String?,
    val currencyId: String?,
    val kilometers: Double?,
    val minutes: Double?,
    val pricePerKm: Int?,
    val pricePerMin: Double?,
    val basePrice: Int?
)