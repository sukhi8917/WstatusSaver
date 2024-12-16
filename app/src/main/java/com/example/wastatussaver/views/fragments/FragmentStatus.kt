package com.example.wastatussaver.views.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.lifecycle.ViewModelProvider
import com.example.wastatussaver.R
import com.example.wastatussaver.data.StatusRepo
import com.example.wastatussaver.databinding.FragmentStatusBinding
import com.example.wastatussaver.utils.Constants
import com.example.wastatussaver.utils.SharedPrefKeys
import com.example.wastatussaver.utils.SharedPrefUtils
import com.example.wastatussaver.utils.getFolderPermissions
import com.example.wastatussaver.viewmodels.StatusViewModel
import com.example.wastatussaver.viewmodels.factories.StatusViewModelFactory
import com.example.wastatussaver.views.adapters.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentStatus.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentStatus : Fragment() {
    private val binding by lazy {
        FragmentStatusBinding.inflate(layoutInflater)
    }
    private lateinit var type: String
    private val WHATSAPP_REQUEST_CODE = 101
    private val WHATSAPP_BUSINESS_REQUEST_CODE = 102

    private val viewPagerTitles = arrayListOf("Images","Videos")
    lateinit var viewModel:StatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {

            arguments?.let {
                val repo = StatusRepo(requireActivity())
                viewModel = ViewModelProvider(
                    requireActivity(),
                    StatusViewModelFactory(repo)
                )[StatusViewModel::class.java]


                type = it.getString(Constants.FRAGMENT_TYPE_KEY,"")

                when(type){
                    Constants.TYPE_WHATSAPP_MAIN ->{
                        // check permission
                        // granted then fetch statuses
                        //ELSE  get permission
                        // fetch statuses

                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED,false
                        )
                        if(isPermissionGranted){
                            getWhatsAppStatuses()

                            binding.swipeRefreshLayout.setOnRefreshListener {
                                refreshStatuses()
                            }
                        }
                        permissionLayout.btnPermission.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUEST_CODE = WHATSAPP_REQUEST_CODE,
                                initialUri = Constants.getWhatsappUri()
                            )
                        }

                        val viewPagerAdapter = MediaViewPagerAdapter(requireActivity())
                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout,statusViewPager){tab,pos ->
                            tab.text = viewPagerTitles[pos]

                        }.attach()



                    }

                    Constants.TYPE_WHATSAPP_BUSINESS ->{
                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED,
                            false
                        )
                        if(isPermissionGranted){
                            getWhatsAppBusinessStatuses()

                            binding.swipeRefreshLayout.setOnRefreshListener {
                                refreshStatuses()
                            }
                        }
                        permissionLayout.btnPermission.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUEST_CODE = WHATSAPP_BUSINESS_REQUEST_CODE,
                                initialUri = Constants.getWhatsappBusinessUri()
                            )
                        }

                        val viewPagerAdapter = MediaViewPagerAdapter(
                            requireActivity(),
                            imagesType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES,
                            videosType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES
                        )
                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout,statusViewPager){tab,pos ->
                            tab.text = viewPagerTitles[pos]

                        }.attach()


                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root


    fun refreshStatuses() {
        when (type) {
            Constants.TYPE_WHATSAPP_MAIN -> {
                Toast.makeText(requireActivity(), "Refreshing WP Statuses", Toast.LENGTH_SHORT)
                    .show()
                getWhatsAppStatuses()
            }

            else -> {
                Toast.makeText(
                    requireActivity(),
                    "Refreshing WP Business Statuses",
                    Toast.LENGTH_SHORT
                ).show()
                getWhatsAppBusinessStatuses()
            }
        }

        Handler(Looper.myLooper()!!).postDelayed({
            binding.swipeRefreshLayout.isRefreshing = false
        },2000)


    }



    fun getWhatsAppStatuses() {
        // function to get wp statuses
        binding.permissionLayoutHolder.visibility = View.GONE
        viewModel.getWhatsAppStatuses()

    }

    fun getWhatsAppBusinessStatuses() {
        // function to get wp statuses
        binding.permissionLayoutHolder.visibility = View.GONE
        viewModel.getWhatsAppBusinessStatuses()


    }

    //permission milne ke bad jo result aayega
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //jese hi user allow krega us folder ko to android hame us folder ki ak uri return krega
        //or fir us uir ko hame apni sharedPref me save krva lena ki hmne folder ki prmission le li
        //ab dubara se permission mt magna
        if(resultCode == AppCompatActivity.RESULT_OK){
            //user ne allow kr diya h permission ko
            val treeUri = data?.data!!
            requireActivity().contentResolver.takePersistableUriPermission(
                //if phone is restart still then permision mile
                treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            if(requestCode == WHATSAPP_REQUEST_CODE){
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_WP_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED,true)
                getWhatsAppStatuses()
            }
            else if(requestCode == WHATSAPP_BUSINESS_REQUEST_CODE){
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED,true)
                getWhatsAppStatuses()
            }
        }
    }
}