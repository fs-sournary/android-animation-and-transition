package com.sournary.animationandtransitiontutorial

import android.os.Bundle
import android.transition.Explode
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_album_list.*

class AlbumListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_list)
        setupAlbumList()

        window.exitTransition = Explode()
    }

    private fun setupAlbumList() {
        val albumAdapter = AlbumAdapter(this)
        album_list.adapter = albumAdapter
    }
}
