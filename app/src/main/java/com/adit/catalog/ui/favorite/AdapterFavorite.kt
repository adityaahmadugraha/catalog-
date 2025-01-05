package com.adit.catalog.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adit.catalog.R
import com.adit.catalog.data.room.ObjekDataEntity
import com.adit.catalog.databinding.ListProductBinding
import com.adit.catalog.util.Constant.formatToRupiah
import com.bumptech.glide.Glide


class AdapterFavorite(
    private val onItemClick: (ObjekDataEntity) -> Unit,
    private val onItemLongClick: (ObjekDataEntity) -> Unit
) : ListAdapter<ObjekDataEntity, AdapterFavorite.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ListProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ObjekDataEntity) {
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
            }


            itemView.setOnClickListener {
                onItemClick(data)
            }


            itemView.setOnLongClickListener {
                onItemLongClick(data)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ObjekDataEntity> =
            object : DiffUtil.ItemCallback<ObjekDataEntity>() {
                override fun areItemsTheSame(oldItem: ObjekDataEntity, newItem: ObjekDataEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ObjekDataEntity,
                    newItem: ObjekDataEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
