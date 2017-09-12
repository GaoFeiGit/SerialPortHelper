package org.xdemo.utils.serial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 属性
 * @author Goofy
 * @Date 2017年6月15日 下午4:52:06
 */
public class Prop {
	
	Properties ps=new Properties();
	/**
	 * 端口号
	 */
	public static final String KEY_PORT="PORT";
	/**
	 * 波特率
	 */
	public static final String KEY_BAUD_RATE="BAUD_RATE";
	/**
	 * 服务器地址
	 */
	public static final String KEY_SERVER_ADDR="SERVER_ADDR";
	/**
	 * 设备型号
	 */
	public static final String KEY_DEVICE_TYPE="DEVICE_TYPE";
	/**
	 * 校验位
	 */
	public static final String KEY_PARITY_BIT="PARITY_BIT";
	/**
	 * 发送的命令
	 */
	public static final String KEY_COMMAND="COMMAND";
	/**
	 * 数据位
	 */
	public static final String KEY_DATA_BIT="DATA_BIT";
	/**
	 * 停止位
	 */
	public static final String KEY_STOP_BIT="STOP_BIT";
	
	public Prop(String config) {
		try {
			ps=new Properties();
			ps.load(new FileInputStream(config));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getProp(String key){
		return ps.getProperty(key);
	}
}
