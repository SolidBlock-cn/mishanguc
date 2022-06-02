import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class Main {
  private static int t() {
    System.out.println("t called!");
    return 32;
  }

  private static int getT() {
    return Suppliers.memoize(new Supplier<Integer>() {
      @Override
      public Integer get() {
        return t();
      }
    }).get();
  }

  public static void main(String[] args) {
    System.out.println(getT());
    System.out.println(getT());
    System.out.println(getT());
    System.out.println(getT());
  }
}
