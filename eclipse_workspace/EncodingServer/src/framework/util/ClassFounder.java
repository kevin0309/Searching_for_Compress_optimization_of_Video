package framework.util;

import java.io.File;
import java.util.ArrayList;
/**
 * 해당 소스 파일 경로에서 특정 handler를 상속받은 모든 class들을 찾는 기능
 * @author 박유현
 * @since 2019.09.30
 */
public class ClassFounder {
	private ArrayList<Class<?>> classes;
	private Class<?> handler;
	
	public ClassFounder(Class<?> handler) {
		super();
		this.classes = new ArrayList<>();;
		this.handler = handler;
	}
	
	private void searchHandlerChildrenRecur(File directory) throws ClassNotFoundException {
		File tmpDirectory;

		if (directory.exists() && directory.isDirectory()) {
			final String[] files = directory.list();
			//.list() : directroy 디렉토리 내에 있는 파일과 디렉토리 이름을 문자열 배열로 반환한다.
			for (final String file : files) {
				if (file.endsWith(".class")) {
					//파일로 존재하지 않는 내부 클래스를 제외
					if (file.indexOf("$") > -1)
						continue;
					
					try {
						String tempPath = directory.getPath();		//디렉토리의 추상경로명을 tempPath에 담는다.
						if (tempPath.indexOf("classes") > -1)
							tempPath = tempPath.split("classes")[1];
						else
							continue;
						
						String[] tempPathArr = tempPath.split("\\\\");	//추상경로명에서 \\\\ 모두 삭제
						String classPath = "";
						for (int i = 1; i < tempPathArr.length; i++)
							classPath+=tempPathArr[i]+".";	//\\\\를 삭제한 추상경로명을 .으로 구분해classPath에 담는다
						classPath+=file.substring(0,file.length() - 6);	//file에서 .java라는 이름을 뺌
						Class<?> tempClass = Class.forName(classPath);	//forName : 인자 스트링의 이름을 가지는 클래스 또는 인터페이스에 관련한 Class 객체를 리턴합니다.
						Class<?>[] tempClassHandler = tempClass.getInterfaces();	//getInterface() :: tempClass가 상속받은 모든 인터페이스를 배열로 리턴
						boolean chk = false;
						for (Class<?> tempHandler : tempClassHandler)
							if (tempHandler.getName().equals(handler.getName()))
								chk = true;
						if (chk)
							classes.add(tempClass);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				else if ((tmpDirectory = new File(directory, file)).isDirectory()) {
					//디렉토리라면 tmpDirectory에 src 디렉토리에 안에 있는새로운 디렉토리 인스턴스 생성한 다음 재귀
					//src 디렉토리에 스트링 file을 나타내는 새로운 File 객체 생성
					searchHandlerChildrenRecur(tmpDirectory);
				}
			}
		}
	}
	
	/**
	 * 해당 소스 파일 경로에서 특정 handler를 상속받은 클래스들을 찾는다.
	 * @param path
	 * @throws ClassNotFoundException
	 */
	public ArrayList<Class<?>> searchHandlerChildren(String path) throws ClassNotFoundException {
		searchHandlerChildrenRecur(new File(path));
		return classes;
	}
}
