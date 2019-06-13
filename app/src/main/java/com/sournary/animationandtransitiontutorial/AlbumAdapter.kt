package com.sournary.animationandtransitiontutorial

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created in 5/22/19 by Sang
 * Description:
 */
class AlbumAdapter(private val context: Context) : RecyclerView.Adapter<AlbumViewHolder>() {

    private val albumArts = arrayOf(
        R.drawable.mean_something_kinder_than_wolves,
        R.drawable.cylinders_chris_zabriskie,
        R.drawable.broken_distance_sutro,
        R.drawable.playing_with_scratches_ruckus_roboticus,
        R.drawable.keep_it_together_guster,
        R.drawable.the_carpenter_avett_brothers,
        R.drawable.please_sondre_lerche,
        R.drawable.direct_to_video_chris_zabriskie
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder.create(parent, object : AlbumActionListener {
            override fun onClick(position: Int, view: AppCompatImageView) {
                val albumArtResId = albumArts[position % albumArts.size]
                val intent = Intent(context, AlbumDetailActivity::class.java).apply {
                    putExtra(AlbumDetailActivity.EXTRA_ALBUM_ART, albumArtResId)
                }
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    context as Activity, view, "albumArt"
                )
                context.startActivity(intent, options.toBundle())
            }
        })
    }

    override fun getItemCount(): Int = albumArts.size * 4

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindView(albumArts[position % albumArts.size])
    }
}
