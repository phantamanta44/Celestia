package io.github.phantamanta44.celestia.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ICTListener {
	
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ListenTo { }
	
}
