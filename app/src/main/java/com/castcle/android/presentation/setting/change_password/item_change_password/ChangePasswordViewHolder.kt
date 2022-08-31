package com.castcle.android.presentation.setting.change_password.item_change_password

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemChangePasswordBinding
import com.castcle.android.presentation.setting.change_password.ChangePasswordListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.util.regex.*

class ChangePasswordViewHolder(
    private val binding: ItemChangePasswordBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ChangePasswordListener,
) : CastcleViewHolder<ChangePasswordViewEntity>(binding.root) {

    override var item = ChangePasswordViewEntity()

    private val regex = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).*$").toRegex()

    init {
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onChangePassword(item.password)
        }
        compositeDisposable += binding.etPassword.onTextChange {
            item.password = it
            updateView()
        }
        compositeDisposable += binding.etConfirmPassword.onTextChange {
            item.confirmPassword = it
            updateView()
        }
    }

    override fun bind(bindItem: ChangePasswordViewEntity) {
        updateView()
        binding.etPassword.setText(item.password)
        binding.etConfirmPassword.setText(item.confirmPassword)
    }

    private fun updateView() {
        val password = binding.etPassword.text.toString()
        val confirm = binding.etConfirmPassword.text.toString()
        val checkCase = password.matches(regex)
        val checkLength = password.length >= 6
        val checkMatch = confirm.isNotBlank() && password == confirm
        binding.tvConfirm.isEnabled = checkCase && checkLength && checkMatch
        binding.tvConfirm.backgroundTintList = if (binding.tvConfirm.isEnabled) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
        updateConditionView(
            condition = checkCase,
            emptyState = password.isBlank(),
            imageView = binding.ivCheckCase,
            textView = binding.tvCheckCase,
        )
        updateConditionView(
            condition = checkLength,
            emptyState = password.isBlank(),
            imageView = binding.ivCheckLength,
            textView = binding.tvCheckLength,
        )
        updateConditionView(
            condition = checkMatch,
            emptyState = confirm.isBlank(),
            imageView = binding.ivCheckMatch,
            textView = binding.tvCheckMatch,
        )
    }

    private fun updateConditionView(
        condition: Boolean,
        emptyState: Boolean,
        imageView: AppCompatImageView,
        textView: AppCompatTextView,
    ) {
        val color = when {
            emptyState -> colorStateList(R.color.gray_1)
            condition -> colorStateList(R.color.blue)
            else -> colorStateList(R.color.red_3)
        }
        textView.setTextColor(color)
        imageView.imageTintList = color
        imageView.setImageResource(
            if (emptyState || condition) {
                R.drawable.ic_check_circle
            } else {
                R.drawable.ic_close
            }
        )
    }

}