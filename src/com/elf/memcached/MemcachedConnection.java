/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.elf.memcached.command.Command;
import com.elf.memcached.command.Command.CommandNames;
import com.elf.memcached.command.RetrievalCommand;
import com.elf.memcached.command.StorageCommand;

/**
 * memcach 连接对象
 * 
 * @author laichendong
 * @since 2011-12-6 下午01:26:16
 */
public class MemcachedConnection {
	public static final String STORED = "STORED";
	public static final String END = "END";
	private Logger logger = Logger.getLogger(MemcachedConnection.class);
	
	/** 连接所持有的socket */
	private Socket socket;
	/** 服务器主机特征字符串，用于标记这个连接属于哪个服务器 */
	private String hostProfile;
	
	/**
	 * 构造方法 需要指定持有的socket对象
	 * 
	 * @param socket
	 *            持有的socket对象
	 */
	public MemcachedConnection(Socket socket, String hostProfile) {
		this.socket = socket;
		this.hostProfile = hostProfile;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public String getHostProfile() {
		return hostProfile;
	}
	
	public void setHostProfile(String hostProfile) {
		this.hostProfile = hostProfile;
	}
	
	/**
	 * 存储数据
	 * 
	 * @param commandName
	 *            存储命令名称
	 * @param key
	 *            待存储的key
	 * @param value
	 *            待存储的数据值
	 * @param exptime
	 *            过期时间
	 * @return 是否存储成功
	 */
	public boolean storage(CommandNames commandName, String key, Object value, long exptime) {
		StorageCommand cmd = new StorageCommand(commandName, key, value, exptime);
		logger.debug("send a command : " + cmd.commandString());
		byte[] c = cmd.commandString().getBytes();
		try {
			OutputStream os = this.socket.getOutputStream();
			os.write(c);
			os.write(Command.RETURN.getBytes());
			os.write(cmd.getData());
			os.write(Command.RETURN.getBytes());
			os.flush();
			
			// TODO 这步非常耗性能！改成nio？
			InputStream is = this.socket.getInputStream();
			String reply = new BufferedReader(new InputStreamReader(is)).readLine();
			return reply.equals(STORED);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Object get(CommandNames commandName, String key) {
		String[] keys = new String[1];
		keys[0] = key;
		RetrievalCommand cmd = new RetrievalCommand(commandName, keys);
		logger.debug("send a command : " + cmd.commandString());
		byte[] c = cmd.commandString().getBytes();
		OutputStream os;
		try {
			os = this.socket.getOutputStream();
			os.write(c);
			os.write(Command.RETURN.getBytes());
			os.flush();
			
			
			BufferedInputStream bis = new BufferedInputStream(this.socket.getInputStream());
			boolean stop = false;
			StringBuffer sb = new StringBuffer();
			int dataSize = 0;
			int index = 0;
			while (!stop) {//解析“响应头”
				int b = bis.read();
				if ((b == 32) || (b == 13)) {// 如果是空格或回车
					switch (index) {
						case 0://"VALUE" 或"END"
							if (END.equals(sb.toString())){
								return null;
							}
							break;
						case 1://key
							break;
						case 2://flag
							break;
						case 3://dataSize
							dataSize = Integer.parseInt(sb.toString());
					}
					
					index++;
					sb = new StringBuffer();
					if (b == 13) {// 回车 “响应头”结束 \r
						bis.read();// 读出最后的那个换行符 \n
						stop = true;
					}
				} else {
					sb.append((char) b);
				}
			}
			//接收数据
			byte[] data = new byte[dataSize];
			bis.read(data);
			return Command.deserialize(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
