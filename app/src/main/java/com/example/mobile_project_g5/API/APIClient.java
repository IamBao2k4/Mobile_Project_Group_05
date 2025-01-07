package com.example.mobile_project_g5;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    // URL cơ bản của API remove.bg
    private static final String Base_Url = "https://api.remove.bg/v1.0/";

    // Biến static giữ đối tượng Retrofit duy nhất (Singleton pattern)
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        // Trả về đối tượng Retrofit đã được khởi tạo
        return retrofit;
    }
}
