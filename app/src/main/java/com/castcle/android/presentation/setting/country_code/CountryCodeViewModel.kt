package com.castcle.android.presentation.setting.country_code

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.custom_view.load_state.item_error_state.ErrorStateViewEntity
import com.castcle.android.core.custom_view.load_state.item_loading.LoadingViewEntity
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.metadata.MetadataRepository
import com.castcle.android.presentation.setting.country_code.item_country_code.CountryCodeViewEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CountryCodeViewModel(
    database: CastcleDatabase,
    private val repository: MetadataRepository,
) : BaseViewModel() {

    private val countryCodeList = database.countryCode().retrieve()

    private val errorState = MutableStateFlow<Throwable?>(null)

    val searchKeyword = MutableStateFlow("")

    val views = combine(countryCodeList, searchKeyword, errorState) { countryCode, keyword, error ->
        when {
            error != null && countryCode.isEmpty() -> listOf(
                ErrorStateViewEntity(action = { fetchCountryCode() }, error = error)
            )
            error == null && countryCode.isEmpty() -> listOf(
                LoadingViewEntity()
            )
            else -> countryCode.filter { filter ->
                filter.dialCode.contains(keyword, true)
                    || filter.code.contains(keyword, true)
                    || filter.name.contains(keyword, true)
            }.map { map ->
                CountryCodeViewEntity(
                    countryCode = map,
                    uniqueId = map.dialCode,
                )
            }
        }
    }

    init {
        fetchCountryCode()
    }

    private fun fetchCountryCode() {
        launch(
            onError = { errorState.value = it },
            onLaunch = { errorState.value = null },
            onSuccess = { errorState.value = null },
        ) {
            repository.fetchCountryCode()
        }
    }

}