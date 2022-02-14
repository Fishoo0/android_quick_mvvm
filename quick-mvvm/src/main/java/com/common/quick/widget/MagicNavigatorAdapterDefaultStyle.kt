//package com.common.module.quick.widget
//
//import android.content.Context
//import android.graphics.Typeface
//import android.util.TypedValue
//import android.view.ViewGroup
//import androidx.viewpager.widget.ViewPager
//import com.app.hubert.guide.util.ScreenUtils
//import com.blackboard.common_module.R
//import com.common.module.skin.DefaultSkinLoader
//import com.common.module.trace.TraceData
//import com.common.module.trace.traceData
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
//import org.json.JSONObject
//import java.io.Serializable
//
///**
// * Styled indicator like QuickCalculate practice
// */
//class MagicNavigatorAdapterDefaultStyle(val viewPager: ViewPager, val list: List<NavigatorItemData>) : CommonNavigatorAdapter() {
//    private fun onUpdateTitleView(view: ColorTransitionPagerTitleView, data: NavigatorItemData, index: Int) {
//        view.normalColor = DefaultSkinLoader.getInstance().getColor(view.context, R.color.bluegrey_800)
//        view.selectedColor = DefaultSkinLoader.getInstance().getColor(view.context, R.color.bluegrey_900)
//        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
//        view.text = data.title
//    }
//
//    override fun getTitleView(parent: ViewGroup, index: Int): IPagerTitleView {
//        val titleView = TitleView(parent.context)
//        titleView.setOnClickListener() { v ->
//            val data = list[index]
//            v.contentDescription = data.buttonKey
//            v.traceData(TraceData.Builder().put(data.paramsName, data.traceParams).build())
//
//            viewPager.adapter?.let {
//                if (it.count == this.count) {
//                    viewPager.currentItem = index
//                }
//            }
//        }
//        onUpdateTitleView(titleView, list[index], index)
//        return titleView
//    }
//
//    override fun getCount(): Int = list.size
//
//    override fun getIndicator(parent: ViewGroup): IPagerIndicator {
//        val context = parent.context
//        val linePagerIndicator = LinePagerIndicator(context)
//        linePagerIndicator.mode = LinePagerIndicator.MODE_EXACTLY
//        linePagerIndicator.setColors(DefaultSkinLoader.getInstance().getColor(context, R.color.brand_500))
//        linePagerIndicator.roundRadius = context?.resources?.getDimension(R.dimen.space_small_4)
//            ?: 0f
//        linePagerIndicator.xOffset = context?.resources?.getDimension(R.dimen.space_small_8) ?: 0f
//        linePagerIndicator.lineWidth = ScreenUtils.dp2px(context, 24).toFloat()
//        linePagerIndicator.lineHeight = ScreenUtils.dp2px(context, 4).toFloat()
//        return linePagerIndicator
//    }
//
//
//    private class TitleView(context: Context?) : ColorTransitionPagerTitleView(context) {
//
//        override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
//            super.onEnter(index, totalCount, enterPercent, leftToRight)
//            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
//            typeface = Typeface.defaultFromStyle(Typeface.BOLD);
//
//        }
//
//        override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
//            super.onLeave(index, totalCount, leavePercent, leftToRight)
//            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//            typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
//        }
//    }
//
//    class NavigatorItemData(val title: String, val buttonKey: String? = "", val paramsName: String? = "", val traceParams: JSONObject? = null, val id: String? = "") : Serializable
//}