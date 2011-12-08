/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.util.Map;

/**
 * @author laichendong
 * @since 2011-12-8 下午02:47:22
 */
public abstract class MemcachedClient {
	
	public abstract boolean set(String key, Object value);
	
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
	
}
