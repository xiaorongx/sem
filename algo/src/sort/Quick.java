package sort;


/**
 * http://aofa.cs.princeton.edu/10analysis/
 * @author xiaorong
 *
 */
public class Quick {
	/**
	 * choose a number in the list, every element in the list which is less than or equal to the chosen element is placed
	 * at the left side of the element, otherwise, place to the right side
	 * @param a
	 * @param left
	 * @param right
	 * @return
	 */
	public static int partition(int[] a, int left, int right) {
		// choose the one with lowest index, it can also been chosen randomly
		int b = a[left];
		int bIdx = right;
		swap(a, left, right);
		right = right - 1;
		while (left <= right) {
			if (a[left] <= b) left++;
			else {
				swap(a, left, right);
				right--;
			}
		}
		swap(a, left, bIdx);
		return left;
	}
	
	public static void swap(int[] a, int idx1, int idx2) {
		int tmp = a[idx1];
		a[idx1] = a[idx2];
		a[idx2] = tmp;
	}
	
	public static void sort(int[] a, int left, int right ) {
		if (left >= right)
			return;
		int partition = partition(a, left, right);
		sort(a, left, partition-1);
		sort(a, partition+1, right);
	}
}
