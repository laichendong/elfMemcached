/**
 * copyright © sf-express Inc
 */
package com.elf.memcached.command;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author laichendong
 * @since 2012-2-1 下午05:04:43
 */
public class EncoderDecoderTest {
	
	@Test
	public void test_encode_null() {
		Flag flag = EncoderDecoder.mark(null);
		
		System.out.println(Arrays.toString(EncoderDecoder.encode(null, flag)));
	}
	
	@Test
	public void test_ecode_number() {
		Flag flag = EncoderDecoder.mark(1);
		System.out.println(flag);
		System.out.println(EncoderDecoder.decode(EncoderDecoder.encode(1, flag), flag));
	}
}
