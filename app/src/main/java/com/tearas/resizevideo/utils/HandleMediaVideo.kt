package com.tearas.resizevideo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore.Video.Media
import android.util.Log
import com.tearas.resizevideo.model.FolderInfo
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.utils.Utils.formatTime
import java.io.File
import java.text.SimpleDateFormat
import java.util.TimeZone

interface IVideo {
    fun createFolderVideo(): String
    fun getPathStorageExternal(): String
    fun getVideoByIdBucket(
        id: String, orderBy: String = Media.DATE_ADDED + " DESC"
    ): ArrayList<MediaInfo>

    fun getAllVideo(
        orderBy: String = Media.DATE_ADDED + " DESC"
    ): ArrayList<MediaInfo>

    fun getVideo(
        selection: String? = null,
        selectionArg: Array<String>? = null,
        orderBy: String = Media.DATE_ADDED + " DESC"
    ): ArrayList<MediaInfo>

    fun getFolderContainVideo(orderBy: String = Media.DATE_ADDED + " DESC"): ArrayList<FolderInfo>
    fun countVideoInFolder(id: String): Int
    fun compareQuantity(quantity: Int): Boolean
    fun getPathFolderMyApp(): String
}

class HandleMediaVideo(private val context: Context) : IVideo {

    override fun getPathFolderMyApp(): String {
        return createFolderVideo()
    }

    override fun createFolderVideo(): String {
        val path = getPathStorageExternal()
        val folderName = "TerasResizeVideo"
        val folder = File(path, folderName)
        return if (folder.exists() || folder.mkdirs()) {
            folder.path
        } else {
            throw IllegalStateException("Unable to create directory $folderName")
        }
    }


    override fun getPathStorageExternal(): String {
        val storeMediaMng = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("khangnfuyen ksaj", "createFolderVideo"+storeMediaMng.storageVolumes[0].directory!!.path)
            storeMediaMng.storageVolumes[0].directory!!.path+"/Movies"
        } else {
            Log.d("khangnfuyen ksaj", "createFolderVideo"+Environment.getExternalStorageDirectory().path)
            Environment.getExternalStorageDirectory().path+"/Movies"
        }
    }

    override fun getVideoByIdBucket(id: String, orderBy: String): ArrayList<MediaInfo> {
        val selection = Media.BUCKET_ID + " = ? "
        val selectionArg = arrayOf(id)
        return getVideo(selection, selectionArg, orderBy)
    }

    @SuppressLint("Recycle", "Range")
    override fun getAllVideo(orderBy: String): ArrayList<MediaInfo> {
        return getVideo(null, null, orderBy)
    }

    @SuppressLint("Range", "Recycle")
    override fun getVideo(
        selection: String?, selectionArg: Array<String>?, orderBy: String
    ): ArrayList<MediaInfo> {
        val videos = ArrayList<MediaInfo>()
        val contentResolver = context.contentResolver
        val projection = arrayOf(
            Media._ID,
            Media.BUCKET_ID,
            Media.DISPLAY_NAME,
            Media.DATA,
            Media.SIZE,
            Media.WIDTH,
            Media.HEIGHT,
            Media.DURATION,
            Media.MIME_TYPE
        )

        val cursor = contentResolver.query(
            Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArg, orderBy
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                cursor.apply {
                    val resolution = Resolution(
                        getInt(getColumnIndex(Media.WIDTH)),
                        getInt(getColumnIndex(Media.HEIGHT)),
                    )

                    val formatTime = getLong(getColumnIndex(Media.DURATION)).formatTime()

                    val mediaInfo = MediaInfo(
                        getLong(getColumnIndex(Media._ID)),
                        getString(getColumnIndex(Media.DISPLAY_NAME)),
                        getString(getColumnIndex(Media.DATA)),
                        getLong(getColumnIndex(Media.SIZE)),
                        resolution,
                        formatTime,
                        getString(getColumnIndex(Media.MIME_TYPE)).split("/")[1],
                        false
                    )
                    videos.add(mediaInfo)
                }
            }
        }
        return videos
    }

    @SuppressLint("Range", "Recycle")
    override fun getFolderContainVideo(orderBy: String): ArrayList<FolderInfo> {
        val folders = ArrayList<FolderInfo>()
        val contentResolver = context.contentResolver
        val projection = arrayOf(
            Media.BUCKET_ID, Media.BUCKET_DISPLAY_NAME, Media.SIZE
        )
        val cursor = contentResolver.query(
            Media.EXTERNAL_CONTENT_URI, projection, null, null, orderBy
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(Media.BUCKET_ID))
                if (folders.none { id == it.id }) {
                    val resolution = FolderInfo(
                        id,
                        cursor.getString(cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME)),
                        countVideoInFolder(id).toString(),
                    )
                    folders.add(resolution)
                }
            }
        }
        return folders
    }

    override fun countVideoInFolder(id: String): Int {
        return getVideoByIdBucket(id).size
    }

    override fun compareQuantity(quantity: Int): Boolean {
        return quantity < getAllVideo().size
    }


}