package com.misys.ja;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class LoggerTransformer implements ClassFileTransformer{

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		ClassPool cp = ClassPool.getDefault();
	    cp.importPackage("com.misys.ja");

	    //region filter agent classes
	    // we do not want to profile ourselves
/*	    if (className.startsWith("com/zeroturnaround/callspy")) {
	      return null;
	    }*/
	    //endregion

	    //region filter out non-application classes
	    // Application filter. Can be externalized into a property file.
	    // For instance, profilers use blacklist/whitelist to configure this kind of filters
/*	    if (!className.startsWith("com/zt")) {
	      return classfileBuffer;
	    }*/
	    //endregion

	    try {
	      CtClass ct = cp.makeClass(new ByteArrayInputStream(classfileBuffer));

	      CtMethod[] declaredMethods = ct.getDeclaredMethods();
	      for (CtMethod method : declaredMethods) {
	        //region instrument method
	          method.insertBefore(" { " +
	                  "Stack.push();" +
	                  "Stack.log(\"" + className + "." + method.getName() + "\"); " +
	                  "}");
	          method.insertAfter("{ Stack.pop(); }", true);
	        //endregion
	      }

	      return ct.toBytecode();
	    } catch (Throwable e) {
	      e.printStackTrace();
	    }

	    return classfileBuffer;
	}

}
