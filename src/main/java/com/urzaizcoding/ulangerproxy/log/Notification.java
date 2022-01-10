package com.urzaizcoding.ulangerproxy.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification implements Comparable<Notification>{
	private String m_message;
	private Object m_sender;
	private Date m_sentDate;
	private NotificationType m_level;
	public Notification(Object sender,NotificationType level) {
		m_sentDate = new Date();
		m_sender = sender;
		m_level = level;
	}
	public Notification(Object sender, String message, NotificationType level) {
		m_sentDate = new Date();
		m_sender = sender;
		m_message = message;
		m_level = level;
	}
	public void setMessage(String message) {
		m_message = message;
	}
	public Object getSender() {
		return m_sender;
	}
	public String getMessage() {
		return m_message;
	}
	public NotificationType getLevel() {
		return m_level;
	}
	public Date getSentDate() {
		return m_sentDate;
	}
	@Override
	public int compareTo(Notification someOtherNotif) {
		if(this.m_sentDate.after(someOtherNotif.m_sentDate)) {
			return 1;
		}
		else if (this.m_sentDate.equals(someOtherNotif.getSentDate())){
			return 0;
		}
		else {
			return -1;
		}
	}
	public String toString() {
		SimpleDateFormat dateFormater = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
		String retour = new String(dateFormater.format(getSentDate()) + "\t"+ getLevel().toString() + "\t"+ getSender()
		+ "\t>>  " + getMessage()+"\n\n");
		return retour;
	}
}
