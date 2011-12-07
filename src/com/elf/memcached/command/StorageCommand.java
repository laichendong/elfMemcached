/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

/**
 * 存储命令
 * 
 * @author laichendong
 * @since 2011-12-6 下午05:58:28
 * @TODO cas
 */
public class StorageCommand extends Command {
	/** 命令名称 */
	private CommandNames commandName;
	/** client用于存储data的键 */
	private String key;
	/**
	 * 是一个16位的无符号int，server会存储并在以后取数据时返回给client。该值用于保存data存储相关信息，对server是透明的。注意从memcached
	 * 1.2.1之后，改用32位int存储，不过你可以强制使用16位，以兼容之前版本。
	 */
	private short flags;
	/** 过期时间，如果设置为0则表示永不过期（但是仍然可能被删除以为新元素腾出空间）。它是一个非负数，保证在过期后，client不再会取到该值。 */
	private long exptime;
	/** 数据块的字节数，不包括\r\n。可以为0，表示value为空。 */
	private int bytes;
	/** 一个通知server不用返回响应结果的可选参数。如果请求命令错误，server不能正确识别noreply，仍然会返回error。 */
	private boolean noreply;
	/** 数据 */
	private Object data;
	
	public StorageCommand(CommandNames commandName, String key, Object value) {
		this.commandName = commandName;
		this.key = key;
		this.flags = 0;
		this.exptime = 0;
	}
	
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
