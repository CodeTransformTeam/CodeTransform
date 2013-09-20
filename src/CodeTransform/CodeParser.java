package CodeTransform;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

class ParserResult	{
	public String 	codeString_;
	public Color 	codeColor_;
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
	abstract void Parser();
	
	/**
	 * 调用这个方法获得分析的结果
	 * @return 
	 * 成功，返回分析完成的代码结果列表
	 * 失败，返回null
	 */
	abstract ArrayList<ParserResult>	getParserResult();
}
