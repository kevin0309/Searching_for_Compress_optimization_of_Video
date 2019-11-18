package works.imageExtract;

import java.util.ArrayList;

import framework.init.ServerConfig;
import framework.util.windowsAppProcessing.WindowsAppProcessOptions;

public class ExtractKeyframeCommand implements WindowsAppProcessOptions {

	private String videoPath;
	private String videoThumbPath;
	
	public ExtractKeyframeCommand(String videoPath, String videoThumbPath) {
		super();
		this.videoPath = videoPath;
		this.videoThumbPath = videoThumbPath;
	}

	@Override
	public String[] generateCmdLine() {
		ArrayList<String> temp = new ArrayList<>();
		temp.add(ServerConfig.getFFMPEGPath());
		temp.add("-i");
		temp.add(videoPath);
		temp.add("-vf");
		//temp.add("\"select=eq(pict_type\\,I)\"");
		//temp.add("-vsync");
		//temp.add("vfr");
		temp.add("fps=1/2");
		temp.add(videoThumbPath);
		
		String[] res = new String[temp.size()];
		for (int i = 0; i < temp.size(); i++)
			res[i] = temp.get(i);
		return res;
	}

}
