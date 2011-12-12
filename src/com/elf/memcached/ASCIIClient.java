/**
 * 
 */
package com.elf.memcached;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import com.elf.memcached.command.Command.CommandNames;

/**
 * ASCII 协议的客户端
 * 
 * @author laichendong
 */
public class ASCIIClient extends MemcachedClient {
	/** 连接池 */
	private MemcachedConnectionPool connectionPool;

	/**
	 * 构造方法，需要指定连接池
	 * 
	 * @param connectionPool
	 *            连接池
	 */
	public ASCIIClient(MemcachedConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) {
		MemcachedConnectionPool connectionPool = new MemcachedConnectionPool(new String[] { "10.90.100.220:11211" });
		connectionPool.initialize();
		ASCIIClient c = new ASCIIClient(connectionPool);
		// long t = System.currentTimeMillis();
		// for (int i = 0; i < 100; i++) {
		// c.set(i + "", i + "");
		// }
		// System.out.println(System.currentTimeMillis() - t);
		c.get("lai");
	}

	/**
	 * 向服务器存储数据
	 * 
	 * @param cmdName
	 *            命令名称
	 * @param key
	 *            待存储的key
	 * @param value
	 *            待存储的value
	 * @param exptime
	 *            过期时间
	 * @return 是否存储成功
	 */
	private boolean storage(CommandNames cmdName, String key, Object value, long exptime) {
		boolean successed = false;
		MemcachedConnection conn = connectionPool.getConnection(key);
		successed = conn.storage(cmdName, key, value, exptime);
		connectionPool.releaseConnection(conn);
		return successed;
	}

	@Override
	public boolean set(String key, Object value) {
		return this.set(key, value, 0L);
	}

	@Override
	public boolean set(String key, Object value, long exptime) {
		return this.storage(CommandNames.SET, key, value, exptime);
	}

	@Override
	public boolean add(String key, Object value) {
		return this.add(key, value, 0L);
	}

	@Override
	public boolean add(String key, Object value, long exptime) {
		return this.storage(CommandNames.ADD, key, value, exptime);
	}

	@Override
	public boolean replace(String key, Object value) {
		return this.replace(key, value, 0L);
	}

	@Override
	public boolean replace(String key, Object value, long exptime) {
		return this.storage(CommandNames.REPLACE, key, value, exptime);
	}

	@Override
	public boolean append(String key, Object value) {
		return this.append(key, value, 0L);
	}

	@Override
	public boolean append(String key, Object value, long exptime) {
		return this.storage(CommandNames.APPEND, key, value, exptime);
	}

	@Override
	public boolean prepend(String key, Object value) {
		return this.prepend(key, value, 0L);
	}

	@Override
	public boolean prepend(String key, Object value, long exptime) {
		return this.storage(CommandNames.PREPEND, key, value, exptime);
	}

	@Override
	public Object get(String key) {
		MemcachedConnection conn = connectionPool.getConnection(key);
		Object obj = conn.get(CommandNames.GET, key);
		connectionPool.releaseConnection(conn);
		return obj;
	}

	@Override
	public Map<String, Object> gets(String... keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String key) {
		boolean result =false;
		MemcachedConnection conn = connectionPool.getConnection(key);
		result = conn.delete(key);
		connectionPool.releaseConnection(conn);
		return result;
	}

}
