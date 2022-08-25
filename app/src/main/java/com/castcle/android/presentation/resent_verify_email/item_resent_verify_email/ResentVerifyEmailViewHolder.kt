package com.castcle.android.presentation.resent_verify_email.item_resent_verify_email

import android.text.Html
import android.widget.TextView
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.context
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemResentVerifyEmailBinding
import com.castcle.android.presentation.resent_verify_email.ResentVerifyEmailListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class ResentVerifyEmailViewHolder(
    private val binding: ItemResentVerifyEmailBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ResentVerifyEmailListener,
) : CastcleViewHolder<ResentVerifyEmailViewEntity>(binding.root) {

    override var item = ResentVerifyEmailViewEntity()

    init {
        compositeDisposable += binding.tvResendEmail.onClick {
            listener.onResentVerifyEmailClicked()
        }
    }

    override fun bind(bindItem: ResentVerifyEmailViewEntity) {
        val text = context().getString(
            R.string.fragment_resent_verify_email_title_3,
            item.email,
        )
        binding.tvTitle3.setText(
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY),
            TextView.BufferType.SPANNABLE,
        )
    }

}