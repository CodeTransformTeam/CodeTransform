package CodeTransform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.InvalidParameterException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class filePanel extends JPanel implements ActionListener{
	/**
	 * 这个东西是给 序列化 校验用的
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> list_;
	private JButton addButton_ = new JButton("添加文件");
	private JButton removeButton_ = new JButton("删除文件");	
	private JPanel rightPanel_ = new JPanel();
	private JPanel leftPanel_ = new JPanel();
	private JLabel tips = new JLabel("<html><br><br><br><br>注意：请选择以.c或<br>.java为后缀的文件</html>");
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private String fileName;
	public filePanel(){
		list_ = new JList<String>(model);
		JScrollPane scrollPane = new JScrollPane(list_);
		scrollPane.setPreferredSize(new Dimension(180, 250));
		leftPanel_.add(scrollPane);
		rightPanel_.setPreferredSize(new Dimension(120,40));
		rightPanel_.add(tips);
		rightPanel_.add(addButton_);
		rightPanel_.add(removeButton_);
		
		this.setLayout(new BorderLayout());
		this.add(leftPanel_,BorderLayout.WEST);
		this.add(rightPanel_,BorderLayout.EAST);
		//为按钮注册监听事件
		addButton_.addActionListener(this);
		removeButton_.addActionListener(this); 
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object object  = e.getSource();
		if(object == addButton_) {
			actionOnAddClicked();
		}
		else if(object == removeButton_) {
			actionOnRemoveClicked();
		}
		else {
			throw new InvalidParameterException();
		}	
	}
	private void actionOnAddClicked() {
		// 点击“添加”按钮
		JFileChooser file = new JFileChooser();
		if(file.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			String fileAbsolutePath = file.getSelectedFile().toString();
			String filePreviousDirectory = file.getCurrentDirectory().toString()+"\\";
			setFileName(fileAbsolutePath.replace(filePreviousDirectory,""));
			if(!fileExist(fileName)) {
				//如果文件名没有重复
				((DefaultListModel<String>) model).addElement(fileName);
				/**
				 * 以下注释部分为获取文件的类型
				 * */
//				File temp = new File(fileAbsolutePath);
//				String fileTypeName = file.getTypeDescription(temp);
//				System.out.println(fileTypeName);
			}
			else {
				//如果文件名有重复
				JOptionPane.showMessageDialog(null,"已选");
			}
		}
	}
	private boolean fileExist(String fileName) {
		// 判断选中的文件是否已经在列表中
		for(int i=0 ; i<model.getSize(); i++) {
			if(fileName.equals(model.get(i))) {
				return true;
			}
		}
		return false;
	}
	private void actionOnRemoveClicked() {
		// 点击“移除”按钮
		if(!list_.isSelectionEmpty()) {
			//如果列表中有选项被选中
			int index = list_.getSelectedIndex();
			model.remove(index);
		}
	}
	
	public void setFileName(String fileName) {
		// 设置文件的名字
		this.fileName = fileName;
	}
		
	public boolean isFileAdded() {
		// 判断文件是否已经被添加
		if(model.isEmpty()) return false;
		return true;
	}
	
	public String[] getList() {
		// 将文件列表复制到一个数组中，方便以后调用
		String[] array = new String[model.getSize()];
		model.copyInto(array);
		return array;
	}
}

