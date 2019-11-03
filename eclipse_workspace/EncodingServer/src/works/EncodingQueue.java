package works;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import framework.init.ServerConfig;
import framework.servlet.fileRequest.HddSelector;
import framework.servlet.fileRequest.SampleVideoDAO;
import framework.util.GenerateFilePathFactory;
import framework.util.LogUtil;
import framework.util.windowsAppProcessing.WindowsAppProcessBuilder;

/**
 * 인코딩 대기열을 구현한 클래스
 * 직접적인 인코딩 프로세스가 실행됨
 * @author 박유현
 * @since 2019.10.13
 */
public class EncodingQueue {

	private long totalVolume;
	private int curStatus;
	private int curEncodingSeq;

	public static final int DELAY = 5 * 1000;
	public static final int INTERVAL = 10 * 1000;
	
	public static final int STATUS_WAIT = 100;
	public static final int STATUS_WORKING = 200;
	
	private static EncodingQueueDAO dao = new EncodingQueueDAO();
	
	public EncodingQueue() {
		super();
		this.totalVolume = 0;
		this.curStatus = STATUS_WAIT;
	}
	
	public long getTotalVolume() {
		return totalVolume;
	}
	
	public int getCurStatus() {
		return curStatus;
	}
	
	/**
	 * INTERVAL milisecond 마다 대기열을 확인해 인코딩을 처리하는 스케줄러를 시작하는 메서드
	 */
	public void observeProcess() {
		ScheduledExecutorService scheduler = EncodingService.getScheduler();
		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (curStatus == STATUS_WAIT)
					try {
						process();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}, DELAY, INTERVAL, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 인코딩 작업 로직
	 * 1. DB에서 대기열 조회
	 * 2. 대기열 상태를 running으로 전환
	 * 3. 인코딩 진행
	 * 4. 완료 후 대기열 상태 finished로 전환 및 로그파일 생성
	 * @throws SQLException
	 */
	private void process() throws SQLException {
		curStatus = STATUS_WORKING;
		EncodingQueueVO element = dao.getNextEncodingWork(ServerConfig.getServerId());
		if (element != null) {
			curEncodingSeq = element.getSeq();
			dao.updateCurWorkStartStatus(curEncodingSeq, new Date(), ServerConfig.getServerId());
			SampleVideoDAO fileDao = new SampleVideoDAO();
			element.setVideo(fileDao.selectSampleVideoById(element.getFileId()));
			ArrayList<EncodingQueueOptionVO> options = dao.getEncodingOptions(element.getPresetCode());
			/*
			 * 여기에 파일 다운로드 기능 추가
			 * 추가 후 targetPath로 지정하는것으로 수정할것
			 * 일단은 없이 진행
			 */
			String targetPath = element.getVideo().getDirectory();
			
			//옵션에 f옵션이 있다면 출력 확장자로 지정, 없다면 원본 유지
			String fileExt = null;
			for (EncodingQueueOptionVO opt : options)
				if (opt.getOptionName().equals("f"))
					fileExt = opt.getOptionValue();
			if (fileExt == null)
				fileExt = element.getVideo().getFileExt();

			HddSelector hs = HddSelector.getInstance();
			GenerateFilePathFactory pathFactory = new GenerateFilePathFactory(new Date(), 
					hs.getHDD(element.getVideo().getVolume() * 2));
			String newPath = pathFactory.makeNewPath(element.getPresetCode(), fileExt);

			String newLogPath = pathFactory.makeNewPath(element.getPresetCode(), "txt");
			VideoEncodeProcessCommand cmd = new VideoEncodeProcessCommand(options, targetPath, newPath);
			WindowsAppProcessBuilder wapb = new WindowsAppProcessBuilder(newLogPath);
			LogUtil.printLog("Encoding start, encoding queue seq : " + curEncodingSeq + 
					", preset : " + element.getPresetCode() + ", target : " + element.getVideo().getFileName());
			try {
				if (wapb.process(cmd.generateCmdLine())) {
					File newFile = new File(newPath);
					element.seteVolume(newFile.length());
					element.seteDate(new Date());
					element.setNewDirectory(newPath);
					dao.updateCurWorkEndStatus(curEncodingSeq, element.geteVolume(), element.geteDate(), 
							element.getNewDirectory());
					LogUtil.printLog("Encoding complete, encoding queue seq : " + curEncodingSeq + 
							", preset : " + element.getPresetCode() + ", dest : " + newPath);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			hs.close();
		}
		curStatus = STATUS_WAIT;
	}
	
}
