package com.dhiva.githubuser.userdetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dhiva.githubuser.R
import com.dhiva.githubuser.core.data.Resource
import com.dhiva.githubuser.core.domain.model.User
import com.dhiva.githubuser.core.ui.ViewModelFactory
import com.dhiva.githubuser.core.utils.loadImage
import com.dhiva.githubuser.databinding.ActivityUserDetailBinding
import com.dhiva.githubuser.home.MainActivity
import com.google.android.material.tabs.TabLayoutMediator


class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserDetailBinding
    private var user: User? = null
    private var username: String? = null
    private lateinit var userDetailViewModel: UserDetailViewModel
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initViewModel()
        getIntentData()
        initViewPager()
    }

    private fun initViewPager() {
        val sectionPagerAdapter = SectionPagerAdapter(this, username)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        userDetailViewModel = ViewModelProvider(this, factory)[UserDetailViewModel::class.java]

        userDetailViewModel.user.observe(this, { it ->
            when(it){
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.mainView.visibility = View.GONE
                    isErrorPHVisible(false)
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.mainView.visibility = View.VISIBLE
                    user = it.data
                    user?.let {
                        setDataToView(it)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.mainView.visibility = View.GONE
                    isErrorPHVisible(true)
                }
            }
        })

        userDetailViewModel.favoriteUsers.observe(this, {
            isFavorite = it.any { user -> user.id == this.user?.id }
            setFabSelected(isFavorite)
        })
    }

    private fun init() {
        binding.ibBack.setOnClickListener(this)
        binding.ibShare.setOnClickListener(this)
        binding.fabFav.setOnClickListener(this)
    }

    private fun getIntentData(){
        val username = intent.getStringExtra(MainActivity.EXTRA_USERNAME)
        username?.let {
            userDetailViewModel.setUsername(it)
            this.username = it
        }
    }

    private fun setDataToView(user: User){
        setFabSelected(user.isFavorite)
        with(binding){
            tvName.text = user.name
            tvUsername.text = user.login
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
            tvLocation.text = user.location
            tvCompany.text = user.company
            tvRepositories.text = resources.getString(R.string.repositories, user.publicRepos.toString())
            ivProfile.loadImage(user.avatarUrl)
        }
    }

    private fun shareContent(){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val username = user?.login
        val shareMessage = "https://github.com/$username"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.text_share)))
    }

    private fun isErrorPHVisible(b: Boolean){
        binding.ivError.visibility = if (b) View.VISIBLE else View.GONE
        binding.tvPhError.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ib_back -> {
                finish()
            }
            R.id.ib_share -> {
                shareContent()
            }
            R.id.fab_fav -> {
                setIsFavorite()
            }
        }
    }

    private fun setIsFavorite(){
        if (!isFavorite){
            user?.let { userDetailViewModel.setFavorite(it, !isFavorite) }
            setFabSelected(true)
        } else {
            showConfirmDialog()
        }
        isFavorite = !isFavorite
    }

    private fun setFabSelected(isSelected: Boolean){
        isFavorite = if (isSelected){
            binding.fabFav.setColorFilter(ContextCompat.getColor(this, R.color.pink))
            true
        } else {
            binding.fabFav.setColorFilter(ContextCompat.getColor(this, R.color.soft_gray))
            false
        }
    }

    private fun showConfirmDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.title_confirm_delete))
        builder.setMessage(getString(R.string.desc_confirm_delete))

        builder.setPositiveButton("YES") { dialog, _ ->
            user?.let {
                userDetailViewModel.setFavorite(it, isFavorite)
            }
            setFabSelected(false)
            dialog.dismiss()
        }

        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }

        val alert: AlertDialog = builder.create()
        alert.show()
    }


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }


}

