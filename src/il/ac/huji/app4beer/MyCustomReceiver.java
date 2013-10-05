package il.ac.huji.app4beer;

import il.ac.huji.app4beer.DAL.Contact;
import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Event;
import il.ac.huji.app4beer.DAL.Message;
import il.ac.huji.app4beer.DAL.ParseProxy;
import il.ac.huji.app4beer.DAL.PushEvent;
import il.ac.huji.app4beer.DAL.ParseProxy.PushEnvelope;

import com.google.gson.Gson;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyCustomReceiver extends BroadcastReceiver {
 
private void handlePushNotifications(Intent intent) {
	PushEnvelope env = ParseProxy.getTheEvelope(intent);
	if (env == null) return;
	Gson gson = new Gson();
	switch (env.getType()) {
	case NewEvent:
		PushEvent pEvent = gson.fromJson(env.getMessage(), PushEvent.class);
		try {
			pEvent.persist();
		} catch (Exception e) {
			e.printStackTrace();
		}
		break;
	case Tweet:
		PushEvent.MessageParcel mParcel = gson.fromJson(env.getMessage(), PushEvent.MessageParcel.class);
		try {
			Message message = new Message(DAL.Instance().readContact(mParcel.get_contact()).get_id(),
					DAL.Instance().readEvent(mParcel.get_event()).get_id(), mParcel.get_message());
			DAL.Instance().insertMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		break;
	case UpdateAttendance:
		PushEvent.Attendance att = gson.fromJson(env.getMessage(), PushEvent.Attendance.class);
		try {
			Contact contact = DAL.Instance().readContact(att.getContact());
			contact.set_attending(att.getAtt());
			Event event = DAL.Instance().readEvent(att.getEvent());
			DAL.Instance().updateParticipant(contact, event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		break;
	}
}

@Override
public void onReceive(Context context, Intent intent) {
	handlePushNotifications(intent);
	Intent msg = new Intent();
	msg.setAction("il.ac.huji.app4beer.UPDATE_EVENT");
	context.sendBroadcast(msg);
  }
}