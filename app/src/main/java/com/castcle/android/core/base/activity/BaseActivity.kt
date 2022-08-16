package com.castcle.android.core.base.activity

import androidx.appcompat.app.AppCompatActivity
import com.castcle.android.databinding.DialogLoadingBinding
import com.castcle.android.presentation.dialog.loading.LoadingDialog
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    private val loadingDialog by lazy {
        LoadingDialog(DialogLoadingBinding.inflate(layoutInflater))
    }

    val compositeDisposable = CompositeDisposable()

    open fun initViewProperties() = Unit

    open fun initListener() = Unit

    open fun initObserver() = Unit

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun showLoading() {
        loadingDialog.show()
    }

    fun dismissLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

}