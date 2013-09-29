package il.ac.huji.app4beer.DAL;

public class Contact {
	private String _name;
	private String _phone;
	
	public Contact(String name, String phone) {
		_name = name;
		_phone = phone;
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

}
