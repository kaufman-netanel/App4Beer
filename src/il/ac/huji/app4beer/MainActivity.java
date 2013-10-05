package il.ac.huji.app4beer;

import java.util.Date;

import il.ac.huji.app4beer.DAL.DAL;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final int SignIn = 42;
	private static final long Delay = 0;
	private long _uptimeMillis;

	class myAsyncTask extends AsyncTask<Void, Void, Void> {
        Context _context;
        Boolean _moveOn;
        myAsyncTask(Context context)    {
             _context=context;           
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (_moveOn) moveOn();
        }
		@Override
		protected Void doInBackground(Void... params) {
       	 	_moveOn = SignUpOrSignIn();
			return null;
		}   
    } 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_uptimeMillis = SystemClock.uptimeMillis();
		myAsyncTask myWebFetch = new myAsyncTask(this.getParent());
        myWebFetch.execute();
	}

	private Boolean SignUpOrSignIn() {
		if (!DAL.Instance().IsSignedIn()) {
	        Intent myIntent = new Intent(MainActivity.this, SignUpOrSignInActivity.class);
	        MainActivity.this.startActivityForResult(myIntent, SignIn);
	        return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
    	switch (reqCode) {
    		case SignIn:
   				if (resCode!=RESULT_OK) {
   					SignUpOrSignIn();
   				}
   				moveOn();
   				break;
    	}
	}

	private void moveOn() {
		long delay = Delay-(SystemClock.uptimeMillis()-_uptimeMillis);
		Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() { 
	     	    Intent myIntent = new Intent(MainActivity.this, Dashboard.class);
	            startActivity(myIntent);
	            finish();
	         } 
	    }, delay>0 ? delay : 1); 
	}
	
}
