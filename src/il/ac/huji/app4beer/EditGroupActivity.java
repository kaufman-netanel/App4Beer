package il.ac.huji.app4beer;

import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Group;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditGroupActivity extends Activity {

	ImageButton _groupDeleteImageButton; 
	ImageButton _groupOkImageButton;
	EditText _groupNameEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_group);
		
		Bundle extras = getIntent().getExtras();
		String groupname = extras == null ? null : (String) extras.getString("name");
		final Boolean newgroup = groupname == null;

		_groupDeleteImageButton = (ImageButton)findViewById(R.id.group_delete_btn);
		_groupOkImageButton = (ImageButton)findViewById(R.id.group_ok);
		_groupNameEditText = (EditText)findViewById(R.id.single_group_name);

		_groupDeleteImageButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	    			if(_groupNameEditText.getText().length() == 0 ) {
	  			      return;
	    			}
	          	DAL.Instance().removeGroup(_groupNameEditText.getText().toString());
	    		setResult(RESULT_OK);
	    		finish();
            }
        });	 

		_groupOkImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    			if(_groupNameEditText.getText().length() == 0 ) {
    			      return;
      			}
    			if (newgroup) {
    				DAL.Instance().insertGroup(new Group(_groupNameEditText.getText().toString()));
    			} else {
    				// TODO update
    			}
            	// TODO duplicate names?
	    		setResult(RESULT_OK);
	    		finish();
            }
        });	 

		if (newgroup) {
			_groupDeleteImageButton.setVisibility(View.INVISIBLE);
		} else {
			_groupNameEditText.setText(groupname);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_group, menu);
		return true;
	}

}
