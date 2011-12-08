/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
	/** \r\n */
	public static final String RETURN = "\r\n";
	/** noreply */
	public static final String NOREPLY = "noreply";
	
	/** 命令名称 */
	protected CommandNames commandName;
	/** 通知server不用返回响应结果 */
	protected boolean noreply;
	
	/**
	 * 存储命令包含的命令名称枚举
	 * 
	 * @author laichendong
	 * @since 2011-12-6 下午06:01:13
	 */
	public enum CommandNames {
		SET("set"), ADD("add"), REPLACE("replace"), APPEND("append"), PREPEND("prepend")//
		, CAS("cas")//
		, GET("get"), GETS("gets")//
		, DELETE("delete")//
		, INCR("incr"), DECR("decr")//
		, TOUCH("touch")//
		, FLUSH_ALL("flush_all")//
		;
		/** 命令名称 */
		String name;
		
		CommandNames(String name) {
			this.name = name;
		}
	}
	
	/**
	 * 序列化 把对象序列化成字节数组
	 * 
	 * @param obj
	 *            待序列化的对象
	 * @return 对象序列化结果
	 */
	public final byte[] serialize(Object obj) {
		byte[] bytes = new byte[0];
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oo;
			oo = new ObjectOutputStream(bos);
			oo.writeObject(obj);
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
	 * 反序列化 把对象从字节数组反序列化出来
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 反序列化出来的对象
	 */
	public final Object deserialize(byte[] bytes) {
		Object obj = new Object();
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
			obj = ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 构造这条命令的命令字符串，storage命令，不包含数据块
	 * 
	 * @return 命令字符串
	 */
	public abstract String commandString();

	public CommandNames getCommandName() {
		return commandName;
	}

	public void setCommandName(CommandNames commandName) {
		this.commandName = commandName;
	}

	public boolean isNoreply() {
		return noreply;
	}

	public void setNoreply(boolean noreply) {
		this.noreply = noreply;
	}
	
}
