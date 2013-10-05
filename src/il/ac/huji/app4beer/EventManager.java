package il.ac.huji.app4beer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.parse.ParseUser;

import il.ac.huji.app4beer.Adapters.CustomContactsAdapter;
import il.ac.huji.app4beer.Adapters.CustomEventAdapter;
import il.ac.huji.app4beer.Adapters.CustomGroupAdapter;
import il.ac.huji.app4beer.Adapters.CustomMessagesAdapter;
import il.ac.huji.app4beer.DAL.Contact;
import il.ac.huji.app4beer.DAL.Contact.Attending;
import il.ac.huji.app4beer.DAL.Event;
import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Message;
import il.ac.huji.app4beer.DAL.ParseProxy;
import il.ac.huji.app4beer.DAL.PushEvent;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class EventManager extends Activity {

	private Event _event;
	
	private TextView _eventTitleTextView;
	private TextView _eventDescriptionTextView;
	private TextView _eventLocationTextView;
	private TextView _eventDateTextView;
	private TextView _eventTimeTextView;
	private EditText _chatMessageTextView;
	private Button _attendButton;
	private ImageButton _sendButton;
	private List<Button> _buttons;
	private List<List<Contact>> _contacts; 
	private List<ArrayAdapter<Contact>> _contactsAdapter;
	private List<CustomPopup> _popUps;
	private Boolean _myEvent;
	private AttendCustomPopup _attendCustomPopup;
	private PushEvent _pushEvent;
	private ArrayAdapter<Message> _adapter;
	private List<Message> _messages;
	private ListView _messagesListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_manager);
		int id = getIntent().getExtras().getInt("event");
		_event = DAL.Instance().readEvent(id);
		_pushEvent = new PushEvent(_event);
		 
		_eventTitleTextView = (TextView)findViewById(R.id.EventManager_title);
		_eventDescriptionTextView = (TextView)findViewById(R.id.EventManager_Desc);
		_eventLocationTextView = (TextView)findViewById(R.id.EventManager_location);
		_eventDateTextView = (TextView)findViewById(R.id.EventManager_date);
		_eventTimeTextView = (TextView)findViewById(R.id.EventManager_time);
		_attendButton = (Button)findViewById(R.id.attendBtn);
		_sendButton = (ImageButton)findViewById(R.id.EventManager_chatSend);
		_chatMessageTextView = (EditText)findViewById(R.id.EventManager_chatMsg);
		_messagesListView = (ListView)findViewById(R.id.chat_list_view);
		
		_eventTitleTextView.setText(_event.get_title());
		_eventDescriptionTextView.setText(_event.get_description());
		_eventLocationTextView.setText(_event.get_location());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(_event.get_date());
		_eventDateTextView.setText(String.format("%02d / %02d / %04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));
		_eventTimeTextView.setText(String.format("%02d : %02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)));

		_popUps = new ArrayList<CustomPopup>();
		_contacts = new ArrayList<List<Contact>>();
		_contactsAdapter =  new ArrayList<ArrayAdapter<Contact>>();
		_buttons = new ArrayList<Button>();
		_buttons.add((Button)findViewById(R.id.att_yes));
		_buttons.add((Button)findViewById(R.id.att_no));
		_buttons.add((Button)findViewById(R.id.att_maybe));
		_buttons.add((Button)findViewById(R.id.att_so));
		for (int i=0;i<4;i++) {
			List<Contact> c = DAL.Instance().Participants(_event.get_id(), i);
			_contacts.add(c);
			_contactsAdapter.add(new CustomContactsAdapter(this, c, false, false));
			_popUps.add(new CustomPopup(this, _buttons.get(i), _contactsAdapter.get(i)));
		}
		
		_myEvent = _event.get_owner().get_name().compareTo(ParseUser.getCurrentUser().getUsername())==0;
		if (_myEvent) {
			_attendButton.setVisibility(View.GONE);
		} else {
			_attendCustomPopup = new AttendCustomPopup(this, _attendButton);
		}

		initChat();
		_sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					_pushEvent.SendMessage(_chatMessageTextView.getText().toString());
					_chatMessageTextView.getText().clear();
					initChat();
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		});
		
	}

	private void initChat() {
		_messages = DAL.Instance().Messages(_event.get_id());
		_adapter =  new CustomMessagesAdapter(this, _messages);
		_messagesListView.setAdapter(_adapter);
		_messagesListView.setSelection(_messagesListView.getCount()-1);
	}
	
	@Override
	protected void onNewIntent (Intent intent) {
		initChat();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_manager, menu);
		return true;
	}

	private class CustomPopup {
		private Boolean _click = true;
		private PopupWindow _popup;
		private Button _button;
		protected ArrayAdapter<Contact> _adapter;
		
		public CustomPopup (Context context, Button button, ArrayAdapter<Contact> adapter) {
			_adapter = adapter;
			_button = button;
			_popup = new PopupWindow(context);
			_popup.setTouchable(true);    
			_popup.setFocusable(false);    
			_popup.setOutsideTouchable(true);
			_popup.setTouchInterceptor(new OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
						_popup.dismiss();
						_button.setTextColor(0xffffffff);
						_button.setBackgroundColor(0xff1e90aa);
						return true;
		            }
		            return false;
				}
			});
			_button.setOnClickListener(new OnClickListener() {

				@Override
	            public void onClick(View v) {
	                if (_click) {
	                	_click = false;
                		_popup.showAsDropDown(_button);
                		_popup.update(_button, -(200-_button.getWidth())/2, 0, 200, 200);
                		_button.setTextColor(0xff000000);
                		_button.setBackgroundColor(0xff2eccee);
	                } else {
	                	_click = true;	                	
	                }
	            }
	        });
			ListView list = new ListView(context);
	        list.setAdapter(_adapter);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			list.setLayoutParams(params);
			list.setBackgroundColor(0xff2eccee);
			list.setPadding(5, 5, 5, 5);
			_popup.setBackgroundDrawable(new BitmapDrawable());
			_popup.setContentView(list);
		}
	
	}
	
	public static final String NOT_COMING = "Not coming.";
	public static final String MAYBE = "Maybe...";
	public static final String OF_COURSE = "Of course!";

	private class AttendCustomPopup {
		private Boolean _click = true;
		private PopupWindow _popup;
		private Button _button;
		private ArrayAdapter<String> _adapter;
		private ArrayList<String> _what;
		
		public AttendCustomPopup (Context context, Button button) {
			
			_what = new ArrayList<String>();
			_what.add(OF_COURSE);
			_what.add(MAYBE);
			_what.add(NOT_COMING);
			_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, _what); 
			_button = button;
			_popup = new PopupWindow(context);
			_button.setOnClickListener(new OnClickListener() {

				@Override
	            public void onClick(View v) {
	                if (_click) {
	                	_popup.showAsDropDown(_button);
	                	_popup.update(_button, -(250-_button.getWidth())/2, 0, 250, 300);
	                	_click = false;
	                } else {
	                	_popup.dismiss();
	                	_click = true;
	                }
	            }
				
	        });
			final ListView list = new ListView(context);
	        list.setAdapter(_adapter);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			list.setLayoutParams(params);
			list.setBackgroundColor(0xff2eccee);
			list.setPadding(5, 5, 5, 5);
			list.setItemsCanFocus(true);
			list.setClickable(true);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String what = (String) list.getItemAtPosition(position);
					_pushEvent.updateAttendance(what);
					
					int att = Attending.SO;
					if (what.equals(EventManager.OF_COURSE)) att=(Attending.YES);
					if (what.equals(EventManager.MAYBE)) att=(Attending.MAYBE);
					if (what.equals(EventManager.NOT_COMING)) att=(Attending.NO);
					Contact contact = DAL.Instance().readContact(ParseUser.getCurrentUser().getUsername());
					contact.set_attending(att);
					try {
						DAL.Instance().updateParticipant(contact, _event);
						if (att==Attending.NO) {
							DAL.Instance().deleteEvent(_event);
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
                	_popup.dismiss();
                	_click = true;

				}
			});
			_popup.setFocusable(true);
			_popup.setBackgroundDrawable(null);
			_popup.setContentView(list);
		}
	
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
    
    private BroadcastReceiver _updateReceiver;

    @Override
    protected void onResume() {
    	super.onResume();

    	_updateReceiver=new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			 if (intent.getAction().equals("il.ac.huji.app4beer.UPDATE_EVENT")) {
    				 initChat();
    				 for (int i=0;i<4;i++) {
    					 List<Contact> c = DAL.Instance().Participants(_event.get_id(), i);
    					 _contacts.set(i, c);
    					 _contactsAdapter.set(i, new CustomContactsAdapter(EventManager.this, c, false, false));
    				 }
    			 }
    		}
    	};
    	IntentFilter updateIntentFilter=new IntentFilter("il.ac.huji.app4beer.UPDATE_EVENT");
    	registerReceiver(_updateReceiver, updateIntentFilter);
    }

    @Override
    protected void onPause() {
    	super.onPause();

    	if (this._updateReceiver!=null)
    		unregisterReceiver(_updateReceiver);
    }
}
