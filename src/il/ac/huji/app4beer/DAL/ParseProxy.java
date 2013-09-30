package il.ac.huji.app4beer.DAL;

import il.ac.huji.app4beer.Dashboard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SendCallback;

public class ParseProxy {

	private static final String ParseApplication = "WK71gB5ZUn0MvUrePNzxVNStnKZBMfUdP6PGwFft";
	private static final String ClientKey = "eWxzNLLcg80vLUrxo8h3mWdtHUFFirqPJy7ljaLf";

	public static void Init(Context context) {
	    Parse.initialize(context, ParseApplication, ClientKey);
	    PushService.setDefaultPushCallback(context, Dashboard.class);
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
	
	static public void Push(String to, Object what) {
		ParseQuery pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("username", to);
		// Send push notification to query
		ParsePush push = new ParsePush();
		push.setQuery(pushQuery); // Set our Installation query
		push.setMessage("yipy!");
		try {
			Gson gson = new Gson();
			String json = gson.toJson(what);
			JSONObject data = new JSONObject(json);
			data.accumulate("alert", "wow");
//			JSONObject data = new JSONObject("{\"alert\": \"BOOO\", \"name\": \"Vaughn\", \"newsItem\": \"Man bites dog\"}");
			push.setData(data);
			push.sendInBackground(new SendCallback() {
			
			@Override
			public void done(ParseException e) {
				if (e!=null) {
					e.printStackTrace();					
				}				
			}
		});
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	static public String getTheJSON(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras==null) return null;
		String jsonData = extras.getString( "com.parse.Data" );
		return jsonData;
	}
		
}
