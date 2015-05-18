package viterbi;

/**
 * http://en.wikipedia.org/wiki/Viterbi_algorithm#Example
 * http://en.wikipedia.org/wiki/Arg_max
 * @author xiaorong
 *
 */
public class Viterbi {
	/**
	 * 
	 * @param states
	 * @param observations
	 * @param initProb
	 * @param transitionProb
	 * @param emitProb
	 * @return an array to represent viterbi path
	 */
	public static int[] viterbi(int[] states, int[] observations, 
			double[] initProb,  // prop of each state
			double[][] transitionProb,  // prop from one state to another
			double[][] emitProb)  // prop of getting an observation given a state
			{
		int numStates = states.length;
		int numObs = observations.length;
		double[][] v = new double[numObs][numStates]; // row is observation, col is state, the value in each cell is the probability of the most probable state sequence responsible for the first t observations that has k as its final state
		int[][] path = new int[numObs][numStates];   // argmax http://en.wikipedia.org/wiki/Arg_max
		
		for (int state = 0; state < numStates; state++) { // for each state of the first observation, 
			v[0][state] = initProb[state]*emitProb[state][0];
			path[0][state] = state;
		}
		
		for (int obs = 1; obs < numObs; obs++) { // for all observations
			System.out.println("obs " + obs);
			for (int state = 0; state < numStates; state++) { // fill each cell
				System.out.println("state " + state);
				double max = -999;
				int argmax = -999;
				for (int state1 = 0; state1 < numStates; state1++) {
					System.out.println("max: prev " + v[obs-1][state1] + " tran: "  + transitionProb[state1][state] + " emit: "+ emitProb[state][obs]);
					double current = v[obs-1][state1] * transitionProb[state1][state] * emitProb[state][obs];
					if (current > max) {
						max = current;
						argmax = state1;
					}
				}
				v[obs][state] = max;
				path[obs][state] = argmax;
			}		
		}
		
		int[] viterbiPath = new int[numObs]; // viterbi path
		double tempMax = -999;

		for (int state = 0; state < numStates; state++) {  // the last state when the max reached is not recorded in path matrix
			if (v[v.length - 1][state] > tempMax) {
				tempMax = v[v.length - 1][state];
				viterbiPath[v.length - 1] = state; 
			}
		}

		for (int obs = v.length - 1; obs > 0; obs--) {
			double max = -999;
			int currentState = -999;
			for (int state = 0; state < numStates; state++) { 
				if (v[obs][state] > max) {
					max = v[obs][state];
					currentState = path[obs][state];
				}
			}
			viterbiPath[obs-1] = currentState;
		}
		return viterbiPath;
	}
}
