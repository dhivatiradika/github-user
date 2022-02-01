package com.dhiva.githubuser.userdetail.followers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhiva.githubuser.R
import com.dhiva.githubuser.core.data.Resource
import com.dhiva.githubuser.core.domain.model.User
import com.dhiva.githubuser.core.ui.ListUserAdapter
import com.dhiva.githubuser.databinding.FragmentViewPagerBinding
import com.dhiva.githubuser.home.MainActivity
import com.dhiva.githubuser.userdetail.UserDetailActivity
import com.dhiva.githubuser.userdetail.UserDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FollowersFragment(private val username: String?) : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    private val viewModel: UserDetailViewModel by viewModel()
    private val listAdapter = ListUserAdapter()

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentViewPagerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerList()
        viewModel.followers.observe(viewLifecycleOwner, { users ->
            when(users){
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE

                    var listUser = listOf<User>()
                    users.data?.let {
                        listUser = it
                    }

                    if (listUser.isNotEmpty()){
                        listAdapter.setData(users.data)
                    } else {
                        binding.tvNoData.visibility = View.VISIBLE
                        binding.tvNoData.text = resources.getString(R.string.no_followers)
                    }
                }
                is Resource.Error -> {
                    binding.rvData.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.tvNoData.text = users.message
                }
            }
        })

        username?.let {
            viewModel.setUsername(it)
        }
    }

    private fun initRecyclerList(){
        binding.rvData.visibility = View.VISIBLE
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager = LinearLayoutManager(context)
        binding.rvData.adapter = listAdapter

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User){
        val intent = Intent(activity, UserDetailActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_USERNAME, user.login)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}