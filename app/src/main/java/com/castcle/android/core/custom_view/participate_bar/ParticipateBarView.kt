package com.castcle.android.core.custom_view.participate_bar

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.extensions.asComma
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.LayoutParticipateBarBinding
import com.castcle.android.domain.cast.entity.CastEntity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ParticipateBarView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding by lazy {
        LayoutParticipateBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val compositeDisposable = CompositeDisposable()

    fun bind(cast: CastEntity, listener: ParticipateBarListener) {
        compositeDisposable.clear()
        compositeDisposable += binding.clLike.onClick {
            listener.onLikeClicked(cast)
        }
        compositeDisposable += binding.clComment.onClick {
            listener.onCommentClicked(cast)
        }
        compositeDisposable += binding.clRecast.onClick {
            listener.onRecastClicked(cast)
        }
        compositeDisposable += binding.clContentFarming.onClick {
            listener.onContentFarmingClicked(cast)
        }

        val likeColor = getParticipateColor(cast.liked)
        binding.ivLike.imageTintList = ColorStateList.valueOf(likeColor)
        binding.tvLike.text = cast.likeCount.toMetrics()
        binding.tvLike.isVisible = cast.likeCount > 0
        binding.tvLike.setTextColor(likeColor)

        val commentColor = getParticipateColor(cast.commented)
        binding.ivComment.imageTintList = ColorStateList.valueOf(commentColor)
        binding.tvComment.text = cast.commentCount.toMetrics()
        binding.tvComment.isVisible = cast.commentCount > 0
        binding.tvComment.setTextColor(commentColor)

        val recastColor = getParticipateColor(cast.recasted)
        binding.ivRecast.imageTintList = ColorStateList.valueOf(recastColor)
        binding.tvRecast.text = cast.recastCount.toMetrics()
        binding.tvRecast.isVisible = cast.recastCount > 0
        binding.tvRecast.setTextColor(recastColor)

        val contentFarmingColor = getParticipateColor(cast.farming)
        binding.ivContentFarming.imageTintList = ColorStateList.valueOf(contentFarmingColor)
        binding.tvContentFarming.text = cast.farmCount.toMetrics()
        binding.tvContentFarming.isVisible = cast.farmCount > 0
        binding.tvContentFarming.setTextColor(contentFarmingColor)
    }

    private fun getParticipateColor(isParticipate: Boolean): Int {
        return if (isParticipate) {
            ContextCompat.getColor(context, R.color.blue)
        } else {
            ContextCompat.getColor(context, R.color.white)
        }
    }

    private fun Int.toMetrics(): String {
        return if (this > 0) asComma() else ""
    }

}