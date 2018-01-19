/**
 * 
 */

/**
 * @author Administrator
 * @copyright wgcwgc
 * @date 2017��12��27��
 * @time ����10:05:26
 * @project_name tts_kdxf_demo
 * @package_name
 * @file_name SingleFileHttpServers.java
 * @type_name SingleFileHttpServers
 * @enclosing_type
 * @tags
 * @todo
 * @others
 * 
 */

/**
 * 
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechUtility;

public class SetWords extends Thread
{
	private static long TIME = 1 * 24 * 60 * 60 * 1000;
	private static Socket connection = null;
	private static ServerSocket server = null;
	private static String rootPath;
	private byte [] content;
	private byte [] header;
	private String encoding;
	private String MIMEType;
	private int port;
	
	private SetWords(String data , String encoding , String MIMEType , int port)
			throws UnsupportedEncodingException
	{
		this(data.getBytes(encoding) , encoding , MIMEType , port);
	}
	
	private SetWords(byte [] data , String encoding , String MIMEType , int port)
			throws UnsupportedEncodingException
	{
		this.encoding = encoding;
		this.content = data;
		this.port = port;
		this.MIMEType = MIMEType;
	}
	
	/**
	 * 
	 * @param MIMEType mp3
	 * @param encoding utf-8
	 * @param port 8088
	 */
	private SetWords(String MIMEType , String encoding , int port)
			throws UnsupportedEncodingException
	{
		this.MIMEType = MIMEType;
		this.encoding = encoding;
		this.port = port;
	}
	
	public void run()
	{
		
		try
		{
			server = new ServerSocket(this.port);
			System.out.println("Accepting connections on port "
					+ server.getLocalPort());
			PrintLog.printLog("Accepting connections on port "
					+ server.getLocalPort());
			System.out.println("Data to be sent:");
			PrintLog.printLog("Data to be sent:");
			while(true)
			{
				connection = null;
				try
				{
					connection = server.accept();
					OutputStream out = new BufferedOutputStream(
							connection.getOutputStream());
					InputStream in = new BufferedInputStream(
							connection.getInputStream());
					StringBuffer request = new StringBuffer();
					
					ByteArrayOutputStream contentBytes = new ByteArrayOutputStream();
					
					while(true)
					{
						int c = in.read();
						if(c == '\r' || c == '\n' || c == - 1)
						{
							break;
						}
						contentBytes.write(c);
						request.append((char) c);
					}
					
					String str = contentBytes.toString();
					System.out.println(str);
					if(judge(str , 0))
					{
						// get
						if(str.startsWith("GET /"))
						{
							// ����Ϸ�
							if(str.contains("setWords?")
									&& str.contains("words=")
									&& str.contains("&sign="))
							{
								str = str.substring(str.indexOf("=") + 1 ,
										str.indexOf(" HTTP/"));
								String [] list = str.split("&");
								if(list.length != 2)
								{
									System.out.println("�����������");
									PrintLog.printLog("�����������");
									writeRespose(request , "�����������" , out , 1);
									continue;
								}
								String string = list[0];
								String sign = list[1].substring(list[1]
										.indexOf("=") + 1);
//								System.out.println(sign);
								System.out.print(string);
								PrintLog.printLog(string);
								if(!judge(string , sign))
								{
									System.out.println("�����������");
									PrintLog.printLog("�����������");
									writeRespose(request , "�����������" , out , 1);
									continue;
								}
								string = URLDecoder.decode(string , "utf-8");
								System.out.println(string);
								PrintLog.printLog(string);
								if(judge(string , 1))
								{
									if( ! new File(rootPath + string + ".mp3")
											.exists())
									{
										Text2SpeechMain.creat(string , rootPath
												+ string + ".pcm");
//								System.out.println("asdf" + rootPath);
										while(true)
										{
											if(new File(rootPath + string
													+ ".pcm").exists())
											{
												break;
											}
										}
										try
										{
											Pcm2Wav.convertAudioFiles(rootPath
													+ string + ".pcm" ,
													rootPath + string + ".wav");
											while(true)
											{
												if(new File(rootPath + string
														+ ".wav").exists())
												{
													break;
												}
											}
											wav2mp3.execute(new File(rootPath
													+ string + ".wav") ,
													rootPath + string + ".mp3");
											while(true)
											{
												if(new File(rootPath + string
														+ ".mp3").exists())
												{
													break;
												}
											}
											new File(rootPath + string + ".pcm")
													.delete();
											new File(rootPath + string + ".wav")
													.delete();
										}
										catch(Exception e)
										{
											System.out.println("��Ƶ�ļ������쳣");
											PrintLog.printLog("��Ƶ�ļ������쳣");
											System.out.println(e);
											PrintLog.printLog(e.toString());
											writeRespose(request ,
													"�������쳣�����Ժ����ԣ�" , out , 1);
											continue;
										}
									}
									writeRespose(request , list[0] , out , 0);
									
								}
								else
								{
									System.out.println("�����а����Ƿ��ַ�");
									PrintLog.printLog("�����а����Ƿ��ַ�");
									writeRespose(request , "�����а����Ƿ��ַ�" , out ,
											1);
								}
							}
							else
							{// ����ͷ���Ϸ�
								System.out.println("����ͷ���Ϸ�");
								PrintLog.printLog("����ͷ���Ϸ�");
								writeRespose(request , "����ͷ���Ϸ�" , out , 1);
							}
						}
						// post
						else
						{
							System.out.println("post");
						}
					}
					else
					{
//						System.out.println("�����쳣");
//						PrintLog.printLog("�����쳣");
//						writeRespose(request , "�����쳣" , out , 1);
					}
				}
				catch(IOException e)
				{
					System.out.println(e);
					PrintLog.printLog(e.toString());
					System.out.println("�������");
					PrintLog.printLog("�������");
				}
				finally
				{
					if(connection != null)
					{
						connection.close();
					}
				}
			}
			
		}
		catch(IOException e)
		{
			System.err.println("Could not start server. Port Occupied");
			PrintLog.printLog("Could not start server. Port Occupied");
		}
		finally
		{
			if(server != null)
			{
				try
				{
					server.close();
				}
				catch(IOException e)
				{
					System.out.println(e);
					PrintLog.printLog(e.toString());
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * request ����ͷ
	 * string ��Ƶ��
	 * out ���������
	 * 
	 * flag��
	 * 		0 ������Ƶ�ļ�
	 * 		1 ����������
	 * 		2 ������
	 */
	private void writeRespose(StringBuffer request , String string ,
			OutputStream out , int flag)
	{
		JSONObject jsonObject = null;
		InputStream localRead = null;
		try
		{
			ByteArrayOutputStream localWrite = new ByteArrayOutputStream();
			if(0 == flag)
			{
//				localRead = new FileInputStream(rootPath + string + ".mp3");
//				int b;
//				while( ( b = localRead.read() ) != - 1)
//				{
//					localWrite.write(b);
//				}
//				this.content = localWrite.toByteArray();
//				MIMEType = "audio/mp3";
				jsonObject = new JSONObject();
				jsonObject.put("result" , 0);
				jsonObject.put("url" , "http://172.16.0.63:8089/getWords?words=" + string + "&sign=" + MD5.md5(Util.SECRETKEY + string));
				jsonObject.put("mesg" , "OK");
				string = jsonObject.toString();
				localWrite.write(string.getBytes(this.encoding));
				this.content = localWrite.toByteArray();
				MIMEType = "text/html";
			}
			else if(1 == flag)
			{
				jsonObject = new JSONObject();
				jsonObject.put("result" , - 1);
				jsonObject.put("mesg" , string);
				string = jsonObject.toString();
				localWrite.write(string.getBytes(this.encoding));
				this.content = localWrite.toByteArray();
				MIMEType = "text/html";
			}
			else if(2 == flag)
			{
				jsonObject = new JSONObject();
				jsonObject.put("result" , 0);
				jsonObject.put("mesg" , string);
				string = jsonObject.toString();
				localWrite.write(string.getBytes(this.encoding));
				this.content = localWrite.toByteArray();
				MIMEType = "text/html";
			}
			// �����⵽��HTTP/1.0���Ժ��Э�飬���չ淶����Ҫ����һ��MIME�ײ�
			String requestContent = request.toString();
			String header = "HTTP/1.1 200 OK\r\n" + "Server: OneFile 1.0\r\n"
					+ "Content-length: " + ( this.content.length ) + "\r\n"
					+ "Content-type: " + MIMEType + "\r\n\r\n";
			this.header = header.getBytes(this.encoding);
			if(requestContent.indexOf("HTTP/") != - 1)
			{
				out.write(this.header);
			}
			
			out.write(this.content);
			out.flush();
		}
		catch(IOException e)
		{
			System.out.println(e);
			PrintLog.printLog(e.toString());
			e.printStackTrace();
		}
		catch(JSONException e)
		{
			System.out.println(e);
			PrintLog.printLog(e.toString());
			e.printStackTrace();
		}
		finally
		{
			if(localRead != null)
			{
				try
				{
					localRead.close();
				}
				catch(IOException e)
				{
					System.out.println("IO���쳣");
					PrintLog.printLog("IO���쳣");
					PrintLog.printLog(e.toString());
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 
	 * @param string
	 * @param sign
	 * @return �ж�md5�����Ƿ���ȷ
	 */
	private boolean judge(String string , String sign)
	{
		System.out.println(string);
		System.out.println(sign);
		System.out.println(MD5.md5(Util.SECRETKEY + string));
		// �жϼ����Ƿ���ȷ
		if(sign.equals(MD5.md5(Util.SECRETKEY + string)))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * @param string
	 * @exception �ж��ַ��Ƿ�Ϸ�
	 * @return
	 */
	private boolean judge(String string , int flag)
	{
		// �ж��Ƿ�������ض��ַ� ��%���� ��HTTP/��
		if(0 == flag)
		{
			if(string.contains("%") && string.contains(" HTTP/"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		// �ж��Ƿ��зǷ��ַ�
		else if(1 == flag)
		{
			if(string.contains("\\") || string.contains("/")
					|| string.contains(":") || string.contains("*")
					|| string.contains("?") || string.contains("\"")
					|| string.contains("|") || string.contains("<")
					|| string.contains("<") || string.contains("��"))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param args 
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String [] args)
	{
		try
		{
			PrintLog.printLog(InetAddress.getLocalHost().getHostName()
					.toString()
					+ "\t"
					+ InetAddress.getLocalHost().getHostAddress().toString());
		}
		catch(UnknownHostException e1)
		{
			PrintLog.printLog(e1.toString());
			System.out.println(e1.toString());
		}
		
		rootPath = Util.getPath();
		System.out.println(rootPath);
		PrintLog.printLog(rootPath);
		SpeechUtility.createUtility(SpeechConstant.APPID + "=59ce0194");
		try
		{
			String contentType = "audio/mp3";
			String encoding = "utf-8";
			int port = 8088;
			Thread thread = new SetWords(contentType , encoding , port);
			thread.start();
			new RemoveMp3Files(TIME).start();
			Scanner cinScanner = new Scanner(System.in);
			String cancel = cinScanner.next();
			if("q" == cancel || "q".equalsIgnoreCase(cancel))
			{
				if(cinScanner != null)
				{
					cinScanner.close();
				}
				if(connection != null)
				{
					connection.close();
				}
				if(server != null)
				{
					server.close();
				}
				thread.stop();
				PrintLog.printLog("exit");
				System.exit(0);
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			PrintLog.printLog("Usage:java SingleFileHTTPServer filename port encoding");
			System.out
					.println("Usage:java SingleFileHTTPServer filename port encoding");
		}
		catch(Exception e)
		{
			PrintLog.printLog(e.toString());
			System.err.println(e);
		}
	}
}
