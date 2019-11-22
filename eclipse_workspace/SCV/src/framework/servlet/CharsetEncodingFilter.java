package framework.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import framework.init.InitConfig;
import framework.init.ServerConfig;

/**
 * 서버가 실행될 때 Charset 인코딩을 설정값으로 바꿔주는 클래스 UTF-8로 고정
 * @author 박유현
 * @since 2019.09.30
 */
@WebFilter (
	urlPatterns = "/*",
	initParams = @WebInitParam(name = "default", value = "UTF-8")
)
public class CharsetEncodingFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding(ServerConfig.getServerEncodingCharSet());
		response.setCharacterEncoding(ServerConfig.getServerEncodingCharSet());
		
		chain.doFilter(request, response);
	}
	/**
	 * 서버가 시작될 때 초기화를 위한 메서드
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		InitConfig.setServerEncodingCharSet(filterConfig.getInitParameter("default"));
	}
	@Override
	public void destroy() {
		
	}	
}
