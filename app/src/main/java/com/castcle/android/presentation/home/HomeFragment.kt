/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

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

    fun isFeedVisible(): Boolean {
        return binding.bottomNavigation.selectedItemId == R.id.feed
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