package org.kiimo.me.service.network.client

import com.google.gson.JsonElement
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kiimo.me.main.fragments.model.deliveries.DeliveryCarrierItem
import org.kiimo.me.main.fragments.model.sender.SenderOrderListResponse
import org.kiimo.me.main.menu.model.CreditCardSaveRequest
import org.kiimo.me.main.sender.model.request.CreateDeliveryRequest
import org.kiimo.me.main.sender.model.request.PayRequest
import org.kiimo.me.main.sender.model.request.pay.PayResponse
import org.kiimo.me.main.sender.model.request.rate.RateDeliveryRequest
import org.kiimo.me.main.sender.model.response.SenderCreateDeliveryResponse
import org.kiimo.me.service.network.client.HttpHeaders.ContentTypeHeader


import org.kiimo.me.models.DeliveryType
import org.kiimo.me.models.DestinationData
import org.kiimo.me.models.DeviceToken
import org.kiimo.me.models.*
import org.kiimo.me.models.delivery.BaseDeliveryResponse
import org.kiimo.me.models.delivery.CalculateDeliveryRequest
import org.kiimo.me.models.delivery.CalculateDeliveryResponse
import org.kiimo.me.models.delivery.UploadSignatureRequest
import org.kiimo.me.models.payment.PreferredPayResponse
import org.kiimo.me.models.payment.PreferredPaymentUser
import org.kiimo.me.register.model.SmsValidationRequest
import org.kiimo.me.register.model.SmsValidationResponse
import org.kiimo.me.register.model.UserRegisterResponse
import org.kiimo.me.register.model.UserSmsCodeRequest
import org.kiimo.me.service.network.client.HttpHeaders.AuthorizationHeader
import org.kiimo.me.service.network.client.HttpHeaders.jsonHeader
import retrofit2.Response
import retrofit2.http.*

object HttpHeaders {

    const val AuthorizationHeader = "Authorization"
    const val ContentTypeHeader = "Content-Type"
    const val jsonHeader = "application/json"
}

interface KiimoDeliverHttpClient {

    @POST("api/sms/send-code")
    fun sendCode(
            @Header(ContentTypeHeader) contentType: String = jsonHeader,
            @Body smsCodeRequest: UserSmsCodeRequest): Observable<UserRegisterResponse>


    @POST("api/sms/validate-code")
    fun validateSmsCode(
            @Header(AuthorizationHeader) userID: String,
            @Body smsCodeRequest: SmsValidationRequest): Observable<SmsValidationResponse>

    @GET("api/self")
    fun getSelf(
            @Header("Authorization") token: String
    ): Observable<Self>


    @POST("api/upload")
    fun uploadPhotoApi(): Observable<String>

    @GET("api/self")
    fun getSelfData(
        @Header("Authorization") token: String
    ): Self


    @POST("api/deliveries/calculate")
    fun deliveryCalculate(
            @Header(AuthorizationHeader) token: String,
            @Body delivryCalculateRequest: CalculateDeliveryRequest): Observable<CalculateDeliveryResponse>


    @PUT("api/self/location")
    fun putLocation(
            @Header(ContentTypeHeader) contentType: String = jsonHeader,
            @Header(AuthorizationHeader) token: String,
            @Body locationRequest: LocationRequest
    ): Observable<Response<Void>>

    @PUT("api/self/delivery-type")
    fun putDeliveryType(
            @Header(ContentTypeHeader) contentType: String = jsonHeader,
            @Header(AuthorizationHeader) token: String,
            @Body deliveryType: DeliveryType
    ): Observable<Response<Void>>

    @PUT("api/self/deviceToken")
    fun putDeviceToken(
            @Header(ContentTypeHeader) contentType: String = jsonHeader,
            @Header(AuthorizationHeader) token: String,
            @Body deviceToken: DeviceToken
    ): Observable<Response<JsonElement>>


    @PUT("api/self/remove-device-token")
    fun removeDeviceToken(
            @Header(AuthorizationHeader) token: String,
            @Body deviceToken: DeviceToken
    ): Observable<Response<JsonElement>>

    @PUT("api/self/status")
    fun putStatus(
            @Header(ContentTypeHeader) contentType: String,
            @Header(AuthorizationHeader) token: String,
            @Body status: Status
    ): Observable<StatusResponse?>

    @POST("api/deliveries/{id}/accept")
    fun acceptDelivery(
            @Header(AuthorizationHeader) token: String,
            @Path("id") id: String
    ): Observable<Response<Void>>


    @POST("api/deliveries/pick-up")
    fun pickUp(
            @Header(AuthorizationHeader) token: String,
            @Body pickUpImageRequest: PackageImageRequest
    ): Observable<Response<Void>>

    @POST("api/deliveries/validate-code")
    fun validateCode(
            @Header(AuthorizationHeader) token: String,
            @Body code: ValidateCodeRequest
    ): Observable<ValidateCodeResponse?>

    @POST("api/deliveries/drop-off")
    fun dropOff(
            @Header(AuthorizationHeader) token: String,
            @Body dropOffRequest: DropOffRequest
    ): Observable<DropOffResponse?>


    @GET
    fun getDestinationData(@Url url: String): Observable<DestinationData?>

    @POST("api/deliveries")
    fun createPackageForDelivery(
            @Header(AuthorizationHeader) token: String,
            @Body request: CreateDeliveryRequest): Observable<SenderCreateDeliveryResponse>


    @POST("api/deliveries/pay")
    fun payForPackage(
            @Header(ContentTypeHeader) contentType: String = jsonHeader, @Header(
                AuthorizationHeader
            ) token: String, @Body payRequest: PayRequest
    ): Observable<PayResponse>


    @POST("api/upload")
    fun uploadImageToServer(
            @Header(AuthorizationHeader) token: String,
            @Body uploadPhotoRequest: UploadPhotoRequest): Observable<UploadImageResponse>


    @POST("api/upload")
    @FormUrlEncoded()
    fun uploadImageToServerField(
            @Header(AuthorizationHeader) token: String,
            @Header(
                ContentTypeHeader
            ) contentHeader: String,
            @Field("media") media: ByteArray,
            @Field("signature") signatureRequest: String
    ): Observable<UploadImageResponse>


    @PUT("api/self/preferred-payment")
    fun savePreferredPayment(
            @Header(AuthorizationHeader) token: String,
            @Body preferedPay: PreferredPaymentUser): Observable<PreferredPayResponse>


    @Multipart
    @POST("api/upload")
    fun uploadMultipardData(
            @Part media: MultipartBody.Part,
            @Part("type") requestBody: RequestBody): Observable<UploadImageResponse>

    @POST("api/deliveries/sender")
    fun getOrdersList(@Header(AuthorizationHeader) token: String): Observable<MutableList<SenderOrderListResponse>>

    @GET("api/deliveries?userType=carrier")
    fun getDeliveriesList(@Header(AuthorizationHeader) token: String): Observable<MutableList<SenderOrderListResponse>>

    @POST("api/deliveries/accept")
    fun rateUser(
            @Header(AuthorizationHeader) token: String,
            @Body rateDeliveryRequest: RateDeliveryRequest): Completable

    @POST("api/card-create")
    fun saveCreditCard(
            @Header(AuthorizationHeader) token: String,
            @Body cardSaveRequest: CreditCardSaveRequest): Observable<Response<JsonElement>>

    @FormUrlEncoded
    @POST("api/card-create")
    fun saveCreditCardFields(
            @Header(AuthorizationHeader) token: String,
            @Header("Content-Type") type: String = "application/x-www-form-urlencoded",
            @Field("cardNo") cardNum: String,
            @Field("cardNo") cardMonth: Int,
            @Field("cardNo") cardYear: Int,
            @Field("cv2") cv2: Int
    ): Observable<Response<JsonElement>>


    @Multipart
    @POST("api/card-create")
    fun saveCreditCardRequestBody(
            @Header(AuthorizationHeader) token: String,
            @Body requestBody: RequestBody
    ): Observable<Response<JsonElement>>


    @POST("api/card-update")
    fun updateCreditCard(
            @Header(AuthorizationHeader) token: String,
            @Body cardSaveRequest: CreditCardSaveRequest): Observable<Response<JsonElement>>

}