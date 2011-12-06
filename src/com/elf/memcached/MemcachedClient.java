/**
 * 
 */
package com.elf.memcached;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author laichendong
 */
public class MemcachedClient {
	/** 连接池 */
	private MemcachedConnectionPool connectionPool;
	
	/**
	 * 构造方法，需要指定连接池
	 * 
	 * @param connectionPool
	 *            连接池
	 */
	public MemcachedClient(MemcachedConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}
	
	/**
	 * 往服务器存储数据
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return
	 */
	public boolean set(String key, Object value) {
		MemcachedConnection conn = connectionPool.getConnection(key);
		//do something
		connectionPool.releaseConnection(conn);
		return false;
	}
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) {
		
	}
	
}
