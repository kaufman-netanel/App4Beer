package il.ac.huji.app4beer.Adapters;

import il.ac.huji.app4beer.R;
import il.ac.huji.app4beer.DAL.Event;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomEventAdapter extends ArrayAdapter<Event> {
	public CustomEventAdapter(
			Context context,
			List<Event> events) {
		super(context, android.R.layout.simple_list_item_1, events);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Event event = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.event_item_in_dashboard, null);
		
		TextView name = (TextView)view.findViewById(R.id.event_in_dashboard_name);
		name.setText(event.get_title());

		TextView date = (TextView)view.findViewById(R.id.event_in_dashboard_date);
		Date now = new Date();
		int color = event.get_date().before(now) ? Color.RED : Color.BLACK;
		name.setTextColor(color);
		date.setTextColor(color);
		date.setText(String.format("%02d/%02d, %02d:%02d", 
				event.get_date().getDate(), 
				event.get_date().getMonth()+1, 
				event.get_date().getHours(),
				event.get_date().getMinutes()));
		return view;
	}
}