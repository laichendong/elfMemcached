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

import com.elf.memcached.command.Coder;
import com.elf.memcached.command.Command;
import com.elf.memcached.command.Command.CommandNames;
import com.elf.memcached.command.DeletionCommand;
import com.elf.memcached.command.FlushAllCommand;
import com.elf.memcached.command.IncrDecrCommand;
import com.elf.memcached.command.RetrievalCommand;
import com.elf.memcached.command.StorageCommand;
import com.elf.memcached.command.TouchCommand;

/**
 * memcach 连接对象
 * 
 * @author laichendong
 * @since 2011-12-6 下午01:26:16
 */
public class MemcachedConnection {
	public static final String STORED = "STORED";
	public static final String DELETED = "DELETED";
	public static final String END = "END";
	private Logger logger = Logger.getLogger(MemcachedConnection.class);
	
	/** 连接所持有的socket */
	private Socket socket;
	/** 服务器主机特征字符串，用于标记这个连接属于哪个服务器 */
	private String hostProfile;
	
	/**
	 * 构造方法 需要指定持有的socket对象
	 * 
	 * @param socket 持有的socket对象
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
	
	/**
	 * 获取指定key上的
	 * 
	 * @param commandName 命令名称
	 * @param key key
	 * @return 获取到的数据，如果没有获取到，返回null
	 */
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
			int flag = -1;
			int index = 0;
			while (!stop) {// 解析“响应头”
				int b = bis.read();
				if ((b == 32) || (b == 13)) {// 如果是空格或回车
					switch (index) {
						case 0:// "VALUE" 或"END"
							if (END.equals(sb.toString())) {
								return null;
							}
							break;
						case 1:// key
							break;
						case 2:// flag
							flag = Integer.parseInt(sb.toString());
							break;
						case 3:// dataSize
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
			// 接收数据
			byte[] data = new byte[dataSize];
			bis.read(data);
			return Coder.decode(data, flag);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从服务器删除有key指定的键值
	 * 
	 * @param key 待删除的key
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
	
	/**
	 * 递增或递减 key上的数据
	 * 
	 * @param commandName 命令名称
	 * @param key key
	 * @param value 递增或递减的量
	 * @return key上改变后的值
	 */
	public long incrDecr(CommandNames commandName, String key, long value) {
		IncrDecrCommand cmd = new IncrDecrCommand(commandName, key, value);
		logger.debug("sand a command : " + cmd.commandString());
		byte[] c = cmd.commandString().getBytes();
		OutputStream os;
		try {
			os = this.socket.getOutputStream();
			os.write(c);
			os.write(Command.RETURN.getBytes());
			os.flush();
			
			String reply = new BufferedReader(new InputStreamReader(this.socket.getInputStream())).readLine();
			if ("CLIENT_ERROR cannot increment or decrement non-numeric value".endsWith(reply)) {
				logger.error("cannot increment or decrement non-numeric value");
				throw new IllegalStateException("cannot increment or decrement non-numeric value");
			}
			System.out.println(reply);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * @param key
	 * @param exptime
	 * @return
	 */
	public boolean touch(String key, long exptime) {
		TouchCommand cmd = new TouchCommand(key, exptime);
		logger.debug("send a command : " + cmd.commandString());
		byte[] c = cmd.commandString().getBytes();
		OutputStream os;
		try {
			os = this.socket.getOutputStream();
			os.write(c);
			os.write(Command.RETURN.getBytes());
			os.flush();
			
			String reply = new BufferedReader(new InputStreamReader(this.socket.getInputStream())).readLine();
			System.out.println(reply);
			if ("TOUCHED".endsWith(reply)) {
				return true;
			} else if ("NOT_FOUND".endsWith(reply)) {
				return false;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * 发送flushAll命令清空所有存储的数据
	 */
	public void flushAll() {
		FlushAllCommand cmd = new FlushAllCommand();
		logger.debug("send a command : " + cmd.commandString());
		byte[] c = cmd.commandString().getBytes();
		OutputStream os;
		try {
			os = this.socket.getOutputStream();
			os.write(c);
			os.write(Command.RETURN.getBytes());
			os.flush();
			
			String reply = new BufferedReader(new InputStreamReader(this.socket.getInputStream())).readLine();
			System.out.println(reply);
		} catch (IOException e) {
		}
	}
	
}
