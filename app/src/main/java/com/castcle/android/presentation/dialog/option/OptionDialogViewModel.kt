package com.castcle.android.presentation.dialog.option

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.withTransaction
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.domain.content.ContentRepository
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.presentation.dialog.option.item_option_dialog.OptionDialogViewEntity
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class OptionDialogViewModel(
    private val contentRepository: ContentRepository,
    private val database: CastcleDatabase,
    private val type: OptionDialogType,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val onError = MutableLiveData<Throwable>()

    val onSuccess = MutableLiveData<Unit>()

    val views = MutableLiveData<List<CastcleViewEntity>>()

    fun deleteContent(contentId: String) {
        launch(
            onError = { onError.postValue(it) },
            onSuccess = { onSuccess.postValue(Unit) },
        ) {
            val userId = database.cast().get(castId = contentId)?.user?.id.orEmpty()
            database.withTransaction {
                contentRepository.deleteContent(contentId = contentId, userId = userId)
                userRepository.getUser(id = userId)
            }
        }
    }

    fun getOptionItems(context: Context) = launch {
        val items = when (type) {
            is OptionDialogType.MyContentOption -> {
                val deleteContentItem = OptionDialogViewEntity(
                    eventType = type.deleteContent,
                    icon = R.drawable.ic_delete,
                    title = context.getString(R.string.delete)
                )
                listOf(deleteContentItem)
            }
            is OptionDialogType.MyPageOption -> {
                val deletePageItem = OptionDialogViewEntity(
                    eventType = type.deletePage,
                    icon = R.drawable.ic_delete,
                    title = context.getString(R.string.delete_page)
                )
                val syncSocialMediaItem = OptionDialogViewEntity(
                    eventType = type.syncSocialMedia,
                    icon = R.drawable.ic_sync_social_media,
                    title = context.getString(R.string.sync_social_media)
                )
                listOf(deletePageItem, syncSocialMediaItem)
            }
            is OptionDialogType.MyUserOption -> {
                val syncSocialMediaItem = OptionDialogViewEntity(
                    eventType = type.syncSocialMedia,
                    icon = R.drawable.ic_sync_social_media,
                    title = context.getString(R.string.sync_social_media)
                )
                listOf(syncSocialMediaItem)
            }
            is OptionDialogType.OtherContentOption -> {
                val reportContentItem = OptionDialogViewEntity(
                    eventType = type.reportContent,
                    icon = R.drawable.ic_report,
                    title = context.getString(R.string.report_cast)
                )
                listOf(reportContentItem)
            }
            is OptionDialogType.OtherUserOption -> {
                val castcleId = database.user().get(type.userId).firstOrNull()?.castcleId.orEmpty()
                val blockUserItem = OptionDialogViewEntity(
                    eventType = type.blockUser,
                    icon = R.drawable.ic_block,
                    title = context.getString(R.string.block_user, castcleId)
                )
                val reportUserItem = OptionDialogViewEntity(
                    eventType = type.reportUser,
                    icon = R.drawable.ic_report,
                    title = context.getString(R.string.report_user, castcleId)
                )
                listOf(blockUserItem, reportUserItem)
            }
        }
        views.postValue(items)
    }

}