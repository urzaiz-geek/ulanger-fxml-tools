package com.urzaizcoding.ulangerproxy.exceptions;

import java.util.function.Consumer;
import java.util.function.Function;

import com.urzaizcoding.ulangerproxy.log.Logger;

public class ExceptionWrappers {
	
	@FunctionalInterface
	public static interface ThrowingFunction<T,R>{
		R apply(T t) throws Exception;
	}
	@SuppressWarnings("unchecked")
	public static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> toWrap,Logger l, Class<E>... exClass ){
		
		return new Consumer<T>() {

			@Override
			public void accept(T t) {
				try {
					toWrap.accept(t);
				}catch (Exception e) {
					try {
						for (Class<E> ex : exClass) {
							E ecasted = ex.cast(e);
							if (l != null) {
								l.writeError(ecasted.getClass().getName(), Logger.getExceptionMessage(ecasted));
							}else {
								System.err.println(ecasted.getMessage());
							}
						}
					} catch (ClassCastException e1) {
						throw new RuntimeException(e);
					}
				}
			}
			
		};
	}
	
	@SafeVarargs
	public static <T,R,E extends Exception> Function<T , R> mapperWrapper(ThrowingFunction<T , R> toWrap,Logger l, Class<E>... exClass){
		return new Function<T, R>() {

			@Override
			public R apply(T t) {
				try {
					return toWrap.apply(t);
				}catch (Exception e) {
					try {
						for (Class<E> ex : exClass) {
							E ecasted = ex.cast(e);
							if (l != null) {
								l.writeError(ecasted.getClass().getName(), Logger.getExceptionMessage(ecasted));
							}else {
								System.err.println(ecasted.getMessage());
							}
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
