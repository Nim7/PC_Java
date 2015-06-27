/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc_5_3_dyv;


class SequentialSum implements SumArray {

	@Override
	public Double Sum(int[] elts) {
		double result = 0;
		int size = elts.length;
        for (int i = 0; i < size ; i++) {
            result += elts[i];
        }
//        return result;
        Double mean = result / size;
        return mean;
	}    
}
