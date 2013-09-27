package il.ac.huji.app4beer.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLiteHelper extends SQLiteOpenHelper {

	public SqlLiteHelper(Context context) {
	    super(context, "app4beer_db", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		    db.execSQL("create table events ( " +
		  	      "_id integer primary key autoincrement, " +
		  	      "date integer, " +
		  	      "name string, " +
		  	      "description string);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
