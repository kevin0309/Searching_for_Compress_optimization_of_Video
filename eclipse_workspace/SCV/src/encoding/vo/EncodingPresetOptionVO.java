package encoding.vo;

import java.util.Date;

public class EncodingPresetOptionVO {

	private int seq;
	private String presetCode;
	private String optionName;
	private String optionValue;
	private int orderBy;
	private Date regdate;
	public EncodingPresetOptionVO(int seq, String presetCode, String optionName, String optionValue, int orderBy,
			Date regdate) {
		super();
		this.seq = seq;
		this.presetCode = presetCode;
		this.optionName = optionName;
		this.optionValue = optionValue;
		this.orderBy = orderBy;
		this.regdate = regdate;
	}

	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getPresetCode() {
		return presetCode;
	}
	public void setPresetCode(String presetCode) {
		this.presetCode = presetCode;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public int getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
}
