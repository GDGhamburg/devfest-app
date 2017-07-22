package de.devfest.data.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import de.devfest.data.DevFestDataRepository
import de.devfest.model.Schedule
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseDevFestDataRepository @Inject constructor(val database: FirebaseDatabase) : DevFestDataRepository {


    override fun getSchedule(): Observable<Schedule> {
        return Observable.create{ emitter ->

            val reference = database.getReference("schedule")
            reference.addChildEventListener(

            )
        }
    }

    class EventListener : ChildEventListener {
        override fun onChildMoved(data: DataSnapshot?, p1: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onChildRemoved(p0: DataSnapshot?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCancelled(p0: DatabaseError?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}