package com.example.pixabaylibrary.features.pixabay.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pixabaylibrary.databinding.CardPixabayBinding
import com.example.pixabaylibrary.features.pixabay.dto.PixabayPagingData
import com.example.pixabaylibrary.utils.ToastUtils
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class PixabayImagesRVAdapter(
    var context: Context
) : PagingDataAdapter<PixabayPagingData.Hit, RecyclerView.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<PixabayPagingData.Hit>() {
            override fun areItemsTheSame(
                oldItem: PixabayPagingData.Hit,
                newItem: PixabayPagingData.Hit
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PixabayPagingData.Hit,
                newItem: PixabayPagingData.Hit
            ): Boolean =
                oldItem == newItem
        }
    }

    var listener: OnItemClick? = null
    lateinit var binding: CardPixabayBinding

    interface OnItemClick{
        fun onItemClickData(item : PixabayPagingData.Hit)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        binding = CardPixabayBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val viewHolder = holder as ImageHolder
        /*Picasso.get().load(item?.previewURL).into(holder.imageView, object :Callback{
            override fun onSuccess() {

            }

            override fun onError(e: Exception?) {
                 ToastUtils.showToastMessage(context, e?.message)
            }
        }).let { object : Picasso.Listener{
            override fun onImageLoadFailed(picasso: Picasso?, uri: Uri?, exception: Exception?) {

            }
        } }*/

        if (!item?.previewURL.isNullOrEmpty()){
            Picasso.get().load(item?.previewURL).into(holder.imageView)
        }
        if (!item?.user.isNullOrEmpty()){
            viewHolder.tvUserName.text = item?.user
        }
        if (!item?.tags.isNullOrEmpty()){
            viewHolder.tvTag.text = item?.tags
        }

        viewHolder.cvImageDetails.setOnClickListener {
            listener?.onItemClickData(item!!)
        }

    }

    inner class ImageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imageView: AppCompatImageView = binding.appImageView
        val tvUserName: AppCompatTextView = binding.appCompTextViewUserName
        val tvTag: AppCompatTextView = binding.appCompTextViewTag
        val cvImageDetails : CardView = binding.cvImageDetail


    }


    /*fun removeItemAt(position: Int) {

        notifyItemRemoved(position)
    }

    fun itemToRemove(position: Int): OtherSupplierProductV3.Result {
        return getItem(position) as OtherSupplierProductV3.Result
    }*/

}