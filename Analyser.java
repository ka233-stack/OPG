import java.io.*;
import java.util.*;

public class Analyser {

	static boolean noError = true;
	static boolean leftCond = false, rightCond = false;
	static StringBuilder text = new StringBuilder();
	static String encoding = "UTF-8";
	static int textIndex = 0, textSize;

	/*
	 * 终结符号集 = {+, *, (, ), i} 优先关系矩阵： | + | * | ( | ) | i ————————————————————— + |
	 * > | < | < | > | < * | > | > | < | > | < ( | < | < | < | = | < ) | > | > | - |
	 * > | - i | > | > | - | > | - 关系： 0 : < 1 : = 2 : > -1: error
	 */
	static Character[] vtList = { '+', '*', '(', ')', 'i' };
	static int[][] matrix = { { 2, 0, 0, 2, 0 }, { 2, 2, 0, 2, 0 }, { 0, 0, 0, 1, 0 }, { 2, 2, -1, 2, -1 },
			{ 2, 2, -1, 2, -1 } };

	static Stack<Character> stack = new Stack<Character>();

	public static void main(String[] args) {

		// 打开文件
		// BufferedReader br = new BufferedReader(new FileReader("text.in"));
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(args[0]));
			text = new StringBuilder(br.readLine());
			textSize = text.length();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 识别并输出
		pushNext();
		while (noError && textIndex < textSize) {
			Character prev = stack.peek(), next = peekNext();
			int res = compare(prev, next);
			if (res == -1) {
				System.out.println('E');
				break;
			} else if (res < 2) {
				pushNext();
			} else {
				reduce();
			}
		}
	}

	static void reduce() {
		Character c = stack.peek();
		if (c == 'i') {
			stack.pop();
			stack.push('E');
		}
	}

	static void pushNext() {
		Character symbol = text.charAt((int) textIndex);
		stack.push(symbol);
		System.out.println("I" + symbol);
		textIndex++;
	}

	static Character peekNext() {
		if (textIndex >= textSize) {
			return null;
		}
		Character res = text.charAt((int) textIndex);
		if (res == '\r' || res == '\n') {
			return null;
		}
		return res;
	}

	static int compare(Character c1, Character c2) {
		int x = locate(c1), y = locate(c2);
		if (x < 0 || y < 0) {
			return -1;
		}
		return matrix[x][y];
	}

	static int locate(Character c) {
		int max = vtList.length;
		for (int i = 0; i < max; i++) {
			if (vtList[i] == c) {
				return i;
			}
		}
		return -1;
	}

}
