package com.example.nitish.cashrichtask

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.nitish.cashrichtask.adapter.YearAdapter
import com.example.nitish.cashrichtask.listener.ClickListener
import com.example.nitish.cashrichtask.model.CashrichModel
import com.example.nitish.cashrichtask.networkApi.CashrichApiClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.R.attr.animationDuration
import android.view.View
import java.util.*
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.charts.BarChart
import org.eazegraph.lib.models.PieModel
import org.eazegraph.lib.charts.PieChart








class MainActivity : AppCompatActivity(), ClickListener {
    override fun clickedItem(position: Int) {
        var data = list.get(position)
        details.text = data.point
        share_value.text = data.equity +"%"
        fixed_value.text = (100 - data.equity.toInt()).toString() +"%"
        var isBlue = data.equity.toInt() >= 50
        background.setBackgroundColor(Color.parseColor( if(isBlue)  "#fbd552"  else "#5b8aca"))
        details.setTextColor(Color.parseColor(if(isBlue)  "#313131"  else "#ffffff"))


        var eq = (data.equity.toFloat() * 12f) /100f

        val eqAnimator = ValueAnimator.ofFloat(sharedTextSize, 12+eq)
        eqAnimator.duration = 600
        eqAnimator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Float
            share_value.setTextSize(animatedValue)
            sharedTextSize = animatedValue
        }
        eqAnimator.start()


        var a = ((100 - data.equity.toFloat()) * 12f) /100f
        val animator = ValueAnimator.ofFloat(fixedTextSize, 12+a)
        animator.duration = 600
        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Float
            fixed_value.setTextSize(animatedValue)
            fixedTextSize = animatedValue
        }
        animator.start()

        equityModel?.value = data.equity.toFloat()
        fixed?.value = (100 - data.equity.toFloat())
        piechart.update()
    }

    var sharedTextSize = 12f
    var fixedTextSize = 12f
    var list = ArrayList<CashrichModel>()
    var adapter : YearAdapter ?= null
    var toggle = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRecyclerView()
        callApi()


        val t = Timer()
        var myTimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {

                    var top = if(toggle) title_ else subtitle
                    var bottom = if(toggle) subtitle else title_
                    val animation: Animation
                    animation = AnimationUtils.loadAnimation(
                        applicationContext,
                        R.anim.move_up

                    )

                    top.startAnimation(animation);
                    val animation2: Animation
                    animation2 = AnimationUtils.loadAnimation(
                        applicationContext,
                        R.anim.move_to_place
                    )
                    animation2?.setAnimationListener(object : Animation.AnimationListener{
                        override fun onAnimationRepeat(animation: Animation?) {

                        }

                        override fun onAnimationEnd(animation: Animation?) {

                        }

                        override fun onAnimationStart(animation: Animation?) {
                            subtitle?.visibility = View.VISIBLE
                        }

                    })
                    bottom.startAnimation(animation2);
                    toggle = !toggle
                }
            }

        }

        t.schedule(myTimerTask, 3000L,3000L)
    }

    private fun setRecyclerView() {
        var layoutManager = LinearLayoutManager(baseContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerview.layoutManager = layoutManager
        adapter = YearAdapter(baseContext, this)
        recyclerview.adapter = adapter
    }

    private fun callApi() {
        var client = CashrichApiClient.getAPICallMethods(baseContext).getTestCashRich()
        client.enqueue(object : Callback<ArrayList<CashrichModel>> {
            override fun onFailure(call: Call<ArrayList<CashrichModel>>, t: Throwable) {

                Log.i("Failure", "")
            }

            override fun onResponse(
                call: Call<ArrayList<CashrichModel>>,
                response: Response<ArrayList<CashrichModel>>
            ) {
                response.body()?.let {
                    list.addAll(it)
                    var data = list[0]
                    equityModel= PieModel("Freetime", data.equity.toFloat(), Color.parseColor("#f29b4d"))
                    fixed= PieModel("Sleep", (100 - data.equity.toFloat()), Color.parseColor("#4d9ef7"))

                    adapter?.setData(list)
                    clickedItem(0)


                    piechart.addPieSlice(equityModel)
                    piechart.addPieSlice(fixed)
                    piechart.startAnimation()
                    piechart?.update()
                }

            }
        })
    }
    var equityModel :  PieModel ?= null
    var fixed :  PieModel ?= null
}
