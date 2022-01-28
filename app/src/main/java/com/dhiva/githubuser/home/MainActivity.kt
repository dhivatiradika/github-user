package com.dhiva.githubuser.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhiva.githubuser.R
import com.dhiva.githubuser.core.ui.ListUserAdapter
import com.dhiva.githubuser.core.data.source.remote.response.User
import com.dhiva.githubuser.databinding.ActivityMainBinding
import com.dhiva.githubuser.favorite.FavoriteActivity
import com.dhiva.githubuser.userdetail.UserDetailActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListUserAdapter
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initSearchView()
    }

    private fun initView() {
        binding.ibFavorite.setOnClickListener(this)
        binding.ibSetting.setOnClickListener(this)
    }

    private fun initViewModel(){
        mainViewModel.users.observe(this, {
            if (it.isNotEmpty()){
                showRecyclerList(it)
            } else{
                binding.rvUser.visibility = View.GONE
                isNotFoundPHVisible(true)
            }
        })

        mainViewModel.isLoading.observe(this,{
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.rvUser.visibility = View.GONE
            isSearchPHVisible(false)
            isNotFoundPHVisible(false)
            isErrorPHVisible(false)
        })

        mainViewModel.isFailure.observe(this, {
            if (it){
                binding.rvUser.visibility = View.GONE
                isErrorPHVisible(true)
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
            if (!b){
                binding.rvUser.visibility = View.GONE
                isSearchPHVisible(true)
                isErrorPHVisible(false)
                isNotFoundPHVisible(false)
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

    private fun showRecyclerList(listUsers: List<User>){
        binding.rvUser.visibility = View.VISIBLE
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        listAdapter = ListUserAdapter(listUsers)
        binding.rvUser.adapter = listAdapter

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
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
            R.id.ib_favorite -> startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
            R.id.ib_setting -> SettingDialog().show(supportFragmentManager, "SettingFragment")
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

}