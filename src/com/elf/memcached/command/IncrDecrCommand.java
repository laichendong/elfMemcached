/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

/**
 * increment/decrement 命令
 * 
 * @author laichendong
 * @since 2011-12-13 上午10:42:47
 */
public final class IncrDecrCommand extends Command {
	
	/** key */
	private String key;
	/** increment 或 decrement 的量 */
	private long value;
	
	public IncrDecrCommand(Command.CommandNames commandName, String key) {
		this(commandName, key, 1L, false);
	}
	
	public IncrDecrCommand(Command.CommandNames commandName, String key, long value) {
		this(commandName, key, value, false);
	}
	
	public IncrDecrCommand(Command.CommandNames commandName, String key, boolean noreply) {
		this(commandName, key, 1L, noreply);
	}
	
	public IncrDecrCommand(Command.CommandNames commandName, String key, long value, boolean noreply) {
		this.commandName = commandName;
		this.key = key;
		this.value = value;
		this.noreply = noreply;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elf.memcached.command.Command#commandString()
	 */
	@Override
	public String commandString() {
		StringBuilder cmd = new StringBuilder();
		cmd.append(this.commandName.name);
		cmd.append(Command.DELIMITER);
		cmd.append(this.key);
		cmd.append(Command.DELIMITER);
		cmd.append(this.value);
		cmd.append(Command.DELIMITER);
		if(this.noreply){
			cmd.append(Command.NOREPLY);
			cmd.append(Command.DELIMITER);
		}
		return cmd.toString();
	}
	
}
