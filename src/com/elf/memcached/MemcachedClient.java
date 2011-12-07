/**
 * 
 */
package com.elf.memcached;

import java.io.IOException;
import java.net.UnknownHostException;

import com.elf.memcached.command.StorageCommand;

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
		boolean successed = false;
		MemcachedConnection conn = connectionPool.getConnection(key);
		successed = conn.storage(StorageCommand.CommandNames.SET, key, value);
		connectionPool.releaseConnection(conn);
		return successed;
	}
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) {
		MemcachedConnectionPool connectionPool = new MemcachedConnectionPool(new String[]{"127.0.0.1:11211"});
		connectionPool.initialize();
		MemcachedClient c = new MemcachedClient(connectionPool);
		c.set("lai", "chendong");
	}
	
}
