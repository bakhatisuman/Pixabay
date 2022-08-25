package com.example.pixabaylibrary.features.pixabay_details.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.pixabaylibrary.R
import com.example.pixabaylibrary.databinding.ActivityImageDetailsBinding
import com.example.pixabaylibrary.features.pixabay.dto.PixabayImage
import com.example.pixabaylibrary.features.pixabay.dto.PixabayPagingData
import com.example.pixabaylibrary.features.pixabay.view.MainActivity
import com.example.pixabaylibrary.utils.NumberUtils
import com.example.pixabaylibrary.utils.ToastUtils
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ImageDetailsActivity : AppCompatActivity() {


    lateinit var binding : ActivityImageDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()
        val pixaBayImage = intent.getSerializableExtra(MainActivity.IMAGE_DETAIL) as PixabayPagingData.Hit?
        if (pixaBayImage != null){
            setUi(pixaBayImage)
        }
    }

    private fun hideActionBar(){
        supportActionBar!!.hide()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    private fun setUi(item : PixabayPagingData.Hit){
        if (item.comments == 0){
            binding.comments.visibility = View.GONE
        }

        if (item.likes == 0){
            binding.likes.visibility = View.GONE
        }

        if (item.downloads == 0){
            binding.downloads.visibility = View.GONE
        }

        if (!item.largeImageURL.isNullOrEmpty()){
            Picasso.get().load(item.largeImageURL).into(binding.appCompactLargeImage , object : Callback{
                override fun onSuccess() {
//                    ToastUtils.showToastMessage(this@ImageDetailsActivity, getString(R.string.image_is_ready_to_view))
                }

                override fun onError(e: Exception?) {
                    ToastUtils.showToastMessage(this@ImageDetailsActivity, getString(R.string.cant_load_from_cache))
                }
            }).let { object : Picasso.Listener{
                override fun onImageLoadFailed(picasso: Picasso?, uri: Uri?, exception: Exception?) {
                    ToastUtils.showToastMessage(this@ImageDetailsActivity, "Exception occur while loading image : ${exception?.message}")
                }
            } }
        }

        if (!item.user.isNullOrEmpty()){
            binding.largeImageUser.text = "Uploaded by: ${item.user} "
        }
        if (!item.tags.isNullOrEmpty()){
            binding.largeImageTags.text = item.tags
        }
        if (item.likes != 0){
            binding.likes.text = item.likes.toString() +" " +resources.getString(R.string.likes_colon)
        }
        if (item.comments != 0){
            binding.comments.text = item.comments.toString()+ " " +resources.getString(R.string.comment_colon)
        }
        if (item.downloads != 0){
            val downloadCount = NumberUtils.prettyNumberCount(item.downloads.toLong())
            binding.downloads.text = downloadCount+" " + resources.getString(R.string.downloads_colon)
        }








    }
}