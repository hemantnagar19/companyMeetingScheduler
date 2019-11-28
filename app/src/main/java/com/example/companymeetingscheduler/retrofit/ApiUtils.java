package com.example.companymeetingscheduler.retrofit;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://fathomless-shelf-5846.herokuapp.com/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }


}
