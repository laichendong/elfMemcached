/**
 * copyright © sf-express Inc
 */
package com.elf.memcached;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * MemcachedConnenctionFactory test case
 * 
 * @author laichendong
 * @since 2011-12-6 上午10:43:25
 */
// @RunWith(Parameterized.class)
public class MemcachedConnenctionFactoryTest {
	
	/**
	 * hostProfile 为空 预期抛出InvalidParameterException
	 */
	@Test(expected = InvalidParameterException.class)
	public void constructorTest0() {
		new MemcachedConnenctionFactory(null);
	}
	
	/**
	 * hostProfile 为空字符 预期抛出InvalidParameterException
	 */
	@Test(expected = InvalidParameterException.class)
	public void constructorTest1() {
		new MemcachedConnenctionFactory("");
	}
	
	/**
	 * 有冒号 没端口 预期抛出InvalidParameterException
	 */
	@Test(expected = InvalidParameterException.class)
	public void constructorTest2() {
		new MemcachedConnenctionFactory("10.90.100.220:");
	}
	
	/**
	 * 端口为字母 预期抛出InvalidParameterException
	 */
	@Test(expected = InvalidParameterException.class)
	public void constructorTest3() {
		new MemcachedConnenctionFactory("10.90.100.220:abc");
	}
	
	/**
	 * 以冒号开头 预期抛出InvalidParameterException
	 */
	@Test(expected = InvalidParameterException.class)
	public void constructorTest4() {
		new MemcachedConnenctionFactory(":11211");
	}
	
	/**
	 * 正常指定主机和端口
	 */
	@Test
	public void constructorTest5() {
		MemcachedConnenctionFactory f = new MemcachedConnenctionFactory("10.90.100.220:11211");
		Assert.assertNotNull(f);
	}
	
	/**
	 * 正常指定主机 使用默认端口
	 */
	@Test
	public void constructorTest6() {
		MemcachedConnenctionFactory f = new MemcachedConnenctionFactory("10.90.100.220");
		Assert.assertNotNull(f);
	}
	
	/**
	 * 主机名不正确 预期抛出UnknownHostException
	 * 
	 * @throws Exception .
	 */
	@Test(expected = UnknownHostException.class)
	public void makeObjectTest1() throws Exception {
		MemcachedConnenctionFactory f = new MemcachedConnenctionFactory("aaa");
		f.makeObject();
	}
	
	/**
	 * 端口号不正确 预期抛出IOException
	 * 
	 * @throws Exception .
	 */
	@Test(expected = IOException.class)
	public void makeObjectTest2() throws Exception {
		MemcachedConnenctionFactory f = new MemcachedConnenctionFactory("10.90.100.220:11221");
		f.makeObject();
	}
	
}
