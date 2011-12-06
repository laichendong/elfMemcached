/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.net.Socket;

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
	
}
