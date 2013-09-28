package il.ac.huji.app4beer.DAL;

import java.util.ArrayList;

public class Group {
	private String _name;
	private ArrayList<Contact> _members;
	
	public Group(String name) {
		_name = name;
	}
	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		this._name = _name;
	}
	public ArrayList<Contact> get_members() {
		return _members;
	}
	public void add_member(Contact contact) {
		_members.add(contact);
	}
	public Boolean remove_member(String name) {
		for (int i=0;i<_members.size();i++){
			if (_members.get(i).get_name().equals(name)) {
				_members.remove(i);
				return true;
			}
		}
		return false;
	}

}
