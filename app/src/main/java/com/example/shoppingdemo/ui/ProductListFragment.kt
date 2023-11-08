package com.example.shoppingdemo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingdemo.*
import com.example.shoppingdemo.adapter.CategoryListAdapter
import com.example.shoppingdemo.adapter.ProductListAdapter
import com.example.shoppingdemo.database.DBHelper
import com.example.shoppingdemo.database.ShoppingRepository
import com.example.shoppingdemo.databinding.FragmentProductListBinding
import com.example.shoppingdemo.utils.hide
import com.example.shoppingdemo.data.Categories
import com.example.shoppingdemo.data.Product
import com.example.shoppingdemo.viewmodel.ShoppingViewModel
import com.example.shoppingdemo.viewmodel.ShoppingViewModelFactory
import com.example.shoppingdemo.utils.readJSONFromAssets
import com.example.shoppingdemo.utils.show
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListFragment : Fragment(),
    ProductListAdapter.CartItemsCount {

    private var _binding: FragmentProductListBinding? = null
    private var TAG: String = ProductListFragment::class.java.simpleName
    private val binding get() = _binding!!
    private lateinit var navHostFragment: NavHostFragment
    private var productListAdapter: ProductListAdapter? = null
    private var categoryListAdapter: CategoryListAdapter? = null
    private lateinit var viewModel: ShoppingViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater)
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

        //  convert to json to gson
        val json = readJSONFromAssets(requireContext(), "shopping.json")
        val gson = Gson()
        val data = gson.fromJson(json, Product::class.java)

        val product: Product = data
        val categories: MutableList<Categories> = product.categories
        var productList: Product? = null

        // first time db is null add all json data to db
        CoroutineScope(Dispatchers.IO).launch {
            productList = viewModel.fetchProductList()
            CoroutineScope(Dispatchers.Main).launch {
                Log.d(TAG, "onViewCreated: productList $productList")
                if (productList == null) {
                    viewModel.insertProductList(product)
                    Log.d(TAG, "onViewCreated: data added in room database $product")
                }
            }
        }

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        categoryListAdapter = CategoryListAdapter(requireContext())
        binding.rvCategoryList.apply {
            layoutManager = linearLayoutManager
            adapter = categoryListAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        val linearLayoutManager1 =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        productListAdapter = ProductListAdapter(requireContext(), this)
        binding.rvProductList.apply {
            layoutManager = linearLayoutManager1
            adapter = productListAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }
        productListAdapter?.apply {
            Log.d(TAG, "categories: categories size  ${categories.size}")
            setData(categories)
            notifyDataSetChanged()
        }

        // total selected cart item count
        CoroutineScope(Dispatchers.IO).launch {
           val totalItem = viewModel.fetchTotalItemCart() ?: 0
            CoroutineScope(Dispatchers.Main).launch {
                if (totalItem > 0) {
                    binding.cartCount.show()
                    binding.cartCount.text = totalItem.toString()
                }
            }
        }

        // navigate to favorite fragment
        binding.favorite.setOnClickListener {
            navHostFragment.navController.navigate(R.id.favoriteFragment)
        }

        // navigate to cart fragment
        binding.cart.setOnClickListener {
            navHostFragment.navController.navigate(R.id.cartFragment)
        }

        // fetch and set categories data
        binding.checkCategoryList.setOnClickListener {
            binding.showCategoryList.show()
            binding.selectCategoryCl.hide()
            val categoriesList = mutableListOf<String>()
            productList?.categories?.forEach {
                it.name?.let { it1 -> categoriesList.add(it1) }
            }
            categoryListAdapter?.apply {
                Log.d(TAG, "categories: categories size  ${categoriesList.size}")
                setData(categoriesList)
                notifyDataSetChanged()
            }
        }

        // show the categories ui
        binding.showCategoryList.setOnClickListener {
            binding.showCategoryList.hide()
            binding.selectCategoryCl.show()
        }

        // close categories ui
        binding.closeBtn.setOnClickListener {
            binding.selectCategoryCl.show()
            binding.showCategoryList.hide()
        }
    }

    // update the count of the cart item size
    override fun onClicked(size: Int) {
        Log.d(TAG, "onClicked: check $size")
        binding.cartCount.show()
        binding.cartCount.text = size.toString()
    }

}