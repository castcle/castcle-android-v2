package com.castcle.android.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.base.activity.BaseActivity
import com.castcle.android.core.extensions.findFragmentInNavHost
import com.castcle.android.core.extensions.hideKeyboard
import com.castcle.android.databinding.ActivityHomeBinding
import com.castcle.android.presentation.feed.FeedFragment
import com.castcle.android.presentation.login.LoginFragment
import com.castcle.android.presentation.setting.account.AccountFragment
import com.twitter.sdk.android.core.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {

    private val viewModel by viewModel<HomeViewModel>()

    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.recursiveRefreshToken.collectLatest {
                showLoading()
                viewModel.logout { popToHomeFragment() }
            }
        }
    }

    override fun initListener() {
        onBackPressedDispatcher.addCallback {
            if (findFragmentInNavHost<HomeFragment>()?.isFeedVisible() == false) {
                findFragmentInNavHost<HomeFragment>()?.navigateByFragment<FeedFragment>()
            } else {
                finish()
            }
        }
    }

    private fun popToHomeFragment() {
        runOnUiThread {
            findNavController(R.id.navHostContainer).popBackStack(R.id.homeFragment, false)
            dismissLoading()
            hideKeyboard()
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            when (findNavController(R.id.navHostContainer).currentDestination?.id) {
                R.id.accountFragment -> findFragmentInNavHost<AccountFragment>()
                    ?.twitterAuthClient
                    ?.onActivityResult(requestCode, resultCode, data)
                R.id.loginFragment -> findFragmentInNavHost<LoginFragment>()
                    ?.twitterAuthClient
                    ?.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun initTwitter() {
        val authConfig = TwitterAuthConfig(
            getString(R.string.twitter_app_id),
            getString(R.string.twitter_app_secret),
        )
        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(authConfig)
            .debug(BuildConfig.DEBUG)
            .build()
        Twitter.initialize(config)
    }

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewProperties()
        initListener()
        initObserver()
        initTwitter()
    }

}