package de.devfest.screens.dashboard

import android.view.View
import de.devfest.model.ScheduleSession
import de.devfest.ui.SessionAdapter

class LoopingSessionAdapter(interactionListener: SessionInteractionListener,
                            itemClickListener: View.OnClickListener)
    : SessionAdapter(interactionListener, itemClickListener) {

    override fun getItemCount(): Int {
        return if (super.getItemCount() == 0) 0 else Int.MAX_VALUE
    }

    override fun getItem(position: Int): ScheduleSession {
        return super.getItem(position % super.getItemCount())
    }

    override fun onBindViewHolder(holder: ScheduleSessionViewHolder, position: Int) {
        super.onBindViewHolder(holder, position % super.getItemCount())
    }
}