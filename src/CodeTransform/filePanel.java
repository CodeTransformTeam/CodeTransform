package CodeTransform;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;


public class FilePanel extends JPanel implements TreeExpansionListener{
	/**
	 * 这个东西是给 序列化 校验用的
	 */
	private static final long serialVersionUID = 1L;
	private JPanel leftPanel_ = new JPanel();
	private JPanel rightPanel_ = new JPanel();
	private JTree root;
	private DefaultMutableTreeNode treeNode,selectNode;
	private String ROOT_NAME = "我的电脑";
	public FilePanel(){
		setLayout(new BorderLayout());
		add(leftPanel_,BorderLayout.WEST);
		add(rightPanel_,BorderLayout.EAST);
		
		/**
		 * 创建根节点 
		 */
		root = new JTree(createRootNode());
		leftPanel_.add(root);
		
		root.addTreeExpansionListener(this);
	}
	@SuppressWarnings("static-access")
	private DefaultMutableTreeNode createRootNode() {
		// TODO Auto-generated method stub
		File dir = new File(".");
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(ROOT_NAME);
		for(int i=0;i<dir.listRoots().length;i++) {
			if(dir.listRoots()[i].isDirectory()) {
				String rootPath = dir.listRoots()[i].getPath(); //将抽象路径转换为一个路径名字符串
				treeNode = new DefaultMutableTreeNode(rootPath);
				rootNode.add(treeNode);
				treeNode = null;
			}
		}
		return rootNode;
	}
	@Override
	public void treeCollapsed(TreeExpansionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void treeExpanded(TreeExpansionEvent e) {
		// TODO Auto-generated method stub
		selectNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
		String path = e.getPath().toString();
//		System.out.println(path);
		File file = new File(path);
		addTreeNode(selectNode,file);
	}
	private void addTreeNode(DefaultMutableTreeNode node, File file) {
		// TODO Auto-generated method stub
		if(node == null && file == null) {
			return;
		}
		if(!file.isDirectory()) {
			return;
		}
		if(!node.isRoot()) {}
	}
}

