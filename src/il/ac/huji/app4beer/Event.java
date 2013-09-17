package il.ac.huji.app4beer;


import java.util.Date;
import android.text.format.Time;


public class Event {
	
	private String _title;
	private String _description;
	private Date _date;
	private Time _time;
	
	
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
	public Time get_time() {
		return _time;
	}
	public void set_time(Time _time) {
		this._time = _time;
	}
	


}
