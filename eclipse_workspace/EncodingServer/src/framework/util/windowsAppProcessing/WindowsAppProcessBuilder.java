package framework.util.windowsAppProcessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import framework.init.ServerConfig;
import framework.util.LogUtil;

/**
 * Windows OS에서 프로그램을 실행하고 출력을 받는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
public class WindowsAppProcessBuilder {

	private ArrayList<String> processLogs = new ArrayList<>();
	private boolean saveLog;
	
	public WindowsAppProcessBuilder(boolean saveLog) {
		this.saveLog = saveLog;
		if (saveLog)
			processLogs = new ArrayList<>();
	}
	
	/**
	 * 윈도우 응용프로그램(exe파일 등..) 실행시키는 유틸
	 * 개발모드(서버 전역설정 devmode)일때는 실행결과가 콘솔창에 출력됨
	 * @param cmdLine
	 * @return
	 */
	public boolean process(String[] cmdLine) {
		// 프로세스 속성을 관리하는 ProcessBuilder 생성.
		ProcessBuilder pb = new ProcessBuilder(cmdLine);
		pb.redirectErrorStream(true);
		Process p = null;
		try {
			p = pb.start();
		} catch (Exception e) {
			e.printStackTrace();
			p.destroy();
			LogUtil.printLog("프로세스 진행 중 에러 발생");
			return false;
		}
		exhaustInputStream(p.getInputStream());   // 자식 프로세스에서 발생되는 inputstrem를 소비시켜야합니다.

		try {
			// p의 자식 프로세스의 작업이 완료될 동안 p를 대기시킴
			p.waitFor();
		} catch (InterruptedException e) {
			p.destroy();
		}
		
		// 정상 종료가 되지 않았을 경우
		if (p.exitValue() != 0) {
			LogUtil.printLog("프로세스가 정상종료되지 않음.");
			return false;
		 }
		p.destroy();
		return true;
	}
	
	private void exhaustInputStream(final InputStream is) {
		// InputStream.read() 에서 블럭상태에 빠지기 때문에 따로 쓰레드를 구현하여 스트림을 소비한다
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			if (ServerConfig.isDev()) {
				//콘솔창에 출력
				String cmd = null;
				while((cmd = br.readLine()) != null) {// 읽어들일 라인이 없을때까지 계속 반복
					System.out.println(cmd);
					if (saveLog)
						processLogs.add(cmd);
				}
			}
			else {
				//콘솔 출력X
				String cmd = null;
				while((cmd = br.readLine()) != null) // 읽어들일 라인이 없을때까지 계속 반복
					if (saveLog)
						processLogs.add(cmd);
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 출력결과를 담고있는 ArrayList 리턴
	 * @return
	 */
	public ArrayList<String> getProcessLogs() {
		return processLogs;
	}
}
