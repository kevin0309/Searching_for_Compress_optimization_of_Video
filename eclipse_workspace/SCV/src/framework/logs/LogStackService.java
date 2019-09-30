package framework.logs;

import java.sql.SQLException;
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
import framework.util.FileUtil;
import framework.util.LogUtil;

/**
 * 일정 간격으로 로그 로컬파일을 생성하고, 그 정보를 DB에 업로드하는 스케줄러 클래스
 * @author 박유현
 * @since 2019.09.30
 */
@WebListener
public class LogStackService implements ServletContextListener {

	private ScheduledExecutorService scheduler;
	private static ArrayList<String> logStack = new ArrayList<>();
	private int interval;
	private LogDAO dao = new LogDAO();
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (interval > 0) {
			scheduler.shutdownNow();
			saveLog(true);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		interval = ServerConfig.getLogStackInterval();
		if (interval <= 0) {
			LogUtil.printLog("LogStackService was not started by the configuration.");
			return;
		}
		scheduler = Executors.newScheduledThreadPool(1);
		LogUtil.printLog("LogStackService scheduler initiated.");
		
		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (logStack.size() != 0)
					saveLog(false);
			}
		}, interval, interval, TimeUnit.MINUTES);
	}

	public static void addLog(String log, boolean isErrorLog) {
		logStack.add((isErrorLog?"[ERR]":"[LOG]")+log);
	}
	
	private void saveLog(boolean isTerminate) {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String[] curDateStr = sdf.format(curDate).split("-");
		String year = curDateStr[0];
		String month = curDateStr[1];
		String date = curDateStr[2];
		String hour = curDateStr[3];
		String min = curDateStr[4];
		
		//로컬파일 생성
		String newDirectory = ServerConfig.getLogStackDirectory()+"/"+year+"/"+month+"/"+date;
		String newFilePath = newDirectory+"/"+sdf.format(curDate)+".txt";
		LogUtil.printLog("make new log file : "+newFilePath);
		FileUtil.makeNewDirectory(newDirectory);
		FileUtil.write(newFilePath, logStack);
		logStack = new ArrayList<>();
		
		//DB에 추가
		if (!ServerConfig.isDev()) {
			try {
				if (isTerminate)
					dao.insertNewLogFile(year, month, date, hour, min, -1, curDate);
				else
					dao.insertNewLogFile(year, month, date, hour, min, interval, curDate);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
