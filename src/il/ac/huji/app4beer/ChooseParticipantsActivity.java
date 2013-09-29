package il.ac.huji.app4beer;

import java.util.List;

import il.ac.huji.app4beer.Adapters.CustomContactsAdapter;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseParticipantsActivity extends Activity {

	public static final int CreateGroup = 42;
	public static final int EditGroup = 43;
	
	private TextView _noGroupsTextView;
	private TextView _contactsTextView;
	private ListView _contactsListView;
	private ListView _groupsListView;
	private Button _createNewGroupButton;
	private ArrayAdapter<Group> _groupsAdapter;
	private List<Group> _groups;
	private ArrayAdapter<Contact> _contactsAdapter;
	private List<Contact> _contacts;
	
	private Boolean _groupsonly; 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_participants);

		_contactsTextView= (TextView)findViewById(R.id.contactsTitle);
		_noGroupsTextView= (TextView)findViewById(R.id.no_groups_text_view);
		_contactsListView = (ListView)findViewById(R.id.group_contact_list);
		_groupsListView = (ListView)findViewById(R.id.group_members_list);
		_createNewGroupButton = (Button)findViewById(R.id.createNewGroupBtn);
		
		Bundle extras = getIntent().getExtras();
		_groupsonly = extras == null ? false : extras.getBoolean("groupsonly", false);
		if (_groupsonly) {
			_contactsTextView.setVisibility(View.GONE);
			_contactsListView.setVisibility(View.GONE);
		} else {
			populateContactsList();
		}
		
		_createNewGroupButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
    	        Intent myIntent = new Intent(ChooseParticipantsActivity.this, EditGroupActivity.class);
    	        startActivityForResult(myIntent, CreateGroup);
            }
        });	 
		
		_groupsListView.setClickable(true);
		_groupsListView.setItemsCanFocus(true);
		_groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		  @Override
		  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

			Group group = (Group)_groupsListView.getItemAtPosition(position);
		    Intent myIntent = new Intent(ChooseParticipantsActivity.this, 
		    		_groupsonly ? EditGroupActivity.class : ViewGroupActivity.class);
		    myIntent.putExtra("name", group.get_name());
		    myIntent.putExtra("id", group.get_id());
		    ChooseParticipantsActivity.this.startActivityForResult(myIntent, ChooseParticipantsActivity.EditGroup);
		  }
		});

		populateGroupsList();

	}

	private void populateGroupsList() {
		_groups = DAL.Instance().Groups();
        if (_groups.size()==0) {
        	_groupsListView.setVisibility(View.GONE);
        	_noGroupsTextView.setVisibility(View.VISIBLE);
        } else {
            _groupsAdapter =  new CustomGroupAdapter(this, _groups, !_groupsonly);
            _groupsListView.setAdapter(_groupsAdapter);
        }
	}

	private void populateContactsList() {
		_contacts = DAL.Instance().Contacts();
        if (_contacts.size()==0) {
        	_contactsListView.setVisibility(View.GONE);
        } else {
        	_contactsAdapter =  new CustomContactsAdapter(this, _contacts, false, true);
        	_contactsListView.setAdapter(_contactsAdapter);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_participants, menu);
		return true;
	}

    @Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
    	switch (reqCode) {
    		case CreateGroup:
    		case EditGroup:
			  if (resCode==RESULT_OK) {
				  populateGroupsList();
			  }
			  break;
    	}
	}

}
