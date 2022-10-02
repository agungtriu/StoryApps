package com.example.storyapps.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.storyapps.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class Utils {
    companion object {
        fun ProgressBar.showLoading(isLoading: Boolean) {
            visibility = if (isLoading) View.VISIBLE else View.GONE

        }

        private const val FILENAME_FORMAT = "dd-MMM-yyyy"
        private val timeStamp: String = SimpleDateFormat(
            FILENAME_FORMAT, Locale.US
        ).format(System.currentTimeMillis())

        private fun createCustomTempFile(context: Context): File {
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(timeStamp, ".jpg", storageDir)
        }

        fun createFile(application: Application): File {
            val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
                File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
            }

            val outputDirectory =
                if (mediaDir != null && mediaDir.exists()) mediaDir else application.filesDir

            return File(outputDirectory, "$timeStamp.jpg")
        }

        fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
            val matrix = Matrix()
            return if (isBackCamera) {
                matrix.postRotate(90f)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                matrix.postRotate(-90f)
                matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
        }

        fun uriToFile(selectedImg: Uri, context: Context): File {
            val contentResolver: ContentResolver = context.contentResolver
            val myFile = createCustomTempFile(context)

            val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
            val outputStream: OutputStream = FileOutputStream(myFile)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()

            return myFile
        }

        fun formatDate(
            currentDateString: String, targetTimeZone: String, context: Context
        ): String {
            val instant = Instant.parse(currentDateString)
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
                .withZone(ZoneId.of(targetTimeZone))
            return covertTimeToText(formatter.format(instant), context)
        }

        @SuppressLint("SimpleDateFormat")
        fun covertTimeToText(dataDate: String, context: Context): String {
            var convTime = ""
            val suffix = context.getString(R.string.utils_time_ago)
            val textSecond = context.getString(R.string.utils_time_second)
            val textSeconds = context.getString(R.string.utils_time_seconds)
            val textMinute = context.getString(R.string.utils_time_minute)
            val textMinutes = context.getString(R.string.utils_time_minutes)
            val textHour = context.getString(R.string.utils_time_hour)
            val textHours = context.getString(R.string.utils_time_hours)
            val textDay = context.getString(R.string.utils_time_day)
            val textDays = context.getString(R.string.utils_time_days)

            try {
                val dateFormat = SimpleDateFormat("dd MMM yyyy | HH:mm")
                val pasTime = dateFormat.parse(dataDate)
                val nowTime = Date()
                val dateDiff = nowTime.time - pasTime.time
                val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
                val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
                val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
                val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
                convTime = if (second < 60) {
                    if (second > 1) {
                        "$second $textSeconds $suffix"
                    } else {
                        "$second $textSecond $suffix"
                    }
                } else if (minute < 60) {
                    if (minute > 1) {
                        "$minute $textMinutes $suffix"
                    } else {
                        "$minute $textMinute $suffix"
                    }
                } else if (hour < 24) {
                    if (hour > 1) {
                        "$hour $textHours $suffix"
                    } else {
                        "$hour $textHour $suffix"
                    }
                } else if (day < 7) {
                    if (day > 1) {
                        "$day $textDays $suffix"
                    } else {
                        "$day $textDay $suffix"
                    }
                } else {
                    dataDate
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                Log.e("ConvTimeE", e.message.toString())
            }
            return convTime
        }
    }
}