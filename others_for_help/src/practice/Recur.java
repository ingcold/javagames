package practice;
public class Recur {
    int i=1;
    int j=1;
    public void in_recur(){

    }
    public void out_recur(){

    }
    public void re(int upbound){
        System.out.println("第"+i+++"次进栈");
        {

        }

        if(i==upbound)return;
        re(upbound);


        {

        }
        System.out.println("第"+j+++"次出栈");
    }

    public static void main(String[] args) {
        Recur recur=new Recur();
        recur.re(6);
    }
}
//-2024.2.7