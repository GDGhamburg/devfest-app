package de.devfest.screens.about

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.devfest.R
import de.devfest.databinding.ItemLicenceBinding

class LicencesFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View = inflater.inflate(R.layout.fragment_licences, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = view.findViewById<RecyclerView>(R.id.list_licences)
        list.layoutManager = LinearLayoutManager(getContext())
        list.adapter = LicenceAdapter(licences, this)
        list.adapter.notifyDataSetChanged()
    }

    private val licences: List<Licence>
        get() {
            val titles = getResources().getStringArray(R.array.licence_titles)
            val texts = getResources().getStringArray(R.array.licence_texts)
            val links = getResources().getStringArray(R.array.licence_links)

            val licences = mutableListOf<Licence>()
            for (i in titles.indices) {
                licences.add(Licence(titles.get(i), texts.get(i),
                        if (i < links.size) links[i] else null))
            }
            return licences
        }

    override fun onClick(v: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag() as String))
        getContext().startActivity(intent)
    }

    private inner class LicenceAdapter(val items: List<Licence>,
                                       val clickListener: View.OnClickListener)
        : RecyclerView.Adapter<LicenceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LicenceViewHolder =
                LicenceViewHolder(DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                R.layout.item_licence, parent, false))

        override fun onBindViewHolder(holder: LicenceViewHolder, position: Int) {
            val item = items[position]
            val titleText = item.title
            val textView = holder.binding.licenceTitle
            if (!TextUtils.isEmpty(item.link)) {
                val content = SpannableString(titleText)
                content.setSpan(UnderlineSpan(), 0, content.length, 0)
                textView.setOnClickListener(clickListener)
                textView.tag = item.link
                textView.text = content
            } else {
                textView.setOnClickListener(null)
                textView.tag = null
                textView.text = titleText
            }
            @Suppress("DEPRECATION")
            holder.binding.licenceText.text = Html.fromHtml(item.licenceText)
        }

        override fun getItemCount() = items.size
    }

    inner class LicenceViewHolder(val binding: ItemLicenceBinding)
        : RecyclerView.ViewHolder(binding.root)

    inner class Licence(val title: String, val licenceText: String, val link: String?)
}