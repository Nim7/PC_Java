/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_5_3_dyv;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author Fermín
 */
public class ForkJoinSum implements SumArray {
    private static class SumTask extends RecursiveTask<Double>{
		private static final long serialVersionUID = 1L;
		private final int[] elts;
        private final int first;
        private final int size;
        
        SumTask(int[] elts, int first, int size) {
            this.elts = elts;
            this.first = first;
            this.size = size;
        }

        @Override
        protected Double compute() {
            if (size==0) {
                return new Double(0);
            }else if (size==1){
                return new Double(elts[first]);
            }else{
                int size1 = (size/2) + (size%2);
                int size2 = size - size1;
                SumTask leftTask = new SumTask(elts,first,size1);
                SumTask rightTask = new SumTask(elts,first+size1,size2);
                leftTask.fork();
                //rigthTask.fork();
                //no es la forma mas eficaz de hacerlo...
                //long leftAns = leftTask.join();
                double rightAns = rightTask.compute(); //forzar a q tenga un hilo activo
                double  leftAns = leftTask.join();
//                return leftAns + rightAns;
                Double res = ((leftAns + rightAns) / 2);
                return res ; //media?
            }
        }
        
    }
    
    @Override
    public Double Sum(int[] elts) {
        ForkJoinPool pool = new ForkJoinPool();
        Double result = pool.invoke(new SumTask(elts,0,elts.length));
        pool.shutdown();
        return result;
    }
}
