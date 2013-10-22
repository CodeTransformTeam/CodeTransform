package CodeTransform;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

class ParsedCode	{
	@Override
	public String toString() {
		String s1 = codeString_;
		s1 += ":";
		s1 += codeColor_.toString();
		return s1;
		//return codeString_ + ":" +  codeColor_.toString();
	}
	//代码内容，可以是空格符，回车符，实际代码内容
	public String 	codeString_;
	//这块代码的颜色，根据不同语言语法着色
	public Color 	codeColor_;
	public String    codeFont_;
	
}

public abstract class CodeParser {

	/**
	 * 调用这个方法对 CodeParser 进行初始化
	 * @param sourceFile
	 * 源代码文件
	 * @return 无
	 */
	abstract void init(File sourceFile);
	
	/**
	 * 调用这个方法对代码进行分析
	 * @return 无
	 */
	abstract void parse();
	
	/**
	 * 调用这个方法获得分析的结果
	 * @return 
	 * 成功，返回分析完成的代码结果列表
	 * 失败，返回null
	 */
	abstract ArrayList <ParsedCode>	getParserResult();
	
	/**
	 * 调用这个方法获可以对那些字段设置颜色，
	 * 例如，关键字，字符串
	 * @return 
	 * 成功，返回字符串数组
	 * 失败，返回null
	 */
	String[] getOptionItems() 	{
		return new String[]{"关键字", "字符串"};
	}
	
	Color getColorByOptionName(String optionString) {
		return Color.WHITE;
	}
	
	private boolean isCharPrintable(byte ch) {
		if (ch > 32 && ch < 127) {
			return true;
		}
		
		return false;
	}
	
	/*
	 * 这个方法，将buffer里面的单词分开，
	 * 可打印的一堆，不可打印的一堆
	 * 例如 "abc+-\r\ndef\t\tgh"分成
	 * "abc+-"、"\r\n"、"\t\t"、"gh"
	 */
	protected ArrayList<String> parsePrintableWords(byte[] buffer) {
		ArrayList<String> result = new ArrayList<String>();
		Boolean stringPrintable = false;
		
		byte[] splitBuffer = new byte[buffer.length];
		int j = 0;
		
		for (int i = 0; i < buffer.length; i++) {
			boolean charPrintable = isCharPrintable(buffer[i]);
			if (charPrintable != stringPrintable) {
				stringPrintable = charPrintable;
				if (j > 0) {
					String string = new String(splitBuffer, 0, j);
					result.add(string);
					//清空掉
					j = 0;
				}
			} 
			splitBuffer[j++] = buffer[i];
		}
		
		if (j > 0) {
			String string = new String(splitBuffer, 0, j);
			result.add(string);
		}
		
		return result;
	}
}
