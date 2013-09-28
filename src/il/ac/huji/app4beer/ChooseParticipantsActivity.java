package il.ac.huji.app4beer;

import java.util.List;

import il.ac.huji.app4beer.Adapters.CustomGroupAdapter;
import il.ac.huji.app4beer.DAL.Contact;
import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Group;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseParticipantsActivity extends Activity {

	private static final int CreateGroup = 42;
	
	private TextView _noGroupsTextView;
	private TextView _contactsTextView;
	private ListView _contactsListView;
	private ListView _groupsListView;
	private Button _createNewGroupButton;
	private ArrayAdapter<Group> _groupsAdapter;
	private List<Group> _groups;
	private ArrayAdapter<Contact> _contactsAdapter;
	private List<Contact> _contacts;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_participants);

		_contactsTextView= (TextView)findViewById(R.id.contactsTitle);
		_noGroupsTextView= (TextView)findViewById(R.id.no_groups_text_view);
		_contactsListView = (ListView)findViewById(R.id.choose_contact_list);
		_groupsListView = (ListView)findViewById(R.id.events_group_list);
		_createNewGroupButton = (Button)findViewById(R.id.createNewGroupBtn);
		
		Boolean groupsonly = getIntent().getExtras().getBoolean("groupsonly", false);
		if (groupsonly) {
			_contactsTextView.setVisibility(View.GONE);
			_contactsListView.setVisibility(View.GONE);
		}
		
		_createNewGroupButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    	        Intent myIntent = new Intent(ChooseParticipantsActivity.this, EditGroupActivity.class);
    	        startActivityForResult(myIntent, CreateGroup);
            }
        });	 
		
		_groups = DAL.Instance().Groups();
        
        if (_groups.size()==0) {
        	_groupsListView.setVisibility(View.GONE);
        	_noGroupsTextView.setVisibility(View.VISIBLE);
        } else {
            _groupsAdapter =  new CustomGroupAdapter(this, _groups);
            _groupsListView.setAdapter(_groupsAdapter);
        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_participants, menu);
		return true;
	}

}
