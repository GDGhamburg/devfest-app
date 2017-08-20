package de.devfest.screens.about

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import de.devfest.R
import de.devfest.databinding.ActivityAboutBinding
import de.devfest.screens.about.project.ProjectFragment
import de.devfest.ui.AnimationUtils
import de.devfest.ui.UiUtils

class AboutActivity : AppCompatActivity() {

    lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_about)
        binding.toolbar.setNavigationOnClickListener { finish() }
        if (UiUtils.pxlsToDips(this, UiUtils.getDisplayWidth(this)) < 620) {
            binding.toolbar.layoutParams.height = UiUtils.getActionBarHeight(this) +
                    resources.getDimensionPixelSize(R.dimen.spacing_min_touch)
        }
//        binding.toolbar.

        binding.aboutPager.adapter = AboutPagerAdapter(this, supportFragmentManager)
        binding.tabs.setupWithViewPager(binding.aboutPager)
        binding.aboutPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var animator: Animator? = null
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                animator?.cancel()
                animator = when (position) {
                    0 -> {
                        binding.headerLogo0.setImageResource(R.mipmap.logo_large)
                        AnimationUtils.fadeViews(binding.headerLogo1, binding.headerLogo0, true)
                    }
                    1 -> {
                        binding.headerLogo1.setImageResource(R.drawable.ic_announcement_large)
                        AnimationUtils.fadeViews(binding.headerLogo0, binding.headerLogo1, true)
                    }
                    2 -> {
                        binding.headerLogo0.setImageResource(R.drawable.ic_github_large)
                        AnimationUtils.fadeViews(binding.headerLogo1, binding.headerLogo0, true)
                    }
                    3 -> {
                        binding.headerLogo1.setImageResource(R.drawable.ic_archive_large)
                        AnimationUtils.fadeViews(binding.headerLogo0, binding.headerLogo1, true)
                    }
                    else -> null
                }
            }
        })
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }

    inner class AboutPagerAdapter(val context: Context, val fragmentManager: FragmentManager)
        : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> AppInfoFragment()
                1 -> CocFragment()
                2 -> ProjectFragment()
                3 -> LicencesFragment()
                else -> throw IllegalStateException("position should be in [0..3]")
            }
        }

        override fun getCount() = 4

        override fun getPageTitle(position: Int) = getString(
                when (position) {
                    0 -> R.string.about_tab_app
                    1 -> R.string.about_tab_coc
                    2 -> R.string.about_tab_project
                    3 -> R.string.about_tab_licences
                    else -> throw IllegalStateException("position should be in [0..3]")
                })
    }
}