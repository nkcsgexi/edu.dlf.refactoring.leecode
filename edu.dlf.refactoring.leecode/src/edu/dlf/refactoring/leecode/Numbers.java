import java.util.HashMap;
import java.util.LinkedHashMap;
public class Numbers {
	

	private static int convertRomanNumber(String s) throws Exception{
		if(s.equals("")) return 0;
		HashMap<Character, Integer> map = new LinkedHashMap<Character,
			Integer>();
		map.put('L', 50);
		map.put('X', 10);
		map.put('V', 5);
		map.put('I', 1);
		for(char c : map.keySet()) {
			int i = s.indexOf(c);
			if(i != -1) {
				int before = convertRomanNumber(s.substring(0, i));
				int after = convertRomanNumber(s.substring(i + 1));
				return map.get(c) + after - before;
			}
		}
		throw new Exception("cannot parse");
	}

	private static boolean isNumPalindrome(int num) {
		int div = 1;
		while(num / div >= 10) div *= 10;
		while(num != 0) {
			int r = num % 10;
			int l = num / div;
			if(r != l) return false;
			num = (num % div)/10;
			div /= 10;
		}
		return true;
	}

			
		


	public static void main(String[] args) {
	}

}
