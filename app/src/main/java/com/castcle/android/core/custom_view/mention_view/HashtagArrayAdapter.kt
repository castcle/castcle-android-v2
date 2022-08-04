package com.castcle.android.core.custom_view.mention_view

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.ArrayAdapter
import com.castcle.android.databinding.ItemMentionHashtagBinding
import com.castcle.android.domain.search.entity.HashtagEntity

class HashtagArrayAdapter(
    context: Context,
    val items: MutableList<HashtagsItem> = mutableListOf()
) : ArrayAdapter<HashtagArrayAdapter.HashtagsItem>(context, 0, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewBinding = ItemMentionHashtagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        if (items.isNotEmpty()) {
            onBindHolder(items[position], viewBinding)
        }
        return viewBinding.root
    }

    override fun getItem(position: Int) = items[position]

    override fun getCount() = items.size

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewBinding = ItemMentionHashtagBinding.inflate(
            LayoutInflater.from(parent.context), parent, true
        )
        if (items.isNotEmpty()) {
            onBindHolder(items[position], viewBinding)
        }
        return viewBinding.root
    }

    override fun addAll(collection: MutableCollection<out HashtagsItem>) {
        super.addAll(collection)
    }

    @SuppressLint("SetTextI18n")
    private fun onBindHolder(
        itemHashtag: HashtagsItem,
        viewBinding: ItemMentionHashtagBinding
    ) {
        with(viewBinding) {
            tvType.text = itemHashtag.hashtag.slug
            tvHashtag.text = "#${itemHashtag.hashtag.name}"
        }
    }

    data class HashtagsItem(val hashtag: HashtagEntity = HashtagEntity()) {
        override fun toString() = hashtag.name
    }

}
