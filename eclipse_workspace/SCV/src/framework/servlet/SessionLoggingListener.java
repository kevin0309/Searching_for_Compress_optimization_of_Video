package framework.servlet;

import java.util.UUID;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 새로운 세션이 만들어졌을 때 동작하는 클래스
 * 랜덤한 accessID 를 attribute로 부여함
 * @author 박유현
 * @since 2019.09.30
 */
@WebListener
public class SessionLoggingListener implements HttpSessionListener{
	
	/**
	 * 세션이 만들어졌을 때 수행되는 메서드
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		session.setMaxInactiveInterval(365*24*60*60);
		session.setAttribute("accessID", UUID.randomUUID().toString());
	}
	/**
	 * 세션이 만료됐을 때 수행되는 메서드
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		
	}
}
