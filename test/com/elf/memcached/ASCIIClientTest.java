/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import junit.framework.Assert;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author laichendong
 * @since 2012-1-11 下午02:32:06
 */
public class ASCIIClientTest {
	
	static MemcachedClient client;
	/** 默认的用来测试的key */
	private static final String K = "key";
	
	/**
	 * 初始化一个有用的客户端
	 */
	@BeforeClass
	public static void init() {
		MemcachedConnectionPool pool = new MemcachedConnectionPool(new String[] { "10.90.100.220:11211",
				"10.90.100.220:11211", "10.90.100.220:11211" });
		pool.initialize();
		client = new ASCIIClient(pool);
	}
	
	@After
	public void clean() {
		client.delete(K);
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#set(java.lang.String, java.lang.Object)}.
	 * 能正确返回true 且 用get能拿到预期的值。
	 */
	@Test
	public void test_set() {
		Assert.assertTrue(client.set(K, "v"));
		Assert.assertEquals("v", client.get(K));
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#set(java.lang.String, java.lang.Object, long)}.
	 * 能正确返回true 且在有效期内可以 用get能拿到预期的值。
	 */
	@Test
	public void test_set_with_exptime() {
		Assert.assertTrue(client.set(K, "v", 10L));
		Assert.assertEquals("v", client.get(K));
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#set(java.lang.String, java.lang.Object, long)}.
	 * 能正确返回true 且在有效期过后不再可以 用get能拿到值。
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test_set_exptime() throws InterruptedException {
		Assert.assertTrue(client.set(K, "v", 1L));
		Thread.sleep(1000);
		System.out.println(client.get(K));
		Assert.assertNull(client.get(K));
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#add(java.lang.String, java.lang.Object)}.
	 * 当服务器里没有这个key时，存储成功
	 */
	@Test
	public void test_add() {
		Assert.assertTrue(client.add(K, "v"));
		Assert.assertEquals(client.get(K), "v");
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#add(java.lang.String, java.lang.Object)}.
	 * 当服务器里有这个key时，存储失败
	 */
	@Test
	public void test_add_exist_key() {
		Assert.assertTrue(client.add(K, "v"));
		Assert.assertTrue(!client.add(K, "v1"));
		Assert.assertEquals(client.get(K), "v");
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#add(java.lang.String, java.lang.Object, long)}.
	 * 在超时后缓存自动失效
	 */
	@Test
	public void test_add_with_exptime() throws InterruptedException {
		Assert.assertTrue(client.add(K, "v", 1L));
		Assert.assertEquals("v", client.get(K));
		Thread.sleep(1000);
		Assert.assertNull(client.get(K));
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#append(String, CharSequence))}.
	 */
	@Test
	public void test_append() {
		// 不能直接对没有存储的key进行append
		Assert.assertEquals(false, client.append("testAppendNotExist", "testAppendNotExist"));
		
		client.add(K, "v");
		client.append(K, "_afterFix");
		Assert.assertEquals("v_afterFix", client.get(K));
	}
	
	/**
	 * 测试append到一个非字符串类的值上
	 */
	@Test
	public void test_append_to_not_string_value() {
		client.set(K, 0);
		client.append(K, "someThing");
		System.out.println("===" + client.get(K));
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#prepend(String, CharSequence)}.
	 */
	@Test
	public void test_prepend() {
		// 不能直接对没有存储的key进行prepend
		Assert.assertEquals(false, client.prepend("testPrependNotExist", "tesPrependNotExist"));
		
		client.add(K, "v");
		client.prepend(K, "preFix_");
		Assert.assertEquals("preFix_v", client.get(K));
	}
	
}
