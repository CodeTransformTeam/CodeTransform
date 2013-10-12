package CodeTransform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class FileManager extends JPanel{
	/**
	 * 这个东西是给 序列化 校验用的
	 */
	private static final long serialVersionUID = 1L;
	private JPanel leftPanel_ = new JPanel();
	private JPanel rightPanel_ = new JPanel();
	private FileTree tree;
	private FileList fileList;
	public FileManager() throws FileNotFoundException{
		setLayout(new BorderLayout());
		add(leftPanel_,BorderLayout.WEST);
		add(rightPanel_,BorderLayout.EAST);
		/**
		 * 创建根节点 
		 */
		tree = new FileTree();
		JScrollPane scrollPanel = new JScrollPane(tree);
		scrollPanel.setPreferredSize(new Dimension(220,400));
		leftPanel_.add(scrollPanel);
		
		fileList = new FileList();
		rightPanel_.add(fileList);
	}
	class FileTree extends JTree implements TreeSelectionListener{
		/**
		 * 序列化校验
		 */
		private static final long serialVersionUID = 1L;
		public static final boolean DIRECTORY_AND_FILE = true;//含有文件节点
		public static final boolean DIRECTORY_NO_FILE = false;//不含有文件节点
		private static final String DESKTOP_EN = "Desktop";
		private static final String DESKTOP_ZH = "我的电脑";
//	    private static final String DISK_EN = "Disk";
	    private static final String DISK_ZH = "磁盘";
	    private JFileTreeNode systemNode = null;
	    private JFileTreeNode rootNode;
	    private DefaultTreeModel jFileTreeModel;
	    private boolean model;//显示文件或不显示文件
	    
	    public FileTree() throws FileNotFoundException {
	        this(DIRECTORY_AND_FILE);
	    }
	    
	    public FileTree(boolean model) {
	        this(null, model);        
	    }

	    public FileTree(File file) {
	       // this(file, DIRECTORY_AND_FILE);
	    }
	    
	    public FileTree(File file, boolean model) {
	        this.model = model;
//	        putClientProperty("JTree.lineStyle", "Angled");
	        //建立默认系统文件树 
	        if (file == null || !file.exists()) {
	            file = new File(System.getProperty("user.home") + File.separator + DESKTOP_EN);
	            if (!file.exists()) {
	                file = new File(System.getProperty("user.home") + File.separator + DESKTOP_ZH);
	            }
	            rootNode = systemNode = new JFileTreeNode(file);
	        } else {
	            rootNode = new JFileTreeNode(file);
	        }
	        rootNode.expand();
	        jFileTreeModel = new DefaultTreeModel(rootNode);
	        setModel(jFileTreeModel);
	        addTreeExpansionListener(new JTreeExpansionListener());
	        addTreeSelectionListener(this);
	    }

	    public String getPathName(TreePath path) {
	        Object o = path.getLastPathComponent();
	        if (o instanceof JFileTreeNode) {
	            return ((JFileTreeNode) o).file.getAbsolutePath();
	        }
	        return null;
	    }
	    @Override
		public void valueChanged(TreeSelectionEvent arg0) {
			// TODO Auto-generated method stub
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();
			if (node == null) return;
			fileList.removeFile();
			Object nodeInfo = node.getUserObject();
			File file = new File(nodeInfo.toString());
			if(file.isDirectory()) {
				File[] files = file.listFiles();
				for(int i = 0;i<files.length;i++) {
					if(!files[i].isHidden()) {
						fileList.addFile(files[i].getName());
					}
				}
			}
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
	        	}
	        	else if (file.getParentFile() == null) {
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
	            }
	            else{
	            	File[] files = file.listFiles();
	            	if (files == null) {
	            		return false;
	            	}
	            	for (int i = 0; i < files.length; i++) {
	                	File f = files[i];
	                	/**
	                 	* 添加文件夹
	                 	* 文件则受系统文件树model控制
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

	    protected class JTreeExpansionListener implements TreeExpansionListener{
	        public void treeExpanded(TreeExpansionEvent e) {
	            JFileTreeNode fileNode = (JFileTreeNode) e.getPath().getLastPathComponent();
	            if (fileNode != null) {
	                // 展开后台线程
	            	new FileNodeExpansion(fileNode,jFileTreeModel).execute();
	            }            
	        }

	        public void treeCollapsed(TreeExpansionEvent e) {
	        }
	    }

	}
}

