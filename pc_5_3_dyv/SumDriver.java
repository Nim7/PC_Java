/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_5_3_dyv;

import java.util.Random;

public class SumDriver {
    final static int NUM_ELEM = 40000000;
    final static int MAX_RND = 99;
  
    public static void main(String[] args) {
        int[] elts = new int [NUM_ELEM];
        
        Random r = new Random();
        for (int i = 0; i < NUM_ELEM; i++) {
            elts[i]=r.nextInt(MAX_RND);
        }
   
        //Versión con Fork-Join
        SumArray forkJoinSum = new ForkJoinSum();  
        double startTimeConcurrent = System.currentTimeMillis();
        	double sum = forkJoinSum.Sum(elts);
        double endTimeConcurrent = System.currentTimeMillis();
        System.out.println("Fork-Join: " + sum + ", " + (endTimeConcurrent - startTimeConcurrent) + " ms.");
        
        //Versión secuencial
        SumArray secuential = new SequentialSum();
        double startTimeSeq = System.currentTimeMillis();
    		double sum2 = secuential.Sum(elts);
    	double endTimeSeq = System.currentTimeMillis();
        System.out.println("Secuencial: " + sum2 + ", " + (endTimeSeq - startTimeSeq) + " ms.");
        
    
    
    
    }
    
    
    
    
    
    
    
    //System.out.println("Suma iterativa: " + iterativeSum.sum(elts));
    
}
