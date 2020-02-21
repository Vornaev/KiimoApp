package org.kiimo.me.models

import org.kiimo.me.main.menu.model.UserProfileInformationResponse

class Profile(
    var name: String = "",
    var lastName: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var password: String = "",
    var photoUserUrl: String = "",
    var personalId: String = ""
) {
    fun loadFromCache(user: UserProfileInformationResponse, phoneNumber: String): Profile {

        this.name = user.firstName
        this.lastName = user.lastName
        this.phoneNumber = phoneNumber
        this.email = user.email
        this.photoUserUrl = user.photo
        this.personalId = user.personalID


        return this
    }
}