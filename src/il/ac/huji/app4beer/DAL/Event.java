package il.ac.huji.app4beer.DAL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {
	
	private String _title;
	private String _description;
	private String _location;
	private Date _date;
	private Integer _id;
	private Contact _owner;
	private ArrayList<Integer> _contacts;
	private ArrayList<Integer> _groups;
	
	public Event(String title, String description, String location, Date date, ArrayList<Integer> contacts, ArrayList<Integer> groups) {
		_title = title;
		_description = description;
		_date = date;
		set_groups(groups);
		set_contacts(contacts);
		_id = -1;
		_location = location;
	}
	
	public Event(Integer id, String title, String description, String location, Date date) {
		_id = id;
		_title = title;
		_description = description;
		_date = date;
		_location = location;
		set_groups(new ArrayList<Integer>());
		set_contacts(new ArrayList<Integer>());
	}
	public String get_title() {
		return _title;
	}
	public void set_title(String _title) {
		this._title = _title;
	}
	public String get_description() {
		return _description;
	}
	public void set_description(String _description) {
		this._description = _description;
	}
	public Date get_date() {
		return _date;
	}
	public void set_date(Date _date) {
		this._date = _date;
	}
	public Integer get_id() {
		return _id;
	}
	public void set_id(Integer _id) {
		this._id = _id;
	}

	public ArrayList<Integer> get_contacts() {
		return _contacts;
	}

	public void set_contacts(ArrayList<Integer> _contacts) {
		this._contacts = _contacts;
	}

	public ArrayList<Integer> get_groups() {
		return _groups;
	}

	public void set_groups(ArrayList<Integer> _groups) {
		this._groups = _groups;
	}
	public void add_contact(int id) {
		_contacts.add(id);
	}
	public void add_group(int id) {
		_groups.add(id);
	}
	public List<Integer> contacts() {
		return _contacts;
	}
	public List<Integer> groups() {
		return _groups;
	}

	public String get_location() {
		return _location;
	}

	public void set_location(String _location) {
		this._location = _location;
	}

	public Contact get_owner() {
		return _owner;
	}

	public void set_owner(Contact contact) {
		this._owner = contact;
	}

}
