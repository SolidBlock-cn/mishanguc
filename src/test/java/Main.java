public class Main {
  public static void main(String[] args) {
    Object s = "123";
    int size = s instanceof String string ? string.length() : 0;
    System.out.println(size);
  }
}
