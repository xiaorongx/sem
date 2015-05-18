package catalan;

/**
 * http://en.wikipedia.org/wiki/Catalan_number
 * @author xiaorong
 *
 */
public class Catalan {

	/**
	 * 
	 * @param left
	 * @param right
	 * @param prefix
	 * @return number of ways to print balanced parenthesis
	 */
	public static int prtParen(int left, int right, String prefix) {
		int count = 0;
		if (left > right) return 0;
		if (left == 0 && right == 0) {
			System.out.println(prefix);
			count++;
			return count;
		}

		if (left != 0) { 
			count += prtParen(left-1, right, prefix + "("); // one choice is to continue print left
			if (left < right) 
				count += prtParen(left, right-1, prefix + ")"); // another choice is to print right 
		}
		else { // only choice is to print right
			count+=prtParen(left, right-1, prefix+")");
		}
		return count;
	}

}
