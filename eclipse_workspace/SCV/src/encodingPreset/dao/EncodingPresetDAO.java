package encodingPreset.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import encodingPreset.vo.EncodingPresetOptionVO;
import encodingPreset.vo.EncodingPresetVO;
import framework.jdbc.DBMng;
import framework.util.LogUtil;

public class EncodingPresetDAO {

	public ArrayList<EncodingPresetVO> getPresetList() {
		DBMng db = null;
		ArrayList<EncodingPresetVO> res = new ArrayList<>();
		
		try {
			db = new DBMng();
			db.setQuery("select * from encoding_preset order by code asc");
			db.setAutoClose(false);
			db.execute();
			
			while (db.next()) {
				EncodingPresetVO temp = new EncodingPresetVO(db.getString("code"), db.getString("name"), db.getDate("regdate"));
				ArrayList<EncodingPresetOptionVO> tempArr = temp.getOpts();
				DBMng db2 = null;
				try {
					db2 = new DBMng();
					db2.setAutoClose(false);
					db2.setQuery("select * from encoding_preset_option where preset_code = ? order by orderby asc");
					db2.setString(temp.getCode());
					db2.execute();
					
					while (db2.next())
						tempArr.add(new EncodingPresetOptionVO(db2.getInt("seq"), db2.getString("preset_code"), db2.getString("option_name"), db2.getString("option_value"), db2.getInt("orderby"), db2.getDate("regdate")));
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
	
	public int getTotalRowCnt() {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("select count(1) cnt from encoding_preset");
			db.execute();
			
			if (db.next()) {
				return db.getInt("cnt");
			}
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
		return -1;
	}
	
	public void insertNewPreset(String code, String name, Date regdate) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("insert into encoding_preset values (?, ?, ?)");
			db.setString(code);
			db.setString(name);
			db.setDate(regdate);
			db.execute();
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public void deletePresetOptions(String presetCode) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("delete from encoding_preset_option where preset_code = ?");
			db.setString(presetCode);
			db.execute();
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
	
	public void insertPresetOption(String presetCode, String optionName, String optionValue, int orderby, Date regdate) {
		DBMng db = null;
		
		try {
			db = new DBMng();
			db.setQuery("insert into encoding_preset_option values (null, ?, ?, ?, ?, ?)");
			db.setString(presetCode);
			db.setString(optionName);
			db.setString(optionValue);
			db.setInt(orderby);
			db.setDate(regdate);
			db.execute();
		} catch (SQLException e) {
			LogUtil.printErrLog("logic error! ("+e.getLocalizedMessage()+")");
			throw new RuntimeException(e);
		} finally {
			db.close();
		}
	}
}
