/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.util.concurrent.ConcurrentMap;

import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * memcached连接池
 * 
 * @author laichendong
 * @since 2011-12-5 下午05:40:09
 */
public class MemcachedConnectionPool {
	/** 默认最大激活连接的数量 */
	public static final int DEFAULT_MAX_ACTIVE = 32;
	/** 默认最大空闲连接的数量 */
	public static final int DEFAULT_MAX_IDLE = 16;
	/** 默认最小空闲连接的数量 */
	public static final int DEFAULT_MIN_IDLE = 8;
	/** 默认最长等待时间，ms为单位 */
	public static final long DEFAULT_MAX_WAIT = 1000L;
	
	/** 最大激活连接数 */
	private int maxActive = DEFAULT_MAX_ACTIVE;
	/** 最大空闲连接数 */
	private int maxIdle = DEFAULT_MAX_IDLE;
	/** 最小空闲连接数 */
	private int minIdle = DEFAULT_MIN_IDLE;
	/** 最长等待时间，ms为单位 */
	private long maxWait = DEFAULT_MAX_WAIT;
	
	/** 服务器ID列表，可以为IP或host name */
	private String[] servers;
	/** 对应到每个服务器的连接池Map */
	private ConcurrentMap<String, GenericObjectPool> pools;
	/** 标记该连接池是否初始化完成 */
	private boolean initialized = false;
	
	
}
