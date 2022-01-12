package com.urzaizcoding.ulangerproxy.exceptions;

import java.util.function.Consumer;
import java.util.function.Function;

import com.urzaizcoding.ulangerproxy.log.Logger;

public class ExceptionWrappers {
	
	@FunctionalInterface
	public interface ThrowingFunction<T,R>{
		R apply(T t) throws Exception;
	}
	
	@FunctionalInterface
	public interface ThrowingConsumer<T> {
		void accept(T t) throws Exception;
	}
	
	public static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> toWrap,Logger l, Class<E> exClass ){
		
		return new Consumer<T>() {

			@Override
			public void accept(T t) {
				try {
					toWrap.accept(t);
				}catch (Exception e) {
					try {
						E ecasted = exClass.cast(e);
						if (l != null) {
							l.writeError(ecasted.getClass().getName(), Logger.getExceptionMessage(ecasted));
						}else {
							System.err.println(ecasted.getMessage());
						}
					} catch (ClassCastException e1) {
						throw new RuntimeException(e);
					}
				}
			}
			
		};
	}
	
	public static <T, E extends Exception> Consumer<T> consumerWrapper(ThrowingConsumer<T> toWrap, Logger l, Class<E> exClass){
		return t -> {
			try {
				toWrap.accept(t);
			} catch (Exception e) {
				try {
					E exCasted = exClass.cast(e);
					if(l != null) {
						l.writeError(exCasted.getClass().getName(), Logger.getExceptionMessage(exCasted));
					}else {
						System.err.println(exCasted.getMessage());
					}
				}catch (ClassCastException e1) {
					throw new RuntimeException(e);
				}
			}
		};
	}
	
	
	public static <T,R,E extends Exception> Function<T , R> mapperWrapper(ThrowingFunction<T , R> toWrap,Logger l, Class<E> exClass){
		return new Function<T, R>() {

			@Override
			public R apply(T t) {
				try {
					return toWrap.apply(t);
				}catch (Exception e) {
					try {
						E ecasted = exClass.cast(e);
						if (l != null) {
							l.writeError(ecasted.getClass().getName(), Logger.getExceptionMessage(ecasted));
						}else {
							System.err.println(ecasted.getMessage());
						}
					} catch (ClassCastException e1) {
						throw new RuntimeException(e);
					}
				}
				
				return null;
			}
			
		};
	}
}
