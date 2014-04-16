package org.zero.command.runner;

import java.util.Arrays;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zero.command.runner.cmd.CommandRunner;

public class ZeroRunner {

	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		if(args.length < 1) {
			System.out.println("Usage Runner [beanName] args...");
			System.exit(1);
		}
		String beanName = args[0];
		
		String[] paramArgs = Arrays.copyOfRange(args, 1, args.length);
		CommandRunner bean = context.getBean(beanName,CommandRunner.class);
		
		try {
			bean.RunCommand(paramArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
