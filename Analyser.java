import java.io.*;
import java.util.*;

public class Analyser {

	static boolean isCompleted = false;
	static boolean leftCond = false, rightCond = false;
	static String text = "";
	static String encoding = "UTF-8";
	static int textIndex = 0, textSize;

	/*
	 * 终结符号集 = {+, *, (, ), i} 优先关系矩阵： | + | * | ( | ) | i ————————————————————— + |
	 * > | < | < | > | < * | > | > | < | > | < ( | < | < | < | = | < ) | > | > | - |
	 * > | - i | > | > | - | > | - 关系： 0 : < 1 : = 2 : > -1: error
	 */
	static Character[] vtList = { '+', '*', '(', ')', 'i' };
	static int[][] matrix = { { 2, 0, 0, 2, 0, 2 }, { 2, 2, 0, 2, 0, 2 }, { 0, 0, 0, 1, 0, 2 }, { 2, 2, -1, 2, -1, 2 },
			{ 2, 2, -1, 2, -1, 2 }, { 0, 0, 0, -1, 0, 2 } };

	static Stack<Character> stack = new Stack<Character>();

	public static void main(String[] args) {

		// 打开文件

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("text.in"));
			// br = new BufferedReader(new FileReader(args[0]));
			text = br.readLine();
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
		stack.push('#');
		while (!isCompleted) {
			Character prev = peekOp(), next = peekNext();
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

	static Character peekOp() {
		Character c = stack.peek();
		if (c == 'E') {
			Character temp = stack.pop();
			Character res = stack.peek();
			stack.push(temp);
			return res;
		}
		return c;
	}

	static void reduce() {
		if (stack.peek() == 'i') {
			stack.pop();
			stack.push('E');
			System.out.println("R");
		} else {
			if (stack.size() == 2 && stack.peek() == 'E') {
				isCompleted = true;
				return;
			}
			Character s3 = stack.pop();
			Character s2 = stack.pop();
			Character s1 = stack.pop();
			if (s1 == 'E' && s3 == 'E' || isOperator(s2)) {
				stack.push('E');
				System.out.println("R");
			} else if (s1 == '(' && s3 == ')' && s2 == 'E') {
				stack.push('E');
				System.out.println("R");
			} else {
				isCompleted = true;
				System.out.println("RE");
			}
		}
	}

	static boolean isOperator(Character c) {
		if (c == '+' || c == '*') {
			return true;
		}
		return false;
	}

	static void pushNext() {
		Character symbol = text.charAt((int) textIndex);
		stack.push(symbol);
		System.out.println("I" + symbol);
		textIndex++;
	}

	static Character peekNext() {
		if (textIndex >= textSize) {
			return '#';
		}
		Character res = text.charAt((int) textIndex);
		if (res == '\r' || res == '\n') {
			return '#';
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
		if (c == '#') {
			return 5;
		}
		return -1;
	}

}
