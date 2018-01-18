import java.io.File;

/**
 * 
 */

/**
 * @author           Administrator
 * @copyright        wgcwgc
 * @date             2018年1月16日
 * @time             下午4:08:44
 * @project_name     TtsServer
 * @package_name     
 * @file_name        RemoveMp3Files.java
 * @type_name        RemoveMp3Files
 * @enclosing_type   
 * @tags             
 * @todo             
 * @others           
 *
 */

public class RemoveMp3Files extends Thread
{
	private long time;
	
	RemoveMp3Files()
	{
		
	}
	
	RemoveMp3Files(long time)
	{
		this.time = time;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		String path = Util.getPath();
		File file = new File(path);
		File [] files;// = file.listFiles();
		while(true)
		{
			files = file.listFiles();
			for(File childFile : files)
			{
				if( ( childFile.toString().endsWith(".pcm")
						|| childFile.toString().endsWith(".wav") || childFile
						.toString().endsWith(".mp3") )
						&& ( System.currentTimeMillis() - childFile
								.lastModified() ) > time
						&& childFile.canWrite() && childFile.canRead())
				{
					childFile.delete();
//				System.out.println(childFile.toString() + System.currentTimeMillis() + "\n" + childFile.lastModified());
				}
			}
		}
		
	}
	
	public static void main(String [] args)
	{
		long time = 1 * 24 * 60 * 60 * 1000;
		new RemoveMp3Files(time).start();
	}
}
