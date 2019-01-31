package com.maosong.component.widget

/**
 * Created by colin on 2018/7/23.
 */
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.colin.component.R
import com.colin.tools.QMUIDisplayHelper

class BotView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val itemViews: MutableList<BotItemView> = mutableListOf()
    private var checkedIndex = 0
    private var mPager: ViewPager? = null
    private var centerClick: View.OnClickListener? = null
    private var mTextSize = 12f

    override fun onFinishInflate() {
        super.onFinishInflate()
        orientation = HORIZONTAL
        clipChildren = false
    }

    fun setItems(itemParams: MutableList<BotItemParams>) {
        if (itemParams.isEmpty() || itemParams.size % 2 != 0) {
            return
        }
        val itemLayoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        for (i in 0 until itemParams.size) {
            val itemView = BotItemView(context)
            itemView.textSize = mTextSize
            itemView.setParams(itemParams[i])
            itemView.layoutParams = itemLayoutParams
            itemViews.add(itemView)
            itemView.setOnClickListener {
                checkItem(i)
            }
            addView(itemView)
        }
        checkItem(0)
    }

    fun setCenterUpItem(params: BotItemParams) {
        //加入中间大按钮
        val centerViewLayoutParams = LayoutParams(0, QMUIDisplayHelper.dp2px(context, 70), 1f)
        centerViewLayoutParams.gravity = Gravity.BOTTOM
        val centerView = BotItemView(context)
        centerView.setCenterMode(params)
        val centerIndex = itemViews.size / 2
        if (centerIndex in 0 until itemViews.size) {
            addView(centerView, centerIndex, centerViewLayoutParams)
            centerView.setOnClickListener {
                centerClick?.onClick(it)
            }
        }
    }

    fun checkItem(index: Int) {
        checkedIndex = index
        for (i in 0 until itemViews.size) {
            itemViews[i].unCheckMe()
            if (index == i) {
                itemViews[i].checkMe()
            }
        }
        mPager?.setCurrentItem(index, false)
    }

    /**
     * 设置item文字的大小
     * @param size 文字大小  单位SP
     */
    fun setTextSize(size: Float) {
        mTextSize = size
    }


    fun setCenterViewClickListener(listener: View.OnClickListener) {
        centerClick = listener
    }

    fun setStepWithViewPager(viewPager: ViewPager) {
        if (viewPager.adapter?.count == itemViews.size) {
            mPager = viewPager
            mPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    checkItem(position)
                }

            })
        } else {
            throw IndexOutOfBoundsException("导航栏item数量与viewpager页数不一样")
        }
    }


    private class BotItemView(context: Context?) : LinearLayout(context) {

        private lateinit var mParams: BotItemParams
        private lateinit var mIcon: ImageView
        private var isChecked = false
        private var isCenterMode = false
        var textSize = 12f

        init {
            orientation = VERTICAL
        }

        fun setParams(params: BotItemParams) {
            if (params.normalIcon == -1)
                return
            mParams = params
            gravity = Gravity.CENTER
            mIcon = ImageView(context)
            val iconLayoutParams =
                LayoutParams(QMUIDisplayHelper.dp2px(context, 25), QMUIDisplayHelper.dp2px(context, 25))
            mIcon.layoutParams = iconLayoutParams
            mIcon.setImageResource(params.normalIcon)
            mIcon.id = R.id.nav_item_icon
            addView(mIcon)

            if (params.title.isBlank())
                return
            val title = TextView(context)
            val titleLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            title.setTextColor(ContextCompat.getColor(context, R.color.main_bot_text))
            title.textSize = textSize
            title.text = params.title
            title.layoutParams = titleLayoutParams
            addView(title)
        }

        fun setCenterMode(params: BotItemParams) {
            if (params.normalIcon == -1)
                return
            mParams = params
            gravity = Gravity.CENTER
            isCenterMode = true
            mIcon = ImageView(context)
            val iconLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dp2px(context, 50))
            mIcon.setImageResource(params.normalIcon)
            mIcon.layoutParams = iconLayoutParams
            mIcon.id = R.id.nav_item_icon
            addView(mIcon)

            if (params.title.isBlank())
                return
            val title = TextView(context)
            val titleLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            title.setTextColor(ContextCompat.getColor(context, R.color.main_bot_text))
            title.textSize = textSize
            title.text = params.title
            title.layoutParams = titleLayoutParams
            addView(title)
        }

        fun checkMe() {
            if (isCenterMode)
                return
            isChecked = true
            mIcon.setImageResource(mParams.checkedIcon)
        }

        fun unCheckMe() {
            if (isCenterMode)
                return
            isChecked = false
            mIcon.setImageResource(mParams.normalIcon)
        }
    }


    data class BotItemParams(val normalIcon: Int, val checkedIcon: Int, val title: String)
}
