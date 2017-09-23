package de.devfest.screens.dashboard

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import de.devfest.R
import de.devfest.databinding.FragmentDashboardBinding
import de.devfest.injection.ApplicationComponent
import de.devfest.model.ScheduleSession
import de.devfest.model.Session
import de.devfest.mvpbase.AuthFragment
import de.devfest.screens.main.ActionBarDrawerToggleHelper
import de.devfest.screens.sessiondetails.SessionDetailsActivity
import de.devfest.ui.SessionAdapter
import de.devfest.ui.SessionAdapter.SessionInteractionListener
import de.devfest.ui.UiUtils
import de.devfest.ui.facedetection.GlideFaceDetector
import timber.log.Timber

class DashboardFragment : AuthFragment<DashboardView, DashboardPresenter>(), DashboardView,
        SessionInteractionListener, View.OnClickListener {

    @Inject
    internal lateinit var presenter: DashboardPresenter

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var toggleHelper: ActionBarDrawerToggleHelper
    private lateinit var nowRunningAdapter: SessionAdapter

    private var isAnimating: Boolean = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        GlideFaceDetector.initialize(context)

        binding = DataBindingUtil.inflate<FragmentDashboardBinding>(inflater!!,
                R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleHelper = ActionBarDrawerToggleHelper(this)

        binding.recyclerviewNowRunning.layoutManager = LinearLayoutManager(context, HORIZONTAL,
                false)
        nowRunningAdapter = LoopingSessionAdapter(this,this)
        nowRunningAdapter.setItemWidth(UiUtils.dipsToPxls(context, 240))
        nowRunningAdapter.setShowAddButton(false)
//        nowRunningAdapter.setShowCard(false)
        binding.recyclerviewNowRunning.adapter = nowRunningAdapter
    }

    override fun onDestroyView() {
        GlideFaceDetector.releaseDetector()
        toggleHelper.destroy(this)
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        checkStartAnimationRunning()
    }

    override fun onPause() {
        super.onPause()
        isAnimating = false
    }

    private fun checkStartAnimationRunning() {
        val delay = 5L
        if (!isAnimating && nowRunningAdapter.itemCount > 2) {
            isAnimating = true
            binding.recyclerviewNowRunning.postDelayed(object: Runnable {
                override fun run() {
                    binding.recyclerviewNowRunning.scrollBy(2, 0)
                    if (isAnimating) binding.recyclerviewNowRunning.postDelayed(this, delay)
                }
            }, delay)
        }
    }

    override fun inject(component: ApplicationComponent): DashboardPresenter {
        component.inject(this)
        return presenter
    }

    override fun onScheduledSessionReceived(session: Session) {
        Timber.e("scheduled session: %s", session.title)
    }

    override fun onRunningSessionReceived(session: Session, scheduled: Boolean) {
        Timber.e("running session: %s", session.title)
        nowRunningAdapter.addSession(session, scheduled)
        checkStartAnimationRunning()
    }

    override fun onError(error: Throwable) {
        Timber.e(error)
    }

    override fun showLogin() {
        // show the login to the user.
        // reach the click to the presenter
        // presenter.onLoginRequested();
    }

    override fun onClick(view: View) {
        val sessionItem = view.tag as ScheduleSession
        SessionDetailsActivity.showWithTransition(sessionItem.session, activity, view)
    }

    override fun onAddSessionClick(session: Session) {
        presenter.onAddSessionClick(session)
    }

    override fun onRemoveSessionClick(session: Session) {
        presenter.onRemoveSessionClick(session)
    }

    override fun getPresenter() = presenter

    companion object {

        val TAG = DashboardFragment::class.java.toString()
    }
}
