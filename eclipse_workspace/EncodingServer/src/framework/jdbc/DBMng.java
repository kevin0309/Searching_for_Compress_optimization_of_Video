package framework.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * JDBC 사용 시 활용하는 여러 클래스들을 한곳에서 관리해주는 클래스<br>
 * STATUS를 이용하여 메소드 접근의 순서를 지키게 되어있음<br><br>
 * 
 * 기본적으로 사용한 뒤 close()메소드로 사용한 자원을 반환해줘야함<br>
 * close()를 사용하지 않았을 경우를 대비해 5초 후 자동으로 반환이 되도록 만들어져있음<br>
 * setAutoClose()메소드로 자동반환 비활성화가능<br><br>
 * 
 * autoCommit이 false일 때 commit()을 하지 않고 close()를 할 시 rollback됨<br><br>
 * 
 * 기본적인 사용절차<br>
 * 1. DBMng 객체 생성(생성자 종류 확인)<br>
 * 2. setConnection()을 사용하여 Connection 설정<br>
 * 3. setQuery()을 사용하여 사용할 SQL문 설정<br>
 * 4. SQL내 설정할 파라미터가 있을 시 set메소드를 사용하여 파라미터 추가<br>
 * 5. 파라미터 추가 완료 후 execute()를 사용하여 SQL문 execute<br>
 * 6. execute 후 next(), get메소드를 사용하여 결과값 처리<br>
 * 7. 모든 작업 완료 후 close메소드를 사용하여 자원 반환<br><br>
 * 
 * (SQLException을 핸들링해야함)
 * @since 2017-02-01
 * @version 1.0  2017-02-01 초본완성
 * @version 1.1  2017-02-10 1. autoClose기능 오류 수정																										<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 						   2. 전체적인 메소드들의 코딩방식 획일화																									<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 						   3. exception 핸들링 이상한 부분 수정																								<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 						   4. 에러메세지 출력기능 추가																											<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 						   5. 진행상 논리오류 수정																											<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 						   6. 주석추가
 * @version 1.2  2017-02-17 특정 Java 버전에서 PreparedStatement와 ResultSet의 isClosed()가 오류나는 문제 발생하여 수정
 * @version 1.3  2017-03-22 객체 선언 후 autoCommit설정을 할 수 있는 메소드, rollback하는 메소드 추가
 * @version 1.4  2017-08-08 1. 새로 옮겨적는 과정에서 불필요한부분 일부 삭제																							<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 						   2. close()메소드에서 ResultSet이 null일경우 에러 수정
 * @version 1.5  2017-08-09 MySQL에서 사용 가능하게 excute()메소드 수정(MySQL에서는 excuteQuery()메소드가 불안정함)
 * @version 1.6  2017-08-10 1. set~~() 메소드를 인덱스 없이 사용할 수 있는 메소드들 추가																				<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *						   2. autoClose의 시간을 조절할 수 있는 기능 추가 setAutoCloseTimeout() 메소드 사용
 * @version 1.7  2018-07-16 resultSet의 메타데이터 조회를 위한  메서드 추가
 * @version 1.8  2018-12-25 1. status들을 DBMngStatus라는 별도의 클래스로 분리																					<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 						   2. 전체적인 오타수정 및 코드간략화, 주석작업																								<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * 						   3. setAutoCloseTimeout() 기능 삭제, 안쓰이는 몇몇 메서드도 삭제
 * @version 1.9  2018-12-29 DBMng autoclose 기능을 스케줄러에서 관리하도록 DBMngScheduler로 분리
 * @version 1.10 2019-10-13 convertTimestampToDate 파라미터가 null일 경우 오류 수정
 * @author 박유현
 * @example 
 * -----Example ({}안은 사용자 입력)-----<br><br>
 * DBMng dbMng = null;<br><br>
 * 
 * try {<blockquote>
 * 		dbMng = new DBMng();<br>
 * 		dbMng.setConnection({사용 가능한 Connection});<br>
 * 		dbMng.setQuery({SQL문});<br>
 * 		dbMng.setString({지정해야 할 파라미터가 있을 시 파라미터 값});<br>
 * 		dbMng.execute();<br><br>
 * 
 * 		while(dbMng.next()) {<blockquote>
 * 			{결과값을 사용한 로직 구현}</blockquote>
 * 		}</blockquote>
 * } catch (SQLException e) {<blockquote>
 * 		e.printStackTrace();</blockquote>
 * } finally {<blockquote>
 * 		dbMng.close();</blockquote>
 * }
 *
 */
public class DBMng {
	/**
	 * 실행 중 발견된 에러에 관한 메세지를 담아놓는 객체
	 */
	private ArrayList<String> errors = new ArrayList<>();
	
	/**
	 * DBMng 객체의 현재 상태를 저장하는 변수
	 */
	private DBMngStatus STATUS;
	private boolean isAutoClose = true;
	private int autoCloseTimeout = 1000*5;
	private Connection conn;
	private PreparedStatement pstmt;
	private ArrayList<Boolean> pstmtParamCnt = new ArrayList<>();
	private ResultSet rs;
	
	/**
	 * JDBC 사용시 편하게 사용하기 위한 클래스<br>
	 * setConnection(Connection connection)을 사용하여 커넥션을 설정해야함<br>
	 * 현재 자동으로 ConnectionProvider에서 받아오도록 설정하여 안해도됨
	 * @throws SQLException
	 */
	public DBMng() throws SQLException {
		setDBMngStatus(DBMngStatus.NOT_CONNECTED);
		setConnection(ConnectionProvider.getConnection());
	}
	
	/**
	 * JDBC 사용시 편하게 사용하기 위한 클래스
	 * @param connection
	 * @throws SQLException
	 */
	public DBMng(Connection connection) throws SQLException {
		setDBMngStatus(DBMngStatus.NOT_CONNECTED);
		setConnection(connection);
	}
	
	/**
	 * DBMng객체의 커넥션을 설정한다.<br>
	 * 사용 가능 상태<br>
	 * STATUS_NOT_CONNECTED,<br>
	 * STATUS_CONNECTED,<br>
	 * STATUS_CLOSED
	 * @param connection
	 * @throws SQLException
	 */
	private void setConnection(Connection connection) throws SQLException {
		if (connection == null || (connection.isClosed() && STATUS != DBMngStatus.CLOSED) || connection.isReadOnly()) {
			setDBMngStatus(DBMngStatus.SET_CONNECTION_ERR);
			if (connection == null)
				addError("커넥션이 NULL입니다.");
			if (connection.isClosed())
				addError("커넥션이 closed 된 상태입니다.");
			if (connection.isReadOnly())
				addError("커넥션이 readonly 상태입니다.");
			throw new SQLException(getErrors());
		}
		if (STATUS == DBMngStatus.NOT_CONNECTED || STATUS == DBMngStatus.CLOSED) {
			conn = connection;
			setDBMngStatus(DBMngStatus.CONNECTED);
			isClosed();
		}
		else if (STATUS == DBMngStatus.CONNECTED) {
			conn.close();
			conn = connection;
			isClosed();
		}
		else if (STATUS == DBMngStatus.ERROR) {
			addError("알수없는 에러");
			throw new SQLException(getErrors());
		}
		else {
			setDBMngStatus(DBMngStatus.INCORRECT_STATUS_ERR);
			addError("알맞지 않은 메소드 접근 순서입니다.");
			throw new SQLException(getErrors());
		}
	}
	
	/**
	 * 커넥션의 autoCommit 속성을 변경한다.<br>
	 * 사용 가능 상태<br>
	 * STATUS_CONNECTED,<br>
	 * STATUS_STATEMENT_READY,<br>
	 * STATUS_PARAMETER_READY
	 * @param autoCommit
	 * @throws SQLException
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		if (checkSTATUS(new DBMngStatus[] {DBMngStatus.CONNECTED, DBMngStatus.STATEMENT_READY, DBMngStatus.PARAMETER_READY}))
			conn.setAutoCommit(autoCommit);
	}
	
	/**
	 * DBMng에서 사용할 SQL문을 지정한다.<br>
	 * 사용 가능 상태<br>
	 * STATUS_CONNECTED,<br>
	 * STATUS_EXECUTED
	 * @param sql
	 * @throws SQLException
	 */
	public void setQuery(String sql) throws SQLException {
		if (checkSTATUS(new DBMngStatus[] {DBMngStatus.CONNECTED, DBMngStatus.EXECUTED})) {
			pstmt = conn.prepareStatement(sql);
			
			if (pstmt.getParameterMetaData().getParameterCount() == 0)
				setDBMngStatus(DBMngStatus.PARAMETER_READY);
			else
				setDBMngStatus(DBMngStatus.STATEMENT_READY);
		}
	}

	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 파라미터 설정의 순서를 지키게 되어있음.
	 * @param index
	 * @param param
	 * @throws SQLException
	 */
	public void setShort(int index, short param) throws SQLException {
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setShort(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 자동으로 증가되는 인덱스를 사용한다.
	 * @param param
	 * @throws SQLException
	 */
	public void setShort(short param) throws SQLException {
		int index = pstmtParamCnt.size()+1;
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setShort(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 파라미터 설정의 순서를 지키게 되어있음.
	 * @param index
	 * @param param
	 * @throws SQLException
	 */
	public void setInt(int index, int param) throws SQLException {
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setInt(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 자동으로 증가되는 인덱스를 사용한다.
	 * @param param
	 * @throws SQLException
	 */
	public void setInt(int param) throws SQLException {
		int index = pstmtParamCnt.size()+1;
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setInt(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 파라미터 설정의 순서를 지키게 되어있음.
	 * @param index
	 * @param param
	 * @throws SQLException
	 */
	public void setLong(int index, long param) throws SQLException {
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setLong(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 자동으로 증가되는 인덱스를 사용한다.
	 * @param param
	 * @throws SQLException
	 */
	public void setLong(long param) throws SQLException {
		int index = pstmtParamCnt.size()+1;
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setLong(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 파라미터 설정의 순서를 지키게 되어있음.
	 * @param index
	 * @param param
	 * @throws SQLException
	 */
	public void setFloat(int index, float param) throws SQLException {
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setFloat(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 자동으로 증가되는 인덱스를 사용한다.
	 * @param param
	 * @throws SQLException
	 */
	public void setFloat(float param) throws SQLException {
		int index = pstmtParamCnt.size()+1;
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setFloat(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 파라미터 설정의 순서를 지키게 되어있음.
	 * @param index
	 * @param param
	 * @throws SQLException
	 */
	public void setDouble(int index, double param) throws SQLException {
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setDouble(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 자동으로 증가되는 인덱스를 사용한다.
	 * @param param
	 * @throws SQLException
	 */
	public void setDouble(double param) throws SQLException {
		int index = pstmtParamCnt.size()+1;
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setDouble(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 파라미터 설정의 순서를 지키게 되어있음.
	 * @param index
	 * @param param
	 * @throws SQLException
	 */
	public void setString(int index, String param) throws SQLException {
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setString(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 자동으로 증가되는 인덱스를 사용한다.
	 * @param param
	 * @throws SQLException
	 */
	public void setString(String param) throws SQLException {
		int index = pstmtParamCnt.size()+1;
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setString(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 파라미터 설정의 순서를 지키게 되어있음.
	 * @param index
	 * @param param java.util.Date
	 * @throws SQLException
	 */
	public void setDate(int index, Date param) throws SQLException {
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setTimestamp(index, convertDateToTimestamp(param));
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 자동으로 증가되는 인덱스를 사용한다.
	 * @param param java.util.Date
	 * @throws SQLException
	 */
	public void setDate(Date param) throws SQLException {
		int index = pstmtParamCnt.size()+1;
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setTimestamp(index, convertDateToTimestamp(param));
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 파라미터 설정의 순서를 지키게 되어있음.
	 * @param index
	 * @param param
	 * @throws SQLException
	 */
	public void setTimestamp(int index, Timestamp param) throws SQLException {
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setTimestamp(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * SQL문이 설정된 후 파라미터를 설정한다.<br>
	 * 자동으로 증가되는 인덱스를 사용한다.
	 * @param param
	 * @throws SQLException
	 */
	public void setTimestamp(Timestamp param) throws SQLException {
		int index = pstmtParamCnt.size()+1;
		if (checkParams(index) && checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			pstmt.setTimestamp(index, param);
			pstmtParamCnt.add(true);
			checkStatement();
		}
	}
	
	/**
	 * pstmt에서 다음으로 쓰일 파라미터의 순서를 확인한다.
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	private boolean checkParams(int index) throws SQLException {
		if (checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			if (pstmtParamCnt.size() != index-1) {
				setDBMngStatus(DBMngStatus.NOT_MATCH_PARAM_ERR);
				addError("파라미터는 순서대로 세팅헤야함.");
				throw new SQLException(getErrors());
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * pstmt의 파라미터 갯수가 맞는지 확인한다.
	 * @throws SQLException
	 */
	private void checkStatement() throws SQLException {
		if (checkSTATUS(DBMngStatus.STATEMENT_READY)) {
			try {
				int paramCnt = pstmt.getParameterMetaData().getParameterCount();
				
				if (paramCnt == pstmtParamCnt.size())
					setDBMngStatus(DBMngStatus.PARAMETER_READY);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.ERROR);
				addError("세팅된 SQL문의 파라미터를 확인 할 수 없습니다.");
				throw new SQLException(getErrors());
			}
		}
	}
	
	/**
	 * PreparedStatement에 세팅된 파라미터를 비운다.
	 * @throws SQLException
	 */
	public void clearParams() throws SQLException {
		if (checkSTATUS(new DBMngStatus[] {DBMngStatus.STATEMENT_READY, DBMngStatus.EXECUTED})) {
			pstmt.clearParameters();
			pstmtParamCnt = new ArrayList<>();
		}
	}
	
	/**
	 * 쿼리를 실행시킨 후 결과를 ResultSet 객체에 저장한다.
	 * 이후 next(), get~~() 메소드를 이용하여 ResultSet에 저장된 값을 조회할 수 있다.
	 * @throws SQLException
	 */
	public void execute() throws SQLException {
		if (checkSTATUS(new DBMngStatus[] {DBMngStatus.PARAMETER_READY, DBMngStatus.STATEMENT_READY})) {
			if (STATUS == DBMngStatus.PARAMETER_READY) {
				pstmt.execute();
				rs = pstmt.getResultSet();
				setDBMngStatus(DBMngStatus.EXECUTED);
				clearParams();
			}
			else {
				setDBMngStatus(DBMngStatus.NOT_MATCH_PARAM_ERR);
				addError("파라미터가 모두 초기화되지 않았습니다.");
				throw new SQLException(getErrors());
			}
		}
	}
	
	/**
	 * autoCommit이 false일 때 execute시킨 Connection을 최종적으로 commit한다.
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED))
			conn.commit();
	}
	
	/**
	 * ResultSet 객체 내 다음값을 조회한다.
	 * @return
	 * @throws SQLException
	 */
	public boolean next() throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED))
			return rs.next();
		else
			return false;
	}
	
	/**
	 * 조회된 ResultSet의 metadata를 조회한다.
	 * @return
	 * @throws SQLException
	 */
	public ResultSetMetaData getResultSetMetaData() throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED))
			return rs.getMetaData();
		else
			return null;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼인덱스의 결과값을 반환한다.
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public short getShort(int columnIndex) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getShort(columnIndex);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 인덱스의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼명의 결과값을 반환한다.
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 */
	public short getShort(String columnLabel) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getShort(columnLabel);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 이름의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼인덱스의 결과값을 반환한다.
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public int getInt(int columnIndex) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getInt(columnIndex);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 인덱스의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼명의 결과값을 반환한다.
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 */
	public int getInt(String columnLabel) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getInt(columnLabel);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 이름의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼인덱스의 결과값을 반환한다.
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public long getLong(int columnIndex) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getLong(columnIndex);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 인덱스의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼명의 결과값을 반환한다.
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 */
	public long getLong(String columnLabel) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getLong(columnLabel);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 이름의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼인덱스의 결과값을 반환한다.
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public float getFloat(int columnIndex) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getFloat(columnIndex);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 인덱스의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼명의 결과값을 반환한다.
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 */
	public float getFloat(String columnLabel) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getFloat(columnLabel);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 이름의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼인덱스의 결과값을 반환한다.
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public double getDouble(int columnIndex) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getDouble(columnIndex);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 인덱스의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼명의 결과값을 반환한다.
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 */
	public double getDouble(String columnLabel) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getDouble(columnLabel);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 이름의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return 0;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼인덱스의 결과값을 반환한다.
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public Date getDate(int columnIndex) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return convertTimestampToDate(rs.getTimestamp(columnIndex));
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 인덱스의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return null;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼명의 결과값을 반환한다.
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 */
	public Date getDate(String columnLabel) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return convertTimestampToDate(rs.getTimestamp(columnLabel));
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 이름의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return null;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼인덱스의 결과값을 반환한다.
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public String getString(int columnIndex) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getString(columnIndex);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 인덱스의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return null;
	}
	
	/**
	 * ResultSet 객체의 현재 row의 해당 컬럼명의 결과값을 반환한다.
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 */
	public String getString(String columnLabel) throws SQLException {
		if (checkSTATUS(DBMngStatus.EXECUTED)) {
			try {
				return rs.getString(columnLabel);
			} catch (SQLException e) {
				setDBMngStatus(DBMngStatus.COLUMN_NOT_FOUND_ERR);
				addError("해당 이름의 컬럼을 찾지 못했습니다.");
				throw new SQLException(getErrors());
			}
		}
		else
			return null;
	}
	
	/**
	 * excute된 진행상황을 rollback시킨다.
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		try {
			conn.rollback();
		} catch (SQLException e) {
			setDBMngStatus(DBMngStatus.ERROR);
			addError("커넥션을 rollback 시키는데에 실패했습니다.");
			throw new SQLException(getErrors());
		}
	}
	
	/**
	 * DBMng의 모든 자원을 반환한다.
	 */
	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				if (!conn.isClosed()) {
					if (!conn.getAutoCommit())
						conn.rollback();
					conn.close();
				}
			errors = new ArrayList<>();
			setDBMngStatus(DBMngStatus.CLOSED);
		} catch (Exception e) {
			e.printStackTrace();
			setDBMngStatus(DBMngStatus.ERROR);
			addError("DBMng의 자원을 반환하는데 실패했습니다.");
			throw new RuntimeException(getErrors(), e);
		}
	}
	
	/**
	 * DBMng의 close 되지 않은 자원을 autoCloseTimeout 시간 후 자동으로 반환시킨다.<br>
	 * setAutoClose() 로 비활성화 할 수 있다.
	 */
	private void isClosed() {
		if (isAutoClose) {
			ScheduledExecutorService scheduler = DBMngScheduler.getScheduler();
			scheduler.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						if (conn != null)
							if (!conn.isClosed())
								if (isAutoClose) {
									System.out.println("DBMng status - Unclosed DB resource detected!");
									close();
									setDBMngStatus(DBMngStatus.RESOURCES_NOT_CLOSED);
								}
					} catch (SQLException e) {
						System.err.println(e);
					}
				}
			}, autoCloseTimeout, TimeUnit.MILLISECONDS);
		}
	}
	
	/**
	 * 현재 STATUS가 해당 값이 맞는지 확인한다.
	 * @param a
	 * @return
	 * @throws SQLException
	 */
	private boolean checkSTATUS(DBMngStatus status) throws SQLException {
		if (STATUS == status)
			return true;
		else if (STATUS == DBMngStatus.ERROR) {
			addError("알 수 없는 에러 발생!");
			throw new SQLException(getErrors());
		}
		else {
			setDBMngStatus(DBMngStatus.INCORRECT_STATUS_ERR);
			addError("알맞지 않은 메소드 접근 순서입니다.");
			throw new SQLException(getErrors());
		}
	}
	
	/**
	 * 현재 STATUS가 해당 값이 맞는지 확인한다.
	 * @param a
	 * @return
	 * @throws SQLException
	 */
	private boolean checkSTATUS(DBMngStatus[] status) throws SQLException {
		boolean chk = false;
		for (DBMngStatus i : status)
			if (STATUS == i)
				chk = true;
		
		if (chk)
			return true;
		else if (STATUS == DBMngStatus.ERROR) {
			addError("알 수 없는 에러 발생!");
			throw new SQLException(getErrors());
		}
		else {
			setDBMngStatus(DBMngStatus.INCORRECT_STATUS_ERR);
			addError("알맞지 않은 메소드 접근 순서입니다.");
			throw new SQLException(getErrors());
		}
	}
	
	private void setDBMngStatus(DBMngStatus status) {
		STATUS = status;
	}
	
	/**
	 * 5초 후 자동 반환되는 기능을 중지한다.
	 * @param isAutoClose
	 */
	public void setAutoClose(boolean isAutoClose) {
		this.isAutoClose = isAutoClose;
	}
	
	/**
	 * 에러내역에 에러메세지를 추가한다.
	 * @param errMsg
	 */
	private void addError(String errMsg) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		StackTraceElement[] trace = new Throwable().getStackTrace();
		
		errors.add(sdf.format(date).toString()+
				"\n   LineNo : "+trace[3].getLineNumber()+
				"\n   Method : "+trace[3].getMethodName()+
				"\n   Reason : "+errMsg);
	}
	
	/**
	 * 에러내역을 반환한다.
	 * @return
	 */
	private String getErrors() {
		String msg = "\n//--> DBMng Error Report <--//";
		if (errors.size() == 0)
			msg+= "\n --> 출력 할 내용이 없습니다.";
		else {
			msg+= "\n --> current status : "+STATUS.getStatusMsg()+"\n";
			for (String str : errors)
				msg += "\n  - " + str;
		}
		msg += "\n//--> DBMng Error Report End <--//";
		return msg;
	}
	
	private Timestamp convertDateToTimestamp(Date date) {
		Timestamp ts = new Timestamp(date.getTime());
		return ts;
	}
	
	private Date convertTimestampToDate(Timestamp timestamp) {
		if (timestamp == null)
			return null;
		else
			return new Date(timestamp.getTime());
	}
}
