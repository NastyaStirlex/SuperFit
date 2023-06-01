package com.nastirlex.superfit.presentation.squats

interface SquatsEvent

class SaveTraining(val date: String, val exercise: String, val repeatCount: Int) : SquatsEvent