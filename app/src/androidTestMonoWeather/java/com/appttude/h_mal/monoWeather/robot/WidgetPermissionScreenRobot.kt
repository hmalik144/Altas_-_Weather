package com.appttude.h_mal.monoWeather.robot

import com.appttude.h_mal.atlas_weather.BaseTestRobot
import com.appttude.h_mal.atlas_weather.R

fun widgetPermissionScreen(func: WidgetPermissionScreenRobot.() -> Unit) =
    WidgetPermissionScreenRobot().apply { func() }

class WidgetPermissionScreenRobot : BaseTestRobot() {
    fun declarationDisplayed() = matchDisplayed(R.id.declaration_text)
}