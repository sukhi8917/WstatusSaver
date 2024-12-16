package com.example.wastatussaver.views.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.wastatussaver.R
import com.example.wastatussaver.databinding.ActivityVideosPreviewBinding
import com.example.wastatussaver.models.MediaModel
import com.example.wastatussaver.utils.Constants
import com.example.wastatussaver.views.adapters.VideoPreviewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideosPreview : AppCompatActivity() {
    private val activity = this
    private val binding by lazy {
       ActivityVideosPreviewBinding.inflate(layoutInflater)
    }

    lateinit var adapter: VideoPreviewAdapter
    private val TAG = "VideosPreview"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        // Toolbar setup for Activity
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Back icon ko enable karein
        supportActionBar?.setHomeButtonEnabled(true) // Ensure the home button is enabled

        // Handle back press for the Activity's toolbar
        binding.toolBar.setNavigationOnClickListener {
            onBackPressed() // Go back to the previous screen when the back button is clicked
        }





        binding.apply {

            val list =
                intent.getSerializableExtra(Constants.MEDIA_LIST_KEY) as ArrayList<MediaModel>
            val scrollTo = intent.getIntExtra(Constants.MEDIA_SCROLL_KEY, 0)
            adapter = VideoPreviewAdapter(list, activity)
            videoRecyclerView.adapter = adapter
            val pageSnapHelper = PagerSnapHelper()
            pageSnapHelper.attachToRecyclerView(videoRecyclerView)
            videoRecyclerView.scrollToPosition(scrollTo)

            videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        Log.d(TAG, "onScrollStateChanged: Dragging")
                        //jese hi drag start krege sare playser stop ho jayege
                        stopAllPlayers()
                    }
                }


            })
        }


    }
    // Back button ke click ko handle karein
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Previous screen par wapas jata hai
        return true
    }

    private fun stopAllPlayers() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                binding.apply {
                    for (i in 0 until videoRecyclerView.childCount) {
                        val child = videoRecyclerView.getChildAt(i)
                        val viewHolder = videoRecyclerView.getChildViewHolder(child)
                        if (viewHolder is VideoPreviewAdapter.ViewHolder) {
                            viewHolder.stopPlayer()
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopAllPlayers()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAllPlayers()
    }
}