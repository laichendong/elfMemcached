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
public final class EncoderDecoder {
	
	/**
	 * 根据对象类型 标记不同的flag。 不同的flag有不同的序列化方式，反序列化时要使用相同的方法反序列化
	 * 
	 * @param obj 待标记的对象
	 * @return 标记
	 */
	public static Flag mark(Object obj) {
		Flag flag = Flag.OBJ_BIN;
		if (obj == null){
			flag = Flag.NULL;
		} else if ((obj instanceof byte[])) {
			flag = Flag.BYTE_ARRY;
		} else if ((obj instanceof Boolean)) {
			flag = Flag.BOOLEAN;
		} else if ((obj instanceof Number)) {
			flag = Flag.NUMBER;
		} else if ((obj instanceof Date)) {
			flag = Flag.DATE;
		} else if ((obj instanceof CharSequence)) {
			flag = Flag.CHAR_SEQUENCE;
		} else {
			flag = Flag.OBJ_BIN;
		}
		return flag;
	}
	
	private static final byte[] encodeString(String s) {
		byte[] bytes = new byte[0];
		try {
			bytes = s.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// 沉默是金
		}
		return bytes;
	}
	
	/**
	 * 使用默认的java序列化机制序列化
	 * 
	 * @param obj 待序列化的对象
	 * @return 序列化结果
	 */
	private static final byte[] encodeObject(Object obj) {
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
	
	public static final Object decode(byte[] bytes, Flag flag) {
		Object obj = null;
		switch (flag) {
			case NULL: {
				obj = null;
				break;
			}
			case BYTE_ARRY: {
				obj = bytes;
				break;
			}
			case BOOLEAN: {
				obj = "1".equals(new String(bytes));
				break;
			}
			case DATE: {
				obj = new Date(Long.valueOf(new String(bytes)));
				break;
			}
			case CHAR_SEQUENCE: {
				obj = new String(bytes);
				break;
			}
			case OBJ_BIN: {
				obj = decode(bytes);
				break;
			}
		}
		return obj;
	}
	
	public static final byte[] encode(Object value, Flag flag) {
		if (flag == Flag.NULL) {
			throw new InvalidParameterException("flag 标志位必须不小于零");
		}
		byte[] bytes = new byte[0];
		switch (flag) {
			case BYTE_ARRY: {
				bytes = (byte[]) value;
				break;
			}
			case BOOLEAN: {
				bytes = encodeString((Boolean) value ? "1" : "0");
				break;
			}
			case DATE: {
				bytes = encodeString(String.valueOf(((Date) value).getTime()));
				break;
			}
			case CHAR_SEQUENCE: {
				bytes = encodeString(value.toString());
				break;
			}
			case OBJ_BIN: {
				bytes = encodeObject(value);
				break;
			}
		}
		return bytes;
	}
}
