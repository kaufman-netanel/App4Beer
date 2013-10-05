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

	static public enum PushType {
		NewEvent, UpdateAttendance, Tweet
	}

	private static final String ParseApplication = "WK71gB5ZUn0MvUrePNzxVNStnKZBMfUdP6PGwFft";
	private static final String ClientKey = "eWxzNLLcg80vLUrxo8h3mWdtHUFFirqPJy7ljaLf";

	public static class PushEnvelope {
		private String alert;
		private String json;
		private PushType type;
		private String action = "il.ac.huji.app4beer.UPDATE_STATUS";
		public String getAlert() {
			return alert;
		}
		public void setAlert(String alert) {
			this.alert = alert;
		}
		public String getMessage() {
			return json;
		}
		public void setMessage(String message) {
			this.json = message;
		}
		public PushType getType() {
			return type;
		}
		public void setType(PushType type) {
			this.type = type;
		}
	}

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

	static public void Push(String to, PushType type, Object what, String alert) {
		ParseQuery pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("username", to);

		ParsePush push = new ParsePush();
		push.setQuery(pushQuery); 

		try {
			Gson gson = new Gson();
			PushEnvelope env = new PushEnvelope();
			env.setAlert(alert);
			env.setMessage(gson.toJson(what));
			env.setType(type);
			String json = gson.toJson(env);
			JSONObject data = new JSONObject(json);
			//if (alert!=null) data.accumulate("alert", alert);
			//data.accumulate("__type", type);
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
			e1.printStackTrace();
		}
	}

	static public String getTheJSON(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras==null) return null;
		String jsonData = extras.getString( "com.parse.Data" );
		return jsonData;
	}
	
	static public PushEnvelope getTheEvelope(Intent intent) {
		Gson gson = new Gson();
		return gson.fromJson(getTheJSON(intent), PushEnvelope.class);
	}

}
