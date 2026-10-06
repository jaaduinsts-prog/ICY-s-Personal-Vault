package com.example.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object ApkShareHelper {
    fun shareAppApk(context: Context) {
        try {
            val appInfo = context.applicationInfo
            val originalApk = File(appInfo.sourceDir)

            val cacheApkDir = File(context.cacheDir, "shared_apks")
            if (!cacheApkDir.exists()) {
                cacheApkDir.mkdirs()
            }
            val destinationApk = File(cacheApkDir, "Protocol_J_Stark_Industries.apk")

            if (originalApk.exists()) {
                // Copy internal APK binary to cache
                FileInputStream(originalApk).use { input ->
                    FileOutputStream(destinationApk).use { output ->
                        input.copyTo(output)
                    }
                }

                val apkUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    destinationApk
                )

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/vnd.android.package-archive"
                    putExtra(Intent.EXTRA_STREAM, apkUri)
                    putExtra(Intent.EXTRA_SUBJECT, "Protocol J // Stark Industries Standalone Package")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Deploying Protocol J APK package (Mark L Edition). Install directly on target device."
                    )
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                val chooser = Intent.createChooser(shareIntent, "Dispatch Stark APK via:")
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooser)
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fallback: Share download and deploy link
        try {
            val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Protocol J // Stark Industries Assistant")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Access and deploy Protocol J Stark Assistant: https://ais-pre-lsdizukwurc2akolwcy63l-382340857324.asia-southeast1.run.app"
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val chooser = Intent.createChooser(fallbackIntent, "Dispatch Protocol J Access Link:")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (err: Exception) {
            Toast.makeText(context, "Unable to dispatch package: ${err.message}", Toast.LENGTH_LONG).show()
        }
    }
}
