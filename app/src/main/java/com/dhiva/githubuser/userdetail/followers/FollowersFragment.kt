package com.dhiva.githubuser.userdetail.followers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhiva.githubuser.R
import com.dhiva.githubuser.core.data.source.remote.response.User
import com.dhiva.githubuser.core.ui.ListUserAdapter
import com.dhiva.githubuser.databinding.FragmentViewPagerBinding
import com.dhiva.githubuser.home.MainActivity
import com.dhiva.githubuser.userdetail.UserDetailActivity
import com.dhiva.githubuser.userdetail.UserDetailViewModel

class FollowersFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    private val viewModel: UserDetailViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentViewPagerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.followers.observe(viewLifecycleOwner, {
            if (it.isEmpty()){
                binding.rvData.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
                binding.tvNoData.text = resources.getString(R.string.no_followers)
            } else {
                showRecyclerList(it)
            }
        })
        viewModel.isLoadingFollowers.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun showRecyclerList(listUsers: List<User>){
        binding.rvData.visibility = View.VISIBLE
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager = LinearLayoutManager(context)
        val listAdapter = ListUserAdapter(listUsers)
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