/**
 * 
 */
package com.elf.memcached;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.pool.impl.GenericObjectPool;

import com.elf.memcached.command.Command;
import com.elf.memcached.command.Command.CommandNames;

/**
 * ASCII 协议的客户端
 * 
 * @author laichendong
 */
public class ASCIIClient implements MemcachedClient {
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
	public <T extends CharSequence> boolean append(String key, T value) throws IllegalStateException {
		return this.append(key, value, 0L);
	}
	
	@Override
	public <T extends CharSequence> boolean append(String key, T value, long exptime) throws IllegalStateException {
		return this.storage(CommandNames.APPEND, key, value.toString(), exptime);
	}
	
	@Override
	public <T extends CharSequence> boolean prepend(String key, T value) throws IllegalStateException  {
		return this.prepend(key, value, 0L);
	}
	
	@Override
	public <T extends CharSequence> boolean prepend(String key, T value, long exptime) throws IllegalStateException  {
		return this.storage(CommandNames.PREPEND, key, value.toString(), exptime);
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
		// TODO 待实现
		return null;
	}
	
	@Override
	public boolean delete(String key) {
		boolean result = false;
		MemcachedConnection conn = connectionPool.getConnection(key);
		result = conn.delete(key);
		connectionPool.releaseConnection(conn);
		return result;
	}
	
	@Override
	public long incr(String key) {
		return this.incrDecr(Command.CommandNames.INCR, key, 1L);
	}
	
	@Override
	public long incr(String key, long value) {
		return this.incrDecr(Command.CommandNames.INCR, key, value);
	}
	
	@Override
	public long decr(String key) {
		return this.incrDecr(Command.CommandNames.DECR, key, 1L);
	}
	
	@Override
	public long decr(String key, long value) {
		return this.incrDecr(Command.CommandNames.DECR, key, value);
	}
	
	private long incrDecr(Command.CommandNames commandName, String key, long value) {
		long result = 0;
		MemcachedConnection conn = connectionPool.getConnection(key);
		result = conn.incrDecr(commandName, key, value);
		connectionPool.releaseConnection(conn);
		return result;
	}
	
	@Override
	public boolean touch(String key, long exptime) {
		boolean result = false;
		MemcachedConnection conn = connectionPool.getConnection(key);
		result = conn.touch(key, exptime);
		connectionPool.releaseConnection(conn);
		return result;
	}
	
	@Override
	public void flushAll() {
		ConcurrentMap<String, GenericObjectPool> pools = connectionPool.getPools();
		for (Entry<String, GenericObjectPool> entry : pools.entrySet()) {
			try {
				MemcachedConnection conn = (MemcachedConnection) entry.getValue().borrowObject();
				conn.flushAll();
			} catch (Exception e) {
			}
		}
		
	}
	
	@Override
	public void flushAll(String... serverProfiles) {
		ConcurrentMap<String, GenericObjectPool> pools = connectionPool.getPools();
		for (Entry<String, GenericObjectPool> entry : pools.entrySet()) {
			try {
				for (String server : serverProfiles) {
					if (server.equals(entry.getKey())) {
						MemcachedConnection conn = (MemcachedConnection) entry.getValue().borrowObject();
						conn.flushAll();
					}
				}
			} catch (Exception e) {
			}
		}
	}

}
