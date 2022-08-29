package com.castcle.android.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseFragment
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.FragmentLoginBinding
import com.castcle.android.presentation.login.item_login.LoginViewRenderer
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import io.reactivex.rxkotlin.plusAssign
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment(), LoginListener {

    private val viewModel by viewModel<LoginViewModel>()

    private val directions = LoginFragmentDirections

    private val facebookLoginManager by inject<LoginManager>()

    private val googleSignInClient by inject<GoogleSignInClient>()

    private val callbackManager = CallbackManager.Factory.create()

    val twitterAuthClient by inject<TwitterAuthClient>()

    override fun initViewProperties() {
        binding.ivBackground.loadImage(R.drawable.bg_login)
        binding.recyclerView.adapter = adapter
        adapter.submitList(viewModel.items.value)
    }

    override fun initListener() {
        compositeDisposable += binding.ivClose.onClick {
            backPress()
        }
    }

    override fun initObserver() {
        viewModel.loginComplete.observe(viewLifecycleOwner) {
            dismissLoading()
            findNavController().popBackStack(R.id.homeFragment, false)
        }
        viewModel.loginError.observe(viewLifecycleOwner) {
            dismissLoading()
            toast(it.message)
        }
    }

    override fun onEmailLoginClicked(email: String, password: String) {
        viewModel.loginWithEmail(email, password)
        hideKeyboard()
        showLoading()
    }

    override fun onFacebookLoginClicked() {
        facebookLoginManager.logOut()
        facebookLoginManager.logInWithReadPermissions(
            callbackManager = callbackManager,
            fragment = this,
            permissions = listOf("email", "public_profile"),
        )
        facebookLoginManager.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    viewModel.loginWithFacebook()
                    showLoading()
                }

                override fun onCancel() {
                    facebookLoginManager.logOut()
                }

                override fun onError(error: FacebookException) {
                    facebookLoginManager.logOut()
                    toast(error.message)
                }
            })
    }

    override fun onForgotPasswordClicked() {
        directions.toForgotPasswordFragment().navigate()
    }

    override fun onGoogleLoginClicked() {
        googleSignInClient.signOut()
        googleSignInCallback.launch(googleSignInClient.signInIntent)
    }

    private val googleSignInCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        if (task.isSuccessful && task.result != null) {
            viewModel.loginWithGoogle(task.result)
            showLoading()
        } else {
            toast(task.exception.toString())
        }
    }

    override fun onTwitterLoginClicked() {
        twitterAuthClient.cancelAuthorize()
        twitterAuthClient.authorize(activity, object : Callback<TwitterSession>() {
            override fun failure(exception: TwitterException?) = toast(exception?.message)
            override fun success(result: Result<TwitterSession>?) {
                viewModel.loginWithTwitter(result?.data?.authToken)
                showLoading()
            }
        })
    }

    override fun onUrlClicked(url: String) {
        openUrl(url)
    }

    override fun onStop() {
        changeSoftInputMode(false)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        changeSoftInputMode(true)
    }

    private val adapter by lazy {
        CastcleAdapter(this, compositeDisposable).apply {
            registerRenderer(LoginViewRenderer())
        }
    }

    private val binding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}