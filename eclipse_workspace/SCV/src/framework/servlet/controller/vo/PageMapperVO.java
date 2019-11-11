package framework.servlet.controller.vo;

/**
 * 페이지 이동과 관련된 값을 저장해두는 VO
 * @author 박유현
 * @since 2019.09.30
 */
public class PageMapperVO {
	private String viewPageURL;
	private boolean isRedirect;
	
	public PageMapperVO(String viewPageURL) {
		super();
		this.viewPageURL = viewPageURL;
		this.isRedirect = false;
	}
	public PageMapperVO(String viewPageURL, boolean isRedirect) {
		super();
		this.viewPageURL = viewPageURL;
		this.isRedirect = isRedirect;
	}

	public String getViewPageURL() {
		return viewPageURL;
	}
	public void setViewPageURL(String viewPageURL) {
		this.viewPageURL = viewPageURL;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

}
