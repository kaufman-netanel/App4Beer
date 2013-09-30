package il.ac.huji.app4beer.DAL;

public class Message {
	private Integer _contactid;
	private Integer _eventid;
	private String _message;
	
	public Message(Integer contactid, Integer eventid, String message) {
		_contactid = contactid;
		_eventid = eventid;
		_message = message;
	}
	
	public Integer get_contactid() {
		return _contactid;
	}
	public void set_contactid(Integer _contactid) {
		this._contactid = _contactid;
	}
	public Integer get_eventid() {
		return _eventid;
	}
	public void set_eventid(Integer _eventid) {
		this._eventid = _eventid;
	}
	public String get_message() {
		return _message;
	}
	public void set_message(String _message) {
		this._message = _message;
	} 

}
