package il.ac.huji.app4beer.Adapters;

import il.ac.huji.app4beer.R;
import il.ac.huji.app4beer.DAL.Group;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomGroupAdapter extends ArrayAdapter<Group> {
	
	public CustomGroupAdapter(
			Context context,
			List<Group> groups) {
		super(context, android.R.layout.simple_list_item_1, groups);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Group group= getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.group_in_manage_groups, null);
		TextView name = (TextView)view.findViewById(R.id.groupName);
		if (group!=null) {
			name.setText(group.get_name());
		}
		return view;
	}
}