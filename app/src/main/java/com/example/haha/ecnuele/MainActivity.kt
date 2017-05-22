package com.haha.ecnuele

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.app.AlertDialog
import android.os.Build


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            val window = this.window
            window.navigationBarColor=0x000000
        }
        val userSettings = getSharedPreferences("setting", 0)
        var room = userSettings.getString("room", "5123")
        var area = userSettings.getString("area", "zb")
        num.setText(room.toString())
        if (area == "zb") xiaoqu.text = "中北校区"
        else if (area == "mh") xiaoqu.text = "闵行校区"
        else xiaoqu.text = "同普路"
        xiaoqu.setOnClickListener {
            val colors = arrayOf<CharSequence>("中北校区", "闵行校区", "同普路")

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("选择校区")
            builder.setItems(colors) { dialog, which ->
                when (which) {
                    0 -> {
                        area = "zb"
                        xiaoqu.text = "中北校区"
                    }
                    1 -> {
                        area = "mh"
                        xiaoqu.text = "闵行校区"
                    }
                    2 -> {
                        area = "TP"
                        xiaoqu.text = "同普路"
                    }
                }
            }
            builder.show()
        }
        ok.setOnClickListener {
            room = num.text.toString()
            val userSettings = getSharedPreferences("setting", 0)
            val editor = userSettings.edit()
            editor.putString("area", area)
            editor.putString("room", room)
            editor.commit()
            val mQueue = Volley.newRequestQueue(this.applicationContext)
            val stringRequest = StringRequest("http://wx.ecnu.edu.cn/CorpWeChat/card/doGetEle.html?area=" + area + "&point=" + room,
                    Response.Listener<String> { response ->
                        var s = response.toString()
                        text1.text = s.substring(s.indexOf("当前") + 9, s.indexOf("度</h3>") + 1)
if (text1.text=="度")text1.text="错误"
                    },
                    Response.ErrorListener
                    {text1.text="错误"}
            )
            stringRequest.retryPolicy = DefaultRetryPolicy(3000, 2, 1f)
            mQueue.add(stringRequest)
        }
        val mQueue = Volley.newRequestQueue(this.applicationContext)
        val stringRequest = StringRequest("http://wx.ecnu.edu.cn/CorpWeChat/card/doGetEle.html?area=" + area + "&point=" + room,
                Response.Listener<String> { response ->
                    var s = response.toString()
                    text1.text = s.substring(s.indexOf("当前") + 9, s.indexOf("度</h3>") + 1)

                },
                Response.ErrorListener
                {text1.text="错误"}
        )
        stringRequest.retryPolicy = DefaultRetryPolicy(3000, 2, 1f)
        mQueue.add(stringRequest)

    }
}
