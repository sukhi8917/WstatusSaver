package com.example.wastatussaver.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.MutableLiveData
import com.example.wastatussaver.models.MEDIA_TYPE_IMAGE
import com.example.wastatussaver.models.MEDIA_TYPE_VIDEO
import com.example.wastatussaver.models.MediaModel
import com.example.wastatussaver.utils.Constants
import com.example.wastatussaver.utils.SharedPrefKeys
import com.example.wastatussaver.utils.SharedPrefUtils
import com.example.wastatussaver.utils.getFileExtension
import com.example.wastatussaver.utils.isStatusExist


//mvm
//repo--> gets data
//viewModel -->process data
//activity/UI ->> show data
class StatusRepo (val context: Context){


    val whatsAppStatusesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppBusinessStatusesLiveData = MutableLiveData<ArrayList<MediaModel>>()

    val activity = context as Activity

    private val wpStatusesList = ArrayList<MediaModel>()
    private val wpBusinessStatusesList = ArrayList<MediaModel>()

    private val TAG = "StatusRepo"

    //jis folder ko permission di yani vo folder jo hide h or jisme sare status saved h uski permision
    //di usme se status ko fetch krna
    fun getAllStatuses(whatsAppType: String = Constants.TYPE_WHATSAPP_MAIN){
        val treeUri = when(whatsAppType){
            Constants.TYPE_WHATSAPP_MAIN->{
                SharedPrefUtils.getPrefString(SharedPrefKeys.PREF_KEY_WP_TREE_URI,"")?.toUri()!!
            }

            else -> {
                SharedPrefUtils.getPrefString(SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI, "")
                    ?.toUri()!!
            }
        }

        Log.d(TAG, "getAllStatuses: $treeUri")

        activity.contentResolver.takePersistableUriPermission(
            treeUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        val fileDocument = DocumentFile.fromTreeUri(activity, treeUri)

        fileDocument?.let {//it:DocumentFile
            it.listFiles().forEach { file->
                Log.d(TAG, "getAllStatuses: ${file.name}")
                if (file.name != ".nomedia" && file.isFile) {
                    val isDownloaded = context.isStatusExist(file.name!!)
                    Log.d(TAG, "getAllStatusesExtension: Extension: ${getFileExtension(file.name!!)} ||${file.name}")
                    val type = if (getFileExtension(file.name!!) == "mp4") {
                        MEDIA_TYPE_VIDEO
                    } else {
                        MEDIA_TYPE_IMAGE
                    }

                    val model = MediaModel(
                        pathUri = file.uri.toString(),
                        fileName = file.name!!,
                        type = type,
                        isDownloaded = isDownloaded
                    )
                    when (whatsAppType) {
                        Constants.TYPE_WHATSAPP_MAIN -> {

                            wpStatusesList.add(model)
                        }

                        else -> {
                            wpBusinessStatusesList.add(model)
                        }

                    }

                }
            }
            when (whatsAppType) {
                Constants.TYPE_WHATSAPP_MAIN -> {
                    Log.d(TAG, "getAllStatuses: Pushing Value to Wp live Data")
                    whatsAppStatusesLiveData.postValue(wpStatusesList)
                }

                else -> {
                    Log.d(TAG, "getAllStatuses: Pushing Value to Wp Business live Data")
                    whatsAppBusinessStatusesLiveData.postValue(wpBusinessStatusesList)
                }

            }

        }

    }
}