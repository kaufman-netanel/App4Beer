package il.ac.huji.app4beer;

import java.util.List;

import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Event;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Dashboard extends Activity {

	private static final int SignIn=42;
	private static final int CreateEvent=43;
	private ArrayAdapter<Event> _adapter;
	private List<Event> _events;
	private ListView _eventsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		DAL.Init(this);
		
		SignUpOrSignIn();
		
		Button createEventButton = 
        		(Button)findViewById(R.id.create_event_btn);
		createEventButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    	        Intent myIntent = new Intent(Dashboard.this, CreateEventActivity.class);
    	        Dashboard.this.startActivityForResult(myIntent, CreateEvent);
            }
        });	 
		
		_events = DAL.Instance().Events();
        _eventsListView = 
        		(ListView)findViewById(R.id.events_group_list);
        _adapter =  new CustomEventAdapter(this, _events);
        _eventsListView.setAdapter(_adapter);

	}

	private void SignUpOrSignIn() {
		if (!DAL.Instance().IsSignedIn()) {
	        Intent myIntent = new Intent(this, SignUpOrSignInActivity.class);
	        startActivityForResult(myIntent, SignIn);
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
		  case SignIn:
			  if (resCode!=RESULT_OK) {
				  SignUpOrSignIn();
			  }
			  break;
	  case CreateEvent:
		  if (resCode==RESULT_OK) {
			  //Event event = (Event) data.getSerializableExtra("event");
			  //DAL.Instance().insertEvent(event);
		      //_adapter.add(event);
				_events = DAL.Instance().Events();
		        _adapter =  new CustomEventAdapter(this, _events);
		        _eventsListView.setAdapter(_adapter);
		  }
		  break;
		  }
	  }
	}
