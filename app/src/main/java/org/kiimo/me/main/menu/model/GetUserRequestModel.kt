package org.kiimo.me.main.menu.model

import org.kiimo.me.register.model.UserRegisterDataRequest

data class GetUserRequestModel(
    val user: UserRegisterDataRequest,
    val userID: String
)

