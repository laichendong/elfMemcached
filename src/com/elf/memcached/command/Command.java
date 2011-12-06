/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

/**
 * 抽象的命令。发往memcached服务器的命令，都应该继承自这个类
 * 
 * @author laichendong
 * @since 2011-12-6 下午02:15:34
 */
public abstract class Command {
	/** 命令各个组成部分之间的分隔符 */
	public static final String DELIMITER = " ";
	/** 回车 */
	public static final String RETURN = "\r\n";
}
