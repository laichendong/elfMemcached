/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

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
	/** 日志记录器 */
	private static Logger logger = Logger.getLogger(MemcachedConnectionPool.class);
	/** MD5实例 */
	private MessageDigest md5 = null;

	public MemcachedConnectionPool() {
		if (!this.initialized) {
			this.initialize();
		}
	}

	/**
	 * 初始化连接池 如果服务器列表为空，将抛出IllegalStateException异常 正常初始化后 initialized将设置成true
	 */
	public synchronized void initialize() {
		if (this.servers == null || this.servers.length <= 0) {
			logger.error("试图在没有指定任何服务器的情况下初始化连接池。");
			throw new IllegalStateException("试图在没有指定任何服务器的情况下初始化连接池。");
		}
		try {
			this.pools = new ConcurrentHashMap<String, GenericObjectPool>();
			for (String server : this.servers) {
				GenericObjectPool poolOnOneServer = new GenericObjectPool(new MemcachedConnenctionFactory(server));
				poolOnOneServer.setMaxActive(this.maxActive);
				poolOnOneServer.setWhenExhaustedAction((byte) 1);
				poolOnOneServer.setMaxWait(this.maxWait);
				poolOnOneServer.setMaxIdle(this.maxIdle);
				poolOnOneServer.setMinIdle(this.minIdle);
				pools.putIfAbsent(server, poolOnOneServer);
			}
			initialized = true;
		} catch (Exception e) {
			initialized = false;
		}
	}

	/**
	 * 获取socket连接
	 * 
	 * @param key
	 *            获取连接的key
	 * @return 到对应服务器的socket连接
	 */
	public Socket getConnection(String key) {
		if(!this.initialized){ // 检查连接池是否初始化
			this.initialize();
		}
		
		long hash = md5HashingAlg(key);
		Socket connection = null;
		GenericObjectPool poolOnTheServer = pools.get(servers[(int) (hash % servers.length)]);
		try {
			connection = (Socket) poolOnTheServer.borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 基于MD5的hash算法
	 * 
	 * @param key
	 *            待hash的key
	 * @return hash值
	 */
	private long md5HashingAlg(String key) {
		if (md5 == null) {
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				logger.error("系统没有提供MD5算法。");
				throw new IllegalStateException("系统没有提供MD5算法。");
			}
		}

		md5.reset();
		md5.update(key.getBytes());
		byte[] bKey = md5.digest();
		long res = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8)
				| (long) (bKey[0] & 0xFF);
		return res;
	}

}
