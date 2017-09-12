package org.xdemo.utils.serial.demo;

import gnu.io.SerialPortEvent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

import org.xdemo.utils.serial.AbstractReadCallback;
import org.xdemo.utils.serial.SerialPortHelper;

/**
 * DEMO，获取串口输出
 * @author Goofy
 * @Date 2017年6月20日 上午9:32:23
 */
public class MyReadCallback extends AbstractReadCallback {

	@Override
	public void call(BufferedReader reader, InputStream is) {
		try {
			/**
			 	方式一：
			 	char[] buff=new char[1024];
				reader.read(buff);
				String c=new String(buff).trim();
				addResult(c);
			 
			 	方式二：
			 	String result = reader.readLine();
				addResult(result.trim());
			 	
			 
			 */
			
			//方式三 对于一些乱码的情况，需要进行字符集转换
			
			String result=reader.readLine();
			result=new String(result.getBytes("GBK"),"GBK");
			addResult(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onError(SerialPortEvent event) {
		System.out.println("出错了,错误类型:\t"+event.getEventType());
	}



	public static void main(String[] args) {
		//连续输出的，一般不需要发送命令，直接就是接受
		final SerialPortHelper sp=new SerialPortHelper();
		sp.open("C:\\serial.config.properties");
		
		//如果需要发送命令的
		//sp.write("P");
		
		//如果需要不停的发送，可以使用#Timer,每隔1秒发送一次
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				sp.write("P");
			}
		}, 0, 1000);
		
		MyReadCallback callback=new MyReadCallback();
		sp.read(callback, Charset.forName("UTF-8"));
		while(true)
			System.out.println(callback.getResult());
	}

}
