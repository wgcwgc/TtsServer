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
	
	/**
	 * @param args
	 */
	public static void main(String [] args)
	{
		
	}
	
}
