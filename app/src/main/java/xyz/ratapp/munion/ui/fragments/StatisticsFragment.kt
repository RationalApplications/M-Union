package xyz.ratapp.munion.ui.fragments

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.taishi.flipprogressdialog.FlipProgressDialog
import kotlinx.android.synthetic.main.fragment_statistics.*
import xyz.ratapp.munion.R
import xyz.ratapp.munion.controllers.interfaces.DataCallback
import xyz.ratapp.munion.data.DataController
import xyz.ratapp.munion.data.pojo.Statistics
import xyz.ratapp.munion.ui.fragments.common.BaseFragment
import xyz.ratapp.munion.ui.views.LoadingDialog
import xyz.ratapp.munion.ui.views.audio.AudiosDialog
import java.util.*

/**
 * <p>Date: 30.10.17</p>
 * @author Simon
 */
class StatisticsFragment : BaseFragment() {

    private lateinit var mChart: PieChart
    private lateinit var mTfRegular: Typeface
    private lateinit var mTfLight: Typeface
    private var dialog: LoadingDialog? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun getFragmentName(context: Context): String {
        return context.resources.getString(R.string.title_statistics)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        getStatistic()
    }

    override fun onStart() {
        super.onStart()
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun getStatistic() {
        val instance = DataController.getInstance(activity)
        instance.getStatistics(object: DataCallback<Statistics>
        {
            override fun onSuccess(data: Statistics) {
                hideDialog()
                setupFragmentFromUiThread(data)
            }

            override fun onFailed(thr: Throwable?) {
                showDialog()
                instance.loadStatistics(dialog, object: DataCallback<Statistics> {
                    override fun onSuccess(data: Statistics) {
                        hideDialog()
                        setupFragmentFromUiThread(data)
                    }

                    override fun onFailed(thr: Throwable?) {
                        hideDialog()
                        Toast.makeText(activity,
                                R.string.cant_load_statistics,
                                Toast.LENGTH_LONG).show()
                    }
                })
            }
        })
    }

    private fun loadStatistic() {
        val instance = DataController.getInstance(activity)
        instance.loadStatistics(dialog, object: DataCallback<Statistics>
        {
            override fun onSuccess(data: Statistics) {
                hideDialog()
                setupFragmentFromUiThread(data)
            }

            override fun onFailed(thr: Throwable?) {
                hideDialog()
                Toast.makeText(activity,
                        R.string.cant_load_statistics,
                        Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupFragmentFromUiThread(data: Statistics) {
        val mainHandler = Handler(activity.mainLooper)

        val myRunnable = {
            setData(data)
            setupData(data)
            setupDelegates(data)
        }
        mainHandler.post(myRunnable)
    }

    private fun setupDelegates(data: Statistics) {
        ll_calls_container.setOnClickListener {

            val dialog = AudiosDialog(context)

            dialog.show()
            dialog.setData(data.talksUrls)
        }
    }

    private fun setupView() {
        setupPieChart()
        btn_refresh_stats.setOnClickListener {
            showDialog()
            loadStatistic()
        }
    }

    private fun showDialog() {
        val root = LinearLayout(context)
        root.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        root.gravity = Gravity.CENTER

        val pb = ProgressBar(context)
        pb.isIndeterminate = true
        pb.isActivated = true
        root.addView(pb, LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT))

        //dialog init
        dialog = LoadingDialog()
        dialog!!.show(activity.fragmentManager, "")
    }

    fun hideDialog() {
        if(dialog != null) {
            dialog!!.dismiss()
        }
    }

    private fun setupData(data: Statistics) {
        startCountAnimation(data.callsCount,
                50, tv_calls_count)
        startCountAnimation(data.looksCount,
                200, tv_looks_count)
        startCountAnimation(data.viewsCount,
                8, tv_views_count)
        tv_object.text = data.objectName
    }

    private fun startCountAnimation(toValue: Int, duration: Long,
                                    textView: TextView) {
        val animator = ValueAnimator.ofInt(0, toValue)
        animator.duration = duration * toValue
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener {
            animation ->
            if(this@StatisticsFragment.isVisible) {
                textView.text = animation.animatedValue.toString()
            }
        }
        animator.start()
    }

    private fun setupPieChart() {
        mTfRegular = Typeface.createFromAsset(activity.assets, "OpenSans-Regular.ttf")
        mTfLight = Typeface.createFromAsset(activity.assets, "OpenSans-Light.ttf")
        mChart = pc_stats

        mChart.setUsePercentValues(true)
        mChart.description.isEnabled = false
        mChart.setExtraOffsets(5f, 5f, 45f, 30f)

        mChart.dragDecelerationFrictionCoef = 0.95f

        mChart.setCenterTextTypeface(mTfLight)
        mChart.centerText = generateCenterSpannableText()

        mChart.isDrawHoleEnabled = true
        mChart.setHoleColor(activity.resources.getColor(R.color.colorPrimary))

        mChart.setTransparentCircleColor(Color.WHITE)
        mChart.setTransparentCircleAlpha(110)

        mChart.holeRadius = 48f
        mChart.transparentCircleRadius = 52f

        mChart.setDrawCenterText(true)

        mChart.rotationAngle = 0f
        // enable rotation of the chart by touch
        mChart.isRotationEnabled = true
        mChart.isHighlightPerTapEnabled = true

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)

        val l = mChart.legend
        l.textColor = resources.getColor(R.color.hint)
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
        mChart.setEntryLabelTextSize(8f)
    }

    private fun setData(stat: Statistics) {
        val map = stat.data
        var range = 0f
        for (entry in map) {
            range += entry.component2()
        }

        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (entry in map) {
            val uri = Uri.parse(entry.component1())
            entries.add(PieEntry((entry.component2() / range) * 100,
                    uri.host))
        }

        val dataSet = PieDataSet(entries, "Просмотры")

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
        data.setValueTextSize(8f)
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

}