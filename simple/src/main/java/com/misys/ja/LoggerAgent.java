/**
 * 
 */
package com.misys.ja;

import java.lang.instrument.Instrumentation;

/**
 * @author amakumar
 *
 */
public class LoggerAgent { 
	
	  public static void premain(String args, Instrumentation instrumentation){
		  LoggerTransformer transformer = new LoggerTransformer();
		    instrumentation.addTransformer(transformer);
		  }
	  
	  public static void agentmain(final String agentArgs,
              final Instrumentation inst) {
		  System.out.println("Hey, look: I'm instrumenting a running JVM!");
		}

}
