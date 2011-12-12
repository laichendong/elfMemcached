/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.elf.memcached.command.Command;
import com.elf.memcached.command.Command.CommandNames;
import com.elf.memcached.command.DeletionCommand;
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
	public static final String DELETED = "DELETED";
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
			os.write(cmd.getData());
			os.write(Command.RETURN.getBytes());
			os.flush();

			// TODO 这步非常耗性能！改成nio？
			String reply = new BufferedReader(new InputStreamReader(this.socket.getInputStream())).readLine();
			return reply.equals(STORED);
			// return true;
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

			BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			String l = null;
			while ((l = br.readLine()) != null) {
				System.out.println(l);
			}
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从服务器删除有key指定的键值
	 * 
	 * @param key
	 *            待删除的key
	 * @return 是否删除成功
	 */
	public boolean delete(String key) {
		DeletionCommand cmd = new DeletionCommand(key);
		logger.debug("sand a command : " + cmd.commandString());
		byte[] c = cmd.commandString().getBytes();
		OutputStream os;
		try {
			os = this.socket.getOutputStream();
			os.write(c);
			os.write(Command.RETURN.getBytes());
			os.flush();

			String reply = new BufferedReader(new InputStreamReader(this.socket.getInputStream())).readLine();
			return reply.equals(DELETED);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
