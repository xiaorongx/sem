package sort;

import static org.junit.Assert.*;

import org.junit.Test;

public class MergeTest {

	@Test
	public void testMerge() {
		int[] a = new int[]{1,3,5,6};
		int[] b = new int[]{1,2,4};
		int[] expect = new int[]{1,1,2,3,4,5,6};
		assertArrayEquals(expect, Merge.merge(a,b));
	}

	@Test
	public void testSortOne() {
		int[] a = new int[]{1};
		int[] expect = new int[]{1};
		assertArrayEquals(expect, Merge.sort(a));
	}
	
	@Test
	public void testSortTwo() {
		int[] a = new int[]{1,2};
		int[] expect = new int[]{1,2};
		assertArrayEquals(expect, Merge.sort(a));;
	}
	
	@Test
	public void testSortTwoInverse() {
		int[] a = new int[]{2,1};
		int[] expect = new int[]{1,2};
		assertArrayEquals(expect, Merge.sort(a));
	}
}
