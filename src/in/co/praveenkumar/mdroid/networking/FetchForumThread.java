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
import in.co.praveenkumar.mdroid.models.Reply;
import in.co.praveenkumar.mdroid.parser.ForumThreadParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class FetchForumThread {
	private final String DEBUG_TAG = "NETWORKING_FORUM_THREAD";
	private String mURL = MainActivity.mURL;
	private ArrayList<Reply> replies = new ArrayList<Reply>();
	private String threadContent = "";
	private String threadReplyId = "";

	public void fetchThread(String threadId) {
		String html = fetchThreadHtml(threadId);
		ForumThreadParser FTP = new ForumThreadParser(html);
		replies = FTP.getReplies();
		threadReplyId = FTP.getReplyId();
	}

	private String fetchThreadHtml(String threadId) {
		String html = "";
		try {
			// Get default app client. Only that has the cookie data
			DefaultHttpClient httpclient = MainActivity.httpclient;

			// Fetch thread page
			Log.d(DEBUG_TAG, "Fetching thread " + threadId);
			HttpGet httpgetCourse = new HttpGet(mURL
					+ "/mod/forum/discuss.php?d=" + threadId);
			HttpResponse response = httpclient.execute(httpgetCourse);
			InputStream is = response.getEntity().getContent();

			// Read content from stream
			String line = "";
			StringBuilder total = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			// Read response until the end
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
			html = total.toString();
			threadContent = html;

		} catch (MalformedURLException e) {
			// URL malformed
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Malformed URL");
		} catch (IOException e) {
			// Error while making connection
			e.printStackTrace();
			Log.d(DEBUG_TAG, "IOException ! Net problem ?");
		}
		return html;
	}

	public ArrayList<Reply> getReplies() {
		return replies;
	}

	public String getThreadContent() {
		return threadContent;
	}

	public String getReplyId() {
		return threadReplyId;
	}
}
