package com.example.mobile_project_g5;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RemoveBgAPI {

    @Multipart
    @POST("removebg")
    Call<ResponseBody> removeBackground(
            @Header("X-Api-Key") String apiKey,
            @Part MultipartBody.Part image
    );

}