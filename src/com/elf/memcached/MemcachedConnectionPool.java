/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
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
	
	/**
	 * 默认构造方法，之后应该使用 setServers(String[] servers) 方法设置服务器列表
	 */
	public MemcachedConnectionPool() {
		
	}
	
	/**
	 * 指定服务器列表的构造方法
	 * 
	 * @param servers 服务器列表
	 */
	public MemcachedConnectionPool(String servers[]) {
		this.servers = servers;
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
			GenericObjectPool.Config conf = makeConfig();
			for (String hostProfile : this.servers) {
				GenericObjectPool poolOnOneServer = new GenericObjectPool(new MemcachedConnenctionFactory(hostProfile),
						conf);
				pools.putIfAbsent(hostProfile, poolOnOneServer);
			}
			initialized = true;
		} catch (Exception e) {
			initialized = false;
			logger.error("不能获取与服务器的连接，连接池初始化失败。", e);
			throw new IllegalStateException("不能获取与服务器的连接，连接池初始化失败。", e);
		}
	}
	
	/**
	 * 创建连接池配置对象
	 * 
	 * @return 连接池配置对象
	 */
	private Config makeConfig() {
		Config conf = new Config();
		conf.lifo = true;// 后进先出
		conf.maxActive = this.maxActive; // 最大激活数（池容量）
		conf.maxIdle = this.maxIdle; // 最大空闲连接数
		conf.maxWait = this.maxWait; // 从池里取出连接时最大的等待时间
		conf.minEvictableIdleTimeMillis = 60 * 1000; // 超过一分钟空闲的连接才能被清理
		conf.minIdle = this.minIdle; // 最小空闲连接
		conf.numTestsPerEvictionRun = -8;// 后台清理时每次检查当前连接数的1/8的连接。
		conf.softMinEvictableIdleTimeMillis = GenericObjectPool.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS;// ？
		conf.testOnBorrow = true; // 从池中取出连接时检查连接的有效性
		conf.testOnReturn = true; // 把连接放回池中时检查连接的有效性
		conf.testWhileIdle = false; // 后台清理连接时。不对没过期的连接进行有效性检查
		conf.timeBetweenEvictionRunsMillis = 5 * 60 * 1000; // 每5分钟进行一次后台连接清理
		conf.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK; // 当从池中取出连接且空闲连接用完时，等待下一个空闲连接（有人还回来）
		return conf;
	}
	
	/**
	 * 获取一个可用的socket连接
	 * 
	 * @param key 获取连接的key
	 * @return 到对应服务器的socket连接
	 */
	public MemcachedConnection getConnection(String key) {
		if (!this.initialized) { // 检查连接池是否初始化
			logger.error("试图从一个没有初始化的连接池里获取连接");
			throw new IllegalStateException("试图从一个没有初始化的连接池里获取连接");
		}
		
		long hash = md5HashingAlg(key);
		MemcachedConnection connection = null;
		GenericObjectPool poolOnTheServer = pools.get(servers[(int) (hash % servers.length)]);
		try {
			connection = (MemcachedConnection) poolOnTheServer.borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 关闭连接池
	 */
	public void close() {
		this.servers = null;
		for (Entry<String, GenericObjectPool> pool : this.pools.entrySet()) {
			try {
				pool.getValue().close();
			} catch (Exception e) {
				// 沉默是金
			}
		}
		this.pools.clear();
		this.pools = null;
		this.initialized = false;
	}
	
	/**
	 * 基于MD5的hash算法
	 * 
	 * @param key 待hash的key
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
		long hash = ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8)
				| (long) (bKey[0] & 0xFF);
		return hash;
	}
	
	/**
	 * 客户端用完连接后，调用该方法还回连接池
	 * 
	 * @param conn 待释放的连接
	 */
	public void releaseConnection(MemcachedConnection conn) {
		String hostProfile = conn.getHostProfile();
		GenericObjectPool poolOnTheServer = pools.get(hostProfile);
		try {
			poolOnTheServer.returnObject(conn);
			conn = null;
		} catch (Exception e) {
			// 沉默是金
		}
	}
	
	public static void main(String[] asd) throws IOException {
		MemcachedConnectionPool connectionPool = new MemcachedConnectionPool(new String[] { "10.90.100.220:11211" });
		connectionPool.initialize();
		long t = System.currentTimeMillis();
		for (int i = 0; i < 32; i++) {
			MemcachedConnection conn = connectionPool.getConnection("lai");
			if (conn.getSocket().isConnected()) {
				conn.getSocket().getOutputStream().write("set key 0 0 1\r\n1\r\n".getBytes());
				conn.getSocket().getOutputStream().flush();
				String s = new BufferedReader(new InputStreamReader(conn.getSocket().getInputStream())).readLine();
				System.out.println(i + s);
			}
			connectionPool.releaseConnection(conn);
		}
		// for(int j=0; j<connectionPool.maxActive; j++){
		// connectionPool.getConnection("lai");
		// }
		int a = connectionPool.pools.get("10.90.100.220:11211").getNumActive();
		int idle = connectionPool.pools.get("10.90.100.220:11211").getNumIdle();
		System.out.println(a + "==" + idle);
		System.out.println(System.currentTimeMillis() - t);
	}
	
	public ConcurrentMap<String, GenericObjectPool> getPools() {
		return pools;
	}
	
}
