package com.dhiva.githubuser.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhiva.githubuser.R
import com.dhiva.githubuser.core.data.Resource
import com.dhiva.githubuser.core.domain.model.User
import com.dhiva.githubuser.core.ui.ListUserAdapter
import com.dhiva.githubuser.databinding.ActivityMainBinding
import com.dhiva.githubuser.userdetail.UserDetailActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val listAdapter = ListUserAdapter()
    private val listAllUserAdapter = ListUserAdapter()
    private val mainViewModel: MainViewModel by viewModel()
    private var isFocus: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initSearchView()
        initRecyclerView()
    }

    private fun initView() {
        binding.ibFavorite.setOnClickListener(this)
        binding.ibSetting.setOnClickListener(this)
    }

    private fun initViewModel(){
        mainViewModel.allUsers.observe(this, {users ->
            if (users != null){
                when(users){
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvUser.visibility = View.GONE
                        isSearchPHVisible(false)
                        isNotFoundPHVisible(false)
                        isErrorPHVisible(false)
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE

                        var listUser = listOf<User>()
                        users.data?.let {
                            listUser = it
                        }

                        if (isFocus){
                            if (listUser.isNotEmpty()){
                                binding.rvAllUser.visibility = View.VISIBLE
                                listAllUserAdapter.setData(users.data)
                            } else {
                                binding.rvAllUser.visibility = View.GONE
                                isNotFoundPHVisible(true)
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvUser.visibility = View.GONE
                        isErrorPHVisible(true)
                    }
                }
            }
        })

        mainViewModel.users.observe(this, { users ->
            if (users != null){
                when(users){
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvUser.visibility = View.GONE
                        isSearchPHVisible(false)
                        isNotFoundPHVisible(false)
                        isErrorPHVisible(false)
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE

                        var listUser = listOf<User>()
                        users.data?.let {
                            listUser = it
                        }

                        if (!isFocus){
                            if (listUser.isNotEmpty()){
                                binding.rvUser.visibility = View.VISIBLE
                                listAdapter.setData(users.data)
                            } else {
                                binding.rvUser.visibility = View.GONE
                                isNotFoundPHVisible(true)
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvUser.visibility = View.GONE
                        isErrorPHVisible(true)
                    }
                }
            }
        })

        mainViewModel.getThemeSettings().observe(this, {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
    }

    private fun initSearchView(){
        binding.searchView.setOnQueryTextFocusChangeListener  { _, b ->
            binding.imageView.visibility = if (b) View.GONE else View.VISIBLE
            isFocus = !b
            if (!b){
                binding.rvUser.visibility = View.GONE
                binding.rvAllUser.visibility = View.VISIBLE
                isSearchPHVisible(false)
                isErrorPHVisible(false)
                isNotFoundPHVisible(false)
            } else {
                binding.rvAllUser.visibility = View.GONE
                isSearchPHVisible(true)
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let { mainViewModel.searchUser(it) }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }

    private fun initRecyclerView(){
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = listAdapter

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })

        binding.rvAllUser.setHasFixedSize(true)
        binding.rvAllUser.layoutManager = LinearLayoutManager(this)
        binding.rvAllUser.adapter = listAllUserAdapter

        listAllUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User){
        val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
        intent.putExtra(EXTRA_USERNAME, user.login)
        startActivity(intent)
    }

    private fun isSearchPHVisible(isVisible: Boolean){
        binding.ivSearch.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.tvPhSearch.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun isNotFoundPHVisible(isVisible: Boolean){
        binding.ivNotFound.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.tvPhNotFound.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun isErrorPHVisible(isVisible: Boolean){
        binding.ivError.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.tvPhError.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    override fun onClick(p0: View?) {
        when (p0?.id){
            R.id.ib_favorite -> {
                val uri = Uri.parse("githubapp://favorite")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
            R.id.ib_setting -> SettingDialog().show(supportFragmentManager, "SettingFragment")
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

}