package org.ardverk.daap.chunks.impl.unknown;

import org.ardverk.daap.chunks.UIntChunk;

public class UnknownAR extends UIntChunk
{
	public UnknownAR()
	{
		this(0);
	}

	public UnknownAR(int value)
	{
		super("caar", "com.apple.itunes.unknown-ar", value);
	}

}