package il.ac.huji.app4beer;

import il.ac.huji.app4beer.DAL.DAL;

import java.util.Date;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class Dashboard extends Activity {

	private static final int SignIn=42;
	private DAL _dal; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		_dal = new DAL(this);
		
		SignUpOrSignIn();
	}

	private void SignUpOrSignIn() {
		if (!_dal.IsSignedIn()) {
	        Intent myIntent = new Intent(this, SignUpOrSignInActivity.class);
	        startActivityForResult(myIntent, SignIn);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dash, menu);
		return true;
	}
	
    @Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		  switch (reqCode) {
		  case SignIn:
			  if (resCode!=RESULT_OK) {
				  SignUpOrSignIn();
			  }
			  break;
		  }
		}


}
