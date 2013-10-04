/*模块名称: Java代码分析
 *主要功能：
 *1，划分单词以及符号
 *2，确定每个关键字颜色
 *3，确定注释块并上色
 *		包括行注释，块注释，文档注释
 *4，确定字符串块并上色
 *5，提供颜色设置功能
 */

package CodeTransform;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

enum CodeBlock {
	CodeBlockKeyWord, // 关键字
	CodeBlockLineComment, // 单行注释
	CodeBlockMultiLineComment, // 多行注释
	CodeBlockDocumentComment, // 文档注释，类似 /** 注释内容 */
	CodeBlockString, // 字符串内容，就是 "这种"
	CodeBlockDefault // 默认块
}

public class JavaParser extends CodeParser {
	private File sourceFile_ = null;
	private ArrayList<ParsedCode> parsedResult_ = null;
	private Node keyWordNode_ = null;
	private Node colorNode_ = null;
	private ArrayList<String> keyWordList_ = null;
	private HashMap<String, Color> colorMap_ = null;

	// xml读取部分
	private DocumentBuilderFactory documentBuilderFactory_ = null;
	private DocumentBuilder documentBuilder_ = null;
	private Document document_ = null;
	private Element rootNode_ = null;

	@Override
	void init(File sourceFile) {
		if (sourceFile.exists() == false) {
			System.err.println("找不到文件 " + sourceFile.getAbsolutePath());
			return;
		}
		
		try {
			sourceFile_ = sourceFile;
			keyWordList_ = new ArrayList<String>();
			colorMap_ = new HashMap<String, Color>();

			initXmlConfig();
			//必须先初始化xml
			initKeyWordList();
			initHashMap();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void parse() {
		assert (sourceFile_ != null);
		assert (keyWordNode_ != null && colorNode_ != null);
		try {
			parsedResult_ = new ArrayList<ParsedCode>();
			InputStream inputStream = new FileInputStream(sourceFile_);
			byte[] buffer = new byte[(int) sourceFile_.length()];
			inputStream.read(buffer);
			ArrayList<String> wordList = parsePrintableWords(buffer);

			for (String string : wordList) {
				ParsedCode parsedCode = new ParsedCode();
				parsedCode.codeString_ = string;
				if (isKeyWord(string)) {
					parsedCode.codeColor_ = this.colorMap_.get("keyword");
				} else {
					parsedCode.codeColor_ = this.colorMap_.get("default");
				}
			}

			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	ArrayList<ParsedCode> getParserResult() {
		return parsedResult_;
	}

	void initKeyWordList() {
		// 这里初始化关键字列表
		NodeList keyWordNodeList = keyWordNode_.getChildNodes();
		int length = keyWordNodeList.getLength();
		for (int i = 0; i < length; i++) {
			Node node = keyWordNodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				NamedNodeMap nodeMap = node.getAttributes();
				Node tmpNode = nodeMap.getNamedItem("name");
				String keyWord = tmpNode.getTextContent();
				if (keyWord.length() > 0) {
					this.keyWordList_.add(keyWord);
				}
			}
		}
	}

	void initHashMap() {
		// 这里初始化颜色哈希表
		NodeList colorNodeList = colorNode_.getChildNodes();
		int colorListLength = colorNodeList.getLength();
		for (int i = 0; i < colorListLength; i++) {
			Node node = colorNodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				NamedNodeMap nodeMap = node.getAttributes();

				Node tmpNode = nodeMap.getNamedItem("name");
				String colorKey = tmpNode.getTextContent();

				tmpNode = nodeMap.getNamedItem("value");
				String colorValueString = tmpNode.getTextContent();
				Color color = ColorBuilder.ColorFromString(colorValueString);
				colorMap_.put(colorKey, color);
			}
		}
	}

	void initXmlConfig() throws ParserConfigurationException, SAXException,
			IOException {
		// 下面是初始化xml文件
		InputStream inputStream = JavaParser.class
				.getResourceAsStream("/res/JavaParserConfig.xml");
		documentBuilderFactory_ = DocumentBuilderFactory.newInstance();
		documentBuilder_ = documentBuilderFactory_.newDocumentBuilder();
		document_ = documentBuilder_.parse(inputStream);
		rootNode_ = document_.getDocumentElement();// 获取根节点

		NodeList nodeList = rootNode_.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName() == "KeyWordSet") {
					keyWordNode_ = node;
				} else if (node.getNodeName() == "ColorSet") {
					colorNode_ = node;
				} else {
					System.out.println("---" + node.getNodeName());
				}
			}
		}

		if (keyWordNode_ == null || colorNode_ == null) {
			throw new IllegalArgumentException("JavaParserConfig.xml 文件非法");
		}
	}

	boolean isKeyWord(String string) {
		for (String keyWorkString : this.keyWordList_) {
			if (keyWorkString.equals(string)) {
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args) {
		try {
			JavaParser parser = new JavaParser();
			parser.init(new File("src/CodeTransform/CodeParser.java"));
			parser.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
