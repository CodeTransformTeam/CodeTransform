package CodeTransform;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

public class FileListPanel extends JPanel implements MouseListener, ActionListener{

	private static final long serialVersionUID = 1L;
	private JList<String> list_;
	private DefaultListModel<String> model_;
	private ArrayList<File> fileArrayList_;
	private JPopupMenu popupMenu_;
	private JMenuItem menuItem_;
	
	public FileListPanel() {
		model_ = new DefaultListModel<String>();
		fileArrayList_ = new ArrayList<File>();
		
		menuItem_ = new JMenuItem("转换");
		menuItem_.addActionListener(this);
		
		popupMenu_ = new JPopupMenu();
		popupMenu_.add(menuItem_);
		
		setLayout(new BorderLayout(0, 0));

		FileListCellRenderer fileListCellRenderer = new FileListCellRenderer();
		fileListCellRenderer.setFileList(fileArrayList_);
		
		list_ = new JList<String>(model_);
		list_.setCellRenderer(fileListCellRenderer);
		list_.addMouseListener(this);
		
		JScrollPane scrollPanel = new JScrollPane(list_);
		this.add(scrollPanel, BorderLayout.CENTER);
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

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			int index = list_.locationToIndex(e.getPoint());
			list_.setSelectedIndex(index);
			
			if (isKnownFile(fileArrayList_.get(index))) {
				popupMenu_.show(list_, e.getX(), e.getY());
			}
		}
	}

	private boolean isKnownFile(File file) {
		String fileNameString = file.getName();
		int index = fileNameString.lastIndexOf(".");
		if (index < 0) {
			return false;
		}
		
		String prefix=fileNameString.substring(index);
		String[] knownPrefixStrings = {".java", ".h", ".cpp", ".c"};
		for (int i = 0; i < knownPrefixStrings.length; i++) {
			if (knownPrefixStrings[i].equalsIgnoreCase(prefix)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			int index = list_.locationToIndex(e.getPoint());
			list_.setSelectedIndex(index);
			
			if (isKnownFile(fileArrayList_.get(index))) {
				popupMenu_.show(list_, e.getX(), e.getY());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.menuItem_) {
			System.out.println("用户点击了 转换菜单");
		}
	}
}
