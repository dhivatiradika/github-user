package com.dhiva.githubuser.userdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.dhiva.githubuser.R
import com.dhiva.githubuser.core.data.source.remote.response.User
import com.dhiva.githubuser.databinding.ActivityUserDetailBinding
import com.dhiva.githubuser.home.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import androidx.appcompat.app.AlertDialog
import com.dhiva.githubuser.core.data.source.local.entity.UserEntity


class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserDetailBinding
    private var user: User? = null
    private var userFav: UserEntity? = null
    private var username: String? = null
    private val userDetailViewModel: UserDetailViewModel by viewModels()
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        getIntentData()
        initViewModel()
        initViewPager()
    }

    private fun initViewPager() {
        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun initViewModel() {
        userDetailViewModel.user.observe(this, {
            user = it
            setDataToView(it)
        })
        userDetailViewModel.isFollowingFragmentCreated.observe(this, {
            if (it) username?.let { it1 -> userDetailViewModel.getFollowing(it1, 1) }
        })
        userDetailViewModel.isLoading.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.mainView.visibility = if (!it) View.VISIBLE else View.GONE
            isErrorPHVisible(false)
        })
        userDetailViewModel.isFailure.observe(this, {
            if (it){
                binding.mainView.visibility = View.GONE
                isErrorPHVisible(true)
            }
        })
        username?.let {
            userDetailViewModel.getUserByUsername(it)?.observe(this, { userEntity ->
                isFavorite = userEntity != null
                setFabSelected(userEntity != null)
                userFav = userEntity
            })
        }
    }

    private fun init() {
        binding.ibBack.setOnClickListener(this)
        binding.ibShare.setOnClickListener(this)
        binding.fabFav.setOnClickListener(this)
    }

    private fun getIntentData(){
        val username = intent.getStringExtra(MainActivity.EXTRA_USERNAME)
        username?.let {
            userDetailViewModel.getUser(it)
            userDetailViewModel.getFollowers(it, 1)
            this.username = it
        }
    }

    private fun setDataToView(user: User){
        with(binding){
            tvName.text = user.name
            tvUsername.text = user.login
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
            tvLocation.text = user.location
            tvCompany.text = user.company
            tvRepositories.text = resources.getString(R.string.repositories, user.publicRepos.toString())
        }

        binding.ivProfile.loadImage(user.avatarUrl)

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
            user?.let { userDetailViewModel.insertToFavorite(it) }
            setFabSelected(true)
        } else {
            showConfirmDialog()
        }
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
            userFav?.let {
                userDetailViewModel.deleteFavorite(it)
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

