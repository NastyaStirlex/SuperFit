package com.nastirlex.superfit.presentation.running

import com.nastirlex.superfit.presentation.push_ups.PushUpsEvent

interface RunningEvent

class SaveTraining : RunningEvent
class FinishTraining(val runningMetersLeft: Int) : RunningEvent