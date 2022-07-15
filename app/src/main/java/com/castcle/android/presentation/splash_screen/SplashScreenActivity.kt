package com.castcle.android.presentation.splash_screen

import android.annotation.SuppressLint
import android.os.Bundle
import com.castcle.android.core.base.activity.BaseActivity
import com.castcle.android.core.extensions.navigate
import com.castcle.android.databinding.ActivitySplashScreenBinding
import com.castcle.android.presentation.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {

    private val viewModel by viewModel<SplashScreenViewModel>()

    override fun initObserver() {
        viewModel.fetchAccessTokenComplete.observe(this) {
            navigate<HomeActivity>(finishCurrent = true)
        }
    }

    private val binding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewProperties()
        initListener()
        initObserver()
    }

}