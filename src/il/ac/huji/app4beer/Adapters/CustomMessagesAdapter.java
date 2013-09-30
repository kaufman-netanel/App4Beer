package il.ac.huji.app4beer.Adapters;

import il.ac.huji.app4beer.R;
import il.ac.huji.app4beer.DAL.Contact;
import il.ac.huji.app4beer.DAL.DAL;
import il.ac.huji.app4beer.DAL.Event;
import il.ac.huji.app4beer.DAL.Message;

import java.util.Date;
import java.util.List;

import com.parse.ParseUser;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomMessagesAdapter extends ArrayAdapter<Message> {
	public CustomMessagesAdapter(
			Context context,
			List<Message> messages) {
		super(context, android.R.layout.simple_list_item_1, messages);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		Contact from = DAL.Instance().readContact(message.get_contactid());
		Boolean from_self = from.get_name().equals(ParseUser.getCurrentUser().getUsername());
		int resource = from_self ? R.layout.chat_item_self_view : R.layout.chat_item_others_view;
		View view = inflater.inflate(resource, null);
		
		TextView content = (TextView)view.findViewById(R.id.chat_content);
		content.setText(message.get_message());

		if (!from_self) {
			TextView user = (TextView)view.findViewById(R.id.chat_user);
			user.setText(from.get_name());
		}
//		TextView date = (TextView)view.findViewById(R.id.event_in_dashboard_date);
//		Date now = new Date();
//		int color = event.get_date().before(now) ? Color.RED : Color.BLACK;
//		name.setTextColor(color);
//		date.setTextColor(color);
//		date.setText(String.format("%02d/%02d, %02d:%02d", 
//				event.get_date().getDate(), 
//				event.get_date().getMonth()+1, 
//				event.get_date().getHours(),
//				event.get_date().getMinutes()));
		return view;
	}
}