package framework.jdbc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import framework.init.InitConfig;
import framework.util.LogUtil;

/**
 * DBMng에서 사용할 스케줄러를 초기화하는 클래스
 * @author 박유현
 * @since 2018-12-29
 */
@WebListener
public class DBMngScheduler implements ServletContextListener {

	private static ScheduledExecutorService scheduler;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		int schedulerThreadsAmount = 1;
		scheduler = Executors.newScheduledThreadPool(schedulerThreadsAmount);
		LogUtil.printLog("DBMng initiated successfully, DBMng holds "+schedulerThreadsAmount+" amounts of Threads.");
		InitConfig.initConfigWithDBAcessRequired();
	}
	
	public static ScheduledExecutorService getScheduler() {
		return scheduler;
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("[DBMngScheduler] - Shutdown scheduler complete.");
		InitConfig.destroyConfigWithDBAcessRequired();
		scheduler.shutdownNow();
	}
}
