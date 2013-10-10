package CodeTransform;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CodeTransformFrame extends JFrame implements ActionListener{
	// 这个东西是给 序列化 校验用的
	private static final long serialVersionUID = 1L;

	private FilePanel topPanel_ = new FilePanel();
	private JPanel bottomPanel_ = new JPanel();
	public CodeTransformFrame() throws Exception {
		// 对frame进行初始化
		super("代码转换器");
		setSize(600,500);
		setLocation(200,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setLayout(new BorderLayout());
		add(topPanel_,BorderLayout.NORTH);
		add(bottomPanel_,BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
//		Object object = e.getSource();
//		if () {
//		} 
//		else {
//			throw new InvalidParameterException();
//		}
	}

}
