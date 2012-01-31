/**
 * copyright © sf-express Inc
 *
 */
package com.elf.memcached.command;

/**
 * 命令中的标志位
 * @author laichendong
 * @since 2012-1-31 下午03:33:05
 */
public enum Flag {
	NULL(-1),
	BOOLEAN(0),
	NUMBER(1),
	CHAR_SEQUENCE(2),
	DATE(3),
	BYTE_ARRY(4),
	OBJ_BIN(5),
	OBJ_JSON(6);
	
	/** 标志位具体的值 */
	private int value;
	
	Flag(int value){
		this.value = value;
	}
	
	/**
	 * 根据给定的int值转换成Flag枚举值。
	 * @param value 给定一个int值
	 * @return 与给定值对应的枚举值，如果找不到对应的枚举值，则返回{@code null}
	 */
	public static Flag fromInt(int value){
		for(Flag flag : Flag.values()){
			if(flag.getValue() == value){
				return flag;
			}
		}
		return null;
	}
	
	/**
	 * 获取标志位具体的值
	 * @return 标志位具体的值
	 */
	public int getValue() {
		return value;
	}
	
}
