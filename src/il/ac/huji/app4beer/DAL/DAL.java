package il.ac.huji.app4beer.DAL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
//import android.database.sqlite.SQLiteDatabase;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class DAL {
	
	private static final String ParseApplication = "WK71gB5ZUn0MvUrePNzxVNStnKZBMfUdP6PGwFft";
	private static final String ClientKey = "eWxzNLLcg80vLUrxo8h3mWdtHUFFirqPJy7ljaLf";

	SqlLiteHelper _sqlLiteHelper;
	private SQLiteDatabase _db;
	private SharedPreferences _preferences;

	private static DAL _instance = null;
	
	public static void Init(Context context) {
		_instance = new DAL(context);
	}
	
	public static DAL Instance() {
		return _instance;
	}
	
	private DAL(Context context) 
	{  
		_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    _sqlLiteHelper = new SqlLiteHelper(context);
	    _db = _sqlLiteHelper.getWritableDatabase();
	    Parse.initialize(context, ParseApplication, ClientKey);
	}
	
	public Boolean IsSignedIn() {
		String displayname = _preferences.getString("displayname", null);
		if (displayname == null) return false;
		String phonenumber = _preferences.getString("phonenumber", null);
		try {
			ParseUser user = ParseUser.logIn(displayname, phonenumber);
			return user != null;
		} catch (ParseException e) {
			return false; 
		} 
	}
	
	public void SaveCredentials(String phonenumber, String displayname) {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.putString("phonenumber", phonenumber);
		editor.putString("displayname", displayname);
		editor.commit();
	}
	public Event readEvent(int id) {
		List<Event> events = Events("_id='"+id+"'");
		return events.size()==0?null:events.get(0);
	}

	public List<Event> Events() {
		return Events(null);
	}
		
	private List<Event> Events(String selection) {
		List<Event> events = new ArrayList<Event>();
		 Cursor cursor = _db.query("events", new String[] { "name", "description", "date", "_id", "location" }, selection, null, null, null, null);
		 if (cursor.moveToFirst()) {
			 do {
			    String name = cursor.getString(0);
			    String description= cursor.getString(1);
			    Date date = cursor.getLong(2)==0 ? null : new Date(cursor.getLong(2));
			    Integer id = cursor.getInt(3);
			    String location = cursor.getString(4);
			    Event event = new Event(id, name, description, location, date);
			    addEventParticipants(event);
			    addEventGroups(event);
			    events.add(event);
			 } while (cursor.moveToNext());
		 }
		 return events;  
	 }
	 
	private void addEventParticipants(Event event) {
		 Cursor cursor = _db.query("participants", new String[] { "contactid" }, 
				 "eventid = '"+event.get_id()+"'",
				 null, null, null, null);
		 if (cursor.moveToFirst()) {
			 do {
			    int contactid = cursor.getInt(0);
			    event.add_contact(contactid);
			 } while (cursor.moveToNext());
		 }
	}

	private void addEventGroups(Event event) {
		 Cursor cursor = _db.query("eventgroups", new String[] { "groupid" }, 
				 "eventid = '"+event.get_id()+"'",
				 null, null, null, null);
		 if (cursor.moveToFirst()) {
			 do {
			    int id = cursor.getInt(0);
			    event.add_group(id);
			 } while (cursor.moveToNext());
		 }
	}

	public void insertEvent(Event event) throws Exception {
		ContentValues content = new ContentValues();
		content.put("name", event.get_title());
		content.put("description", event.get_description());
		content.put("location", event.get_location());
    	content.put("date", event.get_date().getTime());
	    long status = _db.insert("events", null, content) ;
	    if (status == -1) throw new Exception();
	    
	    List<Integer> groups = event.groups();
	    if (groups!=null) {
		    for (int i=0;i<groups.size();i++) {
				content = new ContentValues();
				content.put("eventid", event.get_id());
				content.put("groupid", groups.get(i));
			    status = _db.insert("eventgroups", null, content) ;
			    if (status == -1) throw new Exception();
		    }
	    }
	    
	    List<Integer> contacts = event.contacts();
	    if (contacts!=null) {
		    	for (int i=0;i<contacts.size();i++) {
				content = new ContentValues();
				content.put("eventid", event.get_id());
				content.put("contactid", contacts.get(i));
			    status = _db.insert("participants", null, content) ;
			    if (status == -1) throw new Exception();
		    }
	    }
/*		    ParseObject testObject = new ParseObject("todo");
		    testObject.put("title", todoItem.getTitle());
		    if (todoItem.getDueDate()!=null) {
		    	testObject.put("due", todoItem.getDueDate().getTime());
		    }
		    testObject.saveInBackground();
	*/	     
	}

	public List<Group> Groups() {
		List<Group> groups = new ArrayList<Group>();
		 Cursor cursor = _db.query("groups", new String[] { "_id", "name" }, null, null, null, null, null);
		 if (cursor.moveToFirst()) {
			 do {
				 int id = cursor.getInt(0);
			    String name = cursor.getString(1);
			    groups.add(new Group(name, id));
			 } while (cursor.moveToNext());
		 }
		 return groups;  
	 }
	 
	public boolean insertGroup(Group group) {
	    try {
			ContentValues content = new ContentValues();
			content.put("name", group.get_name());
		    long status = _db.insert("groups", null, content) ;
			return status != -1;
	    } catch (Exception e){
	    	return false;
	    }
	}

	public boolean removeGroup(String groupName) {
	    try {
	    	// TODO cascade?
		    long status = _db.delete("groups", "name=?",new String[] { groupName });
			return status != 0;
	    } catch (Exception e){
	    	return false;
	    }
	}
	
	public List<Contact> Contacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		 Cursor cursor = _db.query("contacts", new String[] { "name", "phone", "_id" }, null, null, null, null, null);
		 if (cursor.moveToFirst()) {
			 do {
				    String name = cursor.getString(0);
				    String phone = cursor.getString(1);
				    int id = cursor.getInt(2);
				    contacts.add(new Contact(name, phone, id));
			 } while (cursor.moveToNext());
		 }
		return contacts;
	}
	
	public boolean insertContact(Contact contact) {
	    try {
			ContentValues content = new ContentValues();
			content.put("name", contact.get_name());
			content.put("phone", contact.get_phone());
		    long status = _db.insert("contacts", null, content) ;
			return status != -1;
	    } catch (Exception e){
	    	return false;
	    }
	}

	public boolean insertMember(Contact contact, Group group) {
	    try {
			ContentValues content = new ContentValues();
			content.put("contactId", contact.get_id());
			content.put("groupId", group.get_id());
		    long status = _db.insert("members", null, content) ;
			return status != -1;
	    } catch (Exception e){
	    	return false;
	    }
	}

	public boolean removeMember(Contact contact, Group group) {
	    try {
	    	long status = _db.delete("members", "contactId=? AND groupId=?",
	    			new String[] { contact.get_id().toString(), group.get_id().toString()});
			return status != 0;
	    } catch (Exception e){
	    	return false;
	    }
	}

	public List<Contact> Members(Group group) {
		List<Contact> members = new ArrayList<Contact>();
		if (group == null) {
			return members;
		}
		final String QUERY = "SELECT contacts.name, contacts.phone, contacts._id FROM members INNER JOIN contacts ON members.contactId=contacts._id WHERE members.groupId=? ";

		Cursor cursor = _db.rawQuery(QUERY, new String[]{group.get_id().toString()});
		if (cursor.moveToFirst()) {
			 do {
				    String name = cursor.getString(0);
				    String phone = cursor.getString(1);
				    int id = cursor.getInt(2);
				    Contact contact = new Contact(name, phone, id);
				    contact.set_selected(true);
				    members.add(contact);
			 } while (cursor.moveToNext());
		 }
		return members;
	}

	/*
	public boolean update(ITodoItem todoItem) {
		try {
		    ContentValues task = createRow(todoItem);
		    String namePrefix = todoItem.getTitle();
		    Boolean status = _db.update("tasks", task, "title=?",new String[] { namePrefix }) != 0;
		    
		    final ITodoItem todoItemEx = todoItem;
		    GetCallback cb = new GetCallback() {
			      public void done(ParseObject obj, ParseException e) {
				        if (e == null) {
				        		obj.put("title", todoItemEx.getTitle());
				        		long due = todoItemEx.getDueDate() == null ? null :
				        			todoItemEx.getDueDate().getTime();
				        		obj.put("due", due);
				        	    obj.saveInBackground();
				        	}
				        }
				    };
		    
		    ParseQuery query = new ParseQuery("todo");
		    query.whereEqualTo("title", todoItem.getTitle());
		    query.getFirstInBackground(cb);

			return status;
	    } catch (Exception e){
	    	return false;
	    }
	}
	 
	public boolean delete(ITodoItem todoItem) {
		try {
		    String namePrefix = todoItem.getTitle();
		    Boolean status = _db.delete("tasks", "title=?",new String[] { namePrefix }) != 0;
		   
		    final ITodoItem todoItemEx = todoItem;
		    GetCallback cb = new GetCallback() {
			      public void done(ParseObject obj, ParseException e) {
				        if (e == null) {
				        		obj.deleteInBackground();
				        	}
				        }
				    };
		    
		    ParseQuery query = new ParseQuery("todo");
		    query.whereEqualTo("title", todoItem.getTitle());
		    query.getFirstInBackground(cb);

		    return status; 
	    } catch (Exception e){
	    	return false;
	    }
	}
	 
*/	 
}