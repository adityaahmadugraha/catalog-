package com.adit.catalog.ui.detail


import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.adit.catalog.R
import com.adit.catalog.data.local.ObjekData
import com.adit.catalog.databinding.ActivityDetailBinding
import com.adit.catalog.util.Constant.formatToRupiah
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    private var isCurrentlyFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val objekData = intent.getParcelableExtra<ObjekData>("EXTRA_DATA")
        Log.d("DetailActivity", "Received data: $objekData")
        objekData?.let {
            displayDetail(it)
            setupFavoriteButton(it)
        }


        binding.icBack.setOnClickListener {
            finish()
        }
    }

    private fun displayDetail(data: ObjekData) {
        binding.apply {
            ivImage.setImageResource(data.image)
            tvTitle.text = data.title
            tvSinopsis.text = data.sinopsis
            tvThick.text = getString(R.string.thick_format, data.thick)
            tvCategory.text = getString(R.string.gendre_format, data.category)
            tvPrice.text = formatToRupiah(data.price)
            tvAuthor.text = data.author
            tvPublished.text = data.published
        }
    }


    private fun setupFavoriteButton(data: ObjekData) {

        detailViewModel.isFavorite(data.id).observe(this) { isFavorite ->
            if (isFavorite != isCurrentlyFavorite) {
                isCurrentlyFavorite = isFavorite
                if (isFavorite) {
                    binding.btnFavorite.setImageResource(R.drawable.ic_favorite_red)
                } else {
                    binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
                }
            }
        }

        binding.btnFavorite.setOnClickListener {

            if (isCurrentlyFavorite) {

                detailViewModel.removeFavorite(data)
            } else {
                detailViewModel.addFavorite(data)
            }
        }
    }

}
