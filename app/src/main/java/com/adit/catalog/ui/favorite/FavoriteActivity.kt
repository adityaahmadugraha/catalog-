package com.adit.catalog.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.adit.catalog.data.room.ObjekDataEntity
import com.adit.catalog.databinding.ActivityFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: AdapterFavorite
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = AdapterFavorite(
            onItemClick = { data ->
            },
            onItemLongClick = { data ->
                showDeleteCDialog(data)
            }
        )
        binding.rvBook.adapter = adapter
    }

    private fun observeViewModel() {
        favoriteViewModel.favoriteBooks.observe(this) { favoriteList ->
            adapter.submitList(favoriteList)
        }
    }

    private fun showDeleteCDialog(item: ObjekDataEntity) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Hapus dari favorit?")
            .setMessage("Apakah Anda yakin ingin menghapus item ini dari daftar favorit?")
            .setPositiveButton("Ya") { _, _ ->

                favoriteViewModel.removeFavorite(item)
            }
            .setNegativeButton("Tidak", null)
            .create()

        dialog.show()
    }
}
