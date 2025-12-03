package com.example.ep_benjaminvillegasrojas_02122025;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FakeStoreApiService {

    @GET("products")
    Call<List<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @POST("products")
    Call<Product> createProduct(@Body Product product);

    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") int id, @Body Product product);

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") int id);
}
