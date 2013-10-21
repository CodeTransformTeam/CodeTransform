package CodeTransform;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;
import javax.swing.tree.DefaultTreeModel;

import CodeTransform.FileManagerPanel.FileTree;
import CodeTransform.FileManagerPanel.FileTree.JFileTreeNode;

/**
 * 展开节点的后台线程
 */

public class FileNodeExpansion extends SwingWorker<Boolean, Void> { 
    private JFileTreeNode node;
    private DefaultTreeModel jFileTreeModel;
    
    public FileNodeExpansion(JFileTreeNode node,
			DefaultTreeModel jFileTreeModel) {
    	this.node = node;
    	this.jFileTreeModel = jFileTreeModel;
	}

	@Override
    protected Boolean doInBackground() throws Exception {
        return node.expand();
    }

    @Override
    protected void done() {
        try {
            //节点可以展开
            if (get()) {
                jFileTreeModel.reload(node);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FileTree.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FileTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
