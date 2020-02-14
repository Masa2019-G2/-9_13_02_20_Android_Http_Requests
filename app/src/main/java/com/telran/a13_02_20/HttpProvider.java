package com.telran.a13_02_20;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpProvider {
    private Gson gson;
    private OkHttpClient client;
    private MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String BASE_URL = "https://contacts-telran.herokuapp.com";

    public HttpProvider() {
        gson = new Gson();
        client = new OkHttpClient();
    }


    public String registration(String email, String password) throws IOException {
        Auth auth = new Auth(email,password);
        String json = gson.toJson(auth);

        RequestBody body = RequestBody.create(JSON,json);

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/registration")
//                .addHeader("Authorization",token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            String responseJson = response.body().string();
            AuthToken token = gson.fromJson(responseJson,AuthToken.class);
            return token.getToken();
        }else if(response.code() == 400 || response.code() == 409){
            String responseJson = response.body().string();
            Error error = gson.fromJson(responseJson,Error.class);
            throw new RuntimeException(error.getMessage());
        }else{
            Log.e("MY_TAG", "registration: " + response.body().string());
            throw new RuntimeException("Server error! Call to support!");
        }
    }
}
