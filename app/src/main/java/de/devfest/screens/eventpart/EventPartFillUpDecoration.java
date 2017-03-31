package de.devfest.screens.eventpart;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.devfest.model.EventPart;
import de.devfest.model.ScheduleSession;
import de.devfest.ui.SessionAdapter;
import de.devfest.ui.UiUtils;

import static de.devfest.ui.SessionAdapter.DIPS_PER_MINUTE;
import static java.util.concurrent.TimeUnit.SECONDS;

public class EventPartFillUpDecoration extends RecyclerView.ItemDecoration {

    private EventPart eventPart;

    public EventPartFillUpDecoration(EventPart eventPart) {
        this.eventPart = eventPart;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        SessionAdapter adapter = (SessionAdapter) parent.getAdapter();
        int position = parent.getChildAdapterPosition(view);

        ScheduleSession session = adapter.getItem(position);
        if (position > 0) {
            ScheduleSession prevSession = adapter.getItem(position - 1);
            long minutesBefore  = SECONDS.toMinutes(session.session.startTime.toEpochSecond() -
                    prevSession.session.endTime.toEpochSecond());
            if (minutesBefore > 15) {
//            if (durationMinutes > 0) {
                minutesBefore -= 30;
                outRect.top = UiUtils.dipsToPxls(view.getContext(), (int) (DIPS_PER_MINUTE * minutesBefore));
            }

            if (position == adapter.getItemCount() - 1) {
                long minutesAfter  = SECONDS.toMinutes(session.session.endTime.toEpochSecond() -
                        eventPart.endTime.toEpochSecond());
//                outRect.bottom = UiUtils.dipsToPxls(view.getContext(), (int) (DIPS_PER_MINUTE * minutesAfter));
            }
        } else if (position == 0) {
//            long minutesBetween  = SECONDS.toMinutes(session.session.startTime.toEpochSecond() -
//                    eventPart.startTime.toEpochSecond());
//            if (minutesBetween > 15) {
////            if (durationMinutes > 0) {
//                minutesBetween -= 30;
//                outRect.top = UiUtils.dipsToPxls(view.getContext(), (int) (DIPS_PER_MINUTE * minutesBetween));
//            }
        }
    }
}
