package com.example.travelmemories.utils

class Utils {
    companion object {
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