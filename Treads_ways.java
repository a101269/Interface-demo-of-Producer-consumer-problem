/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;
import java.awt.*;
import java.awt.event.*;
import static javaapplication7.Result_win.resultarea;
import javax.swing.*;
import java.io.*;
/**
 *
 * @autho
 * r a101269
 */
class BUFFER{
    private final char []buffer1 = new char[Main_win.r1];//栈的空间由设定的参数决定
    private final char []buffer2 = new char[Main_win.r2];//栈的空间由设定的参数决定
    private final char []buffer3 = new char[Main_win.r3];//栈的空间由设定的参数决定
    static String[] x=new String[Main_win.r1];
    static String[] y=new String[Main_win.r2];
    static String[] z=new String[Main_win.r3];   
    public int index1 = 0,index2=0,index3=0;
    private int EmptyFirst,EmptySecond,EmptyThird;//剩余空间
    private static int fullFirst,fullSecond,fullThird;//当前字符数
    private static int putnum1=0,putnum2=0,putnum3=0; // 共放入数量
    private static int getnum1=0,getnum2=0,getnum3=0; // 共取出数量
    static FileWriter data;//数据保存
    static BufferedWriter datawriter;



    
    public synchronized void put(){       
        char c='a';//对c初始化
        boolean suspended=Main_win.s;//suspended为TRUE则等待，为FALSE执行
        if (suspended == true){
	    try {this.wait();}
            catch (InterruptedException e) {}
	}                   
	if(index1 >= buffer1.length)//堆栈已满，不能压栈
	{
            try{this.wait();}//等待，直到有数据出栈
	    catch(InterruptedException e){}
	}
        if(index1>=0&&index1<buffer1.length-1){            
	    this.notify();//通知其他线程把数据出栈，唤醒一个wait的线程 	
            c = (char)(Math.random()*26 + 'A');     //随机产生26个字母
            buffer1[index1] = c;//数据入栈
            x[index1]=String.valueOf(c);
            fullFirst=index1+1;
            EmptyFirst=buffer1.length-fullFirst;
            showPush1(c,index1);//调用界面显示入栈程序
            Main_win.full1.setText(String.valueOf(fullFirst));//int 转String
            Main_win.empty1.setText(String.valueOf(EmptyFirst));
            history.his.append("buffer1 put进字符:"+x[index1]+"\n");
            index1++;//指针向上移动
            putnum1++; 
            
        }
    }	

    public synchronized void move2()
    {
        boolean suspended=Main_win.s;
        if (suspended == true){
	    try {this.wait();}
            catch (InterruptedException e) {}
	}               
	if(index1<=0||index2>=buffer2.length-1)//堆栈无数据或满，不能出栈
	{
	    try	{this.wait();}//等待其他线程把数据入栈
	    catch(InterruptedException e){}
	}
	
        if(index1>0&&index1<=buffer1.length&&index2>=0&&index2<buffer2.length-1)
        { 
            this.notify();//通知其他线程入栈 
	    index1--;
            char c = buffer1[index1];
            buffer2[index2] = c;//数据入栈2
            y[index2]=String.valueOf(c);           
            fullSecond=index2+1;
            EmptySecond=buffer2.length-fullSecond;
            fullFirst-=1;
            EmptyFirst=buffer1.length-fullFirst;
            showPush2(c,index2);
	    showPop1(c,index1);//调用界面显示出栈程序
            Main_win.full1.setText(String.valueOf(fullFirst));//int 转String
            Main_win.empty1.setText(String.valueOf(EmptyFirst)); 
            Main_win.full2.setText(String.valueOf(fullSecond));//int 转String
            Main_win.empty2.setText(String.valueOf(EmptySecond)); 
            history.his.append("buffer2 move字符:"+y[index2]+"\n");            
            index2++;
            getnum1++;
            putnum2++;
        }   
    }
    
    public synchronized void move3()
    {
        boolean suspended=Main_win.s;
        if (suspended == true){
	    try {this.wait();}
            catch (InterruptedException e) {}
	}               
	if(index1<=0||index3>=buffer3.length-1)//堆栈无数据或满，不能出栈
	{
	    try	{this.wait();}//等待其他线程把数据入栈
	    catch(InterruptedException e){}
	}
	 
        if(index1>0&&index1<=buffer1.length&&index3>=0&&index3<buffer3.length-1)
        { 
            this.notify();//通知其他线程入栈
	    char c = buffer1[index1-1];
            buffer3[index3] = c;//数据入栈2
            z[index3]=String.valueOf(c);
            fullThird=index3+1;
            EmptyThird=buffer3.length-fullThird;
            fullFirst-=1;
            EmptyFirst=buffer1.length-fullFirst;
            index1--;
            showPush3(c,index3);
	    showPop1(c,index1);//调用界面显示出栈程序
            Main_win.full1.setText(String.valueOf(fullFirst));//int 转String
            Main_win.empty1.setText(String.valueOf(EmptyFirst)); 
            Main_win.full3.setText(String.valueOf(fullThird));//int 转String
            Main_win.empty3.setText(String.valueOf(EmptyThird)); 
            history.his.append("buffer3 move字符:"+z[index3]+"\n");      
            index3++;
            getnum1++;
            putnum3++;
        }   
    } 
    
    public synchronized void get2()
    {
        boolean suspended=Main_win.s;
        if (suspended == true){
	    try {this.wait();}
            catch (InterruptedException e) {}
	}               
        if(index2 == 0)//堆栈无数据，不能出栈
	{
            try{this.wait();}//等待其他线程把数据入栈
	    catch(InterruptedException e){}
	}	
        if(index2>0&&index2<=buffer2.length){  
            this.notify();//通知其他线程入栈   
            index2--;
            char c = buffer2[index2];
            y[index2]=String.valueOf(c);
            fullSecond=index2;
            EmptySecond=buffer2.length-fullSecond;
	    showPop2(c,index2);//调用界面显示出栈程序
            Main_win.full2.setText(String.valueOf(fullSecond));//int 转String
            Main_win.empty2.setText(String.valueOf(EmptySecond)); 
            history.his.append("buffer2中字符"+y[index2]+"被get\n");
            getnum2++;
        }   
    }
    
    public synchronized void get3()
    {
        boolean suspended=Main_win.s;
        if (suspended == true){
	    try {this.wait();}
            catch (InterruptedException e) {}
	}               
        if(index3 == 0)//堆栈无数据，不能出栈
	{
            try{this.wait();}//等待其他线程把数据入栈
	    catch(InterruptedException e){}
	}   
        if(index3>0&&index3<=buffer3.length){
            this.notify();//通知其他线程入栈
            index3--;           
            char c = buffer3[index3];
            z[index3]=String.valueOf(c);
            fullThird=index3;
            EmptyThird=buffer3.length-fullThird;
	    showPop3(c,index3);//调用界面显示出栈程序
            Main_win.full3.setText(String.valueOf(fullThird));//int 转String
            Main_win.empty3.setText(String.valueOf(EmptyThird));  
            history.his.append("buffer3中字符"+z[index3]+"被get\n");
            getnum3++;
        }   
    }
        
    public static void showPush1(char c,int index){    //字符显示在主界面                   
        String s=String.valueOf(c);                 
        Main_win.see1.append(s+"\n");     
    }
                
    public static void showPop1(char c,int index){
        int i; 
        x[index]="";
        Main_win.see1.setText("");     
        for(i=0;i<=index;i++){    
        if(x[i]!="")
            Main_win.see1.append(x[i]+"\n");       //see1置空后重新添加原来除move掉的字符     
        }     
    }
    public static void showPush2(char c,int index){
        String s=String.valueOf(c);                 
        Main_win.see2.append(s+"\n");  
           
    }
          
    public static void showPop2(char c,int index){
        int i;
        y[index]="";
        Main_win.see2.setText("");
        for(i=0;i<=index;i++){
        if(y[i]!="")
            Main_win.see2.append(y[i]+"\n"); 
               
        }
    }
    public static void showPush3(char c,int index){                         
        String s=String.valueOf(c);           
        Main_win.see3.append(s+"\n");  
        
    }
          
    public static void showPop3(char c,int index){
        int i;
        z[index]="";
        Main_win.see3.setText("");
        for(i=0;i<=index;i++){
            if(z[i]!="")
                Main_win.see3.append(z[i]+"\n"); 
        }
    }
    

    public static void resulewin(){     //最后运行时间，放入取出数目等显示在Result_win
        Result_win.resultarea.setText("");
        Result_win.resultarea.append("运行总时间:"+ Main_win.gotime+"ms"+"\n");
        Result_win.resultarea.append("buffer1放入总数:"+ String.valueOf(putnum1)+"\n");
        Result_win.resultarea.append("buffer1取出总数:"+ String.valueOf(getnum1)+"\n");
        Result_win.resultarea.append("buffer1目前数目:"+ String.valueOf(fullFirst)+"\n");
        Result_win.resultarea.append("buffer2放入总数:"+ String.valueOf(putnum2)+"\n");
        Result_win.resultarea.append("buffer2取出总数:"+ String.valueOf(putnum2)+"\n");
        Result_win.resultarea.append("buffer2目前数目:"+ String.valueOf(fullSecond)+"\n");
        Result_win.resultarea.append("buffer3放入总数:"+ String.valueOf(putnum3)+"\n");
        Result_win.resultarea.append("buffer3取出总数:"+ String.valueOf(putnum3)+"\n");
        Result_win.resultarea.append("buffer3目前数目:"+ String.valueOf(fullThird)+"\n");
    } 
    
    
    public static void datasave(){//保存数据            
        try{ 
            File f=new File("C:\\Users\\a101269\\Desktop","datasave.txt");
            data=new FileWriter(f);
            datawriter = new BufferedWriter(data); 
            String str=history.his.getText();           
            datawriter.append(str);
            datawriter.flush();
            datawriter.close();
            data.close();
        }
        catch (IOException e) {}
    }  
}




class BUFFER_PUT implements Runnable//    BUFFER线程
{
    private BUFFER buff;//生成的字母都保存到同步堆栈中
    int i;
    public BUFFER_PUT(BUFFER a){
        buff = a;
    }
    public void run()
    {   
        for(i=0;i<200;i++){
	buff.put();//把字母入栈
	try{
            Thread.sleep((int)(Main_win.p1));
        }//每产生一个字母后线程就随即睡眠一段时间
	catch(InterruptedException e){}	//suspended为TRUE等待，即暂停
        }
    }
}

class BUFFERT_MOVE implements Runnable//BUFFER2
{ 
    private BUFFER buff;//生成的字母都保存到同步堆栈中
    int i;
    public BUFFERT_MOVE(BUFFER a){
        buff = a;
    }
    public void run()
    {
        for(i=0;i<100;i++){
        buff.move2();//从堆栈中读取字母
	try{
            Thread.sleep((int)(Main_win.m2));
        }//每产生一个字母后线程就随即睡眠一段时间
	catch(InterruptedException e){}	//suspended为TRUE等待，即暂停
        }
    }
}

class BUFFERT_GET implements Runnable//BUFFER2
{
    private BUFFER buff;//生成的字母都保存到同步堆栈中
    int i;
    public BUFFERT_GET(BUFFER a){
        buff = a;
    }
    public void run()
    {
	for(i=0;i<100;i++){
        buff.get2();//从堆栈中读取字母
	try{
            Thread.sleep((int)(Main_win.g2));
        }//每产生一个字母后线程就随即睡眠一段时间
	catch(InterruptedException e){}	//suspended为TRUE等待，即暂停
        }
    }
}

class BUFFERTH_MOVE implements Runnable//BUFFER3
{
    private BUFFER buff;//生成的字母都保存到同步堆栈中
    int i;
    public BUFFERTH_MOVE(BUFFER a){
        buff = a;
    }
    public void run()
    {
	for(i=0;i<100;i++){
        buff.move3();//从堆栈中读取字母
	try{
            Thread.sleep((int)(Main_win.m3));
        }//每产生一个字母后线程就随即睡眠一段时间
	catch(InterruptedException e){}	//suspended为TRUE等待，即暂停
        }
    }
}


class BUFFERTH_GET implements Runnable//BUFFER3
{
    private BUFFER buff;//生成的字母都保存到同步堆栈中
    int i;
    public BUFFERTH_GET(BUFFER a){
        buff = a;
    }
    public void run()
    {
	for(i=0;i<100;i++){
        buff.get3();//从堆栈中读取字母
	try{
            Thread.sleep((int)(Main_win.g3));
        }//每产生一个字母后线程就随即睡眠一段时间
	catch(InterruptedException e){}	//suspended为TRUE等待，即暂停
        }
    }
}