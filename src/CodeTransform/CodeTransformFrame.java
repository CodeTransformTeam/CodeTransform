package CodeTransform;

import java.awt.*;
import javax.swing.*;

public class CodeTransformFrame extends JFrame {
	// 这个东西是给 序列化 校验用的
	private static final long serialVersionUID = 1L;

	private FileManagerPanel topFileManagerPanel_ = new FileManagerPanel();
	public CodeTransformFrame() throws Exception {
		// 对frame进行初始化
		super("代码转换器");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		setLayout(new BorderLayout());
		add(topFileManagerPanel_,BorderLayout.CENTER);
	}

}
