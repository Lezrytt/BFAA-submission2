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
import com.dicoding.githubsearch.databinding.FragmentFollowerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {

//    private lateinit var adapter: ReviewAdapter
    private lateinit var detailViewModel: DetailViewModel
    private var _binding: FragmentFollowerBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFollowerBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getString(FollowingFragment.EXTRA_USER)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

//        val test = detailViewModel.findFollowers()
        binding.tvCategoryName.text = "followers"

//        binding.rvFollower.layoutManager = LinearLayoutManager(requireContext())
        detailViewModel.listFollowers.observe(viewLifecycleOwner, {
            listFollowers -> setFollowersData(listFollowers)
        })
        findFollowers()
        binding.rvFollower.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun findFollowers() {
//        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(EXTRA_USER)
        client.enqueue(object : Callback<FollowersResponse> {
            override fun onResponse(
                call: Call<FollowersResponse>,
                response: Response<FollowersResponse>
            ) {
//                _isLoading.value = false
                if (response.isSuccessful) {
//                    _listFollowers.value = response.body()?.followersResponse
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setFollowersData(responseBody.followersResponse)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FollowersResponse>, t: Throwable) {
//                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFollowersData(listFollowers: List<FollowersResponseItem>) {
        val listUser: ArrayList<User> = ArrayList()
//        listUser.clear()
        for (user in listFollowers) {
            val userList = User(user.login, user.avatarUrl)
            listUser.add(userList)
        }
        val adapter = ReviewAdapter(listUser)
        binding.rvFollower.adapter = adapter
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
        detailViewModel = DetailViewModel()
    }

    companion object {
        const val EXTRA_USER ="extra_user"
        private const val TAG ="FollowerFragment"
        @JvmStatic
        fun newInstance(string: String) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_USER, string)
                }
            }
    }
}