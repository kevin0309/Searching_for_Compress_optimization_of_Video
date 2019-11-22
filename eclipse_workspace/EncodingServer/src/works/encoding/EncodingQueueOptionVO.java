package works.encoding;

/**
 * DB encoding_preset_option 테이블 대응 VO
 * @author 박유현
 * @since 2019.10.13
 */
public class EncodingQueueOptionVO {

	private String optionName;
	private String optionValue;
	private int order;

	public EncodingQueueOptionVO(String optionName, String optionValue, int order) {
		super();
		this.optionName = optionName;
		this.optionValue = optionValue;
		this.order = order;
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
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
