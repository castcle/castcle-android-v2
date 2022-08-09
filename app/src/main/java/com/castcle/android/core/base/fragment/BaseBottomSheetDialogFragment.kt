package com.castcle.android.core.base.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.castcle.android.R
import com.castcle.android.core.base.activity.BaseActivity
import com.castcle.android.core.extensions.cast
import com.castcle.android.core.extensions.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    val compositeDisposable = CompositeDisposable()

    fun backPress() {
        findNavController().popBackStack()
        hideKeyboard()
    }

    open fun initConsumer() = Unit

    open fun initListener() = Unit

    open fun initObserver() = Unit

    open fun initViewProperties() = Unit

    fun NavDirections.navigate() {
        findNavController().navigate(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
        initConsumer()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewProperties()
        initListener()
        initObserver()
    }

    fun showLoading() {
        activity?.cast<BaseActivity>()?.showLoading()
    }

    fun dismissLoading() {
        activity?.cast<BaseActivity>()?.dismissLoading()
    }

}