package ex05.javassistloader;

import java.io.File;
import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;

public class JavassistLoaderExample {
   private static final String WORK_DIR = System.getProperty("user.dir");
   private static final String TARGET_POINT = "target.Point";
   private static final String TARGET_RECTANGLE = "target.Rectangle";

   public static void main(String[] args) throws Exception {
      ClassPool cp = ClassPool.getDefault();
      insertClassPath(cp);
      CtClass cc = setSuperclass(cp);

      addMethodCall(cp, cc);
      executeMethodCall(cp);
   }

   static CtClass setSuperclass(ClassPool cp) throws Exception {
      CtClass cc = cp.get(TARGET_RECTANGLE);
      cc.setSuperclass(cp.get(TARGET_POINT));
      return cc;
   }

   static void addMethodCall(ClassPool cp, CtClass cc) throws Exception {
      CtMethod m1 = cc.getDeclaredMethod("getVal");
      String BLK1 = "\n{\n" //
            + "\t" + "move(10, 20);" + "\n" //
            + "\t" + "System.out.println(\"[TR] getX result : \" + getX()); " + "\n" + "}";
      System.out.println("[DBG] Block: " + BLK1);
      m1.insertBefore(BLK1);
   }

   static void executeMethodCall(ClassPool cp) throws Exception {
      Loader cl = new Loader(cp);
      Class<?> c = cl.loadClass(TARGET_RECTANGLE);
      Object rect = c.newInstance();
      System.out.println("[DBG] Created a Rectangle object.");

      Class<?> rectClass = rect.getClass();
      Method m = rectClass.getDeclaredMethod("getVal", new Class[] {});
      System.out.println("[DBG] Called getDeclaredMethod.");
      Object retVal = m.invoke(c.newInstance(), new Object[] {});
      System.out.println("[DBG] getVal result: " + retVal);
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = WORK_DIR + File.separator + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }
}
