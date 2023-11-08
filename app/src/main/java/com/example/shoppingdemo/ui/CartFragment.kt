package com.example.shoppingdemo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingdemo.R
import com.example.shoppingdemo.viewmodel.ShoppingViewModel
import com.example.shoppingdemo.viewmodel.ShoppingViewModelFactory
import com.example.shoppingdemo.adapter.CartAdapter
import com.example.shoppingdemo.database.DBHelper
import com.example.shoppingdemo.database.ShoppingRepository
import com.example.shoppingdemo.databinding.FragmentCartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartFragment : Fragment(), CartAdapter.TotalPriceCount {

    private var _binding: FragmentCartBinding? = null
    private var TAG: String = CartFragment::class.java.simpleName
    private val binding get() = _binding!!
    private var cartAdapter: CartAdapter? = null
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var viewModel: ShoppingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCartBinding.inflate(inflater)
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
        cartAdapter = CartAdapter(requireContext(), this)
        val recyclerView = binding.rvCart
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = cartAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        // set cart data to adapter
        CoroutineScope(Dispatchers.IO).launch {
            val cartItem = viewModel.fetchAllCart()
            CoroutineScope(Dispatchers.Main).launch {
                if (cartItem.isNotEmpty()) {
                    cartAdapter?.apply {
                        Log.d(TAG, "categories: categories size  ${cartItem.size}")
                        setData(cartItem)
                        notifyDataSetChanged()
                    }
                }
            }
        }

        // back to previous fragment
        binding.backBtn.setOnClickListener {
            navHostFragment.navController.navigateUp()
        }

        // checkout feature coming soon
        binding.checkOut.setOnClickListener {
            Toast.makeText(
                context,
                "Coming Soon Payment Getaway",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // update the total and discount price in cart
    @SuppressLint("SetTextI18n")
    override fun onClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            val subTotal = viewModel.fetchPriceItemCart()
            CoroutineScope(Dispatchers.Main).launch {
                Log.d(TAG, "onClicked: clicked $subTotal")
                binding.subTotal.text = subTotal.toString()
                // discount 20%
                val discount: Float = (subTotal.times(20)).div(100)
                binding.discount.text = "-$discount"
                binding.total.text = (subTotal - discount).toString()
            }
        }
    }
}