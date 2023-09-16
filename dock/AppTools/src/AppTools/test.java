package AppTools;

public class test {
    public static void main(String[] args){
        System.out.println(System.getProperty("java.library.path"));
//      System.loadLibrary("NativeAdd");这个方法找不到NativeAdd.so
        System.load("E:\\java\\code\\JAVACourseCompletionDesign\\App\\src\\App\\hello.dll");
        GetRealUrl2 getRealUrl2 =new GetRealUrl2();
        System.out.println(getRealUrl2.getUrl("hhhh"));
    }
}
