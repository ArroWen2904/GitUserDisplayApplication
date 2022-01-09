package arro.ex.gituserdisplayapplication.repositories

import arro.ex.gituserdisplayapplication.utils.OkHttpUtil
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Arro on 2022/1/8
 */
object ApiUtil {

    private const val DOMAIN: String = "api.github.com"
    private var apiInterface: ApiInterface = getApiInterface()
    const val PER_PAGE = 30

    private fun getApiInterface(): ApiInterface {
        return Retrofit.Builder()
            .client(OkHttpUtil.client)
            .baseUrl(getHostUri())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    private fun getHostUri(): String {
        return "https://$DOMAIN/"
    }

    fun ApiGetGitUser(searchKeyword: String = "", page: String = "0"): Single<Response<ResponseGitUser>> {
        return apiInterface.getGitUsers(searchKeyword, page)
    }
}