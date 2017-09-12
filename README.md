
#xutils http://www.xdemo.org/ 　![Alt text](http://upload-images.jianshu.io/upload_images/607529-01ad59870a978b72.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**1. 安装RXTX**
	
	假设JDK路径如下:
	c:\Program Files\Java\jre1.6.0_01\
	复制 rxtxParallel.dll 到 c:\Program Files\Java\jre1.6.0_01\bin\
	复制 rxtxSerial.dll 到 c:\Program Files\Java\jre1.6.0_01\bin\
	复制 RXTXcomm.jar 到 c:\Program Files\Java\jre1.6.0_01\lib\ext\
	
	注意: 如果在windows xp系统上安装还需要crtdll.dll，C运行时组件，自行去下载

**2. 引入Jar包SerialPortHelper-1.0.jar**
	<dependency>
		<groupId>org.xdemo.utils</groupId>
		<artifactId>SerialPortHelper</artifactId>
		<version>1.0</version>
	</dependency>
	非Maven项目，自行下载SerialPortHelper-1.0.jar包，放到项目中，也可以自行下载build
	
**3. 编写回调类**
	继承 AbstractReadCallback ，在call方法中，获取输入流输入的内容

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
