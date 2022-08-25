package com.example.pixabaylibrary.features.pixabay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pixabaylibrary.databinding.CardPixabayBinding
import com.example.pixabaylibrary.features.pixabay.dto.PixabayImage
import com.squareup.picasso.Picasso


class ImagesAdapter(
    private var context: Context,
    private var list: List<PixabayImage>,
    private var listener: OnItemClick
) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {


    interface OnItemClick{
        fun onItemClickData(item : PixabayImage)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardPixabayBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Picasso.get().load(item.previewURL).into(holder.imageView)
        holder.tvUserName.text = item.user
        holder.tvTag.text = item.tags



    }

    inner class ViewHolder(binding: CardPixabayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageView: AppCompatImageView = binding.appImageView
        val tvUserName: AppCompatTextView = binding.appCompTextViewUserName
        val tvTag: AppCompatTextView = binding.appCompTextViewTag
        private val cvImageDetails : CardView = binding.cvImageDetail
        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                listener.onItemClickData(list[pos])

            }
        }
    }


}