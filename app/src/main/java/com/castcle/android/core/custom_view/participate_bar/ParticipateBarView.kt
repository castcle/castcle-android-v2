package com.castcle.android.core.custom_view.participate_bar

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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

        val likeColor = getParticipateColor(cast.liked)
        binding.ivLike.imageTintList = ColorStateList.valueOf(likeColor)
        binding.tvLike.text = cast.likeCount.toMetrics()
        binding.tvLike.setTextColor(likeColor)

        val commentColor = getParticipateColor(cast.commented)
        binding.ivComment.imageTintList = ColorStateList.valueOf(commentColor)
        binding.tvComment.text = cast.commentCount.toMetrics()
        binding.tvComment.setTextColor(commentColor)

        val recastColor = getParticipateColor(cast.recasted)
        binding.ivRecast.imageTintList = ColorStateList.valueOf(recastColor)
        binding.tvRecast.text = cast.recastCount.toMetrics()
        binding.tvRecast.setTextColor(recastColor)
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