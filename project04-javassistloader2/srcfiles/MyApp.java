import java.lang.reflect.Field;

public class MyApp {
   
   public static void main(String[] paramArrayOfString) throws Exception {
      
      System.out.println("Run...");
      MyApp localMyApp = new MyApp();
      localMyApp.foo();
      System.out.println("1) getClass() is used to show the name: " + localMyApp.getClass().getField("hiddenValue").getName());
      System.out.println("2) .class is used to show the name: " + MyApp.class.getField("hiddenValue").getName());
      System.out.println("3) Class.forName() is used to show the name: " + Class.forName("MyApp").getField("hiddenValue").getName());
      System.out.println("Done.");
   }

   public void foo() {
      System.out.println("Called foo.");
   }
}
