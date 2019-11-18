package works.imageExtract;

import java.io.File;
import java.util.ArrayList;

import framework.init.ServerConfig;
import framework.util.FileUtil;
import framework.util.windowsAppProcessing.WindowsAppProcessOptions;

public class ExtractKeyframeCommand implements WindowsAppProcessOptions {

	private String videoPath;
	private String videoName;
	
	public ExtractKeyframeCommand(String videoPath) {
		super();
		this.videoPath = videoPath;
		videoName = FileUtil.getFileNameExceptExt(new File(videoPath));
	}

	@Override
	public String[] generateCmdLine() {
		ArrayList<String> temp = new ArrayList<>();
		temp.add(ServerConfig.getFFMPEGPath());
		temp.add("-i");
		temp.add(videoPath);
		temp.add("-vf");
		temp.add("\"select=eq(pict_type\\,I)\"");
		temp.add("-vsync");
		temp.add("vfr");
		temp.add(videoName + "/thumb%03d.jpg");
		
		
		String[] res = new String[temp.size()];
		for (int i = 0; i < temp.size(); i++)
			res[i] = temp.get(i);
		return res;
	}

}
