package framework.servlet.fileRequest.upload.logic;

import java.util.ArrayList;

import framework.init.ServerConfig;
import framework.init.ServerHDD;
import framework.util.LogUtil;

/**
 * 다음으로 사용할 HDD를 선정하는 클래스
 * Lazy-holder Singleton Pattern이 적용되어있음
 * @author 박유현
 * @since 2019.10.12
 */
public class HddSelector {
	
	private static ArrayList<Integer> hddCntList;
	private ServerHDD currentUse;
	private int currentUseIndex;
	private HddSelector() {
		hddCntList = new ArrayList<>();
		for (int i = 0; i < ServerConfig.getHDDList().size(); i++)
			hddCntList.add(0);
	}
	
	private static class LazyHolder {
		private static final HddSelector INSTANCE = new HddSelector();
	}
	
	/**
	 * Singleton 패턴 적용
	 * @return INSTANCE
	 */
	public static HddSelector getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * 사용가능한 HDD를 찾아 리턴
	 * 사용중이지 않은 HDD를 우선적으로 리턴함
	 * 사용량 90% 도달 시 더이상 사용하지 않음
	 * @param requestVolume 작업에 필요한 용량
	 * @return ServerHDD
	 * @throws NotEnoughSpaceForHddException 서버에 사용가능한 HDD가 없을 때 예외사항
	 * @see framework.init.ServerHDD
	 */
	public ServerHDD getHDD(long requestVolume) throws NotEnoughSpaceForHddException {
		ArrayList<ServerHDD> hddArr = ServerConfig.getHDDList();
		
		for (int i = 0; i < hddArr.size(); i++)
			if (!hddArr.get(i).isCurrentUse())
				if ((hddArr.get(i).getDriveUsableSpace() - requestVolume) / hddArr.get(i).getDriveTotalSpace() < 0.8) {
					selectHdd(hddArr.get(i), i, requestVolume);
					return hddArr.get(i);
				}
		
		for (int i = 0; i < hddArr.size(); i++)
			if ((hddArr.get(i).getDriveUsableSpace() - requestVolume) / hddArr.get(i).getDriveTotalSpace() < 0.8) {
				selectHdd(hddArr.get(i), i, requestVolume);
				return hddArr.get(i);
			}
		
		for (int i = 0; i < hddArr.size(); i++)
			if (!hddArr.get(i).isCurrentUse())
				if ((hddArr.get(i).getDriveUsableSpace() - requestVolume) / hddArr.get(i).getDriveTotalSpace() < 0.9) {
					selectHdd(hddArr.get(i), i, requestVolume);
					LogUtil.printLog("Server HDD storage is nearly full.(over 80%)");
					return hddArr.get(i);
				}
		
		for (int i = 0; i < hddArr.size(); i++)
			if ((hddArr.get(i).getDriveUsableSpace() - requestVolume) / hddArr.get(i).getDriveTotalSpace() < 0.9) {
				selectHdd(hddArr.get(i), i, requestVolume);
				LogUtil.printLog("Server HDD storage is nearly full.(over 80%)");
				return hddArr.get(i);
			}
		
		LogUtil.printErrLog("Server HDD storage is full.(over 90%) Access denied for system stability.");
		throw new NotEnoughSpaceForHddException();
	}
	
	private void selectHdd(ServerHDD hdd, int i, long requestVolume) {
		currentUse = hdd;
		currentUseIndex = i;
		currentUse.setCurrentUse(true);
		hddCntList.set(i, hddCntList.get(i)+1);
		hdd.setDriveUsableSpace(currentUse.getDriveUsableSpace() - requestVolume);
	}
	
	/**
	 * 사용중인 하드디스크를 사용가능상태로 전환
	 */
	public void close() {
		hddCntList.set(currentUseIndex, hddCntList.get(currentUseIndex)-1);
		if (hddCntList.get(currentUseIndex) < 1) {
			hddCntList.set(currentUseIndex, 0);
			currentUse.setCurrentUse(false);
		}
	}
}
