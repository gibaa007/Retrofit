package com.g7.retrofit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by newagesmb on 26/2/18.
 */

public interface APIService {
    @POST("login")
    @FormUrlEncoded
    Observable<CommonModal<UserPojo>> login(@Field("username") String title,
                                            @Field("password") String body);


    @GET("getDashboardCounts")
    Observable<CommonModal<UserPojo>> home(@HeaderMap() Map<String, String> header);

    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> repoContributors(
            @Path("owner") String owner,
            @Path("repo") String repo);


    @GET("user")
    Call<CommonModal<Contributor>> getUser(@QueryMap Map<String, String> params);

    @POST("user")
    Call<Contributor> postUser(@QueryMap Map<String, String> params);

    @PUT("user")
    Call<Contributor> updateUser(@QueryMap Map<String, String> params);


    @GET("users")
    Call<List<Contributor>> getUsers();

    @GET("users/{name}/commits")
    Call<List<Contributor>> getCommitsByName(@Path("name") String name);

    @GET("users")
    Call<Contributor> getUserById(@Query("id") Integer id);

    @POST("users")
    Call<Contributor> postUser(@Body Contributor user);


    @Multipart
    @POST("image/upload")
    Observable<Contributor> uploadImage(@Header("Header") String token,
                                        @Part("userId") String userId,
                                        @Part("images") RequestBody file);


    @GET("books")
    Call<ArrayList<UserPojo>> listBooks();

    @POST("books")
    Call<UserPojo> addBook(@Body UserPojo book);

    @GET("books/{id}/")
    Call<UserPojo> getBookInfo(@Path("id") int bookId);

    @DELETE("books/{id}/")
    Call<Void> deleteBook(@Path("id") int bookId);

    @PUT("books/{id}/")
    Call<UserPojo> updateBook(@Path("id") int bookId, @Body UserPojo book);

    @DELETE("clean/")
    Call<Void> deleteAll();


    @DELETE("/api/item/{id}")
    Call<Contributor> deleteItem(@Path("id") int itemId);


    @Multipart
    @POST("/post")
    Call<Contributor> addBookCover(@Part("id") RequestBody id, @Part MultipartBody.Part photo);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}
