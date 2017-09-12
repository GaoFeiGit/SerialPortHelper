package org.xdemo.utils.serial;

import gnu.io.SerialPortEvent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 读串口数据时候的回调方法
 * 
 * @author Goofy
 * @Date 2017年6月15日 下午2:47:53
 */
public abstract class AbstractReadCallback {

	StringBuffer sb = new StringBuffer();

	LinkedBlockingQueue<String> results = new LinkedBlockingQueue<String>();

	/**
	 * 当串口输出数据的时候，会调用这个方法，这个方法解析出正确的结果后，需要调用addResult方法，将返回值放进去
	 * @see #addResult
	 * @param reader
	 */
	public abstract void call(BufferedReader reader,InputStream is);
	
	/**
	 * 出错的时候，会将事件类型传过来
	 * @param event #SerialPortEvent 
	 */
	public void onError(SerialPortEvent event){}

	/**
	 * 解析出正确的结果后，需要调用此方法，将结果添加进去
	 * @param result
	 */
	public void addResult(String result) {
		results.clear();
		try {
			results.put(result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取串口的返回结果
	 * @return
	 */
	public String getResult() {
		try {
			return results.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
