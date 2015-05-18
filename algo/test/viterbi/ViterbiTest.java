package viterbi;

import static org.junit.Assert.*;

import org.junit.Test;

public class ViterbiTest {

	/**
	 * states = ('Healthy', 'Fever')
 
observations = ('normal', 'cold', 'dizzy')
 
start_probability = {'Healthy': 0.6, 'Fever': 0.4}
 
transition_probability = {
   'Healthy' : {'Healthy': 0.7, 'Fever': 0.3},
   'Fever' : {'Healthy': 0.4, 'Fever': 0.6}
   }
 
emission_probability = {
   'Healthy' : {'normal': 0.5, 'cold': 0.4, 'dizzy': 0.1},
   'Fever' : {'normal': 0.1, 'cold': 0.3, 'dizzy': 0.6}
   }
	 */
	@Test
	public void test() {
		int[] states = new int[2];
		int[] observations = new int[3];
		double[] initProb = new double[] {0.6, 0.4};
		double[][] transitionProb = new double[][]{
				{0.7, 0.3},
				{0.4, 0.6}
				};
		double[][] emitProb = new double[][]{
				{0.5, 0.4, 0.1},
				{0.1, 0.3, 0.6}
		};
		int[] path = Viterbi.viterbi(states, observations, initProb, transitionProb, emitProb);
		for (int i = 0; i < path.length; i++)
			System.out.print(path[i]);
		
	}

}
