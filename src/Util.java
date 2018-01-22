import java.io.File;
import java.io.IOException;

/**
 * 
 */

/**
 * @author           Administrator
 * @copyright        wgcwgc
 * @date             2018年1月3日
 * @time             下午2:06:26
 * @project_name     TtsServer
 * @package_name     
 * @file_name        Util.java
 * @type_name        Util
 * @enclosing_type   
 * @tags             
 * @todo             
 * @others           
 *
 */

public class Util
{
	public static ReadIniFile readIniFile;
	public static String SECRETKEY = "8848@jzb";
	public static String rootPath = null;
	
	public static String getPath()
	{
		try
		{
			rootPath = new File(".").getCanonicalPath().toString() + "\\";
		}
		catch(IOException e)
		{
			rootPath = System.getProperty("user.dir") + "\\";
		}
		return rootPath;
	}
	
	public static String getAcceptPort()
	{
		readIniFile = new ReadIniFile("config");
		return readIniFile.getValue("port" , "acceptPort");
	}
	
	public static String getSendPort()
	{
		readIniFile = new ReadIniFile("config");
		return readIniFile.getValue("port" , "sendPort");
	}
	
	public static String getServerIP()
	{
		readIniFile = new ReadIniFile("config");
		return readIniFile.getValue("ip" , "sendIP");
	}
	
	public static String getEncryptFileName()
	{
		String content = "";
		long currentTime = System.currentTimeMillis();
		content += currentTime;
		int random = (int) ( 10 * Math.random() );
		content += "#" + random;
		int num = 3;
		while(num -- != 0)
		{
			random = (int) ( 10 * Math.random() );
			content += random;
		}
		return content;
	}
	
	/**
	 * @param args
	 */
	public static void main(String [] args)
	{
		System.out.println(getEncryptFileName());
	}
	
}
