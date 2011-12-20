/**
 * copyright © sf-express Inc
 *
 */
package com.elf.memcached.command;

/**
 * 取回类命令
 * 
 * @author laichendong
 * @since 2011-12-8 下午03:44:52
 */
public final class RetrievalCommand extends Command {

	/** 待获取数据的keys */
	private String[] keys;

	public RetrievalCommand(CommandNames commandName, String[] keys) {
		this.commandName = commandName;
		this.keys = keys;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.elf.memcached.command.Command#commandString()
	 */
	@Override
	public String commandString() {
		StringBuilder cmd = new StringBuilder();
		cmd.append(this.commandName.name);
		cmd.append(DELIMITER);
		for (String key : keys) {
			cmd.append(key);
			cmd.append(DELIMITER);
		}
		if (this.noreply) {
			cmd.append(NOREPLY);
			cmd.append(DELIMITER);
		}
		return cmd.toString();
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

}
