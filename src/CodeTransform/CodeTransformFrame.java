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
	private JButton choosePathButton_ = new JButton("选择路径");
	private JPanel rightTopPanel = new JPanel();
	private JComboBox<String> transformSelection_ = new JComboBox<String>(new String[]{"html5","xhtml 1.0","html 4.01"});
	
	public CodeTransformFrame() throws Exception {
		// 对frame进行初始化
		super("代码转换器");
		setSize(520,350);
		setLocation(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		JPanel rightBottomPanel = new JPanel();
		
		JLabel transformAsLabel = new JLabel("转换为");
		JLabel saveToLabel = new JLabel("存储在");
		
		rightTopPanel.setBorder(new TitledBorder("选项"));
		rightTopPanel.setPreferredSize(new Dimension(180,100));
		rightTopPanel.add(transformAsLabel);
		rightTopPanel.add(transformSelection_);
		rightTopPanel.add(saveToLabel);
		rightTopPanel.add(choosePathButton_);
		rightBottomPanel.add(parseButton_);
		
		rightPanel_.setLayout(new BorderLayout());
		rightPanel_.add(rightTopPanel,BorderLayout.NORTH);
		rightPanel_.add(rightBottomPanel,BorderLayout.SOUTH);
		
		setLayout(new BorderLayout());
		add(leftPanel_,BorderLayout.WEST);
		add(rightPanel_,BorderLayout.EAST);
		
		parseButton_.addActionListener(this);
		choosePathButton_.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object object = e.getSource();
		if (object == parseButton_) {
			actionOnParseClicked();
		} 
		else if (object == choosePathButton_){
			actionOnChooseClicked();
		}
		else {
			throw new InvalidParameterException();
		}
	}
	
	private void actionOnParseClicked() {
		// 点击“转换”按钮
		if(leftPanel_.isFileAdded()){
			//如果文件已经添加
			/**
			 * 先判断是否已经选择了存储路径，如果没有选择，则为默认路径
			 * 再根据下列代码所获取的文件列表信息进行相应的转换操作。
			 */
			// 获取文件列表
			String[] array = leftPanel_.getList();
			for(int i=0;i<leftPanel_.getList().length;i++) {
				System.out.println(array[i]);
			}
			
		}
		else {
			//如果列表中没有文件，则利用文件对话框提醒
			JOptionPane.showMessageDialog(null, "请添加文件");
		}
	}
	
	private void actionOnChooseClicked() {
		// 点击“选择路径”按钮
		JFileChooser file = new JFileChooser();
		if(file.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			rightTopPanel.repaint();
			rightTopPanel.remove(choosePathButton_);
		}
		
	}
}
