package arro.ex.gituserdisplayapplication.repositories

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Arro on 2022/1/8
 */
interface ApiInterface {

    @GET("search/users")
    fun getGitUsers(
        @Query("q") searchKeyword: String,
        @Query("page") page: String
    ): Single<Response<ResponseGitUser>>
}