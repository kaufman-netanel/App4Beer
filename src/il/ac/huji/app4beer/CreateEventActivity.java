package il.ac.huji.app4beer;

import java.util.Date;

import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Event;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateEventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
	
		ImageButton createEventButton = 
        		(ImageButton)findViewById(R.id.sendBtn);
		createEventButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	EditText eventName = (EditText)findViewById(R.id.eventName);
            	EditText eventDescription = (EditText)findViewById(R.id.eventDescription);
    			if(eventName.getText().length() == 0 || eventDescription.getText().length() == 0) {
  			      return;
    			}
    			DAL.Instance().insertEvent(new Event(eventName.getText().toString(),
						eventDescription.getText().toString(), 
						new Date(2013, 6, 16)));
	    		  setResult(RESULT_OK);
	    		  finish();
            }
        });	 
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}

}
