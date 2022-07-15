package com.castcle.android.data.scheduler

import com.castcle.android.domain.scheduler.SchedulerRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.annotation.Singleton

@Singleton
class SchedulerRepositoryImpl : SchedulerRepository {
    override fun io(): Scheduler = Schedulers.io()
    override fun compute(): Scheduler = Schedulers.computation()
    override fun main(): Scheduler = AndroidSchedulers.mainThread()
}