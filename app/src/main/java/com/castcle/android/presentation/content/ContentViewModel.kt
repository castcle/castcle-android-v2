package com.castcle.android.presentation.content

import androidx.lifecycle.*
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.core.api.ContentApi
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.constants.PARAMETER_MAX_RESULTS_SMALL_ITEM
import com.castcle.android.core.custom_view.load_state.item_empty_state_content.EmptyStateContentViewEntity
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.error.CastcleException
import com.castcle.android.core.error.RetryException
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.content.data_source.CommentRemoteMediator
import com.castcle.android.data.content.mapper.CommentResponseMapper
import com.castcle.android.domain.content.ContentRepository
import com.castcle.android.domain.content.entity.CommentEntity
import com.castcle.android.domain.content.entity.ContentEntity
import com.castcle.android.domain.content.type.ContentType
import com.castcle.android.domain.search.SearchRepository
import com.castcle.android.domain.search.entity.HashtagEntity
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ContentViewModel(
    private val api: ContentApi,
    private val commentResponseMapper: CommentResponseMapper,
    private val contentId: String,
    private val contentMapper: ContentMapper,
    private val contentRepository: ContentRepository,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
    private val searchRepository: SearchRepository,
    private val state: SavedStateHandle,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val sessionId = System.currentTimeMillis()

    val content = MutableStateFlow<ContentEntity?>(null)

    val contentOwnerDisplayName = MutableLiveData<String>()

    val hashtags = MutableLiveData<List<HashtagEntity>>()

    val loadState = MutableSharedFlow<LoadState>()

    val mentions = MutableLiveData<List<UserEntity>>()

    val onCommentSuccess = MutableLiveData<Unit>()

    val onError = MutableLiveData<Throwable>()

    val targetCommentId = MutableStateFlow<String?>(null)

    val currentUser = database.user().retrieve(UserType.People)
        .map { it.firstOrNull() }
        .filterNotNull()
        .distinctUntilChanged()

    init {
        getContent()
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val views = content.filterNotNull()
        .onEach { updateActionBarTitle(it.originalUserId) }
        .flatMapLatest { content ->
            Pager(
                config = PagingConfig(
                    initialLoadSize = PARAMETER_MAX_RESULTS_SMALL_ITEM,
                    pageSize = PARAMETER_MAX_RESULTS_SMALL_ITEM,
                ), pagingSourceFactory = {
                    database.content().pagingSource(sessionId)
                }, remoteMediator = CommentRemoteMediator(
                    api = api,
                    content = content,
                    database = database,
                    glidePreloader = glidePreloader,
                    mapper = commentResponseMapper,
                    sessionId = sessionId,
                )
            ).flow.map { pagingData ->
                pagingData.map { contentMapper.apply(it) }
            }
        }.cachedIn(viewModelScope)

    fun fetchContent() {
        launch {
            contentRepository.getContent(contentId = contentId, sessionId = sessionId)
        }
    }

    private fun getContent() {
        launch(
            onError = {
                if (it is CastcleException.ContentNotFoundException) {
                    loadState.emitOnSuspend(
                        RetryException.loadState(
                            error = it,
                            errorItems = EmptyStateContentViewEntity.create(1),
                            retryAction = { getContent() },
                        )
                    )
                } else {
                    loadState.emitOnSuspend(RetryException.loadState(it) { getContent() })
                }
            },
            onLaunch = {
                loadState.emitOnSuspend(LoadState.Loading)
            },
        ) {
            val castInDatabase = database.cast().get(contentId)
            val referenceCastInDatabase = castInDatabase?.cast?.referenceCastId?.let {
                database.cast().get(it)
            }
            if (castInDatabase != null) {
                content.value = ContentEntity(
                    originalCastId = castInDatabase.cast.id,
                    originalUserId = castInDatabase.cast.authorId,
                    referenceCastId = referenceCastInDatabase?.cast?.id,
                    referenceUserId = referenceCastInDatabase?.cast?.authorId,
                    sessionId = sessionId,
                    type = ContentType.Content,
                )
            } else {
                content.value = contentRepository.getContent(
                    contentId = contentId,
                    sessionId = sessionId,
                )
            }
        }
    }

    private fun updateActionBarTitle(userId: String?) {
        launch {
            userId?.let { database.user().get(userId) }
                ?.firstOrNull()
                ?.displayName
                ?.also(contentOwnerDisplayName::postValue)
        }
    }

    fun getHashtag(keyword: String) {
        launch {
            hashtags.postValue(searchRepository.getHashtags(keyword))
        }
    }

    fun getMentions(keyword: String) {
        launch {
            mentions.postValue(searchRepository.getMentions(keyword))
        }
    }

    fun sentComment(message: String) {
        launch(onError = { onError.postValue(it) }) {
            val commentId = targetCommentId.value
            if (commentId.isNullOrBlank()) {
                userRepository.commentCast(contentId = contentId, message = message)
                onCommentSuccess.postValue(Unit)
            } else {
                userRepository.replyComment(commentId = commentId, message = message)
                onCommentSuccess.postValue(Unit)
            }
        }
    }

    fun likeComment(targetComment: CommentEntity) {
        launch {
            if (targetComment.liked) {
                userRepository.unlikeComment(comment = targetComment)
            } else {
                userRepository.likeComment(comment = targetComment)
            }
        }
    }

    fun showReportingContent(id: String, ignoreReportContentId: List<String>) {
        launch {
            database.content().updateIgnoreReportContentId(id, ignoreReportContentId)
        }
    }

    fun saveItemsState(layoutManager: RecyclerView.LayoutManager) {
        state[SAVE_STATE_RECYCLER_VIEW] = layoutManager.onSaveInstanceState()
    }

    fun restoreItemsState(layoutManager: RecyclerView.LayoutManager) {
        layoutManager.onRestoreInstanceState(state[SAVE_STATE_RECYCLER_VIEW])
    }

    companion object {
        private const val SAVE_STATE_RECYCLER_VIEW = "RECYCLER_VIEW"
    }

}