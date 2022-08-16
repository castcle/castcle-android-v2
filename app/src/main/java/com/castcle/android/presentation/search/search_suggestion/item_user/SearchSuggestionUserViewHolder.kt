package com.castcle.android.presentation.search.search_suggestion.item_user

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.loadAvatarImage
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemSearchSuggestionUserBinding
import com.castcle.android.presentation.search.search_suggestion.SearchSuggestionListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SearchSuggestionUserViewHolder(
    private val binding: ItemSearchSuggestionUserBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: SearchSuggestionListener,
) : CastcleViewHolder<SearchSuggestionUserViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.root.onClick {
            listener.onUserClicked(item.user)
        }
    }

    override var item = SearchSuggestionUserViewEntity()

    override fun bind(bindItem: SearchSuggestionUserViewEntity) {
        binding.tvDisplayName.text = item.user.displayName
        binding.tvCastcleId.text = item.user.castcleId
        binding.ivAvatar.loadAvatarImage(imageUrl = item.user.avatar.thumbnail)
    }

}