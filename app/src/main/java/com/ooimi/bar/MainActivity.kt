package com.ooimi.bar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.convex.bar.ConvexNavigationBar

class MainActivity : AppCompatActivity() {
    private lateinit var navigationBar: ConvexNavigationBar
    private lateinit var rootView: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationBar = findViewById(R.id.navigationBar)

        val imageView = ImageView(this)
        imageView.layoutParams = ViewGroup.LayoutParams(50, 50)
        imageView.setImageResource(R.mipmap.ic_push)

        navigationBar.addMenu(
            MenuView(
                this,
                R.mipmap.ic_tab_home_select,
                R.mipmap.ic_tab_home_un_select,
                "首页"
            )
        )
        navigationBar.addMenu(
            MenuView(
                this,
                R.mipmap.ic_tab_xin_select,
                R.mipmap.ic_tab_xin_un_select,
                "收藏"
            )
        )
        navigationBar.addMenu(imageView)
        navigationBar.addMenu(
            MenuView(
                this,
                R.mipmap.ic_tab_ka_select,
                R.mipmap.ic_tab_ka_un_select,
                "卡包"
            )
        )
        navigationBar.addMenu(
            MenuView(
                this,
                R.mipmap.ic_tab_mine_select,
                R.mipmap.ic_tab_mine_un_select,
                "我的"
            )
        )

    }
}