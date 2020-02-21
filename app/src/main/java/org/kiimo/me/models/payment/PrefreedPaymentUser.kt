package org.kiimo.me.models.payment


import com.google.gson.annotations.SerializedName

data class PreferredPaymentUser(
    @SerializedName("preferredPayment")
    val preferredPayment: String = "CASH"
)