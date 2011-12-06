/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

/**
 * 存储命令
 * 
 * @author laichendong
 * @since 2011-12-6 下午05:58:28
 */
public class StorageCommand extends Command {
	
	/**
	 * 存储命令包含的命令名称枚举
	 * 
	 * @author laichendong
	 * @since 2011-12-6 下午06:01:13
	 */
	public enum CommandNames {
		SET("set"), ADD("add"), REPLACE("replace"), APPEND("append"), PREPEND("prepend");
		/** 命令名称 */
		String name;
		
		CommandNames(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	}
	
}
