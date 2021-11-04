package com.dicoding.githubsearch

import retrofit2.Call
import retrofit2.http.*

//interface ApiService {
//    @GET("detail/{id}")
//    fun getRestaurant(
//        @Path("id") id: String
//    ): Call<RestaurantResponse>
//
//    @FormUrlEncoded
//    @Headers("Authorization: token 12345")
//    @POST("review")
//    fun postReview(
//        @Field("id") id: String,
//        @Field("name") name: String,
//        @Field("review") review: String
//    ): Call<PostReviewResponse>
//}
interface ApiService {
    @GET("search/users?")
    fun getSearch(
        @Query("q") login: String
    ): Call<SearchResponse>
    @GET("users/{login}")
    fun getDetail(
        @Path("login") login: String
    ) : Call<DetailResponse>
    @GET("users/{login}/following")
    fun getFollowing(
        @Path("login") login: String
    ) : Call<FollowingResponse>
    @GET("users/{login}/followers")
    fun getFollowers(
        @Path("login") login: String
    ) : Call<FollowersResponse>
}