package com.slack.exercise.ui.widgets

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet


/**
 *
 */
class SlackTextViewRegular : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    fun init() {

        if (!isInEditMode) {
            val typeface = getCustomTypeface(context)
            setTypeface(typeface, Typeface.NORMAL)
        }

    }

    companion object {

        fun getCustomTypeface(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, "font/lato_regular.ttf")
        }
    }

}
