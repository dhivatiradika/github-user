
package com.dhiva.githubuser.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhiva.githubuser.core.domain.model.User
import com.dhiva.githubuser.core.ui.ListUserAdapter
import com.dhiva.githubuser.core.ui.ViewModelFactory
import com.dhiva.githubuser.databinding.ActivityFavoriteBinding
import com.dhiva.githubuser.home.MainActivity
import com.dhiva.githubuser.userdetail.UserDetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val listUserAdapter = ListUserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initRecyclerList()
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        favoriteViewModel.listUser.observe(this, {
            if (it.isNotEmpty()){
                with(binding){
                    ivNotFound.visibility = View.GONE
                    tvPhNotFound.visibility = View.GONE
                }
                listUserAdapter.setData(it)
            } else {
                with(binding){
                    ivNotFound.visibility = View.VISIBLE
                    tvPhNotFound.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun initView() {
        binding.ibBack.setOnClickListener { finish() }
    }

    private fun initRecyclerList(){
        binding.rvUser.visibility = View.VISIBLE
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User){
        val intent = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_USERNAME, user.login)
        startActivity(intent)
    }
}