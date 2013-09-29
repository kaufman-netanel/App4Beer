package il.ac.huji.app4beer;

import il.ac.huji.app4beer.Adapters.CustomContactsAdapter;
import il.ac.huji.app4beer.DAL.Contact;
import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Group;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ViewGroupActivity extends Activity {

	private static final int EditGroup = 43;

	private ImageButton _groupEditImageButton;
	private TextView _groupNameTextView;
	private ListView _membersListView;
	private ArrayAdapter<Contact> _membersAdapter;
	private List<Contact> _members;
	private Group _group;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_group);
		
		Bundle extras = getIntent().getExtras();
		_group = new Group((String) extras.getString("name"), extras.getInt("id", -1));
		_groupNameTextView = (TextView)findViewById(R.id.single_group_name);
		_membersListView = (ListView)findViewById(R.id.group_members_list);
		_groupEditImageButton = (ImageButton)findViewById(R.id.group_edit_btn);

		populateContactsList();

		_groupEditImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Activity a = ViewGroupActivity.this.getParent();
    		    Intent myIntent = new Intent(ViewGroupActivity.this, EditGroupActivity.class);
    		    myIntent.putExtra("name", _group.get_name());
    		    myIntent.putExtra("id", _group.get_id());
    		    ViewGroupActivity.this.startActivityForResult(myIntent, EditGroup);
            }
        });	 

		_groupNameTextView.setText(_group.get_name());

	}

	private void populateContactsList() {
		_members = DAL.Instance().Members(_group);
		_membersAdapter =  new CustomContactsAdapter(this, _members, false, false);
       	_membersListView.setAdapter(_membersAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_group, menu);
		return true;
	}
	
    @Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
    	switch (reqCode) {
    		case EditGroup:
			  if (resCode==RESULT_OK) {
				  populateContactsList();	
			  }
			  break;
    	}
	}



}
