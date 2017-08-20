package de.devfest.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.view.View
import org.jetbrains.annotations.NotNull
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.res.Resources


class AnimationUtils {

    companion object {

        fun longDuration(resources: Resources) =
                resources.getInteger(android.R.integer.config_longAnimTime)

        fun mediumDuration(resources: Resources) =
                resources.getInteger(android.R.integer.config_mediumAnimTime)


        fun fadeViews(fadeOut: View, @NotNull fadeIn: View,
                      animate: Boolean): Animator {
            val duration = longDuration(fadeIn.getResources())
//        if (fadeOut == null) {
//            val animator = createFadeAnimator(fadeIn, false)
//            if (animator != null) animator.setDuration(if (animate) duration.toLong() else 0L)
//            return animator
//        }
            val animatorSet = AnimatorSet()
            val animators = mutableListOf<Animator>()
            val fadeOutAnimator = createFadeAnimator(fadeOut, true)
            animators.add(fadeOutAnimator)
            val fadeInAnimator = createFadeAnimator(fadeIn, false)
            animators.add(fadeInAnimator)
            animatorSet.playTogether(animators)
            animatorSet.duration = if (animate) duration.toLong() else 0L
            animatorSet.start()

            return animatorSet
        }

        private fun createFadeAnimator(v: View, out: Boolean): Animator {

            val animator = if (out)
                ObjectAnimator.ofFloat(null, View.ALPHA, 1f, 0f)
            else
                ObjectAnimator.ofFloat(null, View.ALPHA, 0f, 1f)
            animator.target = v
            animator.duration = mediumDuration(v.resources).toLong()

            if (out) {
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        animator.removeListener(this)
                        setGone(v)
                    }
                })
            } else {
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        animator.removeListener(this)
                        setVisible(v)
                    }
                })
            }
            return animator
        }

        private fun setGone(v: View) {
            v.visibility = View.GONE
        }

        private fun setVisible(v: View) {
            v.visibility = View.VISIBLE
        }

        private fun isVisible(v: View): Boolean {
            return v.visibility == View.VISIBLE
        }
    }
}
