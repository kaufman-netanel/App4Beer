package il.ac.huji.app4beer;

import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.MainActivity.myAsyncTask;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

public class App4Beer extends Application {
	
	class myAsyncTask extends AsyncTask<Void, Void, Void> {
        Context _context;
        myAsyncTask(Context context)    {
             _context=context;           
        }
		@Override
		protected Void doInBackground(Void... params) {
	        DAL.Init(_context);
			return null;
		}   
    } 

	@Override
    public void onCreate() {
        super.onCreate();
		myAsyncTask myWebFetch = new myAsyncTask(this);
        myWebFetch.execute();
	}
}
