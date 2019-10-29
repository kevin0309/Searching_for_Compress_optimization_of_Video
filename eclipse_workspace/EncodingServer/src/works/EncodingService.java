package works;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import framework.init.ServerConfig;
import framework.util.LogUtil;

/**
 * 인코딩 작업 진행을 위한 스케줄러를 생성하는 ServletContextListener
 * @author 박유현
 * @since 2019.10.13
 */
@WebListener
public class EncodingService implements ServletContextListener {
	
	private static ArrayList<EncodingQueue> queues;
	public static ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String ffmpegPath = ServerConfig.getFFMPEGPath();
		try {
			new File(ffmpegPath);
		} catch (NullPointerException e) {
			LogUtil.printErrLog("FFMPEG not found. Can't initiate Encoding service.");
			return;
		}
		
		//int schedulerThreadsAmount = ServerConfig.getHDDList().size();
		int schedulerThreadsAmount = 1;
		scheduler = Executors.newScheduledThreadPool(schedulerThreadsAmount);
		LogUtil.printLog("Encoding service started. It holds " + schedulerThreadsAmount + " amounts of Threads.");
		
		queues = new ArrayList<>();
		for (int i = 0; i < schedulerThreadsAmount; i++) {
			EncodingQueue tempQueue = new EncodingQueue();
			tempQueue.observeProcess();
			queues.add(tempQueue);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		scheduler.shutdownNow();
	}
	
	public static ScheduledExecutorService getScheduler() {
		return scheduler;
	}
	
	public static ArrayList<EncodingQueue> getQueueList() {
		return queues;
	}
	
}