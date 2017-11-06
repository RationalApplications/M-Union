package xyz.ratapp.munion.views

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.fragment_statistics.*
import xyz.ratapp.munion.views.common.FragmentBase
import xyz.ratapp.munion.R
import java.util.ArrayList

/**
 * <p>Date: 30.10.17</p>
 * @author Simon
 */
class StatisticsFragment : FragmentBase(), OnChartValueSelectedListener {
    private lateinit var mChart: PieChart

    private lateinit var mTfRegular: Typeface
    private lateinit var mTfLight: Typeface

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_statistics)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTfRegular = Typeface.createFromAsset(activity.assets, "OpenSans-Regular.ttf")
        mTfLight = Typeface.createFromAsset(activity.assets, "OpenSans-Light.ttf")
        mChart = pc_stats

        mChart.setUsePercentValues(true)
        mChart.description.isEnabled = false
        mChart.setExtraOffsets(10f, 10f, 45f, 30f)

        mChart.dragDecelerationFrictionCoef = 0.95f

        mChart.setCenterTextTypeface(mTfLight)
        mChart.centerText = generateCenterSpannableText()

        mChart.isDrawHoleEnabled = true
        mChart.setHoleColor(activity.resources.getColor(R.color.colorPrimary))

        mChart.setTransparentCircleColor(Color.WHITE)
        mChart.setTransparentCircleAlpha(110)

        mChart.holeRadius = 46f
        mChart.transparentCircleRadius = 50f

        mChart.setDrawCenterText(true)

        mChart.rotationAngle = 0f
        // enable rotation of the chart by touch
        mChart.isRotationEnabled = true
        mChart.isHighlightPerTapEnabled = true

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this)

        setData()

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)

        val l = mChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE)
        mChart.setEntryLabelTypeface(mTfRegular)
        mChart.setEntryLabelTextSize(12f)
    }

    private fun setData() {

        val count = 6
        val range = 2467f
        val markets = arrayListOf("emls.ru", "spb.rucountry.ru", "mirkvartir.ru",
                "realty.yandex.ru", "avito.ru", "restate.ru")
        val values = arrayListOf(615f, 33f, 19f, 170f, 1588f, 42f)

        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(PieEntry((values[i] / range) * 100,
                    markets[i],
                    resources.getDrawable(R.drawable.star)))
        }

        val dataSet = PieDataSet(entries, "Sales Results")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors

        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(mTfLight)
        mChart.data = data

        // undo all highlights
        mChart.highlightValues(null)

        mChart.invalidate()
    }

    private fun generateCenterSpannableText(): SpannableString {

        val s = SpannableString("Активность\nпродаж")
        s.setSpan(ForegroundColorSpan(Color.WHITE), 0, 17, 0)

        return s
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null)
return
        Log.i("VAL SELECTED",
                "Value: " + e.y + ", index: " + h!!.getX()
                + ", DataSet index: " + h.getDataSetIndex())
    }
}