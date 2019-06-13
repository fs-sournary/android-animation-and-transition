package com.sournary.animationandtransitiontutorial

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.palette.graphics.Palette
import androidx.transition.Scene
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_album_detail.*

/**
 * Created in 5/22/19 by Sang
 * Description:
 */
class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var transitionManager: TransitionManager
    private lateinit var expandedScene: Scene
    private lateinit var collapsedScene: Scene
    private lateinit var currentScene: Scene

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_detail)
        setupTransition()
    }

    private fun setupTransition() {
        val slide = Slide(Gravity.BOTTOM).apply {
            excludeTarget(android.R.id.statusBarBackground, true)
        }
        window.enterTransition = slide
        window.sharedElementsUseOverlay = false
//        window.exitTransition = Fade()
//        window.enterTransition = Explode()

        transitionManager = TransitionManager()
        val transitionRoot = detail_container as ViewGroup
        expandedScene = Scene.getSceneForLayout(
            transitionRoot, R.layout.activity_album_detail_expanded, this
        )
        expandedScene.setEnterAction {
            currentScene = expandedScene
            populate()
            val trackPanel = findViewById<LinearLayout>(R.id.track_panel)
            trackPanel.setOnClickListener {
                currentScene = if (currentScene == expandedScene) {
                    collapsedScene
                } else {
                    expandedScene
                }
                transitionManager.transitionTo(currentScene)
            }
            val albumArt = findViewById<AppCompatImageView>(R.id.album_art)
            albumArt.setOnClickListener {
                animate()
            }
        }
//        val expandTransitionSet =
//            TransitionSet().apply { ordering = TransitionSet.ORDERING_SEQUENTIAL }
//        val changeBounds = ChangeBounds().apply { duration = 200 }
//        expandTransitionSet.addTransition(changeBounds)
//        val fadeLyrics = Fade().apply {
//            addTarget(R.id.lyrics)
//            duration = 150
//        }
//        expandTransitionSet.addTransition(fadeLyrics)
        val expandTransitionSet = TransitionInflater.from(this)
            .inflateTransition(R.transition.transition_album_detail_expanded)

        collapsedScene =
            Scene.getSceneForLayout(transitionRoot, R.layout.activity_album_detail, this)
        collapsedScene.setEnterAction {
            currentScene = collapsedScene
            populate()
            val trackPanel = findViewById<LinearLayout>(R.id.track_panel)
            trackPanel.setOnClickListener {
                currentScene = if (currentScene == expandedScene) {
                    collapsedScene
                } else {
                    expandedScene
                }
                transitionManager.transitionTo(currentScene)
            }
            val albumArt = findViewById<AppCompatImageView>(R.id.album_art)
            albumArt.setOnClickListener {
                animate()
            }
        }
        val collapseTransitionSet =
//            TransitionSet().apply { ordering = TransitionSet.ORDERING_SEQUENTIAL }
            TransitionInflater.from(this)
                .inflateTransition(R.transition.transition_album_detail_collapsed)
//        val fadeOutLyrics = Fade().apply {
//            addTarget(R.id.lyrics)
//            duration = 150
//        }
//        collapseTransitionSet.addTransition(fadeOutLyrics)
//        val resetBounds = ChangeBounds().apply { duration = 200 }
//        collapseTransitionSet.addTransition(resetBounds)

        transitionManager.setTransition(expandedScene, collapsedScene, collapseTransitionSet)
        transitionManager.setTransition(collapsedScene, expandedScene, expandTransitionSet)
        collapsedScene.enter()

//        postponeEnterTransition()
    }

    private fun animate() {
        fab.scaleX = 0f
        fab.scaleY = 0f
        fab.animate().scaleX(1f).scaleY(1f).start()
        // animate() is actually convenient wrapper method for property animation API.
        val scaleX = ObjectAnimator.ofFloat(fab, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(fab, "scaleY", 0f, 1f)
        val scaleFab = AnimatorSet().apply { playTogether(scaleX, scaleY) }
        scaleFab.start()
//        val scaleFab = AnimatorInflater.loadAnimator(this, R.animator.scale).apply {
//            setTarget(fab)
//        }

        val titleStartValue = title_panel.top
        val titleEndValue = title_panel.bottom
        val animatorTitle =
            ObjectAnimator.ofInt(title_panel, "bottom", titleStartValue, titleEndValue)
        animatorTitle.interpolator = AccelerateInterpolator()

        val trackStartValue = track_panel.top
        val trackEndValue = track_panel.bottom
        val animatorTrack =
            ObjectAnimator.ofInt(track_panel, "bottom", trackStartValue, trackEndValue)
        animatorTrack.interpolator = DecelerateInterpolator()

        title_panel.bottom = titleStartValue
        track_panel.bottom = titleStartValue
        fab.scaleX = 0f
        fab.scaleY = 0f

//        animatorTitle.duration = 1000
//        animatorTrack.duration = 1000
//        animatorTitle.startDelay = 1000

        AnimatorSet().apply {
            playSequentially(animatorTitle, animatorTrack, scaleFab)
            start()
        }
    }

    private fun populate() {
//        Handler().postDelayed({
        val albumArtResId =
            intent.getIntExtra(EXTRA_ALBUM_ART, R.drawable.mean_something_kinder_than_wolves)
        val albumArt = findViewById<AppCompatImageView>(R.id.album_art)
        albumArt.setImageResource(albumArtResId)
        val albumBitmap = getReducedBitmap(albumArtResId)
        colorizeFromImage(albumBitmap)

//            startPostponedEnterTransition()
//        }, 1000)
//        val albumArtResId =
//            intent.getIntExtra(EXTRA_ALBUM_ART, R.drawable.mean_something_kinder_than_wolves)
//        val albumArt = findViewById<AppCompatImageView>(R.id.album_art)
//        albumArt.setImageResource(albumArtResId)
//        val albumBitmap = getReducedBitmap(albumArtResId)
//        colorizeFromImage(albumBitmap)
    }

    private fun getReducedBitmap(albumArtResId: Int): Bitmap {
        // Reduce image size in memory to avoid memory errors
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = false
            inSampleSize = 8
        }
        return BitmapFactory.decodeResource(resources, albumArtResId, options)
    }

    private fun colorizeFromImage(image: Bitmap) {
        val palette = Palette.from(image).generate()
        // Set panel colors
        val defaultPanelColor = 0xFF808080
        val defaultFabColor = 0xFFEEEEEE
        val titlePanel = findViewById<RelativeLayout>(R.id.title_panel)
        val trackPanel = findViewById<LinearLayout>(R.id.track_panel)
        titlePanel.setBackgroundColor(palette.getDarkVibrantColor(defaultPanelColor.toInt()))
        trackPanel.setBackgroundColor(palette.getLightMutedColor(defaultPanelColor.toInt()))
        // Set fab colors
        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_pressed)
        )
        val colors = intArrayOf(
            palette.getVibrantColor(defaultFabColor.toInt()),
            palette.getLightVibrantColor(defaultFabColor.toInt())
        )
        val fab = findViewById<AppCompatImageButton>(R.id.fab)
        fab.backgroundTintList = ColorStateList(states, colors)
    }

    companion object {

        const val EXTRA_ALBUM_ART = "EXTRA_ALBUM_ART"
    }
}