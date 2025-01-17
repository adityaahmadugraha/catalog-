package com.adit.catalog.ui.main

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.adit.catalog.R
import com.adit.catalog.databinding.ActivityMainBinding
import com.adit.catalog.ui.detail.DetailActivity
import com.adit.catalog.ui.favorite.FavoriteActivity
import com.adit.catalog.util.Constant
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupImageSlider()
        setupSearchView()
        setupChipSorting()
        setupChipGroupStyles()
        setupSwipeRefresh()

        mainViewModel.fetchMenuList()


        binding.apply {
            btnFavorite.setOnClickListener {
                navigateToFavoriteActivity()
            }
            rvBook.layoutManager = GridLayoutManager(this@MainActivity, 2)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.fetchMenuList()
            setupChipGroupStyles()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }



    private fun setupRecyclerView() {
        mainAdapter = MainAdapter(
            onItemClick = { item ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("EXTRA_DATA", item)
                }
                startActivity(intent)
            },
            onFavoriteClick = { item ->
                if (item.isFavorite) {
                    mainViewModel.removeFromFavorites(item)
                } else {
                    mainViewModel.addToFavorites(item)
                }
            }
        )
        binding.rvBook.adapter = mainAdapter
    }

    private fun setupObservers() {
        mainViewModel.filteredList.observe(this) { filteredList ->
            val updatedList = filteredList.map { item ->
                item.copy(isFavorite = mainViewModel.favoriteBooks.value?.any { it.id == item.id }
                    ?: false)
            }

            mainAdapter.submitDataWithScroll(updatedList)

            binding.rvBook.post {
                binding.rvBook.scrollToPosition(0)
            }
        }

        mainViewModel.favoriteBooks.observe(this) { favoriteBooks ->
            val updatedList = mainViewModel.filteredList.value?.map { item ->
                item.copy(isFavorite = favoriteBooks.any { it.id == item.id })
            }
            mainAdapter.submitList(updatedList)
        }
    }


    private fun setupImageSlider() {
        val imageList = listOf(
            SlideModel("https://bit.ly/2YoJ77H", ScaleTypes.FIT),
            SlideModel("https://bit.ly/2BteuF2", ScaleTypes.FIT),
            SlideModel("https://bit.ly/3fLJf72", ScaleTypes.FIT)
        )
            binding.imageSlider.setImageList(imageList)

    }

    private fun setupSearchView() {
        val searchEditText = binding.searchView.etSearchInput
        val clearButton = binding.searchView.ivClear

        searchEditText.addTextChangedListener {
            val query = it.toString().trim()


            binding.chipGroupSort.clearCheck()
            setupChipGroupStyles()

            mainViewModel.filterBook(query)
            clearButton.visibility = if (query.isEmpty()) View.GONE else View.VISIBLE
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            binding.chipGroupSort.clearCheck()
            setupChipGroupStyles()
        }
    }



    private fun setupChipSorting() {
        binding.apply {
            chipSortAz.setOnClickListener {
                mainViewModel.sortList(Constant.SortType.AZ)
                updateChipStyles(chipSortAz)
            }
            chipSortZa.setOnClickListener {
                mainViewModel.sortList(Constant.SortType.ZA)
                updateChipStyles(chipSortZa)
            }
            chipSortPriceLowToHigh.setOnClickListener {
                mainViewModel.sortList(Constant.SortType.PRICE_LOW_TO_HIGH)
                updateChipStyles(chipSortPriceLowToHigh)
            }
            chipSortPriceHighToLow.setOnClickListener {
                mainViewModel.sortList(Constant.SortType.PRICE_HIGH_TO_LOW)
                updateChipStyles(chipSortPriceHighToLow)
            }
        }
    }


    @Suppress("DEPRECATION")
    private fun setupChipGroupStyles() {
        val chipGroupSort: ChipGroup = binding.chipGroupSort

        for (i in 0 until chipGroupSort.childCount) {
            val chip = chipGroupSort.getChildAt(i) as Chip
            chip.setChipBackgroundColorResource(R.color.white)
            chip.setTextColor(resources.getColor(R.color.black))
        }

        chipGroupSort.setOnCheckedChangeListener { _, checkedId ->
            val chip = chipGroupSort.findViewById<Chip>(checkedId)
            if (chip != null) {
                updateChipStyles(chip)
            }
        }
    }

    private fun updateChipStyles(chip: Chip) {
        binding.chipGroupSort.forEach {
            (it as? Chip)?.apply {
                setChipBackgroundColorResource(R.color.white)
                setTextColor(ColorStateList.valueOf(Color.BLACK))
            }
        }

        chip.setChipBackgroundColorResource(R.color.backgroud)
        chip.setTextColor(ColorStateList.valueOf(Color.WHITE))
    }



    private fun navigateToFavoriteActivity() {
        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
        startActivity(intent)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            hideKeyboardOnOutsideTouch(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboardOnOutsideTouch(event: MotionEvent) {
        val v = currentFocus
        if (v is EditText) {
            val outRect = Rect()
            v.getGlobalVisibleRect(outRect)
            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                v.clearFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }
}
