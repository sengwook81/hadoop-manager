package org.zero.commons.core.support;


/**
 * @author SengWook 
 *
 * @param <T> In Out Generic Class
 */
public interface DoWith<T> {
	
	
	/**
	 * 특별한 작업을 하지 않는 CallBack Class
	 * @author Administrator
	 *
	 * @param <T>  In Out Generic Class
	 */
	public static class DO_NOTHING<T> implements DoWith<T> {
		public T doWith(T src) {
			return src;
		}
	}
	
	/**
	 * 입력값을 Type을 가지고 처리하는 Method 
	 * 해당 입력 값을 그대로 반환.
	 * @param User Data
	 * @return User Data
	 */
	public T doWith(final T src);
}
