package framework.util.windowsAppProcessing;

/**
 * WindowsAppProcessBuilder 클래스에서 사용되는 인터페이스
 * 프로세스의 argument 부분을 구현
 * @author 박유현
 * @since 2019.09.30
 * @see framework.util.windowsAppProcessing.WindowsAppProcessBuilder
 */
public interface WindowsAppProcessOptions {

	/**
	 * java.lang.ProcessBuilder 에서 사용되는 프로세스 실행 커맨드를 생성하는 기능
	 * @return
	 * @see java.lang.ProcessBuilder
	 */
	public String[] generateCmdLine();
}
