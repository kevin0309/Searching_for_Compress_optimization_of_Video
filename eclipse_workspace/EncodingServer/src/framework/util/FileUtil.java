package framework.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * 파일을 핸들링 할 때 편한 기능을 모아둔 클래스
 * @author 박유현
 * @since 2019.09.30
 */
public class FileUtil {
	
	/**
	 * 해당 경로에 파일 생성 뒤 입력받은 문자열 입력
	 * @param filePath
	 * @param str
	 */
	public static void write(String filePath, String str) {
		try {
			File file = new File(filePath);
			if (!file.exists())
				file.getParentFile().mkdirs();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(str);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			LogUtil.printErrLog("write File error");
			throw new RuntimeException(e);
		}
	}

	/**
	 * 해당 경로에 파일 생성 뒤 입력받은 문자열 입력
	 * @param filePath
	 * @param strArr
	 */
	public static void write(String filePath, ArrayList<String> strArr) {
		try {
			File file = new File(filePath);
			if (!file.exists())
				file.getParentFile().mkdirs();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (String str : strArr) {
				bw.write(str);
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			LogUtil.printErrLog("write File error");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 해당 경로 파일을 읽어 내용을 ArrayList<String>으로 반환
	 * @param file
	 * @return
	 */
	public static ArrayList<String> readAll(File file) {
		ArrayList<String> result = new ArrayList<>();
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			while (true) {
				String str = br.readLine();
				if (str == null)
					break;
				else
					result.add(str);
			}
			
			br.close();
			return result;
		} catch (IOException e) {
			LogUtil.printErrLog("read File error");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 해당 경로에 파일 생성 뒤 입력받은 문자열을 파일 마지막에 추가
	 * @param filePath
	 * @param strArr
	 */
	public static void add(File file, ArrayList<String> strArr) {
		ArrayList<String> curFileCon = readAll(file);
		curFileCon.addAll(strArr);
		write(file.getPath(), strArr);
	}
	
	/**
	 * 파일을 다른 경로로 이동
	 * @param file
	 * @param newFilePath
	 */
	public static void moveFile(File file, String newFilePath) {
		if (file.exists()) {
			File newFile = new File(newFilePath);
			newFile.getParentFile().mkdirs();
			file.renameTo(newFile);
		}
	}
	
	/**
	 * 파일을 다른 경로에 복사
	 * @param file
	 * @param newFilePath
	 */
	public static void copyFile(File file, String newFilePath) {
		try {
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(newFilePath);
			FileChannel fin = fis.getChannel();
			FileChannel fout = fos.getChannel();
			
			long size = fin.size();
			fin.transferTo(0, size, fout);
			
			fout.close();
			fin.close();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 파일에서 확장자만 추출하여 리턴, 확장자 없는경우 "" 리턴
	 * @param file
	 * @return 파일 확장자
	 */
	public static String getExt(File file) {
		if (file.getName().lastIndexOf(".") < 0)
			return "";
		return file.getName().substring(file.getName().lastIndexOf(".")+1);
	}
	
	/**
	 * 파일에서 확장자를 제외한 파일명을 리턴
	 * @param file
	 * @return
	 */
	public static String getFileNameExceptExt(File file) {
		String fileName;
		try {
			fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			//확장자가 없는파일 처리
			fileName = file.getName();
		}
		return fileName;
	}
	
	/**
	 * 해당 경로에 폴더가 없을경우 폴더 생성
	 * @param newDirectoryPath
	 */
	public static void makeNewDirectory(String newDirectoryPath) {
		File directory = new File(newDirectoryPath);
		if (!directory.exists())
			directory.mkdirs();
	}
	
	/**
	 * long값의 용량을 용량단위로 변환하여 리턴
	 * @param v
	 * @return
	 */
	public static String generateVolumeStr(long v) {
		String res = "";
		double temp = v;
		if (temp < 1024)
			res += Math.round(temp*100)/100d + " Byte";
		else if ((temp /= 1024) < 1024)
			res += Math.round(temp*100)/100d + " KB";
		else if ((temp /= 1024) < 1024)
			res += Math.round(temp*100)/100d + " MB";
		else if ((temp /= 1024) < 1024)
			res += Math.round(temp*100)/100d + " GB";
		else if ((temp /= 1024) < 1024)
			res += Math.round(temp*100)/100d + " TB";
		else if ((temp /= 1024) < 1024)
			res += Math.round(temp*100)/100d + " PB";
		
		return res;
	}
	
	/**
	 * byte 스트림으로부터 charset을 추측하는 메서드
	 * Google API - juniversalchardet 2.1.0v 사용
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String detectCharset(File file) throws IOException {
		UniversalDetector det = new UniversalDetector(null);
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		det.handleData(fileBytes, 0, fileBytes.length);
		det.dataEnd();
		return det.getDetectedCharset();
	}
	
	/**
	 * 파일의 MIME type을 판별하여 리턴
	 * 확인 불가능한 경우 "unknown" 리턴
	 * @param file
	 * @return
	 */
	public static String getMIMEType(File file) {
		String mimeType;
		try {
			mimeType = Files.probeContentType(file.toPath());
		} catch (IOException e) {
			mimeType = URLConnection.guessContentTypeFromName(file.getName());
		}
		if (mimeType == null)
			mimeType = "unknown";
		
		return mimeType;
	}
}
