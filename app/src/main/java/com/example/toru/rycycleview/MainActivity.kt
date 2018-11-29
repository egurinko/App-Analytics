package com.example.toru.rycycleview

import android.R
import android.app.ActivityManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.widget.SimpleAdapter
import android.widget.ListView
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.pm.ApplicationInfo
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.os.Build
import android.provider.Settings
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // To let this app to access data of android
//        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

        val appStats = ArrayList<HashMap<String, Any>>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 86400, time)
            // Sort the stats by the last time used
            if (stats != null) {
                val mySortedMap = TreeMap<Long, UsageStats>()
                for (usageStats in stats) {
                    mySortedMap.put(usageStats.lastTimeUsed, usageStats)
                }
                for(app in mySortedMap){
                    val temp = HashMap<String, Any>()
                    temp["PackageName"] = app.value.packageName
                    val bool  = appStats.any { it["PackageName"] == app.value.packageName}
                    if(bool){
                        var tempTemp = appStats.filter { it["PackageName"] == app.value.packageName}
                        var index = appStats.indexOfFirst { it["PackageName"] ==  app.value.packageName}
                        temp["UsageTime"] = (tempTemp[0]["UsageTime"] as Long).plus(app.value.totalTimeInForeground)
                        appStats.set(index, temp)
                    } else {
                        temp["UsageTime"] = app.value.totalTimeInForeground
                        appStats.add(temp)
                    }

                }
            }
        }

//        val pm = packageManager
//        val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
//
//        val apps = ArrayList<HashMap<String, String>>()
//        for (info in packages) {
//            val app = HashMap<String, String>()
//            app["PackageName"] = info.packageName
//            app["ApplicationName"] = info.applicationInfo.loadLabel(pm).toString()
//            apps.add(app)
//        }

        val adapter = SimpleAdapter(
                this,
                appStats,
                R.layout.simple_list_item_2,
                arrayOf("PackageName", "UsageTime"),
                intArrayOf(R.id.text1, R.id.text2))
        val listView1 = ListView(this)
        listView1.setAdapter(adapter)
        setContentView(listView1)
    }
}
