package framework.logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import framework.init.ServerConfig;
import framework.util.DateUtil;
import framework.util.FileUtil;
import framework.util.LogUtil;

/**
 * 일정 간격으로 로그 로컬파일을 생성하는 스케줄러 클래스
 * @author 박유현
 * @since 2019.11.11
 */
@WebListener
public class LogStackService implements ServletContextListener {

	private ScheduledExecutorService scheduler;
	private static ArrayList<String> logStack = new ArrayList<>();
	private static File curLogFile;
	private static int interval;
	private static Date curDate;
	private static String year;
	private static String month;
	private static String date;
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (interval > 0) {
			scheduler.shutdownNow();
			saveLog(false, true);
			System.out.println("[LogStackService] - Shutdown scheduler complete. Save the remaining logs.");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		interval = ServerConfig.getLogStackInterval();
		if (interval <= 0) {
			LogUtil.printLog("LogStackService was not started by the configuration.");
			return;
		}
		saveLog(true, false);
		scheduler = Executors.newScheduledThreadPool(1);
		LogUtil.printLog("LogStackService scheduler initiated.");
		
		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (LogStackService.logStack.size() != 0)
					LogStackService.saveLog(false, false);
				else
					if (ServerConfig.isDev())
						System.out.println("[DEVMODE]["+DateUtil.getSysdateStr()+"][LogStackService] - Empty logs.");
			}
		}, interval, interval, TimeUnit.MINUTES);
	}

	public static void addLog(String log, boolean isErrorLog) {
		synchronized (logStack) {
			if (logStack.size() == 100)
				addLogToFile();
			
			logStack.add((isErrorLog?"[ERR]":"[LOG]")+log);
		}
	}
	
	/**
	 * 누적된 로그를 저장, DB에 등록
	 * @param isInit
	 * @param isTerminate
	 */
	private static void saveLog(boolean isInit, boolean isTerminate) {
		try {
		if (!isInit) {
			addLogToFile();
			if (curLogFile.exists())
				System.out.println("["+DateUtil.getSysdateStr()+"][LogStackService] - make new log file : "+curLogFile.getName());
		}
		
		curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String[] curDateStr = sdf.format(curDate).split("-");
		year = curDateStr[0];
		month = curDateStr[1];
		date = curDateStr[2];
		
		//로컬파일 생성
		String newDirectory = ServerConfig.getLogStackDirectory()+"/"+year+"/"+month+"/"+date;
		String newFilePath = newDirectory+"/"+sdf.format(curDate)+".txt";
		FileUtil.makeNewDirectory(newDirectory);
		curLogFile = new File(newFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void addLogToFile() {
		if (logStack.size() == 0)
			return;
		try {
			FileWriter fw = new FileWriter(curLogFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String logMsg : logStack) {
				bw.write(logMsg);
				bw.newLine();
			}
			bw.close();
			fw.close();
			logStack = new ArrayList<>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
