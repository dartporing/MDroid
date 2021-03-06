/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
 * 
 * � 2013, Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid.networking;

import in.co.praveenkumar.mdroid.MainActivity;
import in.co.praveenkumar.mdroid.helpers.AppsHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class DoLogin {
	final String DEBUG_TAG = "NETWORKING_LOGIN";
	
	static DefaultHttpClient httpclient;
	static Boolean isLogged = false;
	static String htmlData = "";

	CookieStore cookieStore;
	String getCookie;
	String postCookie;
	String mURL;

	public DoLogin() {
		httpclient = MainActivity.httpclient;
		if (httpclient == null) {
			AppsHttpClient ahc = new AppsHttpClient();
			httpclient = ahc.getHttpClient();
			MainActivity.httpclient = httpclient;
		}
		cookieStore = httpclient.getCookieStore();
		mURL = MainActivity.mURL;
	}

	public DoLogin(String mURL) {
		this.mURL = mURL;
		AppsHttpClient ahc = new AppsHttpClient();
		httpclient = ahc.getHttpClient();
		cookieStore = httpclient.getCookieStore();

		// Set these values in MainActivity for further use by service.
		MainActivity.httpclient = httpclient;
		MainActivity.mURL = mURL;

	}

	public Boolean isLoggedIn() {
		if (htmlData.contains("You are logged in"))
			isLogged = true;
		else
			isLogged = false;
		
		return isLogged;
	}

	public int doLogin(String uName, String pswd) {
		int respCode = 0; // used only when login failed.
		Log.d(DEBUG_TAG, "Started");
		Log.d(DEBUG_TAG, "URL:" + mURL);

		HttpGet httpget = new HttpGet(mURL + "/login/index.php");
		HttpPost httppost = new HttpPost(mURL + "/login/index.php");

		try {
			// Do a HttpGet first. This is to pass cookies check test.
			httpclient.execute(httpget);
			Log.d(DEBUG_TAG, "GET done");

			// Check for value of MoodleSession cookie.
			// This changes on successful login.
			if (!cookieStore.getCookies().isEmpty())
				getCookie = cookieStore.getCookies().get(0).getValue();

			// Now POST request to do login
			// NameValuePairs for POST data holding
			List<NameValuePair> dataForUrl = new ArrayList<NameValuePair>();
			dataForUrl.add(new BasicNameValuePair("username", uName));
			dataForUrl.add(new BasicNameValuePair("password", pswd));

			// Add the NVPs to HTTP post request
			httppost.setEntity(new UrlEncodedFormEntity(dataForUrl));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			InputStream is = response.getEntity().getContent();

			// Read content from stream
			String line = "";
			StringBuilder total = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			// Read response until the end
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
			htmlData = total.toString();
			Log.d(DEBUG_TAG, "POST done");

			// POST method cookie.
			if (!cookieStore.getCookies().isEmpty())
				postCookie = cookieStore.getCookies().get(0).getValue();

		} catch (MalformedURLException e) {
			// URL malformed
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Malformed URL");
			respCode = 1;
		} catch (IOException e) {
			// Error while making connection
			e.printStackTrace();
			Log.d(DEBUG_TAG, "IOException ! Net problem ?");
			respCode = 2;
		} catch (IllegalStateException e) {
			// URL has error.
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Illegal state exception");
			respCode = 1;
		} catch (IllegalArgumentException e) {
			// URL has error.
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Illegal argument exception");
			respCode = 1;
		}

		return respCode;
	}

	public String getContent() {
		return htmlData;
	}

	public int getLoginError() {
		int errVal = 0;

		if (getCookie != null && postCookie != null)
			if (getCookie.contentEquals(postCookie))
				// Incorrect credentials. Not MDroid problem.
				errVal = 3;
			else
				// Correct credentials. MDroid issue
				errVal = 1;

		if (htmlData.contains("You are logged in"))
			// No courses detected case
			errVal = 2;

		return errVal;
	}

}
