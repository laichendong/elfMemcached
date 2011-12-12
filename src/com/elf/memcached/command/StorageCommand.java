/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

/**
 * 存储类命令
 * 
 * @author laichendong
 * @since 2011-12-6 下午05:58:28
 * @TODO cas
 */
public final class StorageCommand extends Command {
	/** 待存储的key */
	private String key;
	/** 标志位 1.2.1后改用32位 为了兼容老版本，这里依然使用16位 */
	private short flags;
	/** 过期时间,exptime >= 0 ; 0表示不过期 */
	private long exptime;
	/** 数据块的字节数，不包括\r\n。可以为0，表示value为空。 */
	private int bytes;
	
	/** 待存储的数据 */
	private byte[] data;
	
	/**
	 * 构造方法
	 * 
	 * @param commandName
	 *            命令名称
	 * @param key
	 *            待存储的key
	 * @param value
	 *            待存储的值
	 */
	public StorageCommand(CommandNames commandName, String key, Object value) {
		this(commandName, key, value, 0, false);
	}
	
	/**
	 * 构造方法
	 * 
	 * @param commandName
	 *            命令名称
	 * @param key
	 *            待存储的key
	 * @param value
	 *            待存储的value
	 * @param exptime
	 *            过期时间
	 */
	public StorageCommand(CommandNames commandName, String key, Object value, long exptime) {
		this(commandName, key, value, exptime, false);
	}
	
	/**
	 * 构造方法
	 * 
	 * @param commandName
	 *            命令名称
	 * @param key
	 *            待存储的key
	 * @param value
	 *            待存储的value
	 * @param exptime
	 *            过期时间
	 * @param noreply
	 *            是否不需要答复
	 */
	public StorageCommand(CommandNames commandName, String key, Object value, long exptime, boolean noreply) {
		// TODO 参数合法性检查
		// TODO 序列化方式的选择和优化，java原生/json。 可用flags字段做标记
		this.commandName = commandName;
		this.key = key; // TODO 使key“无害”
		this.flags = 0;
		this.exptime = exptime;
		this.noreply = noreply;
		this.data = super.serialize(value);
		this.bytes = this.data.length;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elf.memcached.command.Command#commandString()
	 */
	@Override
	public String commandString() {
		StringBuffer cmd = new StringBuffer();
		cmd.append(this.commandName.name);
		cmd.append(DELIMITER);
		cmd.append(this.key);
		cmd.append(DELIMITER);
		cmd.append(this.flags);
		cmd.append(DELIMITER);
		cmd.append(this.exptime);
		cmd.append(DELIMITER);
		cmd.append(this.bytes);
		cmd.append(DELIMITER);
		if (this.noreply) {
			cmd.append(NOREPLY);
			cmd.append(DELIMITER);
		}
		return cmd.toString();
	}
	
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public short getFlags() {
		return flags;
	}
	
	public void setFlags(short flags) {
		this.flags = flags;
	}
	
	public long getExptime() {
		return exptime;
	}
	
	public void setExptime(long exptime) {
		this.exptime = exptime;
	}
	
	public int getBytes() {
		return bytes;
	}
	
	public void setBytes(int bytes) {
		this.bytes = bytes;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
}
