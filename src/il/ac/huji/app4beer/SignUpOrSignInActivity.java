package il.ac.huji.app4beer;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SignUpOrSignInActivity extends Activity {

	private Button _signInButton;
   	private Button _signUpButton;
   	private TextView _errmsg;

   	private final class SignAction implements OnClickListener {
    	
    	private Boolean _signin;
       	
    	public SignAction(Boolean signin) {
    		_signin = signin;
    	}
    	
    	private void onDone(ParseUser user, ParseException e, String what) {
 			
  			if (user != null || e == null) {
  				_errmsg.setText(what);
	    		  setResult(RESULT_OK);
	    		  finish();
			} else {
				switch(e.getCode()){
				case ParseException.USERNAME_TAKEN:
					_errmsg.setText("Sorry, this username has already been taken.");
					break;
				case ParseException.USERNAME_MISSING:
					_errmsg.setText("Sorry, you must supply a username to register.");
					break;
				case ParseException.PASSWORD_MISSING:
					_errmsg.setText("Sorry, you must supply a password to register.");
					break;
				case ParseException.OBJECT_NOT_FOUND:
					_errmsg.setText("Sorry, those credentials were invalid.");
					break;
				default:
					_errmsg.setText(e.getLocalizedMessage());
					break;
				}
				_signInButton.setEnabled(true);
				_signUpButton.setEnabled(true);
			}

    	}
    	
		public void onClick(View v) {
			
			EditText phonenumber = (EditText)findViewById(R.id.phonenumber);
			EditText displayname = (EditText)findViewById(R.id.displayname);
			if(phonenumber.getText().length() == 0 || displayname.getText().length() == 0) {
			      return;
			}

			v.setEnabled(false);
			ParseUser user = new ParseUser();
			user.setUsername(displayname.getText().toString());
			user.setPassword(phonenumber.getText().toString());
			
			_errmsg.setText("Wait for it...");
			if (_signin) {
				ParseUser.logInInBackground(displayname.getText().toString(), 
						phonenumber.getText().toString(), 
						new LogInCallback() {
					@Override
					public void done(ParseUser user, ParseException e) {
						onDone(user, e, "LOGGED");
					}
				});
			} else {
				user.signUpInBackground(new SignUpCallback(){
					@Override
					public void done(ParseException e) {
						onDone(null, e, "SIGNED UP");
					}
				});
			}
		}
	}


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		_signInButton = (Button)findViewById(R.id.signin);
		_signInButton.setOnClickListener(new SignAction(true));	 

		_signUpButton = (Button)findViewById(R.id.signup);
		_signUpButton.setOnClickListener(new SignAction(false));	 
    
		_errmsg = (TextView)findViewById(R.id.errmsg);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
   
}
