package org.kiimo.me.main.menu.model

data class CreditCardModel(
    var cardholderName: String,
    var cardNumber: String,
    var expiryDate: String,
    var ccvCode: String
)