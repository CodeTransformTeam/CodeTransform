package CodeTransform;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;

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
	private DefaultListModel<String> model_;
	private ArrayList<File> fileArrayList_;
	
	public FileListPanel() {
		model_ = new DefaultListModel<String>();
		fileArrayList_ = new ArrayList<File>();
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		FileListCellRenderer fileListCellRenderer = new FileListCellRenderer();
		fileListCellRenderer.setFileList(fileArrayList_);
		
		list_ = new JList<String>(model_);
		list_.setCellRenderer(fileListCellRenderer);
		JScrollPane scrollPanel = new JScrollPane(list_);
		scrollPanel.setPreferredSize(new Dimension(500, 600));
		this.add(scrollPanel);
	}
	
	public void addFile(File file) {
		fileArrayList_.add(file);
		
		String fileName = file.getName();
		model_.addElement(fileName);
	}
	
	public void removeFile() {
		fileArrayList_.clear();
		model_.removeAllElements();
	}
}
