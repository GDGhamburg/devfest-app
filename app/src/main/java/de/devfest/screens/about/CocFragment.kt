package de.devfest.screens.about

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.devfest.R
import de.devfest.databinding.FragmentCocBinding

class CocFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentCocBinding>(inflater, R.layout.fragment_coc,
                container, false)

        @Suppress("DEPRECATION")
        binding.cocText.text = Html.fromHtml(getString(R.string.coc_text,
                getString(R.string.coc_contact)))

        return binding.root
    }

}