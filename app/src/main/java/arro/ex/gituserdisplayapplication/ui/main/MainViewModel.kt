package arro.ex.gituserdisplayapplication.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import arro.ex.gituserdisplayapplication.R
import arro.ex.gituserdisplayapplication.repositories.ApiUtil
import arro.ex.gituserdisplayapplication.repositories.GitUser
import arro.ex.gituserdisplayapplication.repositories.ResponseGitUser
import arro.ex.gituserdisplayapplication.utils.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Arro on 2022/1/8
 */
class MainViewModel(val disposable: CompositeDisposable) : ViewModel() {

    private val TAG = "MainViewModel"
    val gitUserList: SingleLiveEvent<MutableList<GitUser>> =
        SingleLiveEvent<MutableList<GitUser>>().apply { this.value = mutableListOf() }
    val searchKeyword: SingleLiveEvent<String> = SingleLiveEvent()
    val isLoading: SingleLiveEvent<Boolean> =
        SingleLiveEvent<Boolean>().apply { this.value = false }
    val toastMessage: SingleLiveEvent<Int> = SingleLiveEvent()

    var nextPageNumber: Int = 1
    var totalCount: Int = 0

    private val regex = "page=(.*?)>"
    private val nextPageKey = "link"

    fun getGitUser() {
        Log.w(TAG, "getGitUser: ")
        isLoading.value = true

        Log.w(TAG, "nextPageNumber before = ${nextPageNumber}")

        disposable.add(
            ApiUtil.ApiGetGitUser(searchKeyword.value!!, nextPageNumber.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<ResponseGitUser>>() {
                    override fun onSuccess(t: Response<ResponseGitUser>?) {
                        Log.w(TAG, "onSuccess")

                        if (t?.body()?.gitUserList?.size ?: 0 <= ApiUtil.PER_PAGE)
                            getNextPageNumber(t?.headers()?.get(nextPageKey))

                        totalCount = t?.body()?.totalCount ?: 0

                        Log.w(TAG, "nextPageNumber after = ${nextPageNumber}")
                        Log.w(TAG, "first = ${t?.body()?.gitUserList?.first()?.id}")

                        gitUserList.value?.addAll(t?.body()?.gitUserList ?: mutableListOf())
                        gitUserList.value = gitUserList.value

                        isLoading.value = false
                    }

                    override fun onError(e: Throwable?) {
                        Log.w(TAG, "onError")
                        toastMessage.value = R.string.please_check_network
                        isLoading.value = false
                    }
                })
        )
    }

    private fun getNextPageNumber(raw: String?) {
        raw?.let { raw ->
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(raw)

            while (matcher.find()) {
                Log.w(TAG, "getNextPageNumber: ${matcher.group(1)}")
                if (matcher.group(1).toInt() > nextPageNumber) {
                    nextPageNumber = matcher.group(1).toInt()
                    break
                }
            }
        }
    }
}