package org.kiimo.me.main.menu.model

data class CreditCardModel(
        var cardholderName: String,
        var cardNumber: String,
        var expMonth: String,
        var expYear: String,
        var ccvCode: String,
        var expiryDate: String = ""
)
