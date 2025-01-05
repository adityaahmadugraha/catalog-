package com.adit.catalog.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adit.catalog.R
import com.adit.catalog.data.local.ObjekData
import com.adit.catalog.databinding.ListProductBinding
import com.adit.catalog.util.Constant.formatToRupiah
import com.bumptech.glide.Glide

class MainAdapter(
    private val onItemClick: (ObjekData) -> Unit,
    private val onFavoriteClick: (ObjekData) -> Unit
) : ListAdapter<ObjekData, MainAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ListProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ObjekData) {
            binding.apply {
                tvTitle.text = data.title
                tvPrice.text = formatToRupiah(data.price)
                tvDescription.text = data.thick

                Glide.with(itemView.context)
                    .load(data.image)
                    .error(R.color.gray)
                    .into(binding.image)

                if (data.isFavorite) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_red)
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_favorite)
                }

                btnFavorite.setOnClickListener {
                    onFavoriteClick(data)
                }
            }
            itemView.setOnClickListener {
                onItemClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun submitDataWithScroll(data: List<ObjekData>) {
        submitList(data)

        notifyDataSetChanged()
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ObjekData> =
            object : DiffUtil.ItemCallback<ObjekData>() {
                override fun areItemsTheSame(oldItem: ObjekData, newItem: ObjekData): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ObjekData,
                    newItem: ObjekData
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
