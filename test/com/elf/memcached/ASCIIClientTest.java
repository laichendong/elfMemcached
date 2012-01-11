/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author laichendong
 * @since 2012-1-11 下午02:32:06
 */
public class ASCIIClientTest {
	
	MemcachedClient asciiClient;
	
	@BeforeClass
	public void init() {
		MemcachedConnectionPool pool = new MemcachedConnectionPool(new String[] { "10.90.100.220:11211",
				"10.90.100.220:11211", "10.90.100.220:11211" });
		pool.initialize();
		asciiClient = new ASCIIClient(pool);
	}
	
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#set(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testSetStringObject() {
		assertTrue(asciiClient.set("k", "v"));
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#set(java.lang.String, java.lang.Object, long)}.
	 */
	@Test
	public void testSetStringObjectLong() {
		assertTrue(asciiClient.set("k", "v", 100L));
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#add(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testAddStringObject() {
		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link com.elf.memcached.ASCIIClient#add(java.lang.String, java.lang.Object, long)}.
	 */
	@Test
	public void testAddStringObjectLong() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
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
