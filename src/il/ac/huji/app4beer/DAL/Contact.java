package il.ac.huji.app4beer.DAL;

public class Contact {
	
	public static class Source { 
		public final static int OWNER = 0;
		public final static int GROUP = 1;
		public final static int CONTACTS = 2;
	}		
	
	public static class Attending { 
		public final static int YES = 0;
		public final static int NO = 1;
		public final static int MAYBE = 2;
		public final static int SO = 3;
	}		
	
	private String _name;
	private String _phone;
	private Integer _id;
	private Boolean _selected;
	private int _source;
	private int _attending;

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

	public int get_source() {
		return _source;
	}

	public void set_source(int _source) {
		this._source = _source;
	}

	public int get_attending() {
		return _attending;
	}

	public void set_attending(int _attending) {
		this._attending = _attending;
	}

}
