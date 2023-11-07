package com.example.shoppingdemo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingdemo.R
import com.example.shoppingdemo.ShoppingViewModel
import com.example.shoppingdemo.ShoppingViewModelFactory
import com.example.shoppingdemo.adapter.FavoriteAdapter
import com.example.shoppingdemo.database.DBHelper
import com.example.shoppingdemo.databinding.FragmentFavoriteBinding
import com.example.shoppingdemo.database.ShoppingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private var TAG: String = FavoriteFragment::class.java.simpleName
    private val binding get() = _binding!!
    private var favoriteAdapter: FavoriteAdapter? = null
    private lateinit var viewModel: ShoppingViewModel

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cartDao = DBHelper.getInstance(requireContext()).cartDao()
        val favoriteDao = DBHelper.getInstance(requireContext()).favoriteDao()
        val shoppingDao = DBHelper.getInstance(requireContext()).shoppingDao()
        val repository = ShoppingRepository(cartDao, favoriteDao, shoppingDao)
        viewModel = ViewModelProvider(this,
            ShoppingViewModelFactory(repository))[ShoppingViewModel::class.java]

        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(
            R.id.nav_graph_host_fragment
        ) as NavHostFragment


        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favoriteAdapter = FavoriteAdapter(requireContext())
        val recyclerView = binding.rvFavorite
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = favoriteAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val favoriteItem = viewModel.fetchAllFavorite()
            CoroutineScope(Dispatchers.Main).launch {
                if (favoriteItem.isNotEmpty()) {
                    favoriteAdapter?.apply {
                        Log.d(TAG, "categories: categories size  ${favoriteItem.size}")
                        setData(favoriteItem)
                        notifyDataSetChanged()
                    }
                }
            }
        }

        binding.backBtn.setOnClickListener {
            navHostFragment.navController.navigateUp()
        }

    }
}