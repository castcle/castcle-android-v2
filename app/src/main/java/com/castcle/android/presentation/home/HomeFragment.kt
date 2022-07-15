package com.castcle.android.presentation.home

import android.view.MenuItem
import com.castcle.android.R
import com.castcle.android.core.base.fragment.BaseBottomNavigationFragment
import com.castcle.android.core.extensions.cast
import com.castcle.android.presentation.feed.FeedFragment
import com.castcle.android.presentation.search.SearchFragment
import com.google.android.material.navigation.NavigationBarView

class HomeFragment : BaseBottomNavigationFragment(), NavigationBarView.OnItemReselectedListener {

    override fun initViewProperties() {
        initBottomNavigation(
            menuItemsId = R.menu.bottom_navigation_home,
            reselectedListener = this,
            fragment = listOf(
                R.id.feed to FeedFragment.newInstance(),
                R.id.new_cast to null,
                R.id.search to SearchFragment.newInstance(),
            ),
        )
        setIconWithoutTint(index = 1, drawableId = R.drawable.ic_new_cast)
        setIconSize(index = 1, dimenId = com.intuit.sdp.R.dimen._30sdp)
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        if (item.itemId == R.id.feed) {
            childFragmentManager.fragments.find { it is FeedFragment }
                ?.cast<FeedFragment>()
                ?.scrollToTop()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.new_cast) {
            false
        } else {
            super.onNavigationItemSelected(item)
        }
    }

}