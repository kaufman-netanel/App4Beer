package il.ac.huji.app4beer;

import java.util.List;

import com.google.gson.Gson;

import il.ac.huji.app4beer.Adapters.CustomEventAdapter;
import il.ac.huji.app4beer.DAL.Contact;
import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Event;
import il.ac.huji.app4beer.DAL.Message;
import il.ac.huji.app4beer.DAL.ParseProxy;
import il.ac.huji.app4beer.DAL.ParseProxy.PushEnvelope;
import il.ac.huji.app4beer.DAL.PushEvent;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Dashboard extends Activity {

	private static final int ManageEvent=42;
	private static final int CreateEvent=43;
	private ArrayAdapter<Event> _adapter;
	private List<Event> _events;
	private ListView _eventsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		handlePushNotifications(getIntent());

		Button createEventButton = (Button)findViewById(R.id.create_event_btn);
		createEventButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Dashboard.this, CreateEventActivity.class);
				Dashboard.this.startActivityForResult(myIntent, CreateEvent);
			}
		});	 

		_eventsListView = (ListView)findViewById(R.id.group_members_list);
		loadEvents();
		_eventsListView.setClickable(true);
		_eventsListView.setItemsCanFocus(true);
		_eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Event event = (Event)_eventsListView.getItemAtPosition(position);
				opemEventManager(event);
			}
		});

	}

	private void opemEventManager(Event event) {
		Intent myIntent = new Intent(Dashboard.this, EventManager.class);
		myIntent.putExtra("event", event.get_id());
		Dashboard.this.startActivityForResult(myIntent, ManageEvent);
	}

	private void handlePushNotifications(Intent intent) {
		PushEnvelope env = ParseProxy.getTheEvelope(intent);
		if (env == null) return;
		Gson gson = new Gson();
		switch (env.getType()) {
		case NewEvent:
			loadEvents();
			break;
		case Tweet:
			PushEvent.MessageParcel mParcel = gson.fromJson(env.getMessage(), PushEvent.MessageParcel.class);
			try {
				Event event = DAL.Instance().readEvent(mParcel.get_event());
				opemEventManager(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case UpdateAttendance:
			PushEvent.Attendance att = gson.fromJson(env.getMessage(), PushEvent.Attendance.class);
			try {
				Event event = DAL.Instance().readEvent(att.getEvent());
				opemEventManager(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.manage_groups:
			Intent intent = new Intent(this, ChooseParticipantsActivity.class);
			intent.putExtra("groupsonly", true);
			startActivity(intent);
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		switch (reqCode) {
		case ManageEvent:
		case CreateEvent:
			if (resCode==RESULT_OK) {
				loadEvents();
			}
			break;
		}
	}

	private void loadEvents() {
		_events = DAL.Instance().Events();
		_adapter =  new CustomEventAdapter(this, _events);
		_eventsListView.setAdapter(_adapter);
	}

	private BroadcastReceiver _updateReceiver;

	@Override
	protected void onResume() {
		super.onResume();

		_updateReceiver=new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("il.ac.huji.app4beer.UPDATE_EVENT")) {
					loadEvents();
				}
			}
		};
		IntentFilter updateIntentFilter=new IntentFilter("il.ac.huji.app4beer.UPDATE_EVENT");
		registerReceiver(_updateReceiver, updateIntentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (this._updateReceiver!=null)
			unregisterReceiver(_updateReceiver);
	}
}
