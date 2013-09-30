package il.ac.huji.app4beer.DAL;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PushEvent {
	
	private String _title;
	private String _description;
	private String _location;
	private Date _date;
	private HashSet<String> _contacts;
	
	public PushEvent(Event event) {
		_title = event.get_title();
		_description = event.get_description();
		_location = event.get_location();
		_date = event.get_date();
		_contacts = new HashSet<String>();
		Iterator<Integer> contactsIt = event.get_contacts().iterator();
		while (contactsIt.hasNext()) {
			_contacts.add(DAL.Instance().readContact(contactsIt.next()));
		}
		Iterator<Integer> groupsIt = event.get_groups().iterator();
		while (groupsIt.hasNext()) {
			List<Contact> members = DAL.Instance().Members(groupsIt.next());
			Iterator<Contact> membersIt = members.iterator();
			while (membersIt.hasNext()) {
				_contacts.add(membersIt.next().get_name());
			}					
		}
	}
}
