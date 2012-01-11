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
import java.security.InvalidParameterException;
import java.util.Date;

/**
 * 编码解码器，序列化，反序列化
 * 
 * @author laichendong
 * @since 2011-12-13 下午05:00:27
 */
public final class Coder {
	public static final int BOOLEAN = 0;
	public static final int BYTE = 1;
	public static final int SHORT = 2;
	public static final int CHAR = 3;
	public static final int INT = 4;
	public static final int LONG = 5;
	public static final int FLOAT = 6;
	public static final int DOUBLE = 7;
	public static final int STRING = 8;
	public static final int STRING_BUFFER = 9;
	public static final int STRING_BUILDER = 10;
	public static final int DATE = 11;
	public static final int BYTE_ARRY = 12;
	public static final int OBJ_BIN = 13;
	public static final int OBJ_JSON = 14;
	
	/**
	 * 根据对象类型 标记不同的flag。 不同的flag有不同的序列化方式，反序列化时要使用相同的方法反序列化
	 * 
	 * @param obj
	 * @return
	 */
	public static int mark(Object obj) {
		int flag = -1;
		if ((obj instanceof byte[])) {
			flag = BYTE_ARRY;
		} else if ((obj instanceof Boolean)) {
			flag = BOOLEAN;
		} else if ((obj instanceof Byte)) {
			flag = BYTE;
		} else if ((obj instanceof Character)) {
			flag = CHAR;
		} else if ((obj instanceof Short)) {
			flag = SHORT;
		} else if ((obj instanceof Integer)) {
			flag = INT;
		} else if ((obj instanceof Long)) {
			flag = LONG;
		} else if ((obj instanceof Float)) {
			flag = FLOAT;
		} else if ((obj instanceof Double)) {
			flag = DOUBLE;
		} else if ((obj instanceof Date)) {
			flag = DATE;
		} else if ((obj instanceof String)) {
			flag = STRING;
		} else if ((obj instanceof StringBuffer)) {
			flag = STRING_BUFFER;
		} else if ((obj instanceof StringBuilder)) {
			flag = STRING_BUILDER;
		} else {
			flag = OBJ_BIN;
		}
		return flag;
	}
	
	private static final byte[] encode(byte[] s) {
		return s;
	}
	
	private static final byte[] encode(Boolean s) {
		return encode(s ? "1" : "0");
	}
	
	private static final byte[] encode(Byte s) {
		return new byte[] { s };
	}
	
	private static final byte[] encode(Character s) {
		return encode(s + "");
	}
	
	private static final byte[] encode(Short s) {
		return encode(s + "");
	}
	
	private static final byte[] encode(Integer s) {
		return encode(s + "");
	}
	
	private static final byte[] encode(Long s) {
		return encode(s + "");
	}
	
	private static final byte[] encode(Float s) {
		return encode(s + "");
	}
	
	private static final byte[] encode(Double s) {
		return encode(s + "");
	}
	
	private static final byte[] encode(Date s) {
		return encode(s.getTime() + "");
	}
	
	private static final byte[] encode(String s) {
		byte[] bytes = new byte[0];
		try {
			bytes = s.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// 沉默是金
		}
		return bytes;
	}
	
	private static final byte[] encode(StringBuffer s) {
		return encode(s.toString() + "");
	}
	
	private static final byte[] encode(StringBuilder s) {
		return encode(s.toString() + "");
	}
	
	public static void main(String[] s) {
		System.out.println(decode(encode(new byte[] { 53 }), BYTE_ARRY));
		System.out.println(decode(encode(true), BOOLEAN));
		System.out.println(decode(encode(Byte.valueOf("53")), BYTE));
		System.out.println(decode(encode('a'), CHAR));
		System.out.println(decode(encode(4), INT));
		System.out.println(decode(encode((short) 4), SHORT));
		System.out.println(decode(encode(4L), LONG));
		System.out.println(decode(encode(4.2F), FLOAT));
		System.out.println(decode(encode(43.1D), DOUBLE));
		System.out.println(decode(encode(new Date()), DATE));
		System.out.println(decode(encode("ss"), STRING));
		System.out.println(decode(encode(new StringBuffer("sb")), STRING_BUFFER));
		System.out.println(decode(encode(new StringBuilder("ssbb")), STRING_BUILDER));
		System.out.println(decode(encode(new Date()), OBJ_BIN));
	}
	
	/**
	 * 使用默认的java序列化机制序列化
	 * 
	 * @param obj 待序列化的对象
	 * @return 序列化结果
	 */
	private static final byte[] encode(Object obj) {
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
			return bytes;
		} catch (IOException e) {
			return bytes;
		}
	}
	
	/**
	 * 使用java默认的反序列化机制反序列化
	 * 
	 * @param bytes 字节数组
	 * @return 反序列化出来的对象
	 */
	private static final Object decode(byte[] bytes) {
		Object obj = new Object();
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
			obj = ois.readObject();
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return obj;
	}
	
	public static final Object decode(byte[] bytes, int flag) {
		if (flag < 0) {
			throw new InvalidParameterException("flag 标志位必须不小于零");
		}
		Object obj = null;
		switch (flag) {
			case BYTE_ARRY: {
				obj = bytes;
				break;
			}
			case BOOLEAN: {
				obj = "1".equals(new String(bytes));
				break;
			}
			case BYTE: {
				obj = bytes[0];
				break;
			}
			case SHORT: {
				obj = Short.valueOf(new String(bytes));
				break;
			}
			case CHAR: {
				obj = Character.valueOf((char) bytes[0]);
				break;
			}
			case INT: {
				obj = Integer.valueOf(new String(bytes));
				break;
			}
			case LONG: {
				obj = Long.valueOf(new String(bytes));
				break;
			}
			case FLOAT: {
				obj = Float.valueOf(new String(bytes));
				break;
			}
			case DOUBLE: {
				obj = Double.valueOf(new String(bytes));
				break;
			}
			case DATE: {
				obj = new Date(Long.valueOf(new String(bytes)));
				break;
			}
			case STRING: {
				obj = new String(bytes);
				break;
			}
			case STRING_BUFFER: {
				obj = new StringBuffer(new String(bytes));
				break;
			}
			case STRING_BUILDER: {
				obj = new StringBuilder(new String(bytes));
				break;
			}
			case OBJ_BIN: {
				obj = decode(bytes);
				break;
			}
		}
		return obj;
	}
	
	public static byte[] encode(Object value, int flag) {
		if (flag < 0) {
			throw new InvalidParameterException("flag 标志位必须不小于零");
		}
		byte[] bytes = new byte[0];
		switch (flag) {
			case BYTE_ARRY: {
				bytes = encode((byte[]) value);
				break;
			}
			case BOOLEAN: {
				bytes = encode((Boolean) value);
				break;
			}
			case BYTE: {
				bytes = encode((Byte) value);
				break;
			}
			case SHORT: {
				bytes = encode((Short) value);
				break;
			}
			case CHAR: {
				bytes = encode((Character) value);
				break;
			}
			case INT: {
				bytes = encode((Integer) value);
				break;
			}
			case LONG: {
				bytes = encode((Long) value);
				break;
			}
			case FLOAT: {
				bytes = encode((Float) value);
				break;
			}
			case DOUBLE: {
				bytes = encode((Double) value);
				break;
			}
			case DATE: {
				bytes = encode((Date) value);
				break;
			}
			case STRING: {
				bytes = encode((String) value);
				break;
			}
			case STRING_BUFFER: {
				bytes = encode((StringBuffer) value);
				break;
			}
			case STRING_BUILDER: {
				bytes = encode((StringBuilder) value);
				break;
			}
			case OBJ_BIN: {
				bytes = encode(value);
				break;
			}
		}
		return bytes;
	}
}
