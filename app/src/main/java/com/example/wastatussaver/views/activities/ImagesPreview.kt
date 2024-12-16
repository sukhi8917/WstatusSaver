package com.example.wastatussaver.views.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wastatussaver.R
import com.example.wastatussaver.databinding.ActivityImagesPreviewBinding
import com.example.wastatussaver.models.MediaModel
import com.example.wastatussaver.utils.Constants
import com.example.wastatussaver.views.adapters.ImagePreviewAdapter

class ImagesPreview : AppCompatActivity() {
    private val activity = this
    private  val binding by lazy {
        ActivityImagesPreviewBinding.inflate(layoutInflater)
    }

    lateinit var adapter: ImagePreviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        // Toolbar setup
        setSupportActionBar(binding.toolBar) // Toolbar ko ActionBar ke roop me set karein
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Back icon ko enable karein
        binding.apply {

            val list =
                intent.getSerializableExtra(Constants.MEDIA_LIST_KEY) as ArrayList<MediaModel>
            val scrollTo = intent.getIntExtra(Constants.MEDIA_SCROLL_KEY, 0)
            adapter = ImagePreviewAdapter(list, activity)
            imagesViewPager.adapter = adapter
            imagesViewPager.currentItem = scrollTo
        }

    }
    // Back button ke click ko handle karein
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Previous screen par wapas jata hai
        return true
    }
}