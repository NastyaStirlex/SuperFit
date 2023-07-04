package com.nastirlex.superfit.presentation.push_ups

interface PushUpsEvent

class SaveTraining : PushUpsEvent
class FinishTraining(val pushUpsLeft: Int) : PushUpsEvent