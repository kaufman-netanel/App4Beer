package il.ac.huji.app4beer.Adapters;

import il.ac.huji.app4beer.R;
import il.ac.huji.app4beer.DAL.Group;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CustomGroupAdapter extends ArrayAdapter<Group> {
	
	private Boolean _checkedItems;

	public CustomGroupAdapter(
			Context context,
			List<Group> groups, 
			Boolean checkedItems) {
		super(context, checkedItems ? android.R.layout.simple_list_item_multiple_choice : android.R.layout.simple_list_item_1, groups);
		_checkedItems = checkedItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Group group = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		int resource = _checkedItems ? R.layout.group_in_add_participants : R.layout.group_in_manage_groups;
		View view = inflater.inflate(resource, null);
		int nameResource = _checkedItems ? R.id.contact_check_box : R.id.group_name_text_view;
		TextView name = (TextView)view.findViewById(nameResource);
		if (group!=null) {
			name.setText(group.get_name());
			if (_checkedItems) {
				((CheckBox)name).setChecked(group.get_selected());
				((CheckBox)name).setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						group.set_selected(arg1);
						notifyDataSetChanged();
					}
				});

			}
		}
		return view;
	}
}