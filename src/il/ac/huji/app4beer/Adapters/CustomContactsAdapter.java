package il.ac.huji.app4beer.Adapters;

import il.ac.huji.app4beer.R;
import il.ac.huji.app4beer.DAL.Contact;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class CustomContactsAdapter extends ArrayAdapter<Contact> {
	
	private Boolean _defaultChecked;

	public CustomContactsAdapter(
			Context context,
			List<Contact> groups,
			Boolean defaultChecked) {
		super(context, android.R.layout.simple_list_item_1, groups);
		_defaultChecked = defaultChecked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Contact contact= getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.contact_in_add_participants, null);
		CheckBox name = (CheckBox)view.findViewById(R.id.contact_check_box);
		if (contact!=null) {
			name.setText(contact.get_name());
		}
		name.setChecked(_defaultChecked);
		return view;
	}
}