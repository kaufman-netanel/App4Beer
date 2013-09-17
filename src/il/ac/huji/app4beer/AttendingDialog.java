package il.ac.huji.app4beer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AttendingDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attending_dialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attending_dialog, menu);
		return true;
	}

}
