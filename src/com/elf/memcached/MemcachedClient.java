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
	 * 向服务器存储一个k,v对（不过期，设置过期时间请用：{@link com.elf.memcached.MemcachedClient#set(java.lang.String, java.lang.Object, long)}
	 * ）。如果k已经存在，则会覆盖掉之前的值。
	 * 
	 * @param key 存储使用的key
	 * @param value 存储的value
	 * @return 如果存储成功，则返回true，否则返回false。
	 */
	public abstract boolean set(String key, Object value);
	
	/**
	 * 向服务器存储一个k,v对。如果k已经存在，则会覆盖掉之前的值。用exptime设置过期时间。
	 * 如果设置的小于60*60*24*30（30天），表示从设置到exptime秒后过期；如果大于等于这个值，则表示exptime是一个unix时间戳。到这个时间戳表示的时间过期
	 * 
	 * @param key 存储使用的key
	 * @param value 存储的value
	 * @param exptime 过期时间，单位为秒
	 * @return 如果存储成功，则返回true，否则返回false。
	 */
	public abstract boolean set(String key, Object value, long exptime);
	
	/**
	 * 仅当服务器<b>没有</b>用这个key存储数据时，向服务器存储这个k,v对。和{@link com.elf.memcached.MemcachedClient#replace(String, Object)}
	 * 刚好相反。（不过期，设置过期时间请用： {@link com.elf.memcached.MemcachedClient#add(String, Object, long)})
	 * 
	 * @param key 存储使用的key
	 * @param value 存储的value
	 * @return 如果存储成功，则返回true，否则返回false。
	 */
	public abstract boolean add(String key, Object value);
	
	/**
	 * 仅当服务器<b>没有</b>用这个key存储数据时，向服务器存储这个k,v对。和{@link com.elf.memcached.MemcachedClient#replace(String, Object, long)}
	 * 刚好相反。用exptime设置过期时间。
	 * 如果设置的小于60*60*24*30（30天），表示从设置到exptime秒后过期；如果大于等于这个值，则表示exptime是一个unix时间戳。到这个时间戳表示的时间过期
	 * 
	 * @param key 存储使用的key
	 * @param value 存储的value
	 * @param exptime 过期时间，单位为秒
	 * @return 如果存储成功，则返回true，否则返回false。
	 */
	public abstract boolean add(String key, Object value, long exptime);
	
	/**
	 * 仅当服务器<b>有</b>用这个key存储数据时，向服务器存储这个k,v对。和{@link com.elf.memcached.MemcachedClient#add(String, Object)}
	 * 刚好相反。（不过期，设置过期时间请用： {@link com.elf.memcached.MemcachedClient#replace(String, Object, long)})
	 * 
	 * @param key 存储使用的key
	 * @param value 存储的value
	 * @return 如果存储成功，则返回true，否则返回false。
	 */
	public abstract boolean replace(String key, Object value);
	
	/**
	 * 仅当服务器<b>有</b>用这个key存储数据时，向服务器存储这个k,v对。和{@link com.elf.memcached.MemcachedClient#add(String, Object, long)}
	 * 刚好相反。用exptime设置过期时间。
	 * 如果设置的小于60*60*24*30（30天），表示从设置到exptime秒后过期；如果大于等于这个值，则表示exptime是一个unix时间戳。到这个时间戳表示的时间过期
	 * 
	 * @param key 存储使用的key
	 * @param value 存储的value
	 * @param exptime 过期时间，单位为秒
	 * @return 如果存储成功，则返回true，否则返回false。
	 */
	public abstract boolean replace(String key, Object value, long exptime);
	
	public abstract <T extends CharSequence> boolean append(String key, T value);
	
	public abstract <T extends CharSequence> boolean append(String key, T value, long exptime);
	
	public abstract <T extends CharSequence> boolean prepend(String key, T value);
	
	public abstract <T extends CharSequence> boolean prepend(String key, T value, long exptime);
	
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
