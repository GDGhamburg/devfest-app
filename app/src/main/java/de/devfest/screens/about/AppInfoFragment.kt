package de.devfest.screens.about

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.devfest.R
import de.devfest.databinding.FragmentAppInfoBinding

class AppInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAppInfoBinding>(inflater,
                R.layout.fragment_app_info, container, false)

        val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        binding.textAppVersion.text = getString(R.string.app_version, versionName)

        return binding.root
    }
}