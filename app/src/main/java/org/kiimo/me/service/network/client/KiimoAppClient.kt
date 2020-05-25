package org.kiimo.me.service.network.client

import io.reactivex.Observable
import org.kiimo.me.register.model.UserProfileInformationRequest
import org.kiimo.me.register.model.UserRegisterPhoneRequest
import org.kiimo.me.register.model.UserRegisterResponse
import org.kiimo.me.main.menu.model.GetUserRequestModel
import org.kiimo.me.main.menu.model.UserProfileInformationResponse
import org.kiimo.me.register.model.*
import retrofit2.http.*


object RetrofitConsts {
    const val AuthorizationHeader = "AuthorizationHeader"
    const val BaseUserUrl = "um.deliverycoin.net/"
    const val BaseDelUrl = ""
    const val SUCCESS_CODE = 200

}

interface KiimoAppClient {

    //http://um.deliverycoin.net/api/v1.0/kiimoUmWs.php?request=registerUser/string/C06E72A1-787E-4DFC-BF46-411D7B7A7F51
    //register User
    @POST("api/v1.0/kiimoUmWs.php?request=registerUser/string/C06E72A1-787E-4DFC-BF46-411D7B7A7F51")
    fun registerUserPhoneNumber(@Body userData: UserRegisterPhoneRequest): Observable<UserRegisterResponse>

    @POST("api/v1.0/kiimoUmWs.php?request=updateuser/string/C06E72A1-787E-4DFC-BF46-411D7B7A7F51")
    fun updateUserInformation(@Body data: UserProfileInformationRequest): Observable<UserRegisterResponse>

    @POST("api/v1.0/kiimoUmWs.php?request=updateuser/string/C06E72A1-787E-4DFC-BF46-411D7B7A7F51")
    fun updateUserInformation(@Body data: UserProfileUpdatePhotoRequest): Observable<UserRegisterResponse>


    @POST("api/v1.0/kiimoUmWs.php?request=updateuser/string/C06E72A1-787E-4DFC-BF46-411D7B7A7F51")
    fun updateUserInformation(@Body data: UserProfileFragmentUpdateRequest): Observable<UserRegisterResponse>

    @POST("api/v1.0/kiimoUmWs.php?request=getUserByID/string/C06E72A1-787E-4DFC-BF46-411D7B7A7F51")
    fun getUserByID(@Body data: GetUserRequestModel): Observable<UserProfileInformationResponse>

    @POST("api/v1.0/kiimoUmWs.php?request=userLogin/string/C06E72A1-787E-4DFC-BF46-411D7B7A7F51")
    fun userLogin(@Body userData: UserLoginRequest): Observable<UserLoginResponse>

    @POST("api/v1.0/kiimoUmWs.php?request=activateUser/string/C06E72A1-787E-4DFC-BF46-411D7B7A7F51")
    fun activateUser(@Body activateUserRequest: ActivateUserRequest): Observable<StatusMessageDataResponse>

    @POST("api/v1.0/kiimoUmWs.php?request=isValidDeliverer/string/C06E72A1-787E-4DFC-BF46-411D7B7A7F51")
    fun isValidDeliverer(@Body activateUserRequest: ActivateUserRequest): Observable<IsValidDelivererResponse>


}
