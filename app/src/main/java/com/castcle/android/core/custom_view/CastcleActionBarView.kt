package com.castcle.android.core.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.visible
import com.castcle.android.databinding.LayoutCastcleActionBarBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class CastcleActionBarView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private val binding = LayoutCastcleActionBarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val compositeDisposable = CompositeDisposable()

    fun bind(
        leftButtonAction: (() -> Unit)? = null,
        @DrawableRes leftButtonIcon: Int? = R.drawable.ic_back,
        rightButtonAction: (() -> Unit)? = null,
        @DrawableRes rightButtonIcon: Int? = null,
        rightSecondButtonAction: (() -> Unit)? = null,
        @DrawableRes rightSecondButtonIcon: Int? = null,
        title: Any? = "",
        @ColorRes titleColor: Int = R.color.white,
    ) {
        compositeDisposable.clear()
        if (leftButtonAction != null && leftButtonIcon != null) {
            compositeDisposable += binding.ivLeftIcon.onClick { leftButtonAction.invoke() }
        }
        if (leftButtonIcon != null) {
            binding.ivLeftIcon.setImageResource(leftButtonIcon)
        } else {
            binding.ivLeftIcon.setImageDrawable(null)
        }
        if (rightButtonAction != null && rightButtonIcon != null) {
            compositeDisposable += binding.ivRightIcon.onClick { rightButtonAction.invoke() }
        } else {
            binding.ivRightIcon.setOnClickListener(null)
        }
        if (rightButtonIcon != null) {
            binding.ivRightIcon.setImageResource(rightButtonIcon)
        } else {
            binding.ivRightIcon.setImageDrawable(null)
        }
        if (rightSecondButtonAction != null && rightSecondButtonIcon != null) {
            compositeDisposable += binding.ivRightSecondIcon.onClick { rightSecondButtonAction.invoke() }
        } else {
            binding.ivRightSecondIcon.setOnClickListener(null)
        }
        if (rightSecondButtonIcon != null) {
            binding.ivRightSecondIcon.setImageResource(rightSecondButtonIcon)
        } else {
            binding.ivRightSecondIcon.setImageDrawable(null)
        }
        binding.ivLeftIcon.isVisible = leftButtonIcon != null
        binding.ivRightIcon.isVisible = rightButtonIcon != null
        binding.ivRightSecondIcon.isVisible = rightSecondButtonIcon != null
        binding.tvTitle.setTextColor(context.getColor(titleColor))
        binding.tvTitle.text = when (title) {
            is String -> title
            is Int -> try {
                context.getString(title)
            } catch (exception: Exception) {
                ""
            }
            else -> ""
        }
        visible()
    }

}