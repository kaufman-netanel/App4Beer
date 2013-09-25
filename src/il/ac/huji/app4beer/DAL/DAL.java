package il.ac.huji.app4beer.DAL;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
//import android.database.sqlite.SQLiteDatabase;

import com.parse.Parse;

public class DAL {
	
	private static final String ParseApplication = "WK71gB5ZUn0MvUrePNzxVNStnKZBMfUdP6PGwFft";
	private static final String ClientKey = "eWxzNLLcg80vLUrxo8h3mWdtHUFFirqPJy7ljaLf";

	SqlLiteHelper _sqlLiteHelper;
	//private SQLiteDatabase _db;
	private SharedPreferences _preferences;

	public DAL(Context context) 
	{  
		_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    //_sqlLiteHelper = new SqlLiteHelper(context);
	    //_db = _sqlLiteHelper.getWritableDatabase();
	    Parse.initialize(context, ParseApplication, ClientKey);
	}
	
	public Boolean IsSignedIn() {
		String displayname = _preferences.getString("displayname", null);
		return displayname != null;

	}
	
	/*public boolean insert(ITodoItem todoItem) {
	    try {
	    	ContentValues task = createRow(todoItem);
		    Boolean status = _db.insert("tasks", null, task) != -1;
		    
		    ParseObject testObject = new ParseObject("todo");
		    testObject.put("title", todoItem.getTitle());
		    if (todoItem.getDueDate()!=null) {
		    	testObject.put("due", todoItem.getDueDate().getTime());
		    }
		    testObject.saveInBackground();
		    
			return status;
	    } catch (Exception e){
	    	return false;
	    }
	}

	private ContentValues createRow(ITodoItem todoItem) {
		ContentValues task = new ContentValues();
	    task.put("title", todoItem.getTitle());
	    if (todoItem.getDueDate()!=null) {
	    	task.put("due", todoItem.getDueDate().getTime());
	    }
		return task;
	}
	
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
	 
	 public List<ITodoItem> all() {
		 List<ITodoItem> tasks = new ArrayList<ITodoItem>();
		 Cursor cursor = allCursor();
		 if (cursor.moveToFirst()) {
			 do {
			    String title = cursor.getString(0);
			    long t = cursor.getLong(1);
			    Date due = cursor.getLong(1)==0 ? null : new Date(cursor.getLong(1));
			    tasks.add(new Todo(title, due));
			 } while (cursor.moveToNext());
		 }
		 return tasks;  
	 }
	 
	 public Cursor allCursor() {
		 return _db.query("tasks", new String[] { "title", "due" }, null, null, null, null, null);
	 }
*/	 
}