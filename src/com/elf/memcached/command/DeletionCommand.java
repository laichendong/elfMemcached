/**
 * 
 */
package com.elf.memcached.command;

/**
 * 删除类命令
 * 
 * @author laichendong
 * @since 2011-12-10
 */
public final class DeletionCommand extends Command {

	/** 待删除的key */
	private String key;

	/**
	 * 构造方法
	 * 
	 * @param key
	 *            待删除的key
	 */
	public DeletionCommand(String key) {
		this(key, false);
	}

	/**
	 * 构造方法
	 * 
	 * @param key
	 *            待删除的key
	 * @param noreply
	 *            指定是否需要服务端应答
	 */
	public DeletionCommand(String key, boolean noreply) {
		this.commandName = Command.CommandNames.DELETE;
		this.key = key;
		this.noreply = noreply;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.elf.memcached.command.Command#commandString()
	 */
	@Override
	public String commandString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.commandName.name);
		sb.append(Command.DELIMITER);
		sb.append(this.key);
		sb.append(Command.DELIMITER);
		if (this.noreply) {
			sb.append(Command.NOREPLY);
			sb.append(Command.DELIMITER);
		}
		return sb.toString();
	}

}
