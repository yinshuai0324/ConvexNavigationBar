package com.convex.bar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.BaseInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.view.children

/**
 * @描述 凸起的NavigationBar
 * @作者：尹帅
 * @创建时间：2021-10-29 22:13:40
 */
class ConvexNavigationBar : ViewGroup, View.OnClickListener {
    /**
     * 贝尔赛的高度
     */
    private var convexHeight: Float = 0f

    /**
     * 贝尔赛的宽度
     */
    private var convexWidth: Float = 0f

    /**
     * 内容区域的高度
     */
    private var contentHeight: Float = 0f

    /**
     * 当前菜单第一个下标为凸出
     */
    private var convexIndex: Int = 3

    /**
     * 凸出区域的左边内边距
     */
    private var convexPaddingLeft: Float = 0f

    /**
     * 凸出其余右边内边距
     */
    private var convexPaddingRight: Float = 0f

    /**
     * 凸出区域顶部内边距
     */
    private var convexPaddingTop: Float = 0f

    /**
     * 凸出区域底部内边距
     */
    private var convexPaddingBottom: Float = 0f

    /**
     * 凸出区域内边距
     */
    private var convexPadding: Float = 0f

    /**
     * 点击菜单时是否凸出跟随
     */
    private var clickFollow: Boolean = false

    /**
     * 背景颜色
     */
    private var navigationBarBackgroundColor: Int = 0

    /**
     * 动画差值器
     */
    private var animInterpolator: BaseInterpolator = DecelerateInterpolator()
    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 记录贝尔赛的起始坐标 主要做动画用
     */
    private var bezierStartX = 0f

    /**
     * 菜单点击监听
     */
    lateinit var onMenuItemClickListener: (view: View) -> Unit


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ConvexNavigationBar)
        convexHeight = attrs.getDimension(R.styleable.ConvexNavigationBar_convexHeight, 0f)
        convexWidth = attrs.getDimension(R.styleable.ConvexNavigationBar_convexWidth, 0f)
        contentHeight = attrs.getDimension(R.styleable.ConvexNavigationBar_contentHeight, 0f)
        convexIndex = attrs.getInt(R.styleable.ConvexNavigationBar_convexIndex, 0)
        convexPaddingLeft =
            attrs.getDimension(R.styleable.ConvexNavigationBar_convexPaddingLeft, 0f)
        convexPaddingRight =
            attrs.getDimension(R.styleable.ConvexNavigationBar_convexPaddingRight, 0f)
        convexPaddingTop = attrs.getDimension(R.styleable.ConvexNavigationBar_convexPaddingTop, 0f)
        convexPadding = attrs.getDimension(R.styleable.ConvexNavigationBar_convexPadding, 0f)
        convexPaddingBottom =
            attrs.getDimension(R.styleable.ConvexNavigationBar_convexPaddingBottom, 0f)
        clickFollow = attrs.getBoolean(R.styleable.ConvexNavigationBar_clickFollow, false)
        attrs.recycle()
        setWillNotDraw(false)

        //获取背景颜色
        val drawable = this.background
        navigationBarBackgroundColor = if (drawable != null && drawable is ColorDrawable) {
            drawable.color
        } else {
            Color.WHITE
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = if (contentHeight > 0) {
            convexHeight + contentHeight
        } else {
            when (MeasureSpec.getMode(heightMeasureSpec)) {
                MeasureSpec.AT_MOST -> convexHeight + contentHeight
                MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
                MeasureSpec.UNSPECIFIED -> convexHeight + contentHeight
                else -> convexHeight + contentHeight
            }
        }
        //测量View的宽高情况
        setMeasuredDimension(widthSize, heightSize.toInt())
    }

    override fun onLayout(p0: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val otherViewWidth = (width - convexWidth) / (childCount - 1)
        var leftOffset = 0f
        children.forEachIndexed { index, view ->
            //当前View需要移动的宽度
            val offsetWidth = if (convexIndex == index) {
                convexWidth
            } else {
                otherViewWidth
            }
            //当前View需要移动的高度
            val offsetHeight = if (convexIndex == index) {
                height
            } else {
                height - convexHeight
            }

            //View的left
            val left = if (convexIndex == index) {
                val padding = if (convexPadding > 0) convexPadding else convexPaddingLeft
                leftOffset.toInt() + padding.toInt()
            } else {
                leftOffset.toInt()
            }

            //View的top
            val top = if (convexIndex == index) {
                (if (convexPadding > 0) convexPadding else convexPaddingTop).toInt()
            } else {
                convexHeight.toInt()
            }

            //View的right
            val right = if (convexIndex == index) {
                val padding = if (convexPadding > 0) convexPadding else convexPaddingRight
                (leftOffset + offsetWidth - padding).toInt()
            } else {
                (leftOffset + offsetWidth).toInt()
            }

            //View的bottom
            val bottom = if (convexIndex == index) {
                val padding = if (convexPadding > 0) convexPadding else convexPaddingBottom
                height - padding.toInt()
            } else {
                height
            }

            //测量子View
            measureChild(view, offsetWidth.toInt(), offsetHeight.toInt())
            //摆放子View
            view.layout(left, top, right, bottom)
            //累计距离
            leftOffset += offsetWidth
        }
    }

    /**
     * 添加菜单
     */
    fun addMenu(view: View) {
        view.setOnClickListener(this)
        this.addView(view)
    }

    /**
     * 更新二段三阶贝尔赛曲线参数
     */
    private fun updateParams() {
        path.reset()
        val convexCenterX = convexWidth / 2
        //1.第一个控制点
        val controlPoint1 = PointF(bezierStartX, convexHeight)
        //1.第二个控制点
        val controlPoint2 = PointF(bezierStartX + convexCenterX / 2, convexHeight)
        //1.第三个控制点
        val controlPoint3 = PointF(bezierStartX + convexCenterX / 2, 0f)
        //1.第四个控制点
        val controlPoint4 = PointF(bezierStartX + convexWidth / 2, 0f)
        //2.第二个控制点
        val controlPoint6 = PointF(bezierStartX + convexCenterX + (convexCenterX / 2), 0f)
        //2.第三个控制点
        val controlPoint7 = PointF(bezierStartX + convexCenterX + (convexCenterX / 2), convexHeight)
        //2.第四个控制点
        val controlPoint8 = PointF(bezierStartX + convexWidth, convexHeight)

        path.moveTo(0f, convexHeight)
        path.lineTo(controlPoint1.x, controlPoint1.y)

        path.cubicTo(
            controlPoint2.x,
            controlPoint2.y,
            controlPoint3.x,
            controlPoint3.y,
            controlPoint4.x,
            controlPoint4.y
        )

        path.cubicTo(
            controlPoint6.x,
            controlPoint6.y,
            controlPoint7.x,
            controlPoint7.y,
            controlPoint8.x,
            controlPoint8.y
        )

        path.lineTo(width.toFloat(), convexHeight)
        path.lineTo(width.toFloat(), convexHeight + contentHeight)
        path.lineTo(0f, convexHeight + contentHeight)
        path.lineTo(0f, convexHeight)
        path.close()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setBezierIndex(convexIndex, false)
    }

    private fun setBezierIndex(index: Int, anim: Boolean) {
        if (index > childCount - 1) {
            throw Exception("超出子View的数量")
            return
        }
        val childSize = childCount
        val startX = bezierStartX
        val otherViewWidth = (width - convexWidth) / (childSize - 1)
        val endX = otherViewWidth * index
        if (!anim) {
            bezierStartX = endX
            invalidate()
        } else {
            startAnim(startX, endX)
        }
        convexIndex = index
        //重新布局
        requestLayout()
    }

    private fun startAnim(startX: Float, endX: Float) {
        val anim = ValueAnimator.ofFloat(startX, endX)
        anim.duration = 100
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            anim.interpolator = animInterpolator
        }
        anim.addUpdateListener {
            val value = it.animatedValue as Float
            bezierStartX = value
            invalidate()
        }
        anim.start()
    }

    /**
     * 设置动画差值器
     */
    fun setAnimInterpolator(baseInterpolator: BaseInterpolator) {
        this.animInterpolator = baseInterpolator
    }

    override fun dispatchDraw(canvas: Canvas) {
        //更新参数
        updateParams()
        //开始绘制主体区域
        paint.reset()
        paint.isAntiAlias = true
        paint.color = navigationBarBackgroundColor
        paint.style = Paint.Style.FILL
        canvas.drawPath(path, paint)
        super.dispatchDraw(canvas)
    }

    private fun getIndexByView(view: View): Int {
        return children.indexOf(view)
    }

    override fun onClick(view: View) {
        if (clickFollow) {
            val index = getIndexByView(view)
            setBezierIndex(index, true)
        }
        if (::onMenuItemClickListener.isInitialized) {
            onMenuItemClickListener.invoke(view)
        }
    }
}