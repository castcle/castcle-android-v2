package com.castcle.android.presentation.home

import android.os.Bundle
import android.view.MenuItem
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseBottomNavigationFragment
import com.castcle.android.core.extensions.cast
import com.castcle.android.presentation.feed.FeedFragment
import com.castcle.android.presentation.search.top_trends.TopTrendsFragment
import com.google.android.material.navigation.NavigationBarView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : BaseBottomNavigationFragment(), NavigationBarView.OnItemReselectedListener {

    private val shareViewModel by sharedViewModel<HomeViewModel>()

    private val directions = HomeFragmentDirections

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomNavigation(
            menuItemsId = R.menu.bottom_navigation_home,
            reselectedListener = this,
            fragment = listOf(
                R.id.feed to FeedFragment.newInstance(),
                R.id.new_cast to null,
                R.id.search to TopTrendsFragment.newInstance(),
            ),
        )
    }

    override fun initViewProperties() {
        setIconWithoutTint(index = 1, drawableId = R.drawable.ic_new_cast)
        setIconSize(index = 1, dimenId = com.intuit.sdp.R.dimen._30sdp)
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        if (item.itemId == R.id.feed) {
            childFragmentManager.fragments.find { it is FeedFragment }
                ?.cast<FeedFragment>()
                ?.scrollToTop()
        } else if (item.itemId == R.id.search) {
            childFragmentManager.fragments.find { it is TopTrendsFragment }
                ?.cast<TopTrendsFragment>()
                ?.scrollToTop()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.new_cast) {
            shareViewModel.isUserCanEngagement(
                isGuestAction = { directions.toLoginFragment().navigate() },
                isMemberAction = { directions.toNewCastFragment(null, null).navigate() },
                isUserNotVerifiedAction = { directions.toResentVerifyEmailFragment().navigate() },
            )
            false
        } else {
            super.onNavigationItemSelected(item)
        }
    }

}