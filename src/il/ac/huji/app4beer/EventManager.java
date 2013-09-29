package il.ac.huji.app4beer;

import il.ac.huji.app4beer.DAL.Event;
import il.ac.huji.app4beer.DAL.DAL;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class EventManager extends Activity {

	private Event _event;
	
	private TextView _eventTitleTextView;
	private TextView _eventDescriptionTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_manager);
		int id = getIntent().getExtras().getInt("event");
		_event = DAL.Instance().readEvent(id);
		
		_eventTitleTextView = (TextView)findViewById(R.id.EventManager_title);
		_eventDescriptionTextView = (TextView)findViewById(R.id.EventManager_Desc);
		
		_eventTitleTextView.setText(_event.get_title());
		_eventDescriptionTextView.setText(_event.get_description());
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_manager, menu);
		return true;
	}

}
