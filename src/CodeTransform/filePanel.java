package CodeTransform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;

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
	private ArrayList<String> filePath = new ArrayList<String>();
	private JButton addButton_ = new JButton("添加文件");
	private JButton removeButton_ = new JButton("删除文件");	
	private JPanel rightPanel_ = new JPanel();
	private JPanel leftPanel_ = new JPanel();
	private JLabel tips_ = new JLabel("<html><br><br><br><br>注意：请选择以.c或<br>.java为后缀的文件</html>");
	private DefaultListModel<String> model_ = new DefaultListModel<String>();
	private String fileName_;
	public filePanel(){
		list_ = new JList<String>(model_);
		JScrollPane scrollPane = new JScrollPane(list_);
		scrollPane.setPreferredSize(new Dimension(180, 250));
		leftPanel_.add(scrollPane);
		rightPanel_.setPreferredSize(new Dimension(120,40));
		rightPanel_.add(tips_);
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
		// 点击“添加”按钮，在添加过程中可进行多个文件的选择
		JFileChooser file = new JFileChooser();
		file.setMultiSelectionEnabled(true);
		if(file.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			for(int i=0;i<file.getSelectedFiles().length;i++) {
				File[] temp = file.getSelectedFiles();
				String fileAbsolutePath = temp[i].toString();
				String filePreviousDirectory = file.getCurrentDirectory().toString()+"\\";
				setFileName(fileAbsolutePath.replace(filePreviousDirectory,""));
				if(!fileExist(fileName_)) {
					//如果文件名没有重复
					model_.addElement(fileName_);
					// 将每个文件的路径存入ArrayList类中
					filePath.add(fileAbsolutePath);
				}
				else {
					//如果文件名有重复
					JOptionPane.showMessageDialog(null,fileName_+"已选");
				}
			}
		}
	}
	
	public ArrayList<String> getFilePath() {
		// 获取每个文件的路径
		return filePath;
	}
	
	private boolean fileExist(String fileName_) {
		// 判断选中的文件是否已经在列表中
		for(int i=0 ; i<model_.getSize(); i++) {
			if(fileName_.equals(model_.get(i))) {
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
			model_.remove(index);
		}
	}
	
	public void setFileName(String fileName_) {
		// 设置文件的名字
		this.fileName_ = fileName_;
	}
		
	public boolean isFileAdded() {
		// 判断文件是否已经被添加
		if(model_.isEmpty()) return false;
		return true;
	}
	
	public String[] getList() {
		// 将文件名列表复制到一个数组中，方便以后调用
		String[] array = new String[model_.getSize()];
		model_.copyInto(array);
		return array;
	}
	
	
}

