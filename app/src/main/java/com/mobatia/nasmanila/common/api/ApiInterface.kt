package com.mobatia.nasmanila.common.api

import com.mobatia.nasmanila.activities.absence.model.LeavesubmitApiModel
import com.mobatia.nasmanila.activities.absence.model.LeavesubmitResponseModel
import com.mobatia.nasmanila.activities.contact_us.model.StaffCDeptApiModel
import com.mobatia.nasmanila.activities.contact_us.model.StaffCDeptResponseModel
import com.mobatia.nasmanila.activities.contact_us.model.StaffCategoryResponseModel
import com.mobatia.nasmanila.activities.enrichment.CcaModel

import com.mobatia.nasmanila.activities.enrichment.model.Cca_detailsApiModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_detailsResponseModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_reviewsApiModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_reviewsResponseModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_submitApiModel
import com.mobatia.nasmanila.activities.enrichment.model.Cca_submitResponseModel
import com.mobatia.nasmanila.activities.login.model.ForgotPasswordApiModel
import com.mobatia.nasmanila.activities.login.model.ForgotPasswordModel
import com.mobatia.nasmanila.activities.login.model.LoginApiModel
import com.mobatia.nasmanila.activities.login.model.LoginModel
import com.mobatia.nasmanila.activities.login.model.ParentSignupApiModel
import com.mobatia.nasmanila.activities.login.model.ParentSignupModel
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailApiModel
import com.mobatia.nasmanila.activities.parent_essential.model.SendemailResponseModel
import com.mobatia.nasmanila.fragments.about_us.AboutUsResponseModel
import com.mobatia.nasmanila.fragments.about_us.model.AboutUsModel
import com.mobatia.nasmanila.fragments.absence.model.LeaveRequestsResponseModel
import com.mobatia.nasmanila.fragments.absence.model.LeaveRequestsApiModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistApiModel
import com.mobatia.nasmanila.fragments.absence.model.StudentlistResponseModel
import com.mobatia.nasmanila.fragments.calendar.model.CalendarResponseModel
import com.mobatia.nasmanila.fragments.contact_us.model.ContactUsResponseModel
import com.mobatia.nasmanila.fragments.enrichment.model.Enrichment_lessonsModel
import com.mobatia.nasmanila.fragments.home.model.HomeBannerApiModel
import com.mobatia.nasmanila.fragments.home.model.HomeBannerModel
import com.mobatia.nasmanila.fragments.notifications.model.NotificationsApiModel
import com.mobatia.nasmanila.fragments.notifications.model.NotificationsResponseModel
import com.mobatia.nasmanila.fragments.parent_essentials.model.ParentessentialsResponseModel
import com.mobatia.nasmanila.fragments.parents_meeting.model.SendemailstaffptaApiModel
import com.mobatia.nasmanila.fragments.parents_meeting.model.SendemailstaffptaResponseModel
import com.mobatia.nasmanila.fragments.parents_meeting.model.StafflistApiModel
import com.mobatia.nasmanila.fragments.parents_meeting.model.StafflistResponseModel
import com.mobatia.nasmanila.fragments.settings.model.ChangePasswordResponseModel
import com.mobatia.nasmanila.fragments.settings.model.ChangepasswordApiModel
import com.mobatia.nasmanila.fragments.settings.model.LogoutApiModel
import com.mobatia.nasmanila.fragments.settings.model.LogoutResponseModel
import com.mobatia.nasmanila.fragments.settings.model.Terms_of_serviceModel
import com.mobatia.nasmanila.fragments.social_media.model.SocialmediaResponseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @POST("Api-V1/login")
    @Headers("Content-Type: application/json")
    fun loginCall(
        @Body loginBody: LoginApiModel
    ): Call<LoginModel>

    @POST("Api-V1/forgotpassword")
    @Headers("Content-Type: application/json")
    fun forgot_password(
        @Body forgot_passwordBody: ForgotPasswordApiModel
    ): Call<ForgotPasswordModel>

    @POST("Api-V1/parent_signup")
    @Headers("Content-Type: application/json")
    fun parent_signup(
        @Body parent_signupBody: ParentSignupApiModel
    ): Call<ParentSignupModel>

    @POST("Api-V1/home_banner_images")
    @Headers("Content-Type: application/json")
    fun homebanner(
        @Header("Authorization") token:String,
        @Body homebannerBody: HomeBannerApiModel
    ): Call<HomeBannerModel>

    @POST("Api-V1/parents_essentials")
    @Headers("Content-Type: application/json")
    fun parentessentials(
        @Header("Authorization") token:String
    ): Call<ParentessentialsResponseModel>

    @POST("Api-V1/sendemail")
    @Headers("Content-Type: application/json")
    fun sendemailstaff(
        @Header("Authorization") token:String,
        @Body sendemailBody: SendemailApiModel
    ): Call<SendemailResponseModel>

    @POST("Api-V1/notifications")
    @Headers("Content-Type: application/json")
    fun notifications(
        @Header("Authorization") token:String,
        @Body notificationsBody: NotificationsApiModel
    ): Call<NotificationsResponseModel>

    @POST("Api-V1/calender")
    @Headers("Content-Type: application/json")
    fun calender(
        @Header("Authorization") token:String
    ): Call<CalendarResponseModel>

    @POST("Api-V1/social_media")
    @Headers("Content-Type: application/json")
    fun social_media(
        @Header("Authorization") token:String
    ): Call<SocialmediaResponseModel>
    @POST("Api-V1/contact_us")
    @Headers("Content-Type: application/json")
    fun contact_us(
        @Header("Authorization") token:String
    ): Call<ContactUsResponseModel>

    @POST("Api-V1/getstaffcategorylist")
    @Headers("Content-Type: application/json")
    fun getstaffcategorylist(
        @Header("Authorization") token:String
    ): Call<StaffCategoryResponseModel>

    @POST("Api-V1/getstaffdeptlist")
    @Headers("Content-Type: application/json")
    fun getstaffdeptlist(
        @Header("Authorization") token:String,
        @Body notificationsBody: StaffCDeptApiModel
    ): Call<StaffCDeptResponseModel>

    @POST("Api-V1/studentlist")
    @Headers("Content-Type: application/json")
    fun studentlist(
        @Header("Authorization") token:String,
        @Body studentlistBody: StudentlistApiModel
    ): Call<StudentlistResponseModel>

    @POST("Api-V1/leaveRequests")
    @Headers("Content-Type: application/json")
    fun leaveRequests(
        @Header("Authorization") token:String,
        @Body leaveRequestsBody: LeaveRequestsApiModel
    ): Call<LeaveRequestsResponseModel>
    @POST("Api-V1/requestLeave")
    @Headers("Content-Type: application/json")
    fun requestLeave(
        @Header("Authorization") token:String,
        @Body leaveSubmitBody: LeavesubmitApiModel
    ): Call<LeavesubmitResponseModel>

    @POST("Api-V1/about_us")
    @Headers("Content-Type: application/json")
    fun about_us(
        @Header("Authorization") token:String
    ): Call<AboutUsResponseModel>

    @POST("Api-V1/changepassword")
    @Headers("Content-Type: application/json")
    fun changepassword(
        @Header("Authorization") token:String,
        @Body changepasswordBody: ChangepasswordApiModel
    ): Call<ChangePasswordResponseModel>

    @POST("Api-V1/pta_meeting")
    @Headers("Content-Type: application/json")
    fun sendemailstaffpta(
        @Header("Authorization") token:String,
        @Body sendemailstaffptaBody: SendemailstaffptaApiModel
    ): Call<SendemailstaffptaResponseModel>

    @POST("Api-V1/stafflist")
    @Headers("Content-Type: application/json")
    fun stafflist(
        @Header("Authorization") token:String,
        @Body stafflistBody: StafflistApiModel
    ): Call<StafflistResponseModel>

    @POST("Api-V1/enrichment_lessons")
    @Headers("Content-Type: application/json")
    fun enrichment_lessons(
        @Header("Authorization") token:String
    ): Call<Enrichment_lessonsModel>

    @POST("Api-V1/terms_of_service")
    @Headers("Content-Type: application/json")
    fun terms_of_service(
        @Header("Authorization") token:String
    ): Call<Terms_of_serviceModel>

    @POST("Api-V1/logout")
    @Headers("Content-Type: application/json")
    fun logout(
        @Header("Authorization") token:String,
        @Body LogoutBody: LogoutApiModel
    ): Call<LogoutResponseModel>
    @POST("Api-V1/delete_account")
    @Headers("Content-Type: application/json")
    fun delete_account(
        @Header("Authorization") token:String,
        @Body LogoutBody: LogoutApiModel
    ): Call<LogoutResponseModel>

    @POST("Api-V1/cca")
    @Headers("Content-Type: application/json")
    fun cca(
        @Header("Authorization") token:String
    ): Call<CcaModel>

    @POST("Api-V1/cca_details")
    @Headers("Content-Type: application/json")
    fun cca_details(
        @Header("Authorization") token:String,
        @Body cca_detailsBody: Cca_detailsApiModel
    ): Call<Cca_detailsResponseModel>

    @POST("Api-V1/cca_reviews")
    @Headers("Content-Type: application/json")
    fun cca_reviews(
        @Header("Authorization") token:String,
        @Body cca_reviewsBody: Cca_reviewsApiModel
    ): Call<Cca_reviewsResponseModel>

    @POST("Api-V1/cca_submit")
    @Headers("Content-Type: application/json")
    fun cca_submit(
        @Header("Authorization") token:String,
        @Body cca_submitBody: Cca_submitApiModel
    ): Call<Cca_submitResponseModel>





















    @POST("api/v1/parent/logout")
    fun logOut(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/v1/notification/list")
    fun pushNotificationsCall(
        @Field("start") start: Int,
        @Field("limit") limit: Int
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/v1/notification/details")
    fun pushNotificationDetail(
        @Field("notification_id")pushID: String
    ): Call<ResponseBody>

    @POST("api/v1/about_us")
    fun aboutUsListCall(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/v1/sendemail")
    fun sendEmailToStaffCall(
        @Field("staff_email")staffEmail:  String,
        @Field("title")title:  String,
        @Field("message") message: String
    ): Call<ResponseBody>

    @POST("api/v1/social_media")
    fun socialMediaListCall(): Call<ResponseBody>

    @POST("")
    fun nasTodayListCall(): Call<ResponseBody>

    fun newsLetterCategoryCall(): Call<ResponseBody>



    @FormUrlEncoded
    @POST("oauth/access_token")
    fun accessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientID: String,
        @Field("client_secret") clientSecret: String,
        @Field("username") userName: String,
        @Field("password") password: String
    ): Call<ResponseBody>




    @FormUrlEncoded
    @POST("api/logout")
    fun logOut(
        @Field("access_token") accessToken: String,
        @Field("users_id") userID: String,
        @Field("deviceid") deviceID: String,
        @Field("devicetype") deviceType: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/getnotifications")
    fun pushNotificationsCall(
        @Field("access_token") accessToken: String,
        @Field("deviceid") deviceID: String,
        @Field("devicetype") deviceType: String,
        @Field("users_id") userID: String,
        @Field("offset") offset: String,
        @Field("scroll_to") scrollTo: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/getnotification_details")
    fun pushNotificationDetail(
        @Field("access_token") accessToken: String,
        @Field("push_id") pushID: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/leaveRequests")
    fun getLeaveRequests(
        @Field("access_token") accessToken: String,
        @Field("users_id") userID: String,
        @Field("student_id") student_id: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/changepassword")
    fun changePassword(
        @Field("access_token") accessToken: String,
        @Field("userd_id") userId: String,
        @Field("current_password") toString: String,
        @Field("new_password") toString1: String,
        @Field("email") userEmail: Any,
        @Field("deviceid") token: String,
        @Field("devicetype") s: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/sendemail")
    fun sendEmailToStaffCall(
        @Field("access_token") accessToken: String,
        @Field("email") emailNas: String?,
        @Field("users_id") userId: String,
        @Field("title") toString: String,
        @Field("message") toString1: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/about_us")
    fun aboutUsListCall(
        @Field("access_token") accessToken: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/parent_essentials")
    fun newsLetterCategoryCall(
        @Field("access_token") accessToken: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/nastoday")
    fun nasTodayListCall(
        @Field("access_token") accessToken: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/contact_us")
    fun contactUsCall(
        @Field("access_token") accessToken: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/getstaffcategorylist")
    fun staffDirectoryListCall(
        @Field("access_token") accessToken: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("api/studentlist")
    fun getStudentListFirstCall(
        @Field("access_token") accessToken: String,
        @Field("users_id") userId: String
    ): Call<ResponseBody>
}
