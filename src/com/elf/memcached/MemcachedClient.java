/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.util.Map;

/**
 * @author laichendong
 * @since 2011-12-8 下午02:47:22
 */
public interface MemcachedClient {
	
	/**
	 * 向服务器存储一个k,v对。如果k已经存在，则会覆盖掉之前的值。{@link com.elf.memcached.MemcachedClient#set(java.lang.String, java.lang.Object, long)}
	 * 
	 * @param key 存储使用的key
	 * @param value 处处的value
	 * @return 如果存储成功，则返回true，否则返回false。
	 */
	public abstract boolean set(String key, Object value);
	
	/**
	 * @param key
	 * @param value
	 * @param exptime
	 * @return
	 */
	public abstract boolean set(String key, Object value, long exptime);
	
	public abstract boolean add(String key, Object value);
	
	public abstract boolean add(String key, Object value, long exptime);
	
	public abstract boolean replace(String key, Object value);
	
	public abstract boolean replace(String key, Object value, long exptime);
	
	public abstract boolean append(String key, Object value);
	
	public abstract boolean append(String key, Object value, long exptime);
	
	public abstract boolean prepend(String key, Object value);
	
	public abstract boolean prepend(String key, Object value, long exptime);
	
	public abstract Object get(String key);
	
	public abstract Map<String, Object> gets(String... keys);
	
	public abstract boolean delete(String key);
	
	public abstract long incr(String key);
	
	public abstract long incr(String key, long value);
	
	public abstract long decr(String key);
	
	public abstract long decr(String key, long value);
	
	public abstract boolean touch(String key, long exptime);
	
	public abstract void flushAll();
	
	public abstract void flushAll(String... serverProfiles);
	
}
