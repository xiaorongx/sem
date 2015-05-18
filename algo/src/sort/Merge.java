package sort;

import java.util.Arrays;

public class Merge {
	public static int[] merge(int[] a, int[] b) {
		int[] c = new int[a.length + b.length];
		int i = 0;
		int j  = 0;
		int k = 0;
		while (i < a.length && j < b.length) {
			if (a[i] < b[j]) {
				c[k++] = a[i++];
			}
			else {
				c[k++] = b[j++];
			}		
		}
		while (i < a.length) {
			c[k++] = a[i++];
		}
		while (j < b.length) {
			c[k++] = b[j++];
		}
		return c;		
	}
	
	public static int[] sort(int[] a){
		if (a.length <= 1)
			return a;
		int middle = a.length/2;

		int[] left = sort(Arrays.copyOfRange(a, 0, middle));
		int[] right = sort(Arrays.copyOfRange(a, middle, a.length));
		return merge(left, right);
	}
}
