package CodeTransform;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FileListPanel extends JPanel{
	/**
	 * 这个东西是给 序列化 校验用的
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> list_;
	private DefaultListModel<String> model_ = new DefaultListModel<String>();
	public FileListPanel() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		list_ = new JList<String>(model_);
		JScrollPane scrollPanel = new JScrollPane(list_);
		scrollPanel.setPreferredSize(new Dimension(500, 600));
		this.add(scrollPanel);
		
	}
	
	public void addFile(String fileName) {
		model_.addElement(fileName);
	}
	public void removeFile() {
		model_.removeAllElements();
	}
}
