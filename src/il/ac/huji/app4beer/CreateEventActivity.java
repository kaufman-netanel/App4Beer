package il.ac.huji.app4beer;

import java.util.ArrayList;
import java.util.Date;

import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Event;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateEventActivity extends Activity {

	private static final int ChooseParticipants=42;

	private ArrayList<Integer> _contacts;
	private ArrayList<Integer> _groups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
	
		initCreateEventButton();	 
		initAddParticipantsButton();

	}

	private void initCreateEventButton() {
		ImageButton createEventButton = 
        		(ImageButton)findViewById(R.id.sendBtn);
		createEventButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	EditText eventName = (EditText)findViewById(R.id.eventName);
            	EditText eventDescription = (EditText)findViewById(R.id.eventDescription);
    			if(eventName.getText().length() == 0 || eventDescription.getText().length() == 0) {
  			      return;
    			}
    			try {
	    			DAL.Instance().insertEvent(new Event(eventName.getText().toString(),
							eventDescription.getText().toString(), 
							new Date(2013, 6, 16), _contacts, _groups));
		    		setResult(RESULT_OK);
		    		finish();
    			} catch (Exception e) { 				
    			}
            }
        });
	}

	private void initAddParticipantsButton() {
		Button button = 
        		(Button)findViewById(R.id.eventAddParticipantsBtn);
		button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    	        Intent myIntent = new Intent(CreateEventActivity.this, ChooseParticipantsActivity.class);
    	        myIntent.putIntegerArrayListExtra("contacts", _contacts);
    	        myIntent.putIntegerArrayListExtra("groups", _groups);
  	        startActivityForResult(myIntent, ChooseParticipants);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}
	
    @Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		  switch (reqCode) {
		  case ChooseParticipants:
			  if (resCode==RESULT_OK) {
				  _contacts = data.getIntegerArrayListExtra("contacts");
				  _groups = data.getIntegerArrayListExtra("groups");
				  
			  }
			  break;
		  }
	}


}
