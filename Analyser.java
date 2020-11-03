import java.io.*;

public class Tokenizer {

	static boolean noError;
	static String text = "";
	static String token = "";
	static String ch = "";
	static long textIndex, textSize;

	public static void main(String[] args) {

		// 打开文件
		String encoding = "UTF-8";
		// File file = new File("text.in");
		File file = new File(args[0]);
		textSize = file.length();
		byte[] filecontent = new byte[(int) textSize];
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			in.read(filecontent);
			text = new String(filecontent, encoding);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 识别并输出
		noError = true;
		textIndex = -1L;
		while (noError && textIndex < textSize) {
			getsym();
		}
	}

	// 判断是否是数字
	static boolean isDigit() {
		return ch.matches("[0-9]");
	}

	// 判断是否是字母
	static boolean isLetter() {
		return ch.matches("[a-zA-Z]");
	}

	// 将token中的字符串转换成整数
	static int transNum(String str) {
		return Integer.parseInt(str);
	}

	// 判断是否是冒号
	static boolean isColon() {
		return ch.equals(":");
	}

	// 判断是否是加号
	static boolean isPlus() {
		return ch.equals("+");
	}

	// 判断是否是乘号
	static boolean isStar() {
		return ch.equals("*");
	}

	// 判断是否是逗号
	static boolean isComma() {
		return ch.equals(",");
	}

	// 判断是否是等号
	static boolean isEqu() {
		return ch.equals("=");
	}

	// 判断是否是空格
	static boolean isSpace() {
		return ch.equals(" ");
	}

	// 判断是否是换行
	static boolean isNewline() {
		return ch.equals("\n") || ch.equals("\r");
	}

	// 判断是否是换行
	static boolean isTab() {
		return ch.equals("\t");
	}

	// 判断是否是左括号
	static boolean isLpar() {
		return ch.equals("(");
	}

	// 判断是否是右括号
	static boolean isRpar() {
		return ch.equals(")");
	}

	static boolean isEnd() {
		return ch.equals("\0");
	}

	// 拼接到token上
	static void catToken() {
		token += ch;
	}

	// 字符指针回退
	static void retract() {
		textIndex--;
	}

	// 判断是否是保留字
	static void checkRserver(String token) {
		if (token.equals("BEGIN"))
			System.out.println("Begin");
		else if (token.equals("END"))
			System.out.println("End");
		else if (token.equals("FOR"))
			System.out.println("For");
		else if (token.equals("IF"))
			System.out.println("If");
		else if (token.equals("THEN"))
			System.out.println("Then");
		else if (token.equals("ELSE"))
			System.out.println("Else");
		else
			System.out.println("Ident(" + token + ")");
	}

	static void error() {
		System.out.println("Unknown");
		noError = false;
	}

	static void getNextChar() {
		textIndex++;
		if (textIndex < textSize)
			ch = text.substring((int) textIndex, (int) textIndex + 1);
		else
			ch = "\0";
	}

	int hasNextChar() {
		return 0;
	}

	static int clearToken() {
		token = "";
		return 0;
	}

	static int getsym() {
		// 置token为空串
		clearToken();
		getNextChar();
		while (isSpace() || isNewline() || isTab()) {
			// 读取字符，跳过空格、换行和Tab
			getNextChar();
		}
		if (isLetter()) // 判断当前字符是否是一个字母
		{
			while (isLetter() || isDigit()) // 将字符拼接成字符串
			{
				catToken();
				getNextChar();
			}
			retract(); // 指针后退一个字符
			checkRserver(token); // 判断是否是保留字
		} else if (isDigit()) // 判断当前字符是否是一个数字
		{
			while (isDigit()) // 将字符拼接成整数
			{
				catToken();
				getNextChar();
			}
			retract();
			int num = transNum(token); // 将token中的字符串转换成整数
			System.out.println("Int(" + num + ")"); // 此时识别的单词是整数
		} else if (isColon()) // 判断当前字符是否是冒号
		{
			getNextChar();
			if (isEqu()) { // 判断是否是赋值符号
				System.out.println("Assign");
			} else {
				retract();
				System.out.println("Colon");
			}
		} else if (isPlus()) // 判断当前字符是否是加号
			System.out.println("Plus");
		else if (isStar()) // 判断当前字符是否是星号
			System.out.println("Star");
		else if (isLpar()) // 判断当前字符是否是左括号
			System.out.println("LParenthesis");
		else if (isRpar()) // 判断当前字符是否是右括号
			System.out.println("RParenthesis");
		else if (isComma()) // 判断当前字符是否是逗号
			System.out.println("Comma");
		else if (isEnd())
			;
		else
			error();
		return 0;
	}

}
