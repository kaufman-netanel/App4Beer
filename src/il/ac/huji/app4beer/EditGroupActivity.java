package il.ac.huji.app4beer;

import java.util.Iterator;
import java.util.List;

import il.ac.huji.app4beer.Adapters.CustomContactsAdapter;
import il.ac.huji.app4beer.DAL.Contact;
import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Group;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
	private Group _group;
	private Boolean _newgroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_group);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			_group = new Group((String) extras.getString("name"),extras.getInt("id", -1));
		}
		_newgroup = _group == null;

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
    			if (_newgroup) {
    				try {
    					_group = new Group(_groupNameEditText.getText().toString(), -1);
						int id = (int) DAL.Instance().insertGroup(_group);
						_group.set_id(id);
						Iterator<Contact> i = _members.iterator();
						while (i.hasNext()) {
							Contact contact = i.next();
							DAL.Instance().insertMember(contact, _group);
						}
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
    			} else {
    				// TODO update
    			}
            	// TODO duplicate names?
	    		setResult(RESULT_OK);
	    		finish();
            }
        });	 

		if (_newgroup) {
			_groupDeleteImageButton.setVisibility(View.INVISIBLE);
		} else {
			_groupNameEditText.setText(_group.get_name());
		}
		
		populateContactsList();

	}

	private void populateContactsList() {
		_members = DAL.Instance().Members(_group);
		_contacts = DAL.Instance().Contacts();
		
		removeMembersFromContacts();

		_contactsAdapter =  new CustomContactsAdapter(this, _contacts, false, true);
       	_contactsListView.setAdapter(_contactsAdapter);
       	_contactsListView.setClickable(true);
       	_contactsListView.setItemsCanFocus(true);
        _contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Contact contact= (Contact)_contactsListView.getItemAtPosition(position);
			contact.set_selected(true);
			try {
				if (!_newgroup) DAL.Instance().insertMember(contact, _group);
				_membersAdapter.add(contact);
				_contactsAdapter.remove(contact);
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		});

		_membersAdapter =  new CustomContactsAdapter(this, _members, false, true);
       	_membersListView.setAdapter(_membersAdapter);
       	_membersListView.setClickable(true);
       	_membersListView.setItemsCanFocus(true);
       	_membersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Contact contact= (Contact)_membersListView.getItemAtPosition(position);
			contact.set_selected(false);
			_contactsAdapter.add(contact);
			_membersAdapter.remove(contact);
			if (!_newgroup) DAL.Instance().removeMember(contact, _group);
		  }
		});
}

	private void removeMembersFromContacts() {
		for (int i=0;i<_members.size();i++) {
			for (int j=0;j<_contacts.size();j++) {
				if (_contacts.get(j).get_id()==_members.get(i).get_id()) {
					_contacts.remove(j);
					break;
				}
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_group, menu);
		return true;
	}

}
