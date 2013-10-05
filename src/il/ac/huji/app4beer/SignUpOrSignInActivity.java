package il.ac.huji.app4beer;

import il.ac.huji.app4beer.DAL.DAL;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SignUpOrSignInActivity extends Activity {

	private Button _signInButton;
   	private Button _signUpButton;
   	private TextView _errmsg;

   	private final class SignAction implements OnClickListener {
    	
    	private Boolean _signin;
		private EditText _phonenumber;
		private EditText _displayname;

    	public SignAction(Boolean signin) {
    		_signin = signin;
    	}
    	
    	private void onDone(ParseUser user, ParseException e, String what) {
 			
  			if (user != null || e == null) {
  				  _errmsg.setText(what);
  				  DAL.Instance().SaveCredentials(_phonenumber.getText().toString(), _displayname.getText().toString());
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
			
			_phonenumber = (EditText)findViewById(R.id.phonenumber);
			_displayname = (EditText)findViewById(R.id.displayname);
			if(_phonenumber.getText().length() == 0 || _displayname.getText().length() == 0) {
			      return;
			}

			v.setEnabled(false);
			ParseUser user = new ParseUser();
			user.setUsername(_displayname.getText().toString());
			user.setPassword(_phonenumber.getText().toString());
			
			_errmsg.setText("Wait for it...");
			if (_signin) {
				ParseUser.logInInBackground(_displayname.getText().toString(), 
						_phonenumber.getText().toString(), 
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
						if (e==null) {
							ParseInstallation installation = ParseInstallation.getCurrentInstallation();
							installation.put("username", _displayname.getText().toString());
							installation.saveInBackground(new SaveCallback() {
								@Override
								public void done(ParseException e) {
									onDone(null, e, "SIGNED UP");
								}
							});
						}
					}
				});
			}
		}
	}


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
		
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
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
   
}
