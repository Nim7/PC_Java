package pc_4_7_5_1_precedencia_latch;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import pc_0_aux.Auxiliares;

public class ProcedureDiagram {

    static CountDownLatch latchA = new CountDownLatch(1);
    static CountDownLatch latchB = new CountDownLatch(2);
    static CountDownLatch latchC = new CountDownLatch(2);
    static CountDownLatch latchD = new CountDownLatch(3);
    static CountDownLatch latchE = new CountDownLatch(2);
    static CountDownLatch latchF = new CountDownLatch(1);
    static CountDownLatch latchG = new CountDownLatch(2);
    static CountDownLatch latchH = new CountDownLatch(2);
    
    public static void main(String[] args) {
        Thread th1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    prod1();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcedureDiagram.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }  
        ,"thd1");
        Thread th2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    prod2();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcedureDiagram.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }  
        ,"thd2");
        Thread th3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    prod3();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcedureDiagram.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }  
        ,"thd3");
        th1.start();
        th2.start();
        th3.start();
    }
    public static void prod1() throws InterruptedException{
    	latchA.countDown();
    	latchA.await();
        println("A"); //sleep random e imprime 
        latchB.countDown(); 
        latchD.countDown(); 
        latchB.await();
        println("B");
        latchC.countDown(); 
        latchE.countDown(); 
        latchH.countDown(); 
        latchC.await();
        println("C");
    }
    public static void prod2() throws InterruptedException{
        latchD.countDown();
    	latchD.await();
        println("D");
        latchB.countDown();
        latchG.countDown();
        latchE.countDown();
        latchE.await();
        println("E");
        latchC.countDown();
    }
    public static void prod3() throws InterruptedException{
        latchF.countDown();
        latchF.await();
    	println("F");
    	latchD.countDown(); 
        latchG.countDown(); 
        latchG.await();
        println("G");
        latchH.countDown(); 
        latchH.await();
        println("H");
    }
    public static void println(String text){
        Auxiliares.sleepRandom(1000);
        System.out.println(text);
    }
}