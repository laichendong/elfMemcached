/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 抽象的命令。发往memcached服务器的命令，都应该继承自这个类 memcached 文本协议：https://github.com/memcached/memcached/blob/master/doc/protocol.txt
 * 
 * @author laichendong
 * @since 2011-12-6 下午02:15:34
 */
public abstract class Command {
	/** 命令各个组成部分之间的分隔符 */
	public static final String DELIMITER = " ";
	/** 回车 */
	public static final String RETURN = "\r\n";

	public byte[] serialize(Object v) {
		byte[] bytes = new byte[0];
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oo;
			oo = new ObjectOutputStream(bos);
			oo.writeObject(v);
			oo.flush();
			bytes = bos.toByteArray();
			bos.close();
			oo.close();
		} catch (IOException e) {
			// 沉默是金
		}
		return bytes;
	}

	/**
	 * 构造命令字符串
	 * 
	 * @return 命令字符串
	 */
	public abstract String commandString();

}
