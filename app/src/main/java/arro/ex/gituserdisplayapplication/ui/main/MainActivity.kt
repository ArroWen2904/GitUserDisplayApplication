package arro.ex.gituserdisplayapplication.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arro.ex.gituserdisplayapplication.R
import arro.ex.gituserdisplayapplication.databinding.ActivityMainBinding
import arro.ex.gituserdisplayapplication.utils.ViewModelFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    val disposable: CompositeDisposable = CompositeDisposable()
    var gitUserAdapter = GitUserAdapter()
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModelFactory: ViewModelFactory = ViewModelFactory(disposable)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initRecycleView()

        setObserve()

        btn_search.setOnClickListener {
            viewModel.searchKeyword.value = sv_keyword.query.toString()
        }

    }

    private fun initRecycleView() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_git_user.adapter = gitUserAdapter
        rv_git_user.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.item_decoration
            )!!
        )
        rv_git_user.addItemDecoration(dividerItemDecoration)

        rv_git_user.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (viewModel.isLoading.value != true
                    && viewModel.totalCount > totalItemCount
                    && lastVisibleItem + 1 >= totalItemCount
                    && viewModel.gitUserList.value?.size ?: 0 > 0
                )
                    viewModel.getGitUser()
            }
        })
    }

    private fun setObserve() {

        viewModel.searchKeyword.observe(this, Observer {
            if (it.isNullOrEmpty())
                viewModel.toastMessage.value = R.string.search_keyword_cant_empty
            else {
                viewModel.gitUserList.value = mutableListOf()
                viewModel.nextPageNumber = 1
                viewModel.totalCount = 0
                viewModel.getGitUser()
            }
        })

        viewModel.gitUserList.observe(this, Observer {
            gitUserAdapter.updateGitUserList(it)
        })

        viewModel.toastMessage.observe(this, Observer {
            Toast.makeText(this, getString(it), Toast.LENGTH_SHORT).show()
        })
    }
}