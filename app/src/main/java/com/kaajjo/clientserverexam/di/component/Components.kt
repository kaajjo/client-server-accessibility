package com.kaajjo.clientserverexam.di.component

import android.app.Application
import android.content.Context

object Component {
    fun createServerComponent(port: Int, context: Context) = DaggerServerComponent.builder()
        .port(port)
        .application(context as Application)
        .build()
}