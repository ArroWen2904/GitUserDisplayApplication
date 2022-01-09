package arro.ex.gituserdisplayapplication.utils

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import arro.ex.gituserdisplayapplication.ui.main.MainViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Created by Arro on 2022/1/8
 */
class ViewModelFactory(val disposable: CompositeDisposable): ViewModelProvider.Factory{

    @NonNull
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(disposable) as T
            else -> null as T
        }
    }
}