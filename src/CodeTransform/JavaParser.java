package CodeTransform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JavaParser extends CodeParser {

	private File sourceFile_ = null;
	private ArrayList<ParsedCode> result_ = null;
	private Node keyWordNode_ = null;
	private Node colorNode_ = null;

	@Override
	void init(File sourceFile) {
		try {
			
			if (sourceFile.exists() == false) {
				System.err.println("找不到文件 " + sourceFile.getAbsolutePath());
				return;
			}
			
			sourceFile_ = sourceFile;
			result_ = new ArrayList<ParsedCode>();


			InputStream inputStream = JavaParser.class.getResourceAsStream("/res/JavaParserConfig.xml");
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(inputStream);
			Element rootNode = document.getDocumentElement();//获取根节点
			
			NodeList nodeList = rootNode.getChildNodes();
			rootNode.getAttributeNode("");
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
			
			assert(keyWordNode_ != null && colorNode_ != null);
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
			String string = new String(buffer);
			System.out.println(string);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	@Override
	ArrayList<ParsedCode> getParserResult() {
		return result_;
	}

	public static void main(String[] args) {
		try {
			assert(1 == 2);
			JavaParser parser = new JavaParser();
			parser.init(new File("src\\CodeTransform\\CodeParser.java"));
			parser.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
