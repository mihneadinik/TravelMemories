package com.example.travelmemories.utils

import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL

class Utils {
    companion object {
        var mapViewType: Int = MAP_TYPE_NORMAL
        fun getMoodLevel(value: Float): String {
            if (value <= 0.2f) {
                return "Very unhappy"
            }
            if (value <= 0.4f) {
                return "Unhappy"
            }
            if (value <= 0.6f) {
                return "Neutral"
            }
            if (value <= 0.8f) {
                return "Happy"
            }
            return "Very happy"
        }
    }
}