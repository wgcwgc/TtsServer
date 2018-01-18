/**
 * 
 */


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author           Administrator
 * @copyright        wgcwgc
 * @date             2017年10月10日
 * @time             上午11:14:50
 * @project_name     tts_kdxf_demo
 * @package_name     log
 * @file_name        PrintLog.java
 * @type_name        PrintLog
 * @enclosing_type   
 * @tags             
 * @todo             
 * @others           
 *
 */

public class PrintLog
{
	public static void printLog(String contents)
	{
		try
		{
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Util.getPath() + "debug.log" , true));
			contents = ( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(Calendar.getInstance().getTime()) + "\r\n" + contents + "\r\n";
//			System.out.println(contents);
			bufferedWriter.write(contents);
			bufferedWriter.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args)
	{
		printLog("asdf");
	}
}
