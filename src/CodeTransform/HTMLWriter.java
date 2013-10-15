package CodeTransform;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class HTMLWriter {
	
	private CodeParser parser_;

	public  HTMLWriter(CodeParser parser) {
		parser_ = parser;
	}
	
	void	write(File file) {
		try {
			if (file.exists() == false) {
				file.createNewFile();
			}
			
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			writeHeader(fileOutputStream, file.getName());
			writeBody(fileOutputStream);
			writeFooter(fileOutputStream);
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void writeFooter(FileOutputStream fileOutputStream) {
		String footerString = "</body>\r\n</html>";
		try {
			fileOutputStream.write(footerString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeBody(FileOutputStream fileOutputStream) {
		int lineNumber = 1;
		ArrayList<ParsedCode>	parsedCodes = parser_.getParserResult();
		for (ParsedCode parsedCode : parsedCodes) {
			
		}
	}

	private void writeHeader(FileOutputStream fileOutputStream, String title) {
		String headerString = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
				+ "\"http://www.w3.org/TR/html4/loose.dtd\">\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
				+ "<title>" + title + "</title>\r\n" 
				+ "</head>\r\n" 
				+ "<body>\r\n";
		try {
			fileOutputStream.write(headerString.getBytes());
			System.out.println("HTMLWriter.writeHeader()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HTMLWriter writer = new HTMLWriter(null);
		writer.write(new File("Test.html"));
	}

}
