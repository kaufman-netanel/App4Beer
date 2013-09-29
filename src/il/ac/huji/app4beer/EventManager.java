package il.ac.huji.app4beer;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
	private TextView _eventLocationTextView;
	private TextView _eventDateTextView;
	private TextView _eventTimeTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_manager);
		int id = getIntent().getExtras().getInt("event");
		_event = DAL.Instance().readEvent(id);
		
		_eventTitleTextView = (TextView)findViewById(R.id.EventManager_title);
		_eventDescriptionTextView = (TextView)findViewById(R.id.EventManager_Desc);
		_eventLocationTextView = (TextView)findViewById(R.id.EventManager_location);
		_eventDateTextView = (TextView)findViewById(R.id.EventManager_date);
		_eventTimeTextView = (TextView)findViewById(R.id.EventManager_time);
		
		_eventTitleTextView.setText(_event.get_title());
		_eventDescriptionTextView.setText(_event.get_description());
		_eventLocationTextView.setText(_event.get_location());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(_event.get_date());
		_eventDateTextView.setText(String.format("%02d / %02d / %04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));
		_eventTimeTextView.setText(String.format("%02d : %02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_manager, menu);
		return true;
	}

}
