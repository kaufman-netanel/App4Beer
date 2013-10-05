package il.ac.huji.app4beer.Adapters;

import il.ac.huji.app4beer.R;
import il.ac.huji.app4beer.DAL.Contact;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class CustomContactsAdapter extends ArrayAdapter<Contact> {
	
	private Boolean _defaultChecked;
	private Boolean _checkable;

	public CustomContactsAdapter(
			Context context,
			List<Contact> groups,
			Boolean defaultChecked,
			Boolean checkable) {
		super(context, android.R.layout.simple_list_item_1, groups);
		_defaultChecked = defaultChecked;
		_checkable = checkable;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Contact contact= getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		int resource = _checkable ? R.layout.contact_in_add_participants : R.layout.contact_in_view_participants;
		View view = inflater.inflate(resource, null);
		TextView name = (TextView)view.findViewById(R.id.contact_check_box);
		if (contact!=null) {
			name.setText(contact.get_name());
		}
		name.setClickable(_defaultChecked);
		if (_checkable) {
			((CheckBox)name).setChecked(contact.get_selected());
			((CheckBox)name).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					contact.set_selected(arg1);
				}
			});
		}
		return view;
	}
}