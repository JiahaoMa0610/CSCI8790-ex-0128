package ex05.javassistloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavassistLoaderExample3 {
   private static final String WORK_DIR = System.getProperty("user.dir");
	/*
	 * private static final String TARGET_POINT = "target.Point"; private static
	 * final String TARGET_RECTANGLE = "target.Rectangle";
	 */
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + File.separator + "output";
   public static String dad;
   public static String[] kid = new String[2];
   private static Scanner scanner = new Scanner(System.in);
   public static String modified = "method";
   public static void main(String[] args) {
	   
      while (true) {
    	 UtilMenu.showMenuOptions();
         int option = UtilMenu.getOption();
         switch (option) {
         case 1:
             System.out.println("Enter the three classes (Use comma as delimeter):");
             String[] classes = getInputs();
             System.out.println(classes[0]);
             try {
    				ClassPool pool = ClassPool.getDefault();
    				insertClassPath(pool);

    				//CtClass cckid1 = pool.makeClass(kid[0]);
    				CtClass cckid1 = pool.get("target." + kid[0]);
    				cckid1.writeFile(outputDir); // debugWriteFile
    				System.out.println("[DBG] write output to: " + outputDir);

    				CtClass cckid2 = pool.get("target." + kid[1]);
    				cckid2.writeFile(outputDir); // debugWriteFile
    				System.out.println("[DBG] write output to: " + outputDir);

    				CtClass ccdad = pool.get("target." + dad);
    				ccdad.writeFile(outputDir);
    				System.out.println("[DBG] write output to: " + outputDir);

    				cckid1.defrost(); // modifications of the class definition will be permitted.
    				cckid1.setSuperclass(ccdad);
    				cckid2.defrost();
    				cckid2.setSuperclass(ccdad);
    				cckid1.writeFile(outputDir);
    				cckid2.writeFile(outputDir);
    				System.out.println("[DBG] write output to: " + outputDir);
    				
    	            System.out.println("[DBG] Enter (1) the method to be modified and\n" //
    	                    + "(2) a method call to be inserted (e.g., add,incX,getX):");
    	              
    	              String[] arguments = UtilMenu.getArguments();
    	              System.out.println(arguments.length);
    	              System.out.println(modified);
    	              if (!modified.equals(arguments[0])) {
    	                  modified = arguments[0];
    	                  analysisProcess(arguments[0], arguments[1], arguments[2]);


    	                  break;
    	              }
    	              else {
    	              	System.out.printf("[WRN] This method " + arguments[0] + " has been modified!!\n");
    	              	break;
    	              }

    			} catch (Exception e) {
    				e.printStackTrace();
    			}

		default:
            break;
         }


      }
   }

   static void analysisProcess(String methodDecl, String methodCall, String methodCall2) {
      try {
         ClassPool cp = ClassPool.getDefault();
         insertClassPath(cp);
         System.out.println(kid[0]);
         CtClass cc1 = cp.get("target." + kid[0]);
         CtClass cc2 = cp.get("target." + kid[1]);

        // cc.setSuperclass(cp.get(TARGET_POINT));
         CtMethod m1 = cc1.getDeclaredMethod(methodDecl);
         String BLK1 = "\n{\n" //
                 + "\t" + methodCall + "();" + "\n" //
                 + "\t" + "System.out.println(\"[TR] getX result : \""+ " + "+ methodCall2 + "()"  +");"+ "\n" + "}";
         System.out.println("[DBG] Block: " + BLK1);
         CtMethod m2 = cc2.getDeclaredMethod(methodDecl);
         String BLK2 = "\n{\n" //
                 + "\t" + methodCall + "();" + "\n" //
                 + "\t" + "System.out.println(\"[TR] getX result : \""+ " + "+ methodCall2 + "()"  +");"+ "\n" + "}";
         System.out.println("[DBG] Block: " + BLK2);
         cc1.defrost();
         cc2.defrost();
         m1.insertBefore(BLK1);
         m2.insertBefore(BLK2);


         Loader cl = new Loader(cp);
         Class<?> c = cl.loadClass("target." +kid[0]);
         Object rect = c.newInstance();
         System.out.println("[DBG] Created a "+ kid[0]+" object.");
         Class<?> c1 = cl.loadClass("target." +kid[1]);
         Object rect1 = c1.newInstance();
         System.out.println("[DBG] Created a "+ kid[1]+" object.");

         Class<?> rectClass = rect.getClass();
         Method m = rectClass.getDeclaredMethod(methodDecl, new Class[] {});
         System.out.println("[DBG] Called getDeclaredMethod.");
         Object invoker = m.invoke(rect, new Object[] {});
         System.out.println("[DBG] "+ methodDecl + " result: " + invoker);
         
         Class<?> rectClass1 = rect1.getClass();
         Method mm = rectClass1.getDeclaredMethod(methodDecl, new Class[] {});
         System.out.println("[DBG] Called getDeclaredMethod.");
         Object invoker1 = mm.invoke(rect1, new Object[] {});
         System.out.println("[DBG] "+ methodDecl + " result: " + invoker1);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = WORK_DIR + File.separator + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }
	public static String[] getInputs() {
		String[] fathers = new String[3];
		String[] child = new String[3];
		int index = 0;
		int kindex = 0;
		String input = scanner.nextLine();
		if (input.trim().equalsIgnoreCase("q")) {
			System.err.println("Terminated.");
			System.exit(0);
		}
		List<String> list = new ArrayList<String>();
		String[] inputArr = input.split(",");
		int temp = 0;
		if (inputArr.length == 3) {
			for (String iElem : inputArr) {
				list.add(iElem.trim());
				if (iElem.startsWith("Common")) {
					fathers[index] = iElem.trim();
					index++;
				} else {
					child[kindex] = iElem.trim();
					kindex++;
				}
			}
			if (index == 1) {
				dad = fathers[0];
				kid[0] = child[0];
				kid[1] = child[1];
			} else if (index > 1) {
				for (String x : fathers) {
					if (x != null && x.length() > temp) {
						temp = x.length();
						dad = x;
					}
				}
				List<String> er = new ArrayList<String>(list);
				er.remove(dad);
				kid = er.toArray(new String[0]);
			} else {
				dad = inputArr[0].trim();
				kid[0] = inputArr[1].trim();
				kid[1] = inputArr[2].trim();
				
			}

		} else {
			System.out.println("[WRN] Invalid Input");
		}
		return list.toArray(new String[0]);
	}
}
