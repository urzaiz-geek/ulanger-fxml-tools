package com.urzaizcoding.ulangerproxy.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger{
	public static final Logger loggerObject = new Logger();
	public static LogOutPut outPutMode = LogOutPut.STD_OUTPUT;
	private String logFilePath;
	private boolean m_autoFlush;
	PriorityQueue<Notification> m_receivedNotifs;
	ExecutorService executor = null;
	private static final int MAX_LOGS = 5;
	private boolean readyWrite;
	
	
	private Logger() {
		logFilePath = "log";
		executor = Executors.newFixedThreadPool(1);
		m_receivedNotifs = new PriorityQueue<Notification>();
		m_autoFlush = false;
		readyWrite = true;
	}
	public String getLogPath() {
		return logFilePath;
	}
	public void setFilePath(String path) {
		File testPath = new File(path);
		if (testPath.exists() && testPath.isFile()) {
			logFilePath = path;
		}
		else {
			logFilePath = "log";
		}
	}
	
	public void flush() {
		/**
		 * forces the notification to be written in the specified log file even if we have not reached the expected 
		 * number of notification to log
		 */
		if(readyWrite && m_receivedNotifs.size() > 0) {
			Runnable task = report();
			executor.submit(task);
		}
	}
	public void setAutoFlush(boolean flush) {
		m_autoFlush = flush;
	}
	public boolean isAutoFlush() {
		return m_autoFlush;
	}
	private void writeNotif(String sender, String message, NotificationType type) {
		Notification notif = new Notification(sender,message, type);
		if (outPutMode == LogOutPut.STD_OUTPUT) {
			System.out.println(notif);
		}
		else if(outPutMode == LogOutPut.FILE_OUTPUT) {
			m_receivedNotifs.add(notif);
			if(m_receivedNotifs.size() == MAX_LOGS || m_autoFlush) {
				flush();
			}
		}
		else {
			System.out.println(notif);
			m_receivedNotifs.add(notif);
			if(m_receivedNotifs.size() == MAX_LOGS || m_autoFlush) {
				flush();
			}
		}
	}
	public void writeDebug(String sender,String message) {
		writeNotif(sender, message, NotificationType.DEBUG);
	}
	public void writeError(String sender, String message) {
		writeNotif(sender, message, NotificationType.ERROR);
	}
	public void writeInfo(String sender, String message) {
		writeNotif(sender, message, NotificationType.INFO);
	}
	public void writeFatal(String sender, String message) {
		writeNotif(sender, message, NotificationType.FATAL);
	}
	public void writeWarning(String sender, String message) {
		writeNotif(sender, message, NotificationType.WARNING);
	}
	/**
	 * return a runnable which is the writting to file task that can then be submitted to the Threadpool
	 * @return {@linkplain Runnable}
	 */
	private Runnable report() {
		return new Runnable() {
			FileWriter fic = null;
			PrintWriter printer = null;
			@Override
			public void run() {
				try {
					fic = new FileWriter(logFilePath,true);
					printer = new PrintWriter(fic);
					String messageToWrite;
					synchronized (m_receivedNotifs) {
						readyWrite = false;
						int i = 0;
						while (i < MAX_LOGS && m_receivedNotifs.size() > 0) {
							Notification notif = m_receivedNotifs.poll();
							messageToWrite = notif.toString();
							printer.write(messageToWrite);
							i++;
						}
					}
					readyWrite = true;
				}catch(FileNotFoundException e){
					System.out.println("error with the file path");
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					//releasing ressources
					try {
						fic.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					printer.close();
					System.out.println("End of the task");
				}
				
			}
		};
	}
	/**
	 * convert and exception to a string containing an alleged version of the stacktrace
	 * it tells us where the exception were thrown and who threw it.
	 * @param e : and Exception
	 * @return : a String describing the exception an his context
	 */
	public static String getExceptionMessage(Exception e) {
		String retour = new String("Message : "+e.getMessage());
		for(StackTraceElement elt : e.getStackTrace()) {
			if (elt.getFileName() != null) {
				retour += "\n\tFile : "+elt.getFileName() 
					+"\n\tClass : "+elt.getClassName()
					+"\n\tMethod : "+elt.getMethodName()
					+"\n\tLine : "+elt.getLineNumber();
				break;
			}
		}
		return retour;
	}
	/**
	 * this method release the resources held by this object, have to be called once we are done logging
	 */
	public void close(){
		if(m_receivedNotifs.size() != 0) {
			flush();
		}
		executor.shutdown();
	}
	/**
	 * java equivalent for object destruction
	 */
	public void finalize() {
		this.close();
	}
}
