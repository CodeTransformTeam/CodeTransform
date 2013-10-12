package CodeTransform;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FileList extends JPanel{
	/**
	 * 这个东西是给 序列化 校验用的
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> list_;
	private DefaultListModel<String> model_ = new DefaultListModel<String>();
	public FileList() {
		// TODO Auto-generated constructor stub
		list_ = new JList<String>(model_);
		JScrollPane scrollPane = new JScrollPane(list_);
		scrollPane.setPreferredSize(new Dimension(330, 400));
		this.add(scrollPane);
		this.setVisible(true);
	}
	
	public void addFile(String fileName) {
		// TODO Auto-generated constructor stub
		model_.addElement(fileName);
	}
	public void removeFile() {
		model_.removeAllElements();
	}
}
