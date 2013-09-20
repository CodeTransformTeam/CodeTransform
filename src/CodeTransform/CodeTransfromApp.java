package CodeTransform;
public class CodeTransfromApp {

	private CodeTransformFrame frame_;
	
	public CodeTransfromApp() throws Exception {
		frame_ = new CodeTransformFrame();
	}
	
	public void run() {
		frame_.setVisible(true);
	}
	
	public static void main(String[] args) {
		try {
			CodeTransfromApp app = new CodeTransfromApp();
			app.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
