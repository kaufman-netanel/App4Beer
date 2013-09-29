package il.ac.huji.app4beer.DAL;

public class Contact {
	private String _name;
	private String _phone;
	private Integer _id;
	private Boolean _selected;

	public Contact(String name, String phone, Integer id) {
		_name = name;
		_phone = phone;
		set_id(id);
		_selected = false;
	}

	public String get_phone() {
		return _phone;
	}
	public void set_phone(String _phone) {
		this._phone = _phone;
	}
	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		this._name = _name;
	}

	public Integer get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public Boolean get_selected() {
		return _selected;
	}

	public void set_selected(Boolean _selected) {
		this._selected = _selected;
	}

}
