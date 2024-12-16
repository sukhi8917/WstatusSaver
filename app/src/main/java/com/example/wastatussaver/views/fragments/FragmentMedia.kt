package com.example.wastatussaver.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.wastatussaver.R
import com.example.wastatussaver.data.StatusRepo
import com.example.wastatussaver.databinding.FragmentMediaBinding
import com.example.wastatussaver.models.MediaModel
import com.example.wastatussaver.utils.Constants
import com.example.wastatussaver.viewmodels.StatusViewModel
import com.example.wastatussaver.viewmodels.factories.StatusViewModelFactory
import com.example.wastatussaver.views.adapters.MediaAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMedia.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMedia : Fragment() {
    private val binding by lazy {
        FragmentMediaBinding.inflate(layoutInflater)
    }

    lateinit var viewModel: StatusViewModel
    lateinit var adapter: MediaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Make sure the fragment can handle the options me

        binding.apply {
            arguments?.let {
                val repo = StatusRepo(requireActivity())
                viewModel = ViewModelProvider(
                    requireActivity(),
                    StatusViewModelFactory(repo)
                )[StatusViewModel::class.java]
                val mediaType = it.getString(Constants.MEDIA_TYPE_KEY, "")
                when (mediaType) {
                    Constants.MEDIA_TYPE_WHATSAPP_IMAGES -> {
                        viewModel.whatsAppImagesLiveData.observe(requireActivity()) { unFilteredList ->
                            val filteredList = unFilteredList.distinctBy { model ->
                                model.fileName
                            }

                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model ->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                            if (list.size == 0) {
                                tempMediaText.visibility = View.VISIBLE
                            } else {
                                tempMediaText.visibility = View.GONE
                            }

                        }
                    }

                    Constants.MEDIA_TYPE_WHATSAPP_VIDEOS -> {
                        viewModel.whatsAppVideosLiveData.observe(requireActivity()) { unFilteredList ->
                            val filteredList = unFilteredList.distinctBy { model ->
                                model.fileName
                            }

                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model ->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                            if (list.size == 0) {
                                tempMediaText.visibility = View.VISIBLE
                            } else {
                                tempMediaText.visibility = View.GONE
                            }
                        }
                    }

                    Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES -> {
                        viewModel.whatsAppBusinessImagesLiveData.observe(requireActivity()) { unFilteredList ->
                            val filteredList = unFilteredList.distinctBy { model ->
                                model.fileName
                            }

                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model ->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                            if (list.size == 0) {
                                tempMediaText.visibility = View.VISIBLE
                            } else {
                                tempMediaText.visibility = View.GONE
                            }
                        }
                    }

                    Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_VIDEOS -> {
                        viewModel.whatsAppBusinessVideosLiveData.observe(requireActivity()) { unFilteredList ->
                            val filteredList = unFilteredList.distinctBy { model ->
                                model.fileName
                            }

                            val list = ArrayList<MediaModel>()
                            filteredList.forEach { model ->
                                list.add(model)
                            }
                            adapter = MediaAdapter(list, requireActivity())
                            mediaRecyclerView.adapter = adapter
                            if (list.size == 0) {
                                tempMediaText.visibility = View.VISIBLE
                            } else {
                                tempMediaText.visibility = View.GONE
                            }
                        }
                    }
                }


            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


    // Handle back button click for the Fragment
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed() // Handle back action in Activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

