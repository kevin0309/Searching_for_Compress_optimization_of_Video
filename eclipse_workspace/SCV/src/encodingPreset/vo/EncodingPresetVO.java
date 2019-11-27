package encodingPreset.vo;

import java.util.ArrayList;
import java.util.Date;

public class EncodingPresetVO {

	private String code;
	private String name;
	private Date regdate;
	
	private ArrayList<EncodingPresetOptionVO> opts;

	public EncodingPresetVO(String code, String name, Date regdate) {
		super();
		this.code = code;
		this.name = name;
		this.regdate = regdate;
		this.opts = new ArrayList<>();
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public ArrayList<EncodingPresetOptionVO> getOpts() {
		return opts;
	}
}
