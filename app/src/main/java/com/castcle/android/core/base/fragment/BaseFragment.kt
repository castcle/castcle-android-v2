package com.castcle.android.core.base.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.castcle.android.core.base.activity.BaseActivity
import com.castcle.android.core.extensions.cast
import com.castcle.android.core.extensions.hideKeyboard
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {

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
        activity?.runOnUiThread {
            findNavController().navigate(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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