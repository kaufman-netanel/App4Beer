package il.ac.huji.app4beer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class SignUpOrSignInActivity extends Activity {

    private final class SignAction implements OnClickListener {
    	private Boolean _signin;
    	
    	public SignAction(Boolean signin) {
    		_signin = signin;
    	}
    	
		public void onClick(View v) {
			Intent result = new Intent();
			EditText phonenumber = (EditText)findViewById(R.id.phonenumber);
			result.putExtra("phonenumber", phonenumber.getText().toString());
			EditText displayname = (EditText)findViewById(R.id.displayname);
			result.putExtra("displayname", displayname.getText().toString());
			result.putExtra("signin", _signin);
			setResult(_signin ? RESULT_OK : RESULT_CANCELED, result);
			finish();
		}
	}


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		Button signInButton = (Button)findViewById(R.id.signin);
		signInButton.setOnClickListener(new SignAction(true));	 

		Button signUpButton = (Button)findViewById(R.id.signup);
		signUpButton.setOnClickListener(new SignAction(false));	 
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
   
}
