package com.castcle.android.data.ads

import androidx.room.withTransaction
import com.castcle.android.core.api.AdvertiseApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.ErrorMapper
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.data.ads.mapper.AdvertiseResponseMapper
import com.castcle.android.data.base.BaseUiState
import com.castcle.android.domain.ads.AdvertiseRepository
import com.castcle.android.domain.ads.entity.AdvertiseEntityWithContent
import com.castcle.android.domain.ads.entity.BoostAdRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Singleton

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
//  Created by sklim on 15/9/2022 AD at 15:20.

@Singleton
class AdvertiseRepositoryImpl(
    private val advertiseApi: AdvertiseApi,
    private val database: CastcleDatabase,
    private val mapper: AdvertiseResponseMapper
) : AdvertiseRepository {

    val scope = CoroutineScope(Job() + Dispatchers.IO)

    override suspend fun onRefreshAdvertiseHistory(): Job {
        return scope.async {
            database.withTransaction {
                database.advertise().delete()
                database.advertiseList().delete()
            }
        }.job
    }

    override suspend fun getAdvertiseHistory(): Flow<List<AdvertiseEntityWithContent>> {
        return database.withTransaction {
            database.advertiseList().retrieve()
        }
    }

    override suspend fun fetchAdvertiseHistory(filter: String): Flow<BaseUiState<Nothing>> {
        val ownerUser = database.withTransaction {
            database.user().get()
        }
        return flow {
            emit(BaseUiState.Loading(null, true))
            apiCall {
                advertiseApi.getAdvertiseHistory(filter = filter).also { it ->
                    if (it.isSuccessful) {
                        mapper.apply(it.body(), ownerUser).run {
                            handlerSaveAdvertise(this)
                        }
                        emit(BaseUiState.Loading(null, false))
                    } else {
                        emit(BaseUiState.Loading(null, false))
                        emit(BaseUiState.Error(exception = ErrorMapper().map(it.errorBody())))
                    }
                }
            }
        }
    }

    private suspend fun handlerSaveAdvertise(
        advertiseResponseResult: AdvertiseResponseMapper.AdvertiseResponseResult
    ) {
        database.withTransaction {
            database.advertise().insert(advertiseResponseResult.advertisesEntity.distinctBy {
                it.adsId
            })
            database.advertiseList().insert(advertiseResponseResult.advertiseList.distinctBy {
                it.advertiseReferenceId
            })
            database.user().upsert(advertiseResponseResult.user.distinctBy {
                it.id
            })
        }
    }

    override suspend fun createBoostPage(boostAdRequest: BoostAdRequest): Flow<BaseUiState<Nothing>> {
        val ownerUser = database.withTransaction {
            database.user().get()
        }
        return flow {
            emit(BaseUiState.Loading(null, true))
            apiCall {
                if (boostAdRequest.contentId != null) {
                    advertiseApi.createBoostAdContent(boostAdRequest)
                } else {
                    advertiseApi.createBoostAdUser(boostAdRequest)
                }.also {
                    emit(BaseUiState.Loading(null, false))

                    if (it.isSuccessful) {
                        mapper.applySingle(it.body(), ownerUser).run {
                            handlerSaveAdvertise(this)
                        }
                        emit(BaseUiState.SuccessNonBody)
                    } else {
                        emit(BaseUiState.Error(exception = ErrorMapper().map(it.errorBody())))
                    }
                }
            }
        }
    }
}