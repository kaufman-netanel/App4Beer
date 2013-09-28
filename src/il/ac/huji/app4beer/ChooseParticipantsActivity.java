package il.ac.huji.app4beer;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class ChooseParticipantsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_participants);
		Boolean groupsonly = getIntent().getExtras().getBoolean("groupsonly", false);
		if (groupsonly) {
			View title = (View)findViewById(R.id.contactsTitle);
			title.setVisibility(View.GONE);
			View list = (View)findViewById(R.id.choose_contact_list);
			list.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_participants, menu);
		return true;
	}

}
