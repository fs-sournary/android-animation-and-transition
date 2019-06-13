package com.sournary.animationandtransitiontutorial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created in 5/22/19 by Sang
 * Description:
 */
class AlbumViewHolder(itemView: View, listener: AlbumActionListener) :
    RecyclerView.ViewHolder(itemView) {

    private val albumArtImage = itemView.findViewById<AppCompatImageView>(R.id.album_art)

    init {
        itemView.setOnClickListener { listener.onClick(adapterPosition, albumArtImage) }
    }

    fun bindView(@DrawableRes resId: Int) {
        albumArtImage.setImageResource(resId)
    }

    companion object {

        fun create(parent: ViewGroup, listener: AlbumActionListener): AlbumViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.album_gird_item, parent, false)
            return AlbumViewHolder(view, listener)
        }
    }
}
