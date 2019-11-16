package com.slack.exercise

import android.app.IntentService
import android.content.Intent
import com.slack.exercise.utils.Utils
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * This service will silently copy the backlisted items from the R.raw.blacklist.txt into the
 * files directory of the app. This copied file is later used to improve the user experience of the app
 */
class BlackListCopyService : IntentService(BlackListCopyService::class.qualifiedName) {

    companion object {
        const val BLACKLIST_FILE_NAM = "blacklisted.txt"
    }

    override fun onHandleIntent(intent: Intent?) {
        val file = File(filesDir, BLACKLIST_FILE_NAM)
        if (!file.exists()) {
            // for now we will only consider the presense of file
            // as a signal to update it from blacklist database.
            // We can check the length as well before we start copying the blacklist
            // but possibility of someone erasing the file data is very low.
            Timber.d("Blacklist file not found. Copying from raw resources")
            copyBlackLisData()
        } else {
            Timber.d("Blacklist found. Not copying from raw resources")
        }
    }

    private fun copyBlackLisData() {
        try {
            val file = File(filesDir, BLACKLIST_FILE_NAM)
            val outputStream = FileOutputStream(file);
            outputStream.write(Utils.readRawTextFile(resources, R.raw.blacklist)?.toByteArray());
            outputStream.flush()
            outputStream.close()
        } catch (e : IOException) {
            e.printStackTrace();
        }
    }
}