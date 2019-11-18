package framework.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import framework.init.ServerHddVO;

/**
 * 업로드된 파일이 저장될 경로를 만드는 클래스
 * 연도, 월별로 폴더를 만들어 관리
 * @author 박유현
 * @since 2019.10.13
 */
public class GenerateFilePathFactory {

	private Date date;
	private ServerHddVO currentHdd;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
	private String curYear;
	private String curMonth;
	private String id;
	
	public GenerateFilePathFactory(Date date, ServerHddVO currentHdd) {
		this.date = date;
		this.currentHdd = currentHdd;
		curYear = sdf.format(date);
		curMonth = sdf2.format(date);
		id = RandomStringGenerator.getRandomURLSafeString(8);
		FileUtil.makeNewDirectory(currentHdd.getFullPath()+"/"+curYear+"/"+curMonth);
	}
	
	/**
	 * DB에 저장될 파일명
	 * {현재시각}_{랜덤코드 8자리}.{원본확장자}
	 * @param fileExt
	 * @return
	 */
	private String toNewFileNameString(String fileExt, String fileDivision) {
		String res = "";
		res += date.getTime();
		res += "_" + id;
		if (fileDivision != null)
			res += "_" + fileDivision;
		res += "." + fileExt;
		return res;
	}
	
	/**
	 * 파일이 저장될 폴더까지의 절대경로
	 * 해당 폴더가 존재하지 않을경우 만들어준다.
	 * @param fileDivision
	 * @return 파일이 저장될 폴더까지의 절대경로
	 */
	private String toDirAbsolutePathString(String fileDivision) {
		String res = "";
		res += currentHdd.getFullPath();
		res += "/" + curYear;
		res += "/" + curMonth;
		if (fileDivision != null) {
			res += "/" + fileDivision;
			FileUtil.makeNewDirectory(res);
		}
		
		return res;
	}
	
	/**
	 * 파일이 저장될 폴더까지의 절대경로 + 파일명
	 * 해당 폴더가 존재하지 않을경우 만들어준다.
	 * @param fileDivision
	 * @param fileExt
	 * @return 파일이 저장될 폴더까지의 절대경로 + 파일명
	 */
	public String makeNewPath(String fileDivision, String fileExt) {
		String res = "";
		res += toDirAbsolutePathString(fileDivision);
		res += "/" + toNewFileNameString(fileExt, fileDivision);
		return res;
	}
	
	/**
	 * 파일이 저장될 폴더까지의 절대경로 + 파일명
	 * 해당 폴더가 존재하지 않을경우 만들어준다.
	 * @param fileDivision
	 * @param fileExt
	 * @return 파일이 저장될 폴더까지의 절대경로 + 파일명
	 */
	public String makeNewThumbPath(String fileDivision) {
		String res = "";
		res += toDirAbsolutePathString(fileDivision);
		res += "/" + date.getTime() + "_" + id;
		if (fileDivision != null)
			res += "_" + fileDivision;
		res += "/";
		File file = new File(res);
		file.mkdir();
		return res;
	}
}
