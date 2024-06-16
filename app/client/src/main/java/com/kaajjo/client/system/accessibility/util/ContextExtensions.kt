package com.kaajjo.client.system.accessibility.util

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter

/**
 * Checks whether the accessibility service is enabled
 *
 * @param accessibilityService class of the accessibility service
 * @return
 */
fun Context.isAccessibilityServiceEnabled(accessibilityService: Class<*>?): Boolean {
    val expectedComponentName = ComponentName(this, accessibilityService!!)

    val enabledServicesSetting: String =
        Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
            ?: return false

    val colonSplitter = SimpleStringSplitter(':')
    colonSplitter.setString(enabledServicesSetting)

    while (colonSplitter.hasNext()) {
        val componentNameString = colonSplitter.next()
        val enabledService = ComponentName.unflattenFromString(componentNameString)

        if (enabledService != null && enabledService == expectedComponentName) return true
    }

    return false
}
