/**
 * 
 */


import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;

import java.io.File;

/**
 * @author Administrator
 * @copyright wgcwgc
 * @date 2017年10月9日
 * @time 下午4:48:59
 * @project_name tts_kdxf_demo
 * @package_name mp4
 * @file_name wav2mp4.java
 * @type_name wav2mp4
 * @enclosing_type
 * @tags
 * @todo
 * @others
 * 
 */

/**
     * 执行转化过程
     * 
     * @param source
     *            源文件
     * @param desFileName
     *            目标文件名
     * @return 转化后的文件
     */
public class wav2mp3
{
	
	public static File execute(File source , String desFileName)
			throws Exception
	{
		File target = new File(desFileName);
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(128000)); // 音频比率 MP3默认1280000
//		audio.setBitRate(new Integer(36000)); // 音频比率 MP3默认1280000
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		encoder.encode(source , target , attrs);
		return target;
	}
	
	public static void main(String [] args) throws Exception
	{
		File file = new File(
				"C://Users//Administrator//Desktop//t2s2//out//t2s//五年级上册生词//第一单元//读读记记//baochang.wav");
		execute(file ,
				"C://Users//Administrator//Desktop//t2s2//out//t2s//五年级上册生词//第一单元//读读记记//baochang.mp3");
	}
}
