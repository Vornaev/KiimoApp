package org.kiimo.me.register.model

object UserCreationProperties {
    private var phoneExists: Boolean = false

    fun handleResponse(dataRequest: UserRegisterResponse) {
        phoneExists = dataRequest.status == 201
    }

    fun isAlreadyUser(): Boolean {
        return phoneExists
    }

    private var isSenderAccocunt = false

    fun isSender(): Boolean {
        return isSenderAccocunt
    }

    fun isDeliverer(): Boolean {
        return !isSenderAccocunt
    }

    fun setAccountTypeSender() {
        isSenderAccocunt = true
    }

    fun setAccountTypeDeliverer() {
        isSenderAccocunt = false
    }


}