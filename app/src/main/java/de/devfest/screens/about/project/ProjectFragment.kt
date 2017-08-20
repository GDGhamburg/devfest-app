package de.devfest.screens.about.project

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import de.devfest.R
import de.devfest.databinding.FragmentProjectBinding
import de.devfest.databinding.ItemContributorBinding
import de.devfest.injection.ApplicationComponent
import de.devfest.model.Contributor
import de.devfest.mvpbase.BaseFragment
import de.devfest.ui.UiUtils
import de.devfest.utils.ExternalAppUtils
import javax.inject.Inject

class ProjectFragment : BaseFragment<ProjectView, ProjectPresenter>(), ProjectView, View.OnClickListener {

    @Inject
    lateinit var presenter: ProjectPresenter

    lateinit var adapter: ContributorAdapter

    override fun inject(component: ApplicationComponent): ProjectPresenter {
        component.inject(this)
        return presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<FragmentProjectBinding>(inflater,
                R.layout.fragment_project, container, false)

        binding.recyclerviewContributors.layoutManager = GridLayoutManager(context,
                UiUtils.getGridSmallColumnCount(context))
        adapter = ContributorAdapter(this)
        binding.recyclerviewContributors.adapter = adapter

        return binding.root
    }

    override fun onContributorsAvailable(contributors: List<Contributor>) {
        adapter.contributors.clear()
        adapter.contributors.addAll(contributors)
        adapter.notifyDataSetChanged()
    }

    override fun onError(error: Throwable) {

        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View) {
        ExternalAppUtils.openLink(context, view.tag as String)
    }

    inner class ContributorAdapter(val onClickListener: View.OnClickListener)
        : RecyclerView.Adapter<ContributorViewHolder>() {

        val contributors = mutableListOf<Contributor>()

        override fun getItemCount() = contributors.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributorViewHolder {
            val binding = DataBindingUtil.inflate<ItemContributorBinding>(
                    LayoutInflater.from(parent.context), R.layout.item_contributor, parent, false)
            binding.root.setOnClickListener(onClickListener)
            return ContributorViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ContributorViewHolder, position: Int) {
            holder.bind(contributors.get(position))
        }
    }

    inner class ContributorViewHolder(val binding: ItemContributorBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(contributor: Contributor) {
            binding.root.setTag(contributor.htmlUrl)
            binding.textContributorName.setText(contributor.login)

            if (!contributor.avatarUrl.isNullOrBlank()) {
                Glide.with(binding.imageContributor.getContext())
                        .load(contributor.avatarUrl)
                        .placeholder(R.drawable.ic_speaker_placeholder_grey)
                        .into(binding.imageContributor)
            } else {
                Glide.clear(binding.imageContributor)
            }
        }
    }
}
