**1. 安装`RXTX`**
	
	假设JDK路径如下:
	c:\Program Files\Java\jre1.6.0_01\
	复制 rxtxParallel.dll 到 c:\Program Files\Java\jre1.6.0_01\bin\
	复制 rxtxSerial.dll 到 c:\Program Files\Java\jre1.6.0_01\bin\
	复制 RXTXcomm.jar 到 c:\Program Files\Java\jre1.6.0_01\lib\ext\
	
	注意: 如果在windows xp系统上安装还需要crtdll.dll，C运行时组件，自行去下载

**2. 引入Jar包SerialPortHelper-1.0.jar**
```xml
	<dependency>
		<groupId>org.xdemo.utils</groupId>
		<artifactId>SerialPortHelper</artifactId>
		<version>1.0</version>
	</dependency>
```
	非Maven项目，自行下载`SerialPortHelper-1.0.jar`包，放到项目中，也可以自行下载build
	
**3. 编写回调类**
	继承 `AbstractReadCallback` ，在`call`方法中，获取输入流输入的内容
```java
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
```
**4. 配置文件，参考如下**
	
	#端口号
	PORT=COM5
	
	#波特率
	BAUD_RATE=9600
	
	#奇偶校验	NONE:0,ODD:1,EVEN:2,MARK:3,SPACE:4
	PARITY_BIT=0
	
	#数据位	5,6,7,8
	DATA_BIT=8
	
	#停止位	1:1,2:2,1.5:3
	STOP_BIT=1
	
	#字符编码
	CHARSET=UTF-8
