package framework.util.windowsAppProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import framework.util.LogUtil;

/**
 * Windows OS에서 프로그램을 실행하고 출력을 받는 클래스
 * @author 박유현
 * @since 2019.09.30
 */
public class WindowsAppProcessBuilder {

	private boolean saveLogToFile;
	private String logFilePath;
	private File logFile;
	private BufferedWriter bw;
	private ArrayList<String> logs;
	
	public WindowsAppProcessBuilder() {
		this.saveLogToFile = false;
		this.logFilePath = null;
		logs = new ArrayList<>();
	}
	
	public WindowsAppProcessBuilder(String logFilePath) {
		this.saveLogToFile = true;
		this.logFilePath = logFilePath;
		logs = new ArrayList<>();
	}
	
	/**
	 * 윈도우 응용프로그램(exe파일 등..) 실행시키는 유틸
	 * 개발모드(서버 전역설정 devmode)일때는 실행결과가 콘솔창에 출력됨
	 * @param cmdLine
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public boolean process(String[] cmdLine) throws IOException {
		if (saveLogToFile) {
			logFile = new File(logFilePath);
			bw = new BufferedWriter(new FileWriter(logFile));
		}
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
			bw.close();
			return false;
		}
		exhaustInputStream(p.getInputStream());   // 자식 프로세스에서 발생되는 inputstrem를 소비시켜야합니다.

		try {
			// p의 자식 프로세스의 작업이 완료될 동안 p를 대기시킴
			p.waitFor();
		} catch (InterruptedException e) {
			p.destroy();
			bw.close();
		}
		
		// 정상 종료가 되지 않았을 경우
		if (p.exitValue() != 0) {
			LogUtil.printLog("프로세스가 정상종료되지 않음.");
			bw.close();
			return false;
		}
		p.destroy();
		bw.close();
		return true;
	}
	
	private void exhaustInputStream(final InputStream is) {
		// InputStream.read() 에서 블럭상태에 빠지기 때문에 따로 쓰레드를 구현하여 스트림을 소비한다
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			if (saveLogToFile) { //로그파일로 저장
				String cmd = null;
				while((cmd = br.readLine()) != null) {// 읽어들일 라인이 없을때까지 계속 반복
					bw.write(cmd);
					bw.newLine();
				}
			}
			else { //배열에 저장
				String cmd = null;
				while((cmd = br.readLine()) != null) // 읽어들일 라인이 없을때까지 계속 반복
					logs.add(cmd);
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getLogs() {
		return logs;
	}
}
