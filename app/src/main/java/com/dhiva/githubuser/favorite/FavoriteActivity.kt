
package com.dhiva.githubuser.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhiva.githubuser.core.ui.ListUserAdapter
import com.dhiva.githubuser.core.data.source.remote.response.User
import com.dhiva.githubuser.databinding.ActivityFavoriteBinding
import com.dhiva.githubuser.home.MainActivity
import com.dhiva.githubuser.userdetail.UserDetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        favoriteViewModel.listUser.observe(this, {
            val users : List<User> = it.map { userEntity ->
                User(userEntity.followers, userEntity.name, userEntity.avatarUrl, userEntity.following,
                        userEntity.company, userEntity.location, userEntity.publicRepos, userEntity.login)
            }
            showRecyclerList(users)
        })
    }

    private fun initView() {
        binding.ibBack.setOnClickListener { finish() }
    }

    private fun showRecyclerList(listUsers: List<User>){
        binding.rvUser.visibility = View.VISIBLE
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listAdapter = ListUserAdapter(listUsers)
        binding.rvUser.adapter = listAdapter

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
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