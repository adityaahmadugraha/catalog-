package com.adit.catalog.ui.main

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.adit.catalog.databinding.ActivityMainBinding
import com.adit.catalog.ui.detail.DetailActivity
import com.adit.catalog.ui.favorite.FavoriteActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
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
        observeViewModel()
        setupImageSlider()
        setupSearchView()

        mainViewModel.fetchMenuList()

        binding.btnFavorite.setOnClickListener {
            intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
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
                Log.d("MainActivity", "Tombol favorit diklik untuk: ${item.title}")
                if (item.isFavorite) {
                    mainViewModel.removeFromFavorites(item)
                } else {
                    mainViewModel.addToFavorites(item)
                }
            }
        )
        binding.rvBook.adapter = mainAdapter
    }

    private fun observeViewModel() {
        mainViewModel.filteredList.observe(this) { filteredList ->
            mainAdapter.submitList(filteredList)
        }

        mainViewModel.favoriteBooks.observe(this) { favoriteBooks ->
            val updatedList = mainViewModel.filteredList.value?.map { item ->
                item.copy(isFavorite = favoriteBooks.any { it.id == item.id })
            }
            mainAdapter.submitList(updatedList)
        }
    }

    private fun setupImageSlider() {
        val imageList = ArrayList<SlideModel>().apply {
            add(SlideModel("https://bit.ly/2YoJ77H", ScaleTypes.FIT))
            add(SlideModel("https://bit.ly/2BteuF2", ScaleTypes.FIT))
            add(SlideModel("https://bit.ly/3fLJf72", ScaleTypes.FIT))
        }
        binding.imageSlider.setImageList(imageList)
    }

    private fun setupSearchView() {
        val searchEditText = binding.searchView.etSearchInput
        val clearButton = binding.searchView.ivClear

        searchEditText.addTextChangedListener {
            val query = it.toString()
            mainViewModel.filterMenu(query)
            clearButton.visibility = if (query.isEmpty()) View.GONE else View.VISIBLE
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
