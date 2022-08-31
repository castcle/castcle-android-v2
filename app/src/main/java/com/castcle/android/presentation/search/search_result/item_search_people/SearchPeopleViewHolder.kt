package com.castcle.android.presentation.search.search_result.item_search_people

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemSearchPeopleBinding
import com.castcle.android.presentation.feed.FeedListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class SearchPeopleViewHolder(
    private val binding: ItemSearchPeopleBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: FeedListener,
) : CastcleViewHolder<SearchPeopleViewEntity>(binding.root) {

    init {
        compositeDisposable += binding.root.onClick {
            listener.onUserClicked(item.user)
        }
        compositeDisposable += binding.tvFollow.onClick {
            listener.onFollowClicked(item.user)
        }
    }

    override var item = SearchPeopleViewEntity()

    override fun bind(bindItem: SearchPeopleViewEntity) {
        binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
        binding.tvOverview.isGone = item.user.overview.isNullOrBlank()
        binding.tvOverview.text = item.user.overview
        binding.tvUserName.text = item.user.displayName
        binding.tvCastcleId.text = item.user.castcleId
        binding.ivOfficial.isVisible = item.user.verifiedOfficial
        with(binding.tvFollow) {
            isVisible = !item.user.isOwner
            text = if (item.user.followed) {
                string(R.string.followed)
            } else {
                string(R.string.follow)
            }
            background = if (item.user.followed) {
                drawable(R.drawable.bg_corner_16dp)
            } else {
                drawable(R.drawable.bg_outline_corner_16dp)
            }
            setTextColor(
                if (item.user.followed) {
                    color(R.color.white)
                } else {
                    color(R.color.blue)
                }
            )
        }
    }

}