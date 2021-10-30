package com.ooimi.bar

import android.content.Context
import android.graphics.Color
import android.media.Image
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class MenuView : LinearLayout {
    private val icon: ImageView
    private val text: TextView
    private var selRes: Int = 0
    private var unSelRes: Int = 0
    private var title: String = ""
    private var isSelect: Boolean = false

    constructor(context: Context, selRes: Int, unSelRes: Int, title: String) : super(context) {
        this.selRes = selRes
        this.unSelRes = unSelRes
        this.title = title
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.view_menu_item, this, true)
        icon = findViewById(R.id.icon)
        text = findViewById(R.id.text)
        updateUI()
    }

    fun setSelect(select: Boolean) {
        this.isSelect = select
        updateUI()
    }

    private fun updateUI() {
        text.text = title
        if (isSelect) {
            icon.setImageResource(selRes)
            text.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else {
            icon.setImageResource(selRes)
            text.setTextColor(Color.GRAY)
        }
    }

}