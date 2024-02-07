package com.tearas.resizevideo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore.Video.Media
import android.widget.Toast
import com.arthenica.ffmpegkit.FFprobeKit
import com.arthenica.ffmpegkit.Log
import com.arthenica.ffmpegkit.MediaInformation
import com.tearas.resizevideo.model.FolderInfo
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.model.MediaInfo
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

interface IVideo {

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

    fun getVideoByPath(name: String): MediaInfo?
    fun getVideoSave(): ArrayList<MediaInfo>
    fun getAudioSave(): ArrayList<MediaInfo>
    fun getVideoUnSave(): ArrayList<MediaInfo>

    fun getFolderContainVideo(orderBy: String = Media.DATE_ADDED + " DESC"): ArrayList<FolderInfo>
    fun countVideoInFolder(id: String): Int
    fun compareQuantity(quantity: Int): Boolean
    fun getPathExternalFolderVideo(): String
    fun createFolderVideo(): String
    fun createFolderAudio(): String
    fun getPathVideoCacheFolder(): String
    fun getPathExternalFolderAudio(): String
    fun createFolder(nameFolder: String, isVideo: Boolean): String
    fun replaceFiles(pathInput: String, pathOutput: String): File?
}

class HandleMediaVideo(private val context: Context) : IVideo {
    override fun replaceFiles(pathInput: String, pathOutput: String): File? {
        val fileOriginal = File(pathInput)
        val fileReplace = File(pathOutput)
        return try {
            fileReplace.inputStream().use { input ->
                fileOriginal.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            fileReplace.renameTo(fileOriginal)
            Toast.makeText(context, "Replace success", Toast.LENGTH_SHORT).show()
            fileOriginal
        } catch (e: Exception) {
            Toast.makeText(context, "Replace error", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun saveFileToExternalStorage(
        context: Context, isVideo: Boolean, inputStream: InputStream, fileName: String
    ): File? {
        val folder = File(
            if (isVideo) getPathExternalFolderVideo()
            else getPathExternalFolderAudio()
        )
        return try {
            val fileSave = File(folder, fileName)
            if (fileSave.exists()) {
                Toast.makeText(context, "File exists", Toast.LENGTH_SHORT).show()
                return fileSave
            }
            val outputStream = FileOutputStream(fileSave)
            val buffer = ByteArray(1024)
            var bytesRead: Int = 0
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            inputStream.close()
            outputStream.close()

            Toast.makeText(context, "Save Successfully", Toast.LENGTH_SHORT).show()
            return fileSave
        } catch (e: Exception) {
            Toast.makeText(context, "Error when save", Toast.LENGTH_SHORT).show()
            return null
        }

    }


    override fun getPathVideoCacheFolder(): String {
        val file = File(context.cacheDir.path, "Videos")
        if (file.exists() || file.mkdirs()) return file.path
        return ""
    }

    override fun getPathExternalFolderVideo(): String {
        return createFolderVideo()
    }

    override fun getPathExternalFolderAudio(): String {
        return createFolderAudio()
    }

    override fun createFolderAudio(): String {
        return createFolder("TerasResizeAudio", false)
    }

    override fun createFolderVideo(): String {
        return createFolder("TerasResizeVideo", true)
    }

    override fun createFolder(nameFolder: String, isVideo: Boolean): String {
        val path = getPathStorageExternal() + if (isVideo) "/Movies" else "/Music"
        val folder = File(path, nameFolder)
        return if (folder.exists() || folder.mkdirs()) {
            folder.path
        } else {
            throw IllegalStateException("Unable to create directory $nameFolder")
        }
    }


    override fun getPathStorageExternal(): String {
        val storeMediaMng = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            storeMediaMng.storageVolumes[0].directory!!.path
        } else {
            Environment.getExternalStorageDirectory().path
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
            Media.MIME_TYPE,
            Media.BITRATE
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

                    val formatTime = Utils.formatTime(getLong(getColumnIndex(Media.DURATION)))

                    val mediaInfo = MediaInfo(
                        getLong(getColumnIndex(Media._ID)),
                        getString(getColumnIndex(Media.DISPLAY_NAME)),
                        getString(getColumnIndex(Media.DATA)),
                        getLong(getColumnIndex(Media.SIZE)),
                        resolution,
                        formatTime,
                        getString(getColumnIndex(Media.MIME_TYPE)).split("/")[1],
                        getLong(getColumnIndex(Media.BITRATE)),
                        false
                    )
                    if (formatTime != "00:00:00") {
                        videos.add(mediaInfo)
                    }
                }
            }
        }
        return videos
    }

    override fun getVideoByPath(name: String): MediaInfo? {
        val selection = "${Media.DATA} like ?"
        val selectionArg = arrayOf("$name")  // Thư mục cụ thể
        return if (getVideo(selection, selectionArg).isNotEmpty()) getVideo(
            selection,
            selectionArg
        )[0] else null
    }

    override fun getVideoSave(): ArrayList<MediaInfo> {
        val cacheDir = File(getPathExternalFolderVideo())
        val videos = ArrayList<MediaInfo>()
        cacheDir.listFiles()?.forEachIndexed { index, file ->
            if (file.isFile && file.name.endsWith(".mp4")) {
                val formatTime = Utils.formatTime(file.length())
                val fFmpegKit = FFprobeKit.getMediaInformation(file.path).mediaInformation
                val mediaInfo = createMediaInfo(fFmpegKit, index.toLong())
                if (formatTime != "00:00:00") {
                    videos.add(mediaInfo)
                }
            }
        }
        return videos
    }

    override fun getAudioSave(): ArrayList<MediaInfo> {
        val cacheDir = File(getPathExternalFolderAudio() )
        val videos = ArrayList<MediaInfo>()
        cacheDir.listFiles()?.forEachIndexed { index, file ->
            try {
                val fFmpegKit = FFprobeKit.getMediaInformation(file.path).mediaInformation
                val mediaInfo = createMediaInfo(fFmpegKit, index.toLong())
                videos.add(mediaInfo)
            } catch (e: Exception) {
            }
        }
        return videos
    }

    private fun createMediaInfo(
        mediaInfo: MediaInformation,
        executionId: Long,
    ): MediaInfo {
        val filename = mediaInfo.filename.substringAfterLast("/")
        val size = mediaInfo.size.toLong()
        val resolution = if (mediaInfo.filename.endsWith("mp4")) {
            Resolution(mediaInfo.streams[0].width.toInt(), mediaInfo.streams[0].height.toInt())
        } else null
        val durationFormatted = Utils.formatTime(mediaInfo.duration.toFloat().toLong() * 1000)
        val extension = mediaInfo.filename.substringAfterLast(".")
        val bitrate = mediaInfo.bitrate.toFloat().toLong()

        return MediaInfo(
            executionId,
            filename,
            mediaInfo.filename,
            size,
            resolution,
            durationFormatted,
            extension,
            bitrate,
            false
        )
    }

    override fun getVideoUnSave(): ArrayList<MediaInfo> {
        val cacheDir = File(getPathVideoCacheFolder())
        val videos = ArrayList<MediaInfo>()
        cacheDir.listFiles()?.forEachIndexed { index, file ->
            if (file.name.endsWith(".mp4")) {
                val formatTime = Utils.formatTime(file.length())
                try {
                    val fFmpegKit = FFprobeKit.getMediaInformation(file.path).mediaInformation
                    val mediaInfo = createMediaInfo(fFmpegKit, index.toLong())
                    if (formatTime != "00:00:00") {
                        videos.add(mediaInfo)
                    }
                } catch (e: Exception) {

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