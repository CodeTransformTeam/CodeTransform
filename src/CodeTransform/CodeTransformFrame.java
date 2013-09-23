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
	private JPanel leftPanel = new JPanel();
	private JPanel middlePanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JButton add_button = new JButton("添加文件");
	private JButton remove_button = new JButton("删除文件");		
	private JButton parse_button = new JButton("转换");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox transformSelection = new JComboBox(new Object[]{"html5","xhtml 1.0","html 4.01"});
	
	public CodeTransformFrame() throws Exception {
		// 对frame进行初始化
		super("代码转换器");
		setSize(500,350);
		setLocation(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		leftPanel.add(new JTextArea(15,15));
		
		middlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		middlePanel.add(add_button);
		middlePanel.add(remove_button);
		
		JPanel rightTopPanel = new JPanel();
		JPanel rightBottomPanel = new JPanel();
		JLabel jlbTransform = new JLabel("转换为");
		JLabel jlbSave = new JLabel("存储在");
		
		rightTopPanel.setBorder(new TitledBorder("选项"));
		rightTopPanel.setLayout(new GridLayout(2,1));
		rightTopPanel.add(jlbTransform);
		rightTopPanel.add(transformSelection);
		rightTopPanel.add(jlbSave);
		rightBottomPanel.add(parse_button);
		
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(rightTopPanel,BorderLayout.NORTH);
		rightPanel.add(rightBottomPanel,BorderLayout.SOUTH);
		
		setLayout(new BorderLayout());
		add(leftPanel,BorderLayout.WEST);
		add(middlePanel,BorderLayout.CENTER);
		add(rightPanel,BorderLayout.EAST);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// 获取以及判断源对象
		Object object = e.getSource();
		if (object == add_button) {
			actionOnAddClicked();
		} else if (object == remove_button) {
			actionOnRemoveClicked();
		} else if (object == parse_button) {
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
