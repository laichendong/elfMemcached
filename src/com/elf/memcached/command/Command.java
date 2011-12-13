/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

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
	 * @throws IOException
	 */
	public static final byte[] serialize(Object obj) {
		byte[] bytes = new byte[0];
		try {
			if ((obj instanceof byte[])) {
				return (byte[]) obj;
			} else if ((obj instanceof Boolean)) {
				return serialize((Boolean) obj);
			} else if ((obj instanceof Byte)) {
				return serialize((Byte) obj);
			} else if ((obj instanceof Character)) {
				return serialize((Character) obj);
			} else if ((obj instanceof Short)) {
				return serialize((Short) obj);
			} else if ((obj instanceof Integer)) {
				return serialize(((Integer) obj).intValue());
			} else if ((obj instanceof Long)) {
				return serialize(((Long) obj).longValue());
			} else if ((obj instanceof Float)) {
				return serialize(((Float) obj).floatValue());
			} else if ((obj instanceof Double)) {
				return serialize(((Double) obj).doubleValue());
			} else if ((obj instanceof Date)) {
				return serialize((Date) obj);
			} else if ((obj instanceof String)) {
				return serialize((String) obj);
			} else if ((obj instanceof StringBuffer)) {
				return serialize((StringBuffer) obj);
			} else if ((obj instanceof StringBuilder)) {
				return serialize((StringBuilder) obj);
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oo;
				oo = new ObjectOutputStream(bos);
				oo.writeObject(obj);
				oo.flush();
				bytes = bos.toByteArray();
				bos.close();
				oo.close();
				return bytes;
			}
		} catch (IOException e) {
			return bytes;
		}
	}
	
	private static byte[] serialize(Byte b) {
		byte[] bytes = new byte[1];
		bytes[0] = b;
		return bytes;
	}
	
	private static byte[] serialize(Boolean b) {
		byte[] bytes = new byte[1];
		bytes[0] = b ? (byte) 1 : 0;
		return bytes;
	}
	
	private static byte[] serialize(int i) {
		// byte[] bytes = new byte[4];
		// bytes[0] = (byte) (int) (i >> 24 & 0xFF);
		// bytes[1] = (byte) (int) (i >> 16 & 0xFF);
		// bytes[2] = (byte) (int) (i >> 8 & 0xFF);
		// bytes[3] = (byte) (int) (i >> 0 & 0xFF);
		// return bytes;
		return serialize(i + "");
	}
	
	private static byte[] serialize(long l) {
//		byte[] bytes = new byte[8];
//		bytes[0] = (byte) (l >> 56 & 0xFF);
//		bytes[1] = (byte) (l >> 48 & 0xFF);
//		bytes[2] = (byte) (l >> 40 & 0xFF);
//		bytes[3] = (byte) (l >> 32 & 0xFF);
//		bytes[4] = (byte) (l >> 24 & 0xFF);
//		bytes[5] = (byte) (l >> 16 & 0xFF);
//		bytes[6] = (byte) (l >> 8 & 0xFF);
//		bytes[7] = (byte) (l >> 0 & 0xFF);
//		return bytes;
		return serialize(l + "");
	}
	
	private static byte[] serialize(Character c) {
		return serialize((int) c.charValue());
	}
	
	private static byte[] serialize(String s) {
		byte[] bytes = new byte[0];
		try {
			bytes = s.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return bytes;
	}
	
	private static byte[] serialize(StringBuffer sb) {
		return serialize(sb.toString());
	}
	
	private static byte[] serialize(StringBuilder sb) {
		return serialize(sb.toString());
	}
	
	private static byte[] serialize(float f) {
		return serialize(f+"");
	}
	
	private static byte[] serialize(double d) {
		return serialize(Double.doubleToLongBits(d));
	}
	
	private static byte[] serialize(Short s) {
		return serialize(s.intValue());
	}
	
	private static byte[] serialize(Date d) {
		return serialize(d.getTime());
	}
	
	/**
	 * 反序列化 把对象从字节数组反序列化出来
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 反序列化出来的对象
	 */
	public static final Object deserialize(byte[] bytes) {
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
