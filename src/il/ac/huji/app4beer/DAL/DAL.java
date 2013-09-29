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

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
		return displayname != null;
	}
	
	public void SaveCredentials(String phonenumber, String displayname) {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.putString("phonenumber", phonenumber);
		editor.putString("displayname", displayname);
		editor.commit();
	}

	public List<Event> Events() {
		List<Event> events = new ArrayList<Event>();
		 Cursor cursor = allEventsCursor();
		 if (cursor.moveToFirst()) {
			 do {
			    String name = cursor.getString(0);
			    String description= cursor.getString(1);
			    Date date = cursor.getLong(2)==0 ? null : new Date(cursor.getLong(2));
			    events.add(new Event(name, description, date));
			 } while (cursor.moveToNext());
		 }
		 return events;  
	 }
	 
	 public Cursor allEventsCursor() {
		 return _db.query("events", new String[] { "name", "description", "date" }, null, null, null, null, null);
	 }
	
	public boolean insertEvent(Event event) {
	    try {
	    	ContentValues content = createEventRow(event);
		    long status = _db.insert("events", null, content) ;
		    
/*		    ParseObject testObject = new ParseObject("todo");
		    testObject.put("title", todoItem.getTitle());
		    if (todoItem.getDueDate()!=null) {
		    	testObject.put("due", todoItem.getDueDate().getTime());
		    }
		    testObject.saveInBackground();
	*/	     
			return status != -1;
	    } catch (Exception e){
	    	return false;
	    }
	}

	private ContentValues createEventRow(Event event) {
		ContentValues content = new ContentValues();
		content.put("name", event.get_title());
		content.put("description", event.get_description());
    	content.put("date", event.get_date().getTime());
		return content;
	}

	public List<Group> Groups() {
		List<Group> groups = new ArrayList<Group>();
		 Cursor cursor = _db.query("groups", new String[] { "name" }, null, null, null, null, null);
		 if (cursor.moveToFirst()) {
			 do {
			    String name = cursor.getString(0);
			    groups.add(new Group(name));
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
		 Cursor cursor = _db.query("contacts", new String[] { "name", "phone" }, null, null, null, null, null);
		 if (cursor.moveToFirst()) {
			 do {
				    String name = cursor.getString(0);
				    String phone = cursor.getString(1);
				    contacts.add(new Contact(name, phone));
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