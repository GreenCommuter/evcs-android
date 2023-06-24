package org.evcs.android.network.service;

import org.evcs.android.model.user.AuthUser;
import org.evcs.android.model.user.CodeWrapper;
import org.evcs.android.model.user.NameWrapper;
import org.evcs.android.model.user.PhoneWrapper;
import org.evcs.android.model.user.User;
import org.evcs.android.model.user.UserCar;
import org.evcs.android.model.user.UserRequest;
import org.evcs.android.model.user.UserRequestFacebook;
import org.evcs.android.model.user.UserRequestGoogle;
import org.evcs.android.model.user.UserRequestSignup;
import org.evcs.android.model.user.ZipCodeWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface UserService {

    @POST("/members/v1/login")
    Call<AuthUser> logIn(@Body UserRequest user);

    @POST("/members/v1/login")
    Call<AuthUser> logInGoogle(@Body UserRequestGoogle user);

    @POST("/members/v1/login")
    Call<AuthUser> logInFacebook(@Body UserRequestFacebook user);

    @POST
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<UserRequestGoogle.Tokens> getAccessToken(@Url String url,
                                                  @Field("grant_type") String grantType,
                                                  @Field("client_id") String clientId,
                                                  @Field("client_secret") String clientSecret,
                                                  @Field("redirect_uri") String redirectUri,
                                                  @Field("code") String serverOauthCode,
                                                  @Field("id_token") String idToken);

    @POST("/members/v1/users")
    Call<AuthUser> register(@Body UserRequestSignup user);

    @POST("/members/v1/users/send_phone_verification_code")
    Call<Void> sendPhoneToVerify(@Body PhoneWrapper phone);

    @POST("/members/v1/users/verify_phone")
    Call<Void> sendCode(@Body CodeWrapper code);

    @GET("/members/v1/users/me")
    Call<User> getCurrentUser();

    @PUT("/members/v1/users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body Object userWrapper);

    @POST("/members/v1/user_cars")
    Call<UserCar> saveUserCar(@Body UserCar userCar);

    @POST("/members/v1/reset_password")
    Call<Void> changePassword(@Query("email") String email,
                              @Query("identifier") String identifier,
                              @Query("password") String newPassword,
                              @Query("password_confirmation") String confirmation);

    @POST("/members/v1/update_password")
    Call<Void> changePassword(@Query("previous_password") String oldPassword,
                              @Query("password") String newPassword,
                              @Query("password_confirmation") String confirmation);

    @GET("/members/v1/forgot_password")
    Call<Void> requestPasswordReset(@Query("email") String email);

    @DELETE("/members/v1/logout")
    Call<Void> logOut(@Query("device_token") String token);

}
