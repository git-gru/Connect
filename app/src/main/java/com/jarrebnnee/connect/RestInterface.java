package com.jarrebnnee.connect;

import com.jarrebnnee.connect.Helper.Device.Device;
import com.jarrebnnee.connect.Helper.Login.Login;
import com.jarrebnnee.connect.Helper.Registration.Register;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by kundan on 8/8/2015.
 */
public interface RestInterface {
    @FormUrlEncoded
    @POST("/Device.php?")
    void getDeviceResponce(@FieldMap HashMap<String, String> params, Callback<Device> cb);

    @FormUrlEncoded
    @POST("/Register.php?")
    void getRegisterResponce(@FieldMap HashMap<String, String> params, Callback<Register> cb);

    @FormUrlEncoded
    @POST("/login.php?")
    void getLoginResponce(@FieldMap HashMap<String, String> params, Callback<Login> cb);

   /* @GET("/")
    void getCategoryResponce(Callback<Category> cb);

    @FormUrlEncoded
    @POST("/login.php?")
    void getListingCategoryMarket(@FieldMap HashMap<String, String> params, Callback<ListingCategory> cb);

    @FormUrlEncoded
    @POST("/login.php?")
    void getSingleMarketPlace(@FieldMap HashMap<String, String> params, Callback<SingleMarketPlace> cb);

    @FormUrlEncoded
    @POST("/login.php?")
    void getSearchMarketPlace(@FieldMap HashMap<String, String> params, Callback<SearchFIlter> cb);*/



}
