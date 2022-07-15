package com.castcle.android.domain.scheduler

import io.reactivex.Scheduler

interface SchedulerRepository {
    fun io(): Scheduler
    fun main(): Scheduler
    fun compute(): Scheduler
}