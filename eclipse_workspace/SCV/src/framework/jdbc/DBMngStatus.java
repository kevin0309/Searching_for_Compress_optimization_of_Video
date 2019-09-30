package framework.jdbc;

/**
 * DBMng에서 사용되는 status 값 ENUM
 * @author 박유현
 * @since 2018-12-25
 */
public enum DBMngStatus {
	RESOURCES_NOT_CLOSED("DBMng status - unclosed DB resources detected."),	//자원이 반환되지 않은 상태
	SET_CONNECTION_ERR("DBMng status - Connection error."),					//커넥션이 제대로 연결되지 않아 사용할 수 없는 상태
	NOT_MATCH_PARAM_ERR("DBMng status - Not matched parameters."),			//PreparedStatement 객체의 모든 파라미터가 채워지지 않은 상태
	ERROR("DBMng status - Unknown ERROR!"),									//알수없는 에러가 발생하여 사용할 수 없는 상태
	INCORRECT_STATUS_ERR("DBMng status - Incorrect STATUS."),				//메소드 사용 시 알맞지 않는 순서로 접근한 상태
	COLUMN_NOT_FOUND_ERR("DBMng status - Columnlabel not found."),			//get~~ 메소드에서 컬럼명을 사용해 값을 불러올 시 컬럼명이 맞지않아 오류가 발생한 상태
	NOT_CONNECTED("DBMng status - Not initiated yet."),						//DBMng객체가 생성만 되어있는 상태
	CONNECTED("DBMng status - Connected."),									//Connection이 설정된 상태
	STATEMENT_READY("DBMng status - Ready to use PreparedStatement."),		//PreparedStatement가 선언된 상태
	PARAMETER_READY("DBMng status - Ready to execute PreparedStatement."),	//PreparedStatement의 파라미터가 모두 추가되었거나 추가할 파라미터가 없어 사용할 준비가 된 상태
	EXECUTED("DBMng status - Executed."),									//Execute 된 상태
	CLOSED("DBMng status - Closed.");										//모든 자원이 반환된 상태, setConnection(Connection connection)을 사용하여 새로운 연결을 구성 할 수 있음
	
	private String statusMsg;
	
	private DBMngStatus(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public String getStatusMsg() {
		return statusMsg;
	}
	
}
