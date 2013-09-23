package CodeTransform;

public class CodeTransformApp{

	private CodeTransformFrame frame_;
	
	public CodeTransformApp() throws Exception {
		frame_ = new CodeTransformFrame();
	}
	
	/**
	 * 运行程序
	 */
	public void run() {
		frame_.setVisible(true);
	}
	
	public static void main(String[] args) {
		try {
			CodeTransformApp app = new CodeTransformApp();
			app.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
