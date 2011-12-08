/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.apache.log4j.net.SocketAppender;

import com.elf.memcached.command.Command;
import com.elf.memcached.command.StorageCommand;
import com.elf.memcached.command.StorageCommand.CommandNames;

/**
 * memcach 连接对象
 * 
 * @author laichendong
 * @since 2011-12-6 下午01:26:16
 */
public class MemcachedConnection {
	
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
	 * @return
	 */
	public boolean storage(CommandNames commandName, String key, Object value) {
		StorageCommand cmd = new StorageCommand(commandName, key, value);
		System.out.println(cmd.commandString());
		byte[] c = cmd.commandString().getBytes();
		try {
			OutputStream os = this.socket.getOutputStream();
			os.write(c);
			os.write(cmd.getData());
			os.write(Command.RETURN.getBytes());
			os.flush();
			
			//TODO 这步非常耗性能！
			System.out.println(new BufferedReader( new InputStreamReader(this.socket.getInputStream())).readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
