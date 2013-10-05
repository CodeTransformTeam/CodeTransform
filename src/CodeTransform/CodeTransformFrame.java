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
	
	private filePanel leftPanel_ = new filePanel();
	private JPanel rightPanel_ = new JPanel();	
	private JButton parseButton_ = new JButton("转换");

	private JComboBox<String> transformSelection_ = new JComboBox<String>(new String[]{"html5","xhtml 1.0","html 4.01"});
	
	public CodeTransformFrame() throws Exception {
		// 对frame进行初始化
		super("代码转换器");
		setSize(500,350);
		setLocation(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
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
		add(rightPanel_,BorderLayout.EAST);
		
		parseButton_.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object object = e.getSource();
		if (object == parseButton_) {
			actionOnParseClicked();
		} else {
			throw new InvalidParameterException();
		}
	}
	private void actionOnParseClicked() {
		// 点击转换按钮
		if(leftPanel_.isFileAdded()){
			//如果文件已经添加
			
			
		}
		else {
			//如果列表中没有文件，则利用文件对话框提醒
			JOptionPane.showMessageDialog(null, "请添加文件");
		}
	}
}
