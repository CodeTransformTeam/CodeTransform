package CodeTransform;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JavaParser extends CodeParser {

	private File sourceFile_ = null;
	private ArrayList<ParsedCode> parsedResult_ = null;
	private Node keyWordNode_ = null;
	private Node colorNode_ = null;
	
	//xml读取部分
	private DocumentBuilderFactory documentBuilderFactory_ = null;
	private DocumentBuilder documentBuilder_ = null;
	private Document document_ = null;
	private Element rootNode_ = null;
	
	@Override
	void init(File sourceFile) {
		try {
			
			if (sourceFile.exists() == false) {
				System.err.println("找不到文件 " + sourceFile.getAbsolutePath());
				return;
			}
			
			sourceFile_ = sourceFile;
			parsedResult_ = new ArrayList<ParsedCode>();

			//下面是初始化xml文件
			InputStream inputStream = JavaParser.class.getResourceAsStream("/res/JavaParserConfig.xml");
			documentBuilderFactory_ = DocumentBuilderFactory.newInstance();
			documentBuilder_ = documentBuilderFactory_.newDocumentBuilder();
			document_ = documentBuilder_.parse(inputStream);
			rootNode_ = document_.getDocumentElement();//获取根节点
			
			NodeList nodeList = rootNode_.getChildNodes();
			
			for(int i = 0; i < nodeList.getLength(); i++){
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	void parse() {
		assert (sourceFile_ != null);
		assert(keyWordNode_ != null && colorNode_ != null);
		try {
			InputStream inputStream = new FileInputStream(sourceFile_);
			byte[] buffer = new byte[(int) sourceFile_.length()];
			inputStream.read(buffer);
			ArrayList<String> wordList = parseWords(buffer);

			for (String string : wordList) {
				if (isKeyWord(string)) {
					System.out.println("key word:" + string);
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
	
	boolean isKeyWord(String string){
		NodeList nodeList = keyWordNode_.getChildNodes();
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				NamedNodeMap nodeMap = node.getAttributes();
				Node tmpNode = nodeMap.getNamedItem("name");
				if (tmpNode.getTextContent().equalsIgnoreCase(string)) {
					return true;
				}
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

