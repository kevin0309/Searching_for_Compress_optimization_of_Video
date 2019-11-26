package encoding.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import encoding.vo.EncodingPresetOptionVO;
import encoding.vo.EncodingPresetVO;
import framework.jdbc.DBMng;
import framework.util.LogUtil;

public class EncodingPresetDAO {

	public ArrayList<EncodingPresetVO> getPresetList() {
		DBMng db = null;
		ArrayList<EncodingPresetVO> res = new ArrayList<>();
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_preset order by code asc");
			db.execute();
			
			while (db.next()) {
				EncodingPresetVO temp = new EncodingPresetVO(db.getString("code"), db.getString("name"), db.getDate("regdate"));
				ArrayList<EncodingPresetOptionVO> tempArr = temp.getOpts();
				DBMng db2 = null;
				try {
					db2 = new DBMng();
					db2.setQuery("select * from encoding_preset_option where preset_code = ? order by orderby asc");
					db2.setString(temp.getCode());
					db2.execute();
					
					while (db2.next())
						tempArr.add(new EncodingPresetOptionVO(db2.getInt("seq"), db2.getString("preset_code"), db2.getString("option_name"), db2.getString("optionValue"), db2.getInt("orderby"), db2.getDate("regdate")));
				} catch (SQLException e) {
					LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
					throw new RuntimeException(e);
				} finally {
					db2.close();
				}

				res.add(temp);
			}
			return res;
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
}
