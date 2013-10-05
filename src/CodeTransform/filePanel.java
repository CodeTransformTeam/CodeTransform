package CodeTransform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class filePanel extends JPanel implements   ActionListener{
	/**
	 * 这个东西是给 序列化 校验用的
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> list_;
	private JButton addButton_ = new JButton("添加文件");
	private JButton removeButton_ = new JButton("删除文件");	
	private JPanel rightPanel_ = new JPanel();
	private JPanel leftPanel_ = new JPanel();
	private DefaultListModel<String> model = new DefaultListModel<String>();
	public filePanel(){
		list_ = new JList<String>(model);
		JScrollPane scrollPane = new JScrollPane(list_);
		scrollPane.setPreferredSize(new Dimension(180, 250));
		leftPanel_.add(scrollPane);
		rightPanel_.setLayout(new GridLayout(10,1));
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
		// TODO Auto-generated method stub
		JFileChooser file = new JFileChooser();
		if(file.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			String fileAbsolutePath = file.getSelectedFile().toString();
			String fileDirectory = file.getCurrentDirectory().toString()+"\\";
			String fileName = fileAbsolutePath.replace(fileDirectory,"");
			if(model.isEmpty()) {
				((DefaultListModel<String>) model).addElement(fileName);
			}
			else {
//				System.out.println(fileName);
				if(!fileExist(fileName)) {
					//如果文件名没有重复
					((DefaultListModel<String>) model).addElement(fileName);
				}
				else {
					//如果文件名有重复
					JOptionPane.showMessageDialog(null,"已选");
				}
			}
		}
	}
	private boolean fileExist(String fileName) {
		// TODO Auto-generated method stub
		for(int i=0 ; i<model.getSize(); i++) {
			if(fileName.equals(model.get(i))) {
				return true;
			}
		}
		return false;
	}
	private void actionOnRemoveClicked() {
		// TODO Auto-generated method stub
		if(!list_.isSelectionEmpty()) {
			//如果列表中有选项被选中
			int index = list_.getSelectedIndex();
			model.remove(index);
		}
	}
	
	public boolean isFileAdded() {
		// TODO Auto-generated method stub
		if(model.isEmpty()) return false;
		return true;
	}
	
	
}

