package il.ac.huji.app4beer.DAL;

import il.ac.huji.app4beer.MainActivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

public class ParseProxy {

	private static final String ParseApplication = "WK71gB5ZUn0MvUrePNzxVNStnKZBMfUdP6PGwFft";
	private static final String ClientKey = "eWxzNLLcg80vLUrxo8h3mWdtHUFFirqPJy7ljaLf";

	public static void Init(Context context) {
	    Parse.initialize(context, ParseApplication, ClientKey);
	    PushService.setDefaultPushCallback(context, MainActivity.class);
	    ParseInstallation.getCurrentInstallation().saveInBackground();
	}

	static public List<String> Users() {
		List<String> users = new ArrayList<String>();
		ParseQuery query = ParseUser.getQuery();
		List<ParseObject> parseUsers;
		try {
			parseUsers = query.find();
			for (int i=0;i<parseUsers.size();i++) {
				users.add(((ParseUser)parseUsers.get(i)).getUsername());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return users;
	}
		
}
