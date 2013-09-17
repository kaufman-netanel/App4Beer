package il.ac.huji.app4beer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChooseTimeOfActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_time_of);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_time_of, menu);
		return true;
	}

}
