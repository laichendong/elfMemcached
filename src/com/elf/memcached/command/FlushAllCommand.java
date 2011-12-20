/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

/**
 * @author laichendong
 * @since 2011-12-16 下午05:18:19
 */
public final class FlushAllCommand extends Command {
	
	public FlushAllCommand() {
		this.commandName = Command.CommandNames.FLUSH_ALL;
		this.noreply = false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elf.memcached.command.Command#commandString()
	 */
	@Override
	public String commandString() {
		StringBuilder cmd = new StringBuilder();
		cmd.append(commandName.name);
		cmd.append(Command.DELIMITER);
		if (noreply) {
			cmd.append(Command.NOREPLY);
			cmd.append(Command.DELIMITER);
		}
		return cmd.toString();
	}
	
}
