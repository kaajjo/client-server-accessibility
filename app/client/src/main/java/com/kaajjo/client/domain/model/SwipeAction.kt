package com.kaajjo.client.domain.model

/**
 * Swipe action
 *
 * @property relativePos initial position relative to the height/width of the screen
 * @property relativeSwipe swipe length relative to the height of the screen
 * @property swipeDown to swipe down or up
 * @constructor Create empty Swipe action
 */
data class SwipeAction(
    val relativePos: Pair<Float, Float> = Pair(0f, 0f),
    val relativeSwipe: Float = 0f,
    val swipeDown: Boolean = false
)
