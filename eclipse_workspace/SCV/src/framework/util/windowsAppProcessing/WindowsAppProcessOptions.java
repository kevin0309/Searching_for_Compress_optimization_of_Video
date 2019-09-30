package framework.util.windowsAppProcessing;

import org.json.simple.JSONArray;

public interface WindowsAppProcessOptions {

	public String[] generateCmdLine();
	public JSONArray generateJSONCmdLine();
}
