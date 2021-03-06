/**
 * 
 */
package com.elf.memcached;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.security.InvalidParameterException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.log4j.Logger;

/**
 * memcached连接工厂，用于为连接池创建连接
 * 
 * @author laichendong
 * @since 2011-12-5
 */
public class MemcachedConnenctionFactory extends BasePoolableObjectFactory {
	/** 默认连接的端口 */
	public static final int DEFAULT_PORT = 11211;
	/** 服务器IP或主机名 */
	private String host;
	/** 服务器监听的端口 */
	private int port;
	/** 日志记录器 */
	private Logger logger = Logger.getLogger(MemcachedConnenctionFactory.class);
	/** 服务器特征字符串，标识与之对应的服务器上的socket连接池 */
	private String hostProfile;
	
	/**
	 * 构造方法
	 * 
	 * @param hostProfile 服务器特征字符串， 主机名:端口或只有主机名
	 */
	public MemcachedConnenctionFactory(String hostProfile) {
		if (hostProfile == null || hostProfile.isEmpty()) {
			logger.error("试图对一个为未指定主机创建连接。");
			throw new InvalidParameterException("试图对一个为未指定主机创建连接。");
		}
		int colonPosition = hostProfile.indexOf(":");
		if (colonPosition > 0) { // 有指定端口号,且端口号之前至少有一个字母表示主机地址。
			try {
				this.port = Integer.valueOf(hostProfile.substring(colonPosition + 1)).intValue();
			} catch (NumberFormatException e) {
				logger.error("指定的端口号不是数字。");
				throw new InvalidParameterException("指定的端口号不是数字。");
			}
			this.host = hostProfile.substring(0, colonPosition);
			this.hostProfile = hostProfile;
		} else if (colonPosition == -1) { // 没有指定端口号，使用默认端口号
			this.port = DEFAULT_PORT;
			this.host = hostProfile;
			this.hostProfile = hostProfile;
		} else if (colonPosition == 0) { // 以冒号开头，错误hostProfile的格式
			logger.error("以冒号开头，错误hostProfile的格式。");
			throw new InvalidParameterException("以冒号开头，错误hostProfile的格式。");
		}
	}
	
	/**
	 * 创建一个Memcached连接对象
	 * 
	 * @see org.apache.commons.pool.BasePoolableObjectFactory#makeObject()
	 */
	@Override
	public Object makeObject() throws Exception {
		SocketChannel localSocketChannel = SocketChannel.open();
		localSocketChannel.socket().connect(new InetSocketAddress(host, port));
		Socket socket = localSocketChannel.socket();
		MemcachedConnection conn = new MemcachedConnection(socket, hostProfile);
		return conn;
	}
	
	@Override
	public void destroyObject(Object obj) throws Exception {
		obj = null;
	}
	
	@Override
	public boolean validateObject(Object obj) {
		boolean b = super.validateObject(obj);
		b = b && ((MemcachedConnection) obj).getSocket().isConnected();
		return b;
	}
	
}
