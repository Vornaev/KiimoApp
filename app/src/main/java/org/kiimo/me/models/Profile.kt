package org.kiimo.me.models

import org.kiimo.me.main.menu.model.UserProfileInformationResponse

class Profile(
        var name: String = "",
        var lastName: String = "",
        var email: String = "",
        var phoneNumber: String = "",
        var password: String = "",
        var photoUserUrl: String = "",
        var personalId: String = "",
        var street: String = "",
        var country: String ="",
        var place: String = "",
        var zip :String = "",
        var houseNumber :String = ""
) {
    fun loadFromCache(user: UserProfileInformationResponse, phoneNumber: String): Profile {

        this.name = user.firstName
        this.lastName = user.lastName
        this.phoneNumber = phoneNumber
        this.email = user.email
        this.photoUserUrl = user.photo
        this.personalId = user.personalID
        this.street = user.address.street
        this.houseNumber = user.address.houseNumber
        this.zip = user.address.zip
        this.place = user.address.place
        this.country = user.address.countryCode

        return this
    }
}