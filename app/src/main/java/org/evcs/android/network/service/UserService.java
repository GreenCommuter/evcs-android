package org.evcs.android.network.service;

import org.evcs.android.model.user.AuthUser;
import org.evcs.android.model.user.CodeWrapper;
import org.evcs.android.model.user.NameWrapper;
import org.evcs.android.model.user.PhoneWrapper;
import org.evcs.android.model.user.User;
import org.evcs.android.model.user.UserCar;
import org.evcs.android.model.user.UserRequest;
import org.evcs.android.model.user.UserRequestSignup;
import org.evcs.android.model.user.ZipCodeWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("/members/v1/login")
    Call<AuthUser> logIn(@Body UserRequest user);

//    @POST("/members/v1/login")
//    Call<AuthUser> logInGoogle(@Body UserRequestGoogle user);

//    @POST("/members/v1/login")
//    Call<AuthUser> logInFacebook(@Body UserRequestFacebook user);

//    @POST
//    @FormUrlEncoded
//    @Headers("Content-Type:application/x-www-form-urlencoded")
//    Call<UserRequestGoogle.Tokens> getAccessToken(@Url String url,
//                                                  @Field("grant_type") String grantType,
//                                                  @Field("client_id") String clientId,
//                                                  @Field("client_secret") String clientSecret,
//                                                  @Field("redirect_uri") String redirectUri,
//                                                  @Field("code") String serverOauthCode,
//                                                  @Field("id_token") String idToken);

    @POST("/members/v1/users")
    Call<AuthUser> register(@Body UserRequestSignup user);

    @POST("/members/v1/users/send_phone_verification_code")
    Call<Void> sendPhoneToVerify(@Body PhoneWrapper phone);

    @POST("/members/v1/users/verify_phone")
    Call<Void> sendCode(@Body CodeWrapper code);

//    @GET("/api/v1/users/me")
//    Call<User> getCurrentUser();

    @PUT("/members/v1/users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body ZipCodeWrapper zipcode);

    @PUT("/members/v1/users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body NameWrapper zipcode);

    @POST ("/members/v1/user_cars")
    Call<UserCar> saveUserCar(@Body UserCar userCar);

}
