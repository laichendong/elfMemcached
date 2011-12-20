/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;


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
	 * 构造这条命令的命令字符串，storage命令，不包含数据块
	 * 
	 * @return 命令字符串
	 */
	public abstract String commandString();
	
}
