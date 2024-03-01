package javagames.filesandres;
import java.io.*;
public class ClasspathResources {
    public ClasspathResources() {
    }

    public void runTest() {
        System.out.println();
        System.out.println("*******************");
        System.out.println("ClassLoader-Absolute Path");
        System.out.println();
        InputStream in = ClassLoader.getSystemResourceAsStream("javagames/filesandres/Test1.txt");
        printResource(in);

        System.out.println();
        System.out.println("***********************");
        System.out.println("getClass()-Relative path");
        System.out.println();
        in = getClass().getResourceAsStream("Test2.txt");
        printResource(in);

        System.out.println();
        System.out.println("***********************");
        System.out.println("getClass()-Absolute path");
        System.out.println();
        in = getClass().getResourceAsStream("/javagames/filesandres/Test3.txt");
        printResource(in);

        System.out.println();
        System.out.println("***********************");
        System.out.println("getClass()-Absolute path");
        System.out.println();
        in=ClasspathResources.class.getResourceAsStream("Test3.txt");
        printResource(in);

        in = getClass().getResourceAsStream("fat/finget/mistake");
        if (in == null) {
            System.out.println();
            System.out.println("***********************");
            System.out.println("Got a null back");
        }
        in = ClassLoader.getSystemResourceAsStream("fat/finger/mistake");
        if (in == null) System.out.println("Got another null back");
    }
    private void printResource(InputStream in){
        try{
            InputStreamReader reader=new InputStreamReader(in);
            BufferedReader buf=new BufferedReader(reader);
            String line=null;
            while((line=buf.readLine())!=null) System.out.println(line);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                in.close();
            }catch (Exception e){}
        }
    }

    public static void main(String[] args) {
        new ClasspathResources().runTest();
    }
}
