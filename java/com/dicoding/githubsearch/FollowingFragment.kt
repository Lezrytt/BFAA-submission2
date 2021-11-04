package com.dicoding.githubsearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubsearch.databinding.FragmentFollowingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    private lateinit var adapter: ReviewAdapter
    private lateinit var detailViewModel: DetailViewModel
    private var _binding: FragmentFollowingBinding? = null

    private val binding get() = _binding!!

    companion object {
        var TAG = "FollowingFragment"
        const val EXTRA_USER = "extra_user"
        private const val SectionNumber = "section_number"
        @JvmStatic
        fun newInstance(string: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
//                    putInt(SectionNumber, index)
                    putString(EXTRA_USER, string)
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getString(EXTRA_USER)

//        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
//        if (user != null) {
//        val test = detailViewModel.findFollowing(user.toString())
//        }
        binding.tvName.text = "Following"

//        detailViewModel.listFollowing.observe(viewLifecycleOwner, {
//                listFollowing -> setFollowingData(listFollowing)
//        })

        binding.rvFollowing.layoutManager = LinearLayoutManager(requireContext())
        findFollowing()
    }

    private fun findFollowing() {
//        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(EXTRA_USER)
        client.enqueue(object : Callback<FollowingResponse> {
            override fun onResponse(
                call: Call<FollowingResponse>,
                response: Response<FollowingResponse>
            ) {
//                _isLoading.value = false
                if (response.isSuccessful) {
//                    _listFollowing.value = response.body()?.followingResponse
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setFollowingData(responseBody.followingResponse)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FollowingResponse>, t: Throwable) {
//                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun setFollowingData(listFollowing: List<FollowingResponseItem>) {
        val listUser: ArrayList<User> = ArrayList()
//        listUser.clear()
        for (user in listFollowing) {
            val userList = User(user.login, user.avatarUrl)
            listUser.add(userList)
        }
        val adapter = ReviewAdapter(listUser)
        binding.rvFollowing.adapter = adapter
        adapter.setOnItemClickCallback(object : ReviewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }
    private fun showSelectedUser(user: User) {
        val moveWithObjectIntent = Intent(activity, DetailActivity::class.java)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveWithObjectIntent)
    }

}