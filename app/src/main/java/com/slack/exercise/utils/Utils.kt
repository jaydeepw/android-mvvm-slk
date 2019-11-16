package com.slack.exercise.utils

import android.content.res.Resources
import android.support.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStream

class Utils {

    companion object {
        fun readRawTextFile(resources: Resources, @RawRes resId: Int): String? {
            val inputStream: InputStream = resources.openRawResource(resId)
            return inputStream.bufferedReader().use(BufferedReader::readText)
        }
    }
}