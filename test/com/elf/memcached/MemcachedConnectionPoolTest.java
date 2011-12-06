/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * MemcachedConnectionPool test case
 * 
 * @author laichendong
 * @since 2011-12-6 上午09:41:02
 */
public class MemcachedConnectionPoolTest {
	
	MemcachedConnectionPool pool;
	
	/**
	 * 测试之前初始化连接池
	 */
	@Before
	public void initPool() {
		pool = new MemcachedConnectionPool(
				new String[] { "10.90.100.220:11211", "10.90.100.220:11211", "10.90.100.220:11211" });
		pool.initialize();
	}
	
	/**
	 * 测试之后关闭连接池
	 */
	@After
	public void closePool() {
		pool.close();
	}
	
	/**
	 * 测试获取连接
	 */
	@Test
	public void getConnectionTest() {
		MemcachedConnection conn = pool.getConnection("laichendong");
		Assert.assertTrue(conn.getSocket().isConnected());
	}
}
