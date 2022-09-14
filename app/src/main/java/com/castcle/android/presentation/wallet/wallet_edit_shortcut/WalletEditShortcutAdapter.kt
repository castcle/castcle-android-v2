package com.castcle.android.presentation.wallet.wallet_edit_shortcut

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castcle.android.R
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemWalletEditShortcutBinding
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.wallet.entity.WalletShortcutEntity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.util.*

class WalletEditShortcutAdapter(
    private val compositeDisposable: CompositeDisposable,
    private val listener: WalletEditShortcutListener,
) : RecyclerView.Adapter<WalletEditShortcutAdapter.ViewHolder>() {

    val items = mutableListOf<ViewEntity>()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemWalletEditShortcutBinding.inflate(inflate, parent, false)
        return ViewHolder(binding, compositeDisposable, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.getOrNull(position) ?: ViewEntity())
    }

    override fun getItemCount() = items.size

    fun onItemDelete(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onItemUpdate(newItems: List<ViewEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemWalletEditShortcutBinding,
        private val compositeDisposable: CompositeDisposable,
        private val listener: WalletEditShortcutListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentItem = ViewEntity()

        init {
            compositeDisposable += binding.ivDelete.onClick {
                listener.onItemDelete(bindingAdapterPosition, currentItem.shortcut)
            }
        }

        fun bind(item: ViewEntity) {
            currentItem = item
            binding.ivAvatar.loadAvatarImage(item.user.avatar.thumbnail)
            binding.tvCastcleId.text = if (item.isYou) {
                "${item.user.castcleId} (You)"
            } else {
                item.user.castcleId
            }
            binding.tvCastcleId.setTextColor(
                if (item.isYou) {
                    color(R.color.blue)
                } else {
                    color(R.color.white)
                }
            )
        }

    }

    data class ViewEntity(
        val isYou: Boolean = false,
        val shortcut: WalletShortcutEntity = WalletShortcutEntity(),
        val user: UserEntity = UserEntity(),
    )

}