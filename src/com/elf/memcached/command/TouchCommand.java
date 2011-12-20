/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

/**
 * @author laichendong
 * @since 2011-12-16 下午04:33:26
 */
public final class TouchCommand extends Command {
	
	private String key;
	private long exptime;
	
	public TouchCommand(String key, long exptime) {
		this.commandName = Command.CommandNames.TOUCH;
		this.key = key;
		this.exptime = exptime;
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
		cmd.append(key);
		cmd.append(Command.DELIMITER);
		cmd.append(exptime);
		cmd.append(Command.DELIMITER);
		if (noreply) {
			cmd.append(Command.NOREPLY);
			cmd.append(Command.DELIMITER);
		}
		return cmd.toString();
	}
	
}
