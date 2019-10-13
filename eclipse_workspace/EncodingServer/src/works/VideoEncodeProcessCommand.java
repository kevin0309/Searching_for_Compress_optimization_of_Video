package works;

import java.util.ArrayList;

import framework.init.ServerConfig;
import framework.util.windowsAppProcessing.WindowsAppProcessOptions;

/**
 * 비디오 인코딩 프로세스 커맨드를 구현한 클래스
 * @author 박유현
 * @since 2019.10.13
 */
public class VideoEncodeProcessCommand implements WindowsAppProcessOptions {
	
	private ArrayList<EncodingQueueOptionVO> commandOptions;
	private String targetPath;
	private String newPath;
	
	public VideoEncodeProcessCommand(ArrayList<EncodingQueueOptionVO> commandOptions, String targetPath, String newPath) {
		super();
		this.commandOptions = commandOptions;
		this.targetPath = targetPath;
		this.newPath = newPath;
	}

	@Override
	public String[] generateCmdLine() {
		ArrayList<String> temp = new ArrayList<>();
		temp.add(ServerConfig.getFFMPEGPath());
		temp.add("-i");
		temp.add(targetPath);
		for (EncodingQueueOptionVO opt : commandOptions) {
			temp.add("-"+opt.getOptionName());
			if (opt.getOptionValue() != null)
				temp.add(opt.getOptionValue());
		}
		temp.add(newPath);
		
		String[] res = new String[temp.size()];
		for (int i = 0; i < temp.size(); i++)
			res[i] = temp.get(i);
		return res;
	}

}
