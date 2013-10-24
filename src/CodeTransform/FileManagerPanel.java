package CodeTransform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class FileManagerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private FileTree tree_;
	private FileListPanel fileListPanel_;

	public FileManagerPanel() throws FileNotFoundException {
		setLayout(new BorderLayout());

		// 创建根节点
		tree_ = new FileTree();
		JScrollPane scrollPanel = new JScrollPane(tree_);
		scrollPanel.setPreferredSize(new Dimension(250, 600));
		add(scrollPanel, BorderLayout.WEST);

		fileListPanel_ = new FileListPanel(tree_);
		fileListPanel_.setPreferredSize(new Dimension(500, 600));
		add(fileListPanel_, BorderLayout.EAST);

	}

	class FileTree extends JTree implements MouseListener {
		/**
		 * 序列化校验
		 */
		private static final long serialVersionUID = 1L;
		public static final boolean DIRECTORY_AND_FILE = true;// 含有文件节点
		public static final boolean DIRECTORY_NO_FILE = false;// 不含有文件节点
		private static final String DESKTOP_EN = "Desktop";
		private static final String DESKTOP_ZH = "我的电脑";
		private static final String DISK_ZH = "磁盘";
		private JFileTreeNode systemNode = null;
		private JFileTreeNode rootNode;
		private DefaultTreeModel jFileTreeModel;
		private boolean model;// 显示文件或不显示文件

		public FileTree() throws FileNotFoundException {
			this(DIRECTORY_AND_FILE);
		}

		public FileTree(boolean model) {
			this(null, model);
		}

		public FileTree(File file) {
			this(file, DIRECTORY_AND_FILE);
		}

		public FileTree(File file, boolean model) {
			this.model = model;

			// 建立默认系统文件树
			if (file == null || !file.exists()) {
				file = new File(System.getProperty("user.home")
						+ File.separator + DESKTOP_EN);
				if (!file.exists()) {
					file = new File(System.getProperty("user.home")
							+ File.separator + DESKTOP_ZH);
				}
				rootNode = systemNode = new JFileTreeNode(file);
			} else {
				rootNode = new JFileTreeNode(file);
			}
			rootNode.expand();
			jFileTreeModel = new DefaultTreeModel(rootNode);
			setModel(jFileTreeModel);
			addTreeExpansionListener(new JTreeExpansionListener());
			addMouseListener(this);
		}

		public String getPathName(TreePath path) {
			Object o = path.getLastPathComponent();
			if (o instanceof JFileTreeNode) {
				return ((JFileTreeNode) o).file.getAbsolutePath();
			}
			return null;
		}

		/**
		 * 文件树节点
		 */
		protected class JFileTreeNode extends DefaultMutableTreeNode {
			/**
			 * 序列化校验
			 */
			private static final long serialVersionUID = 1L;
			protected File file;
			protected boolean isDirectory;

			public JFileTreeNode(File file) {
				this.file = file;
				isDirectory = file.isDirectory();
				setUserObject(file);
			}

			@Override
			public boolean isLeaf() {
				if (file == null) {
					return false;
				}
				return !isDirectory;
			}

			@Override
			public String toString() {
				if (this.equals(systemNode)) {
					return DESKTOP_ZH;
				} else if (file.getParentFile() == null) {
					return DISK_ZH + "(" + file.getPath().substring(0, 2) + ")";
				} else {
					return file.getName();
				}
			}

			@Override
			public boolean getAllowsChildren() {
				return isDirectory;
			}

			/**
			 * 节点展开
			 * 
			 * @return 展开是否含有子节点
			 */
			public boolean expand() {
				this.removeAllChildren();
				if (this.equals(systemNode)) {
					// 获得电脑中的盘符
					File[] roots = File.listRoots();
					for (int i = 0; i < roots.length; i++) {
						if (roots[i].exists()) {
							this.add(new JFileTreeNode(roots[i]));
						}
					}
				} else {
					File[] files = file.listFiles();
					if (files == null) {
						return false;
					}
					for (int i = 0; i < files.length; i++) {
						File f = files[i];
						/**
						 * 添加文件夹 文件则受系统文件树model控制
						 */
						if (f.isDirectory() || model) {
							if (!f.isHidden()) {
								this.add(new JFileTreeNode(f));
							}
						}
					}
				}
				return true;
			}
		}

		protected class JTreeExpansionListener implements TreeExpansionListener {
			public void treeExpanded(TreeExpansionEvent e) {
				JFileTreeNode fileNode = (JFileTreeNode) e.getPath()
						.getLastPathComponent();
				if (fileNode != null) {
					// 展开后台线程
					new FileNodeExpansion(fileNode, jFileTreeModel).execute();
				}
			}

			public void treeCollapsed(TreeExpansionEvent e) {
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree_
					.getLastSelectedPathComponent();
			if (node == null)
				return;
			fileListPanel_.removeAllFiles();
			Object nodeInfo = node.getUserObject();
			File file = new File(nodeInfo.toString());
			if (file.isFile()) {
				fileListPanel_.addFile(file);
			}
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (!files[i].isHidden()) {
						fileListPanel_.addFile(files[i]);
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}

		public void expandTree(File path) {
			if (path == null) {
				throw new IllegalArgumentException();
			}
			tree_.expandRow(getMinSelectionRow());

		}
	}
}
