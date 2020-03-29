package org.kiimo.me.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import org.kiimo.me.main.menu.model.CreditCardModel
import org.kiimo.me.main.menu.model.UserProfileInformationResponse
import org.kiimo.me.models.Profile
import org.kiimo.me.register.model.UserProfileInformationRequest

object PreferenceUtils {

    private fun getPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * Saves the provided key-value pair in shared preferences.
     *
     * @param key       The name of the preference
     * @param value     The new value of the preference
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not implemented yet!")
        }
    }

    /**
     * Returns the value on a given key.
     *
     * @param key           The preference name
     * @param defaultValue  Optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    private inline fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? =
        when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not implemented yet!")
        }

    //Token
    private const val USER_TOKEN = "key.user.token"

    fun saveUseToken(context: Context, token: String) {
        getPreferences(context)[USER_TOKEN] = token
    }

    fun getUserToken(context: Context): String =
        getPreferences(context).getString(USER_TOKEN, "") ?: ""

    //Phone
    private const val USER_PHONE_NUMBER = "key.user.phone.number"

    fun saveUserPhoneNumber(context: Context, phoneNumber: String) {
        getPreferences(context)[USER_PHONE_NUMBER] = phoneNumber
    }

    fun getUserPhoneNumber(context: Context): String =
        getPreferences(context).getString(USER_PHONE_NUMBER, "") ?: ""


    //PROFILE
    private const val USER_PROFILE_KEY = "key.user.profile.information"

    fun saveUserProfile(context: Context, user: UserProfileInformationResponse) {
        val string = JsonUtil.getJsonStringFromModel(user)

        getPreferences(context)[USER_PROFILE_KEY] = string
    }

    fun saveUserProfile(context: Context, user: UserProfileInformationRequest) {
        val string = JsonUtil.getJsonStringFromModel(user)

        getPreferences(context)[USER_PROFILE_KEY] = string
    }

    fun getUserProfile(context: Context): String {
        return getPreferences(context).getString(USER_PROFILE_KEY, "") ?: ""
    }

    fun getUserParsed(context: Context): UserProfileInformationRequest {
        return JsonUtil.loadModelFromJson(
            getPreferences(context).getString(USER_PROFILE_KEY, "") ?: ""
        )
    }

    //Password
    const val PASSWORD_KEY = "key.user.password"

    fun saveUserPassword(context: Context, password: String) {
        getPreferences(context)[PASSWORD_KEY] = password
    }

    fun getUserPassord(context: Context): String {
        return getPreferences(context).getString(PASSWORD_KEY, "") ?: ""
    }

    fun logout(context: Context) {
        getPreferences(context)[USER_PHONE_NUMBER] = ""
        getPreferences(context)[USER_PROFILE_KEY] = ""
        getPreferences(context)[USER_TOKEN] = ""
        getPreferences(context)[PASSWORD_KEY] = ""
        getPreferences(context)[FIREBASE_TOKEN] = ""
        getPreferences(context)[CARD_DETAILS] = ""
        getPreferences(context)[PAYMENT_TYPE] = 0
    }

    //Firebase TOken
    const val FIREBASE_TOKEN = "kiimo.firebase.devicetoken.key"

    fun saveFirebaseToken(context: Context, fcmToken: String) {
        getPreferences(context)[FIREBASE_TOKEN] = fcmToken
    }

    fun getUserFirebaseToken(context: Context): String {
        return getPreferences(context).getString(FIREBASE_TOKEN, "") ?: ""
    }

    private const val ACCOUNT_CREATE_TYPE = "kiimo.me.account.type"

    fun saveAccountType(context: Context, isSender: Boolean) {
        getPreferences(context)[ACCOUNT_CREATE_TYPE] = isSender
    }

    fun getAccountTypeIsSender(context: Context): Boolean {
        return getPreferences(context).getBoolean(ACCOUNT_CREATE_TYPE, true)
    }


    private const val PAYMENT_TYPE = "org.kiimo.me.payment.type"
    fun savePaymentTypeForUser(context: Context, paymentType: Int) {
        getPreferences(context)[PAYMENT_TYPE] = paymentType
    }

    fun getPaymentTypeForUser(context: Context): Int {
        return getPreferences(context).getInt(PAYMENT_TYPE, 0)
    }

    private const val CARD_DETAILS = "org.kiimo.me.payment.type.card.details"
    fun saveCreditCard(context: Context, creditCard: CreditCardModel?) {
        if (creditCard != null) {
            val cardString = JsonUtil.getJsonStringFromModel(creditCard)
            getPreferences(context)[CARD_DETAILS] = cardString
        }
    }

    fun getCreditCardDetails(context: Context): CreditCardModel? {
        val card = getPreferences(context).getString(CARD_DETAILS, "")
        if (card != null && card.isNotEmpty()) {
            return JsonUtil.loadModelFromJson(card)
        }
        return null
    }
}


