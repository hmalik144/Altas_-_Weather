package com.appttude.h_mal.atlas_weather.atlasWeather.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import androidx.preference.PreferenceManager
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.atlasWeather.notification.NotificationReceiver
import com.appttude.h_mal.atlas_weather.atlasWeather.widget.NewAppWidget
import java.util.*


class UnitSettingsActivity : PreferenceActivity() {
    private var prefListener: OnSharedPreferenceChangeListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)
        fragmentManager.beginTransaction().replace(android.R.id.content, MyPreferenceFragment()).commit()

        //listener on changed sort order preference:
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        prefListener = OnSharedPreferenceChangeListener { _, key ->
            if (key == "temp_units") {
                val intent = Intent(baseContext, NewAppWidget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(ComponentName(application, NewAppWidget::class.java))
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                sendBroadcast(intent)
            }
            if (key == "notif_boolean") {
                setupNotificationBroadcaster(baseContext)
            }

            if (key == "widget_black_background"){
                val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                val widgetManager = AppWidgetManager.getInstance(this)
                val ids = widgetManager.getAppWidgetIds(ComponentName(this, NewAppWidget::class.java))
                AppWidgetManager.getInstance(this).notifyAppWidgetViewDataChanged(ids, R.id.whole_widget_view)
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                sendBroadcast(intent)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }

    fun setupNotificationBroadcaster(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        val broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 6)
        cal.set(Calendar.MINUTE, 8)
        cal.set(Calendar.SECOND, 5)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, AlarmManager.INTERVAL_DAY, broadcast)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    //    @Override
    //    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
    //        Log.i(TAG, "onSharedPreferenceChanged: " + s);
    //        if (s == "temp_units"){
    //            Intent intent = new Intent(getBaseContext(), NewAppWidget.class);
    //            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
    //
    //            int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
    //            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
    //            sendBroadcast(intent);
    //        }
    //    }
    class MyPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.prefs)
        }
    }
}

