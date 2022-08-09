package com.castcle.android.presentation.dialog.option

import androidx.lifecycle.MutableLiveData
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.domain.content.ContentRepository
import com.castcle.android.domain.user.UserRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class OptionDialogViewModel(
    private val contentRepository: ContentRepository,
    private val database: CastcleDatabase,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val onError = MutableLiveData<Throwable>()

    val onSuccess = MutableLiveData<Unit>()

    fun deleteContent(contentId: String) {
        launch(
            onError = { onError.postValue(it) },
            onSuccess = { onSuccess.postValue(Unit) },
        ) {
            val userId = database.cast().get(castId = contentId)?.user?.id.orEmpty()
            contentRepository.deleteContent(contentId = contentId, userId = userId)
            userRepository.getUser(id = userId)
        }
    }

}