package org.xdemo.utils.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.TooManyListenersException;

/**
 * 串口调试辅助类
 * 
 * @author Goofy
 * @Date 2017年6月15日 上午9:47:57
 */
public class SerialPortHelper implements SerialPortEventListener {

	protected Prop prop;

	protected CommPortIdentifier portId;
	protected SerialPort serialPort;
	protected BufferedReader reader;
	protected PrintWriter writer;

	AbstractReadCallback callback;

	private InputStream is;

	/**
	 * 打开串口
	 * @param propConfigFile 串口配置文件路径
	 * @return
	 */
	public SerialPortHelper open(String propConfigFile) {
		try {
			prop = new Prop(propConfigFile);
			portId = CommPortIdentifier.getPortIdentifier(prop.getProp(Prop.KEY_PORT));
			serialPort = (SerialPort) portId.open("PORTID", 2000);
			/*
			 * serialPort.setSerialPortParams(1200,// 波特率
			 * SerialPort.DATABITS_8,// 数据位数 SerialPort.STOPBITS_2,// 停止位
			 * SerialPort.PARITY_NONE);// 校验
			 */

			serialPort.setSerialPortParams(Integer.parseInt(prop.getProp(Prop.KEY_BAUD_RATE)),// 波特率
					Integer.parseInt(prop.getProp(Prop.KEY_DATA_BIT)),// 数据位数
					Integer.parseInt(prop.getProp(Prop.KEY_STOP_BIT)),// 停止位
					Integer.parseInt(prop.getProp(Prop.KEY_PARITY_BIT)));// 校验
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 读取串口数据，实际上是通过{@link #serialEvent(SerialPortEvent)}事件监听来实现的
	 * 
	 * @param callback
	 *            回调方法
	 * @param charset
	 *            字符集
	 * @return
	 */
	public SerialPortHelper read(AbstractReadCallback callback, Charset charset) {
		try {
			this.callback = callback;
			reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream(), charset));
			is = new BufferedInputStream(serialPort.getInputStream());
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			return this;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 写入命令，命令在配置文件中
	 * 
	 * @return true 写入成功，否则失败
	 */
	public boolean write(String command) {
		try {
			writer = new PrintWriter(new OutputStreamWriter(serialPort.getOutputStream(), "UTF-8"), true);
			writer.println(command);
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 串口事件响应
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			callback.onError(event);
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			synchronized (reader) {
				// 数据到达，回调
				callback.call(reader, is);
			}
		}
	}

}
