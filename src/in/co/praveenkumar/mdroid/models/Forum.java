/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	12-02-2014
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

package in.co.praveenkumar.mdroid.models;

public class Forum {
	int id;
	String subject;
	int postCount;

	// constructors
	public Forum() {
	}

	public Forum(int id, String subject, int postCount) {
		this.id = id;
		this.subject = subject;
		this.postCount = postCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}

}
