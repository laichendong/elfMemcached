/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author laichendong
 * @since 2012-1-11 下午02:32:06
 */
public class ASCIIClientTest {
	
	static MemcachedClient asciiClient;
	
	/**
	 * 初始化一个有用的客户端
	 */
	@BeforeClass
	public static void init() {
		MemcachedConnectionPool pool = new MemcachedConnectionPool(new String[] { "10.90.100.220:11211",
				"10.90.100.220:11211", "10.90.100.220:11211" });
		pool.initialize();
		asciiClient = new ASCIIClient(pool);
	}
	
	@After
	public void clean() {
		asciiClient.delete("k");
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#set(java.lang.String, java.lang.Object)}.
	 * 能正确返回true 且 用get能拿到预期的值。
	 */
	@Test
	public void test_set() {
		Assert.assertTrue(asciiClient.set("k", "v"));
		Assert.assertEquals("v", asciiClient.get("k"));
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#set(java.lang.String, java.lang.Object, long)}.
	 * 能正确返回true 且在有效期内可以 用get能拿到预期的值。
	 */
	@Test
	public void test_set_with_exptime() {
		Assert.assertTrue(asciiClient.set("k", "v", 10L));
		Assert.assertEquals("v", asciiClient.get("k"));
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#set(java.lang.String, java.lang.Object, long)}.
	 * 能正确返回true 且在有效期过后不再可以 用get能拿到值。
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test_set_exptime() throws InterruptedException {
		Assert.assertTrue(asciiClient.set("k", "v", 1L));
		Thread.sleep(1000);
		System.out.println(asciiClient.get("k"));
		Assert.assertNull(asciiClient.get("k"));
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#add(java.lang.String, java.lang.Object)}.
	 * 当服务器里没有这个key时，存储成功
	 */
	@Test
	public void test_add() {
		Assert.assertTrue(asciiClient.add("k", "v"));
		Assert.assertEquals(asciiClient.get("k"), "v");
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#add(java.lang.String, java.lang.Object)}.
	 * 当服务器里有这个key时，存储失败
	 */
	@Test
	public void test_add_exist_key() {
		Assert.assertTrue(asciiClient.add("k", "v"));
		Assert.assertTrue(!asciiClient.add("k", "v1"));
		Assert.assertEquals(asciiClient.get("k"), "v");
	}
	
	/**
	 * 测试{@link com.elf.memcached.ASCIIClient#add(java.lang.String, java.lang.Object, long)}.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test_add_with_exptime() throws InterruptedException {
		Assert.assertTrue(asciiClient.add("k", "v", 1L));
		Assert.assertEquals("v", asciiClient.get("k"));
		Thread.sleep(1000);
		Assert.assertNull(asciiClient.get("k"));
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#replace(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testReplaceStringObject() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#replace(java.lang.String, java.lang.Object, long)}.
	 */
	@Test
	public void testReplaceStringObjectLong() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#append(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testAppendStringObject() {
		Assert.assertTrue(!asciiClient.append("testAppendStringObject", "testAppendStringObject"));
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#append(java.lang.String, java.lang.Object, long)}.
	 */
	@Test
	public void testAppendStringObjectLong() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#prepend(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testPrependStringObject() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#prepend(java.lang.String, java.lang.Object, long)}.
	 */
	@Test
	public void testPrependStringObjectLong() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#get(java.lang.String)}.
	 */
	@Test
	public void testGet() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#gets(java.lang.String[])}.
	 */
	@Test
	public void testGets() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#delete(java.lang.String)}.
	 */
	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#incr(java.lang.String)}.
	 */
	@Test
	public void testIncrString() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#incr(java.lang.String, long)}.
	 */
	@Test
	public void testIncrStringLong() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#decr(java.lang.String)}.
	 */
	@Test
	public void testDecrString() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#decr(java.lang.String, long)}.
	 */
	@Test
	public void testDecrStringLong() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#touch(java.lang.String, long)}.
	 */
	@Test
	public void testTouch() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#flushAll()}.
	 */
	@Test
	public void testFlushAll() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#flushAll(java.lang.String[])}.
	 */
	@Test
	public void testFlushAllStringArray() {
		fail("Not yet implemented");
	}
	
}
