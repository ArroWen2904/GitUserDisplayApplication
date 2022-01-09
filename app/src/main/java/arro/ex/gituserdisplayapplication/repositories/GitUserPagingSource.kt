package arro.ex.gituserdisplayapplication.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException


/**
 * Created by Arro on 2022/1/8
 */
//class GitUserPagingSource(private val apiInterface: ApiInterface): PagingSource<String, GitUser>() {
//
//    override suspend fun load(params: LoadParams<String>): LoadResult<String, GitUser> {
//        return try {
//            val gitUserList = apiInterface.getGitUsers("").
//
//            LoadResult.Page(gitUserList)
//        } catch (e: IOException) {
//            LoadResult.Error(e)
//        } catch (e: HttpException) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<String, GitUser>): String? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey
//        }
//    }
//}