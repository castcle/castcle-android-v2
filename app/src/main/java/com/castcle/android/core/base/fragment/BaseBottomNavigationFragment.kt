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

package com.castcle.android.core.base.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.annotation.*
import androidx.core.view.*
import androidx.fragment.app.commitNow
import com.castcle.android.R
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutBottomNavigationBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.navigation.NavigationBarView

abstract class BaseBottomNavigationFragment : BaseFragment(),
    NavigationBarView.OnItemSelectedListener {

    fun initBottomNavigation(
        @ColorRes colorActiveId: Int = R.color.blue,
        @ColorRes colorInactiveId: Int = R.color.white,
        fragment: List<Pair<Int, BaseFragment?>>,
        @IdRes frameLayoutId: Int = R.id.frameLayout,
        @IdRes initMenuItemId: Int? = null,
        @MenuRes menuItemsId: Int,
        selectedListener: NavigationBarView.OnItemSelectedListener? = null,
        reselectedListener: NavigationBarView.OnItemReselectedListener? = null,
    ) {
        val labelColor = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled),
            ),
            intArrayOf(color(colorActiveId), color(colorInactiveId), color(colorInactiveId))
        )
        childFragmentManager.commitNow {
            fragment.forEach { (id, fragment) ->
                val existFragment = childFragmentManager.findFragmentByTag(id.toString())
                if (existFragment == null && fragment != null) {
                    add(frameLayoutId, fragment, id.toString())
                    hide(fragment)
                }
            }
            fragment.find { it.first == initMenuItemId }
                ?.second
                ?.also(::show)
        }
        binding.bottomNavigation.menu.clear()
        binding.bottomNavigation.inflateMenu(menuItemsId)
        binding.bottomNavigation.setOnItemSelectedListener(selectedListener ?: this)
        binding.bottomNavigation.itemTextColor = labelColor
        binding.bottomNavigation.itemIconTintList = labelColor
        binding.bottomNavigation.setOnItemReselectedListener(null)
        binding.bottomNavigation.selectedItemId =
            initMenuItemId ?: binding.bottomNavigation.menu[0].itemId
        binding.bottomNavigation.setOnItemReselectedListener(reselectedListener)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        childFragmentManager.commitNow {
            val target = childFragmentManager.findFragmentByTag(item.itemId.toString())
            val current = getCurrentVisibleFragment(childFragmentManager)
            if (target != current) {
                current?.also(::hide)
                target?.also(::show)
            }
        }
        return true
    }

    @SuppressLint("RestrictedApi")
    fun setIconSize(index: Int, @DimenRes dimenId: Int) {
        binding.bottomNavigation.getChildAt(0)
            ?.cast<BottomNavigationMenuView>()
            ?.getChildAt(index)
            ?.cast<NavigationBarItemView>()
            ?.setIconSize(dimenDp(dimenId).toInt())
    }

    fun setIconWithoutTint(index: Int, @DrawableRes drawableId: Int) {
        binding.bottomNavigation.menu.getItem(index)?.apply {
            MenuItemCompat.setIconTintMode(this, PorterDuff.Mode.DST)
            icon = drawable(drawableId)
        }
    }

    inline fun <reified Fragment> navigateByFragment() {
        childFragmentManager.fragments.find { it is Fragment }
            ?.tag
            ?.toIntOrNull()
            ?.also(binding.bottomNavigation::setSelectedItemId)
    }

    fun navigateByMenuId(@IdRes menuId: Int) {
        binding.bottomNavigation.menu.children.find { it.itemId == menuId }
            ?.itemId
            ?.also(binding.bottomNavigation::setSelectedItemId)
    }

    val binding by lazy {
        LayoutBottomNavigationBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}