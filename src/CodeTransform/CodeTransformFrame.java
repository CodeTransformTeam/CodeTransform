package CodeTransform;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class CodeTransformFrame extends JFrame implements ActionListener{
	// 这个东西是给 序列化 校验用的
	private static final long serialVersionUID = 1L;
	
	private JPanel leftPanel_ = new JPanel();
	private JPanel middlePanel_ = new JPanel();
	private JPanel rightPanel_ = new JPanel();
	
	private JButton addButton_ = new JButton("添加文件");
	private JButton removeButton_ = new JButton("删除文件");		
	private JButton parseButton_ = new JButton("转换");

	private JComboBox<String> transformSelection_ = new JComboBox<String>(new String[]{"html5","xhtml 1.0","html 4.01"});
	
	public CodeTransformFrame() throws Exception {
		// 对frame进行初始化
		super("代码转换器");
		setSize(500,350);
		setLocation(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		leftPanel_.add(new JTextArea(15,15));
		
		middlePanel_.setLayout(new FlowLayout(FlowLayout.CENTER));
		middlePanel_.add(addButton_);
		middlePanel_.add(removeButton_);
		
		JPanel rightTopPanel = new JPanel();
		JPanel rightBottomPanel = new JPanel();
		
		JLabel transformAsLabel = new JLabel("转换为");
		JLabel saveToLabel = new JLabel("存储在");
		
		rightTopPanel.setBorder(new TitledBorder("选项"));
		rightTopPanel.setLayout(new GridLayout(2,1));
		rightTopPanel.add(transformAsLabel);
		rightTopPanel.add(transformSelection_);
		rightTopPanel.add(saveToLabel);
		rightBottomPanel.add(parseButton_);
		
		rightPanel_.setLayout(new BorderLayout());
		rightPanel_.add(rightTopPanel,BorderLayout.NORTH);
		rightPanel_.add(rightBottomPanel,BorderLayout.SOUTH);
		
		setLayout(new BorderLayout());
		add(leftPanel_,BorderLayout.WEST);
		add(middlePanel_,BorderLayout.CENTER);
		add(rightPanel_,BorderLayout.EAST);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// 获取以及判断源对象
		Object object = e.getSource();
		if (object == addButton_) {
			actionOnAddClicked();
		} else if (object == removeButton_) {
			actionOnRemoveClicked();
		} else if (object == parseButton_) {
			actionOnParseClicked();
		} else {
			throw new InvalidParameterException();
		}
	}
	private void actionOnParseClicked() {
		// 点击转换按钮
		
	}
	private void actionOnRemoveClicked() {
		// 点击删除文件按钮
		
	}
	private void actionOnAddClicked() {
		// 点击添加文件按钮
		
	}
}
