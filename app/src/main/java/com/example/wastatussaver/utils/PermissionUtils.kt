package com.example.wastatussaver.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract


fun getFolderPermissions(context: Context, REQUEST_CODE: Int, initialUri: Uri) {
    //initialUri means konse folder ki permission deni h start me
    val activity = context as Activity
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)

    //hide folder ko dekhne ke liye
    intent.putExtra("android.content.extra.SHOW_ADVANCED", true)



    activity.startActivityForResult(intent, REQUEST_CODE)
}