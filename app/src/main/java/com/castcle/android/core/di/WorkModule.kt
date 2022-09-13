package com.castcle.android.core.di

import android.content.Context
import androidx.work.*
import com.castcle.android.core.work.*
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.bind
import org.koin.dsl.module
import javax.inject.Provider

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 6/9/2022 AD at 19:22.

val workModule = module {
    single {
        WorkManager.getInstance(get())
    }
    factory {
        CastcleWorkerFactory(get())
    }

    single<ImageUploaderWorkHelper> {
        ImageUploaderWorkHelperImpl(get(), get())
    } bind ImageUploaderWorkHelper::class

    single<WorkRequestBuilder> {
        WorkRequestBuilderImpl()
    } bind WorkRequestBuilder::class

    worker {
        UpLoadProfileAvatarWorker(get(), get(), get())
    }
}

class CastcleWorkerFactory constructor(
    private val workerFactories: Map<Class<out ListenableWorker>,
        @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return workerFactories.entries.firstOrNull { entry ->
            Class.forName(workerClassName).isAssignableFrom(entry.key)
        }?.value?.get()?.create(appContext, workerParameters)
    }
}

interface ChildWorkerFactory {
    fun create(appContext: Context, workerParams: WorkerParameters): ListenableWorker
}