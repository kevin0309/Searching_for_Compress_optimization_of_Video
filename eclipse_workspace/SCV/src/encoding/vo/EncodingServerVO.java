package encoding.vo;

import java.util.Date;

public class EncodingServerVO {
	private int seq;
	private String desc;
	private String address;
	private String macAddress;
	private int status;
	private Date regdate;
	public EncodingServerVO(int seq, String desc, String address, String macAddress, int status, Date regdate) {
		super();
		this.seq = seq;
		this.desc = desc;
		this.address = address;
		this.macAddress = macAddress;
		this.status = status;
		this.regdate = regdate;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
}
