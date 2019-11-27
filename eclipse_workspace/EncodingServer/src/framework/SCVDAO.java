package framework;

import java.sql.SQLException;

import framework.jdbc.DBMng;

public class SCVDAO {

	public int lastInsertId() {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select LAST_INSERT_ID()");
			db.execute();
			
			if (db.next())
				return db.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return -1;
	}
}
