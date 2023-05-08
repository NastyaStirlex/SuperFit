package com.nastirlex.superfit.presentation

import com.nastirlex.superfit.R

sealed class TrainingType {
    class PushUp() {
        companion object {
            val image = R.drawable.push_up
            val title = R.string.push_ups_title
            val description = R.string.push_ups_description
        }
    }

    class Plank() {
        companion object {
            val image = R.drawable.plank
            val title = R.string.plank_title
            val description = R.string.plank_description
        }
    }

    class Squats() {
        companion object {
            val image = R.drawable.squats
            val title = R.string.squats_title
            val description = R.string.squats_description
        }
    }

    class Crunch() {
        companion object {
            val image = R.drawable.crunch
            val title = R.string.crunch_title
            val description = R.string.crunch_description
        }
    }

    class Running() {
        companion object {
            val image = R.drawable.running
            val title = R.string.running_title
            val description = R.string.running_description
        }
    }
}