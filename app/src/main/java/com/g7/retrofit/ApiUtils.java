package com.g7.retrofit;

/**
 * Created by newagesmb on 26/2/18.
 */

public class ApiUtils {
    private ApiUtils() {}

//    public static final String BASE_URL = "http://newagesme.com:3040/";
    public static final String BASE_URL = "http://10.10.10.91:5550/client/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
