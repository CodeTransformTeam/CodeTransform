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

public class CppParser extends CodeParser {
	enum CodeBlock {
		CodeBlockKeyWord, // 关键字
		CodeBlockLineComment, // 单行注释
		CodeBlockMultiLineComment, // 多行注释
		CodeBlockString, // 字符串内容，就是 "这种"
		CodeBlockChar, // 字符内容，就是 '这种'
		CodeBlockDefault // 默认块
	}

	private File sourceFile_ = null;
	private ArrayList<ParsedCode> parsedResult_ = null;
	private Node keyWordNode_ = null;
	private Node colorNode_ = null;
	private Node fontNode_ = null;
	private ArrayList<String> keyWordList_ = null;
	private HashMap<String, Color> colorMap_ = null;
	private HashMap<String, String> fontMap_ = null;

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
			fontMap_ = new HashMap<String, String>();

			initXmlConfig();
			// 必须先初始化xml
			initKeyWordList();
			initColorHashMap();
			initFontHashMap();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void parse() {
		try {
			parsedResult_ = new ArrayList<ParsedCode>();
			InputStream inputStream = new FileInputStream(sourceFile_);
			byte[] buffer = new byte[(int) sourceFile_.length()];
			inputStream.read(buffer);
			ArrayList<String> wordArrayList = parsePrintableWords(buffer);

			for (int i = 0; i < wordArrayList.size(); i++) {
				String wordString = wordArrayList.get(i);

				if (hasKeyWord(wordString)) {
					// 关键字块
					i = parseKeyWord(wordArrayList, i);

				} else if (wordString.indexOf("//") >= 0) {
					i = parseLineComment(wordArrayList, i);

				} else if (wordString.indexOf("/*") >= 0) {
					i = parseMultiLineComment(wordArrayList, i);

				} else if (wordString.indexOf("\"") >= 0) {
					i = parseString(wordArrayList, i);

				} else if (wordString.indexOf('\'') >= 0) {
					// 单个字符
					i = parseChar(wordArrayList, i);

				} else {
					i = parseDefault(wordArrayList, i);
				}
			}

			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int parseChar(ArrayList<String> wordArrayList, int i) {
		Color color = this.colorMap_.get("Char");

		String wordString = wordArrayList.get(i);
		int beginIndex = wordString.indexOf('\'');
		int endIndex = -1;
		String commentString = wordString.substring(beginIndex);
		if (beginIndex > 0) {
			// 不是在单词第一个字母
			String leftString = wordString.substring(0, beginIndex);
			wordArrayList.set(i, leftString);
			wordArrayList.add(i + 1, commentString);
			// 简单处理下就好了，返回去重新分析，没准刚刚分出来的是关键字
			i--;
		} else {
			// 接下来都是字符串内容

			// 屏蔽第一个"字符
			endIndex = 1;
			do {
				endIndex = wordString.indexOf('\'', endIndex);
				if (endIndex > 0 && wordString.charAt(endIndex - 1) == '\\') {
					endIndex++;
					// 不是结束符，转义字符
					continue;
				} else if (endIndex > 0
						&& wordString.charAt(endIndex - 1) != '\\') {
					break;
				} else if (endIndex == 0) {
					break;
				}

				ParsedCode parsedCode = new ParsedCode();
				parsedCode.codeString_ = wordString;
				parsedCode.codeColor_ = color;
				parsedCode.codeFont_ = fontMap_.get("Char");
				parsedResult_.add(parsedCode);

				if (wordArrayList.size() > i + 1) {
					wordString = wordArrayList.get(++i);
				} else {
					break;
				}
			} while (true);

			// 到这里字符串块结束，但是结束符还没弄进去
			endIndex++;
			String leftString = wordString.substring(0, endIndex);
			wordArrayList.set(i, leftString);
			ParsedCode parsedCode = new ParsedCode();

			parsedCode.codeString_ = leftString;
			parsedCode.codeColor_ = color;
			parsedCode.codeFont_ = fontMap_.get("Char");
			parsedResult_.add(parsedCode);

			if (wordString.length() > beginIndex) {
				String remainString = wordString.substring(endIndex);
				wordArrayList.add(i + 1, remainString);
			}
		}

		return i;
	}

	private int parseString(ArrayList<String> wordArrayList, int i) {
		Color color = this.colorMap_.get("String");
		String wordString = wordArrayList.get(i);
		int beginIndex = wordString.indexOf("\"");
		int endIndex = -1;
		String commentString = wordString.substring(beginIndex);
		if (beginIndex > 0) {
			// 不是在单词第一个字母
			String leftString = wordString.substring(0, beginIndex);
			wordArrayList.set(i, leftString);
			wordArrayList.add(i + 1, commentString);
			// 简单处理下就好了，返回去重新分析，没准刚刚分出来的是关键字
			i--;
		} else {
			// 接下来都是字符串内容

			// 屏蔽第一个"字符
			endIndex = 1;
			do {

				endIndex = wordString.indexOf("\"", endIndex);
				if (endIndex > 0 && wordString.charAt(endIndex - 1) == '\\') {
					endIndex++;
					// 不是结束符，转义字符
					continue;
				} else if (endIndex > 0
						&& wordString.charAt(endIndex - 1) != '\\') {
					break;
				} else if (endIndex == 0) {
					break;
				}

				ParsedCode parsedCode = new ParsedCode();
				parsedCode.codeString_ = wordString;
				parsedCode.codeColor_ = color;
				parsedCode.codeFont_ = fontMap_.get("String");
				parsedResult_.add(parsedCode);

				if (wordArrayList.size() > i + 1) {
					wordString = wordArrayList.get(++i);
				} else {
					break;
				}
			} while (true);

			// 到这里字符串块结束，但是结束符还没弄进去
			endIndex++;
			String leftString = wordString.substring(0, endIndex);
			wordArrayList.set(i, leftString);
			ParsedCode parsedCode = new ParsedCode();

			parsedCode.codeString_ = leftString;
			parsedCode.codeColor_ = color;
			parsedCode.codeFont_ = fontMap_.get("String");
			parsedResult_.add(parsedCode);

			if (wordString.length() > beginIndex) {
				String remainString = wordString.substring(endIndex);
				wordArrayList.add(i + 1, remainString);
			}
		}

		return i;
	}

	private int parseMultiLineComment(ArrayList<String> wordArrayList, int i) {
		Color color = this.colorMap_.get("MultiLineComment");
		String wordString = wordArrayList.get(i);
		int beginIndex = wordString.indexOf("/*");
		String commentString = wordString.substring(beginIndex);
		if (beginIndex > 0) {
			// 注释不是在单词第一个字母
			String leftString = wordString.substring(0, beginIndex);
			wordArrayList.set(i, leftString);
			wordArrayList.add(i + 1, commentString);
			// 简单处理下就好了，返回去重新分析，没准刚刚分出来的是关键字
			i--;
		} else {
			// 接下来都是注释内容
			while (wordString.indexOf("*/") < 0) {
				ParsedCode parsedCode = new ParsedCode();
				parsedCode.codeString_ = wordString;
				parsedCode.codeColor_ = color;
				parsedCode.codeFont_ = fontMap_.get("MultiLineComment");
				parsedResult_.add(parsedCode);

				wordString = wordArrayList.get(++i);
			}

			// 到这里注释块结束，但是结束符还没弄进去
			beginIndex = wordString.indexOf("*/") + 2;
			String leftString = wordString.substring(0, beginIndex);
			wordArrayList.set(i, leftString);
			ParsedCode parsedCode = new ParsedCode();
			parsedCode.codeString_ = leftString;
			parsedCode.codeColor_ = color;
			parsedCode.codeFont_ = fontMap_.get("MultiLineComment");
			parsedResult_.add(parsedCode);
			if (wordString.length() > beginIndex) {
				String remainString = wordString.substring(beginIndex);
				wordArrayList.add(i + 1, remainString);
			}
		}

		return i;
	}

	private String getKeyWord(String wordString) {
		for (String keyWorkString : this.keyWordList_) {
			if (wordString.indexOf(keyWorkString) >= 0) {
				return keyWorkString;
			}
		}

		return null;
	}

	private int parseKeyWord(ArrayList<String> wordArrayList, int i) {
		// 关键字

		String wordString = wordArrayList.get(i);
		String keyWordString = getKeyWord(wordString);
		int keyWordIndex = wordString.indexOf(keyWordString);
		if (keyWordIndex == 0) {
			// 在开始位置
			ParsedCode parsedCode = new ParsedCode();
			parsedCode.codeString_ = keyWordString;
			parsedCode.codeColor_ = colorMap_.get("KeyWord");
			parsedCode.codeFont_ = fontMap_.get("KeyWord");
			parsedResult_.add(parsedCode);

			if (keyWordString.length() < wordString.length()) {
				// 还有剩下的
				String remainString = wordString.substring(keyWordString
						.length());
				wordArrayList.add(i + 1, remainString);
			}
		} else {
			// 不是开始位置，简单处理一下返回
			String leftString = wordString.substring(0, keyWordIndex);
			String rightString = wordString.substring(keyWordIndex);
			wordArrayList.set(i, leftString);
			wordArrayList.add(i + 1, rightString);
			i--;
		}

		return i;
	}

	private int parseLineComment(ArrayList<String> wordArrayList, int i) {
		// 注释块1
		String wordString = wordArrayList.get(i);
		ParsedCode parsedCode = new ParsedCode();
		parsedCode.codeString_ = wordString;

		int beginIndex = wordString.indexOf("//");
		String commentString = wordString.substring(beginIndex);
		if (beginIndex > 0) {
			// 注释不是在单词第一个字母
			String leftString = wordString.substring(0, beginIndex);
			wordArrayList.set(i, leftString);
			wordArrayList.add(i + 1, commentString);

			// 简单处理下就好了，返回去重新分析，没准刚刚分出来的是关键字
			i--;
		} else {
			parsedCode.codeColor_ = colorMap_.get("LineComment");
			parsedCode.codeFont_ = fontMap_.get("LineComment");
			parsedResult_.add(parsedCode);
			// 接下来都是注释内容
			while (wordString.indexOf('\n') < 0) {
				if (i < wordArrayList.size() - 1) {
					wordString = wordArrayList.get(++i);
				} else {
					break;
				}

				parsedCode = new ParsedCode();
				parsedCode.codeString_ = wordString;
				parsedCode.codeColor_ = colorMap_.get("LineComment");
				parsedCode.codeFont_ = fontMap_.get("LineComment");
				parsedResult_.add(parsedCode);
			}
		}

		return i;
	}

	private int parseDefault(ArrayList<String> wordArrayList, int i) {
		// 普通块
		String wordString = wordArrayList.get(i);
		ParsedCode parsedCode = new ParsedCode();
		parsedCode.codeString_ = wordString;
		parsedCode.codeColor_ = colorMap_.get("Default");
		parsedCode.codeFont_ = fontMap_.get("Default");
		parsedResult_.add(parsedCode);
		return i;
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

	void initColorHashMap() {
		// 这里初始化颜色哈希表和字体哈希表
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
				Color color = ColorConverter.ColorFromString(colorValueString);
				colorMap_.put(colorKey, color);
			}
		}
	}

	void initFontHashMap() {
		// 这里初始化颜色哈希表和字体哈希表
		NodeList colorNodeList = fontNode_.getChildNodes();
		int colorListLength = colorNodeList.getLength();
		for (int i = 0; i < colorListLength; i++) {
			Node node = colorNodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				NamedNodeMap nodeMap = node.getAttributes();

				Node tmpNode = nodeMap.getNamedItem("name");
				String fontKey = tmpNode.getTextContent();

				tmpNode = nodeMap.getNamedItem("value");
				String valueString = tmpNode.getTextContent();
				fontMap_.put(fontKey, valueString);
			}
		}
	}

	void initXmlConfig() throws ParserConfigurationException, SAXException,
			IOException {
		// 下面是初始化xml文件
		InputStream inputStream = JavaParser.class
				.getResourceAsStream("/res/CppParserConfig.xml");
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
				} else if (node.getNodeName() == "FontSet") {
					fontNode_ = node;
				} else {
					System.out.println("---" + node.getNodeName());
				}
			}
		}

		if (keyWordNode_ == null || colorNode_ == null || fontNode_ == null) {
			throw new IllegalArgumentException("JavaParserConfig.xml 文件非法");
		}
	}

	boolean hasKeyWord(String string) {
		for (String keyWorkString : this.keyWordList_) {
			if (string.indexOf(keyWorkString) >= 0) {
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args) {
		try {
			CppParser parser = new CppParser();
			parser.init(new File("temp/cpptest.cpp"));
			parser.parse();
			ArrayList<ParsedCode> resultArrayList = parser.getParserResult();
			for (int i = 0; i < resultArrayList.size(); i++) {
				System.out.println("i = " + i + ": ---------------------【"
						+ resultArrayList.get(i) + "】----------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}