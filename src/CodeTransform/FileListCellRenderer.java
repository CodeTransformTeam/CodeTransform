package CodeTransform;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFileChooser;
import javax.swing.JList;


public class FileListCellRenderer extends DefaultListCellRenderer {
	
	private static final long serialVersionUID = 1L;
	
	private JFileChooser fileChooser_;
	private ArrayList<File> fileArrayList_;

	public FileListCellRenderer() {
		fileChooser_ = new JFileChooser();
	}
	
	public void setFileList(ArrayList<File> fileArrayList) {
		fileArrayList_ = fileArrayList;
	}
	
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		if (fileArrayList_ != null) {
			File file = new File(fileArrayList_.get(index).getPath());
			if (file.exists()) {
				super.setIcon(fileChooser_.getIcon(file));
			} else {
				super.setIcon(null);
			}
		}
		
		setText(value.toString());
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		return this;
	}

}
