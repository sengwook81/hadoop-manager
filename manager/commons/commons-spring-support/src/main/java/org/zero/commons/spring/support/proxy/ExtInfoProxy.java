package org.zero.commons.spring.support.proxy;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class ExtInfoProxy {

	private class ExtInfoMethodInterceptor implements MethodInterceptor
	{

		private Object extInfo;
		private Object orgObj;

		private Class<?> extClass;
		public ExtInfoMethodInterceptor(Object orgObj , Object extInfo) {
			this.orgObj = orgObj;
			this.extInfo = extInfo;
			this.extClass = extInfo.getClass(); 
		}

		@Override
		public Object intercept(Object originalObj, Method method, Object[] args,
				MethodProxy proxy) throws Throwable {
			
			System.out.println(method.getDeclaringClass());
			Object invokeSuper = null;
			
			if(method.getName().equals("toString") && args.length == 0) {
				return "Proxy Class : [" + orgObj + "\n, " + extInfo; 
			}
			if(method.getDeclaringClass().isAssignableFrom(extClass))
			{
				invokeSuper = method.invoke(extInfo, args);
			}
			else 
			{
				 invokeSuper = method.invoke(orgObj, args);
			}
			return invokeSuper;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T createProxy(T src, Object extInfo) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(src.getClass());
		if(extInfo.getClass().getInterfaces().length == 0)
		{
			throw new RuntimeException("Add Extend Info Proxy's ExtVo Needs Interface");
		}
		enhancer.setInterfaces(extInfo.getClass().getInterfaces());
		enhancer.setCallback(new ExtInfoMethodInterceptor(src,extInfo));

		return (T) enhancer.create();
	}
}
