package CodeTransform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

public class filePanel extends JPanel implements ActionListener{
	/**这个东西是给 序列化 校验用的,使用JList,
	 * 出于考虑很少实现ListModel中的所有的方法比较麻烦，并且很少使用addListDataListener(ListDataListener l)、	
	 * removeListDataListener(ListDataListener l)方法，所以通过继承抽象类AbstractListModel.
	 */
	private static final long serialVersionUID = 1L;
	private JButton addButton_ = new JButton("添加文件");
	private JButton removeButton_ = new JButton("删除文件");	
	private JPanel rightPanel_ = new JPanel();
	private JPanel leftPanel_ = new JPanel();
	public filePanel(){
		ListModel<String> model = new dataModel();
		JList<String> list = new JList<String>(model);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(180, 100));
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
		JFileChooser fileName = new JFileChooser();
		if(fileName.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
		}
	}
	private void actionOnRemoveClicked() {
		// TODO Auto-generated method stub
		
	}
}

class dataModel extends AbstractListModel<String>{
	//这个东西是给 序列化 校验用的
	private static final long serialVersionUID = 1L;

	@Override
	public String getElementAt(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}}
