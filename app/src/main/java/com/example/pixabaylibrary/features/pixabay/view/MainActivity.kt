package com.example.pixabaylibrary.features.pixabay.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.example.pixabaylibrary.R
import com.example.pixabaylibrary.databinding.ActivityMainBinding
import com.example.pixabaylibrary.features.pixabay.adapter.PixabayImagesRVAdapter
import com.example.pixabaylibrary.features.pixabay.dto.PixabayPagingData
import com.example.pixabaylibrary.features.pixabay_details.view.ImageDetailsActivity
import com.example.pixabaylibrary.features.viewmodel.PixabayViewModel
import com.example.pixabaylibrary.utils.AlertDialogUtils
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var adapter: PixabayImagesRVAdapter? = null
    var searchImage: SearchView? = null

    companion object {
        val IMAGE_DETAIL = "image_detail"
    }

    private val viewModel: PixabayViewModel by viewModels()
    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("MainActivity OnCreate calls ")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        getPagingPixabayImages()

    }
    private fun initAdapter() {
        val recyclerView = binding.imageRecyclerView
        adapter = PixabayImagesRVAdapter(this@MainActivity)
        adapter?.listener = object : PixabayImagesRVAdapter.OnItemClick {
            override fun onItemClickData(item: PixabayPagingData.Hit) {
                showMoreInformationDialog(item)
            }
        }
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        searchImage = menuItem?.actionView as SearchView

//        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
        menuItem.expandActionView()
        searchImage!!.setQuery(viewModel.query, true)
        searchImage!!.queryHint = getString(R.string.search_query)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        searchProduct()
        return super.onPrepareOptionsMenu(menu)
    }


    private fun searchProduct() {

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchImage!!.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchImage!!.maxWidth = Integer.MAX_VALUE

        searchImage!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.v("Search Submit")
                // filter recycler view when query submitted
                if (!query.isNullOrEmpty()) {
                    viewModel.query = query
                    getPagingPixabayImages()
                }

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Timber.v("Search TextChangeListerner")
                // filter recycler view when text is changed
                if (!query.isNullOrEmpty()) {
                    viewModel.query = query
                    getPagingPixabayImages()

                }

                return false

            }
        })

    }


   private fun showRecyclerViewAndHideNoDataText(){
       binding.imageRecyclerView.visibility = View.VISIBLE
       binding.noDataFound.visibility = View.GONE
   }
    private fun hideRecyclerViewAndShowNoDataText(){
       binding.imageRecyclerView.visibility = View.GONE
       binding.noDataFound.visibility = View.VISIBLE
   }


    fun getPagingPixabayImages() {
        lifecycleScope.launch {
            viewModel.getPixabayPagingImages().collect {
                adapter!!.submitData(it)
                adapter!!.addOnPagesUpdatedListener {
                    if (adapter!!.itemCount < 1){
                        hideRecyclerViewAndShowNoDataText()
                    }else{
                        showRecyclerViewAndHideNoDataText()
                    }
                }



            }

        }

        // hide keyboard

    }


    private fun goToImageDetailsActivity(item: PixabayPagingData.Hit) {
        val intent = Intent(this@MainActivity, ImageDetailsActivity::class.java)
        intent.putExtra(IMAGE_DETAIL, item)
        startActivity(intent)
    }

    private fun showMoreInformationDialog(item: PixabayPagingData.Hit) {
        AlertDialogUtils.show(this@MainActivity, getString(R.string.yes), getString(R.string.no),
            getString(R.string.dialog_message), getString(R.string.dialog_title),
            object : AlertDialogUtils.AlertDialogListener {
                override fun onPositiveButtonClick(dialog: AlertDialog) {
                    // go to details page
                    goToImageDetailsActivity(item)

                }

                override fun onNegativeButtonClick(dialog: AlertDialog) {
                    dialog.dismiss()
                }
            })
    }


    /*private fun setRecycleView(list : List<PixabayImage>){
        val recyclerView = binding.imageRecyclerView
        val adapter = ImagesAdapter(this,list,object : ImagesAdapter.OnItemClick{
            override fun onItemClickData(item: PixabayImage) {
                // show a dialog
//                showMoreInformationDialog(item)
//                ToastUtils.showToastMessage(this@MainActivity,item.user )
//                Timber.v("PixaBay Item ${GsonUtils.toString(item)}")
            }
        })
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }*/

    /*private fun getListOfImages(){
        lifecycleScope.launchWhenStarted {
            viewModel.getPixabayData().collect{
                when{

                    it.isTokenExpired -> {
                        showErrorMessage(it.error)
                    }

                    !it.exception.isNullOrEmpty() ->{
                        showErrorMessage(it.exception)
                    }


                    !it.error.isNullOrEmpty() -> {
                        showErrorMessage(it.error)
                    }

                    it.data != null -> {
                        setRecycleView(it.data!!.hits)
                        Timber.v("MainActivity : onSuccess ${GsonUtils.toString(it.data)}")

                    }
                }
            }
        }
    }*/
}