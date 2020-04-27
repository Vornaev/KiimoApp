package org.kiimo.me.main.menu.model


import com.google.gson.annotations.SerializedName

data class CreditCardSaveRequest(
        var cardNo: String,
        var expMonth: String,
        var expYear: String,
        var cv2: String
)