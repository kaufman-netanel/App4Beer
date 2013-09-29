package il.ac.huji.app4beer;

import java.util.List;

import il.ac.huji.app4beer.Adapters.CustomContactsAdapter;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class EditGroupActivity extends Activity {

	private ImageButton _groupDeleteImageButton; 
	private ImageButton _groupOkImageButton;
	private EditText _groupNameEditText;
	private ListView _contactsListView;
	private ListView _membersListView;
	private ArrayAdapter<Contact> _contactsAdapter;
	private List<Contact> _contacts;
	private ArrayAdapter<Contact> _membersAdapter;
	private List<Contact> _members;

	
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
		_contactsListView = (ListView)findViewById(R.id.group_contact_list);
		_membersListView = (ListView)findViewById(R.id.group_members_list);

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
		
		populateContactsList();

	}

	private void populateContactsList() {
		_contacts = DAL.Instance().Contacts();
       	_contactsAdapter =  new CustomContactsAdapter(this, _contacts, false);
       	_contactsListView.setAdapter(_contactsAdapter);
       	_contactsListView.setClickable(true);
       	_contactsListView.setItemsCanFocus(true);
        _contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Contact contact= (Contact)_contactsListView.getItemAtPosition(position);
			_contactsAdapter.remove(contact);
			_membersAdapter.add(contact);
		  }
		});

		_members = DAL.Instance().Contacts();
		_membersAdapter =  new CustomContactsAdapter(this, _members, true);
       	_membersListView.setAdapter(_membersAdapter);
       	_membersListView.setClickable(true);
       	_membersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Contact contact= (Contact)_membersListView.getItemAtPosition(position);
			_contactsAdapter.add(contact);
			_membersAdapter.remove(contact);
		  }
		});
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_group, menu);
		return true;
	}

}
