package org.kiimo.me.main.menu.model


import com.google.gson.annotations.SerializedName

data class CreditCardSaveRequest(
        var cardNo: String,
        var expMonth: Int,
        var expYear: Int,
        var cv2: Int
)