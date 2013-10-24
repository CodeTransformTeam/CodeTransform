package CodeTransform;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

@SuppressWarnings("unused")
public class SettingFrame extends JFrame implements ItemListener, TableModel, ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton singleTransformButton_;
	private JButton multiTransformButton_;
	
	private CodeParser parser_;
	private File file_;
	private JTable table_;

	public SettingFrame(File file) {
		super("单文件转换设定");
		setSize(400, 300);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new BorderLayout(10, 10));

		file_ = file;

		String prefix = getFilePrefix(file);
		if (prefix.equalsIgnoreCase(".cpp") || prefix.equalsIgnoreCase(".c")
				|| prefix.equalsIgnoreCase(".h")) {
			parser_ = new CppParser();
		} else if (prefix.equalsIgnoreCase(".java")) {
			parser_ = new JavaParser();
		}

		createNorthPanel();
		createCenterPanel();
		createSouthPanel();
	}

	public SettingFrame(File[] files) {
		super("多文件转换设定");
		setSize(600, 450);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		JTable table = new JTable(this);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setEnabled(true);
		table.setRowHeight(40);
		add(scrollPane, BorderLayout.CENTER);
		table_ = table;
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		southPanel.setBackground(Color.LIGHT_GRAY);
		add(southPanel, BorderLayout.SOUTH);
		
		JLabel label = new JLabel("关键字颜色：");
		southPanel.add(label);
		
		JTextField textField = new JTextField("#000000");
		textField.setPreferredSize(new Dimension(80, 30));
		southPanel.add(textField);
		
		label = new JLabel("字体名称：");
		southPanel.add(label);
		
		textField = new JTextField("sans-serif");
		textField.setPreferredSize(new Dimension(80, 30));
		southPanel.add(textField);
		
		label = new JLabel("字体大小：");
		southPanel.add(label);
		
		textField = new JTextField("medium");
		textField.setPreferredSize(new Dimension(80, 30));
		southPanel.add(textField);
		
		JButton transformButton = new JButton("转换");
		Dimension dimension = transformButton.getPreferredSize();
		transformButton.setPreferredSize(new Dimension(dimension.width, 30));
		transformButton.addActionListener(this);
		southPanel.add(transformButton);
		multiTransformButton_ = transformButton;
		
	}

	private void createNorthPanel() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
		northPanel.setPreferredSize(new Dimension(400, 90));
		// northPanel.setBackground(Color.gray);
		add(northPanel, BorderLayout.NORTH);

		JPanel blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(10, 30));
		northPanel.add(blankPanel);

		JLabel label = new JLabel("源文件：");
		label.setPreferredSize(new Dimension(75, 30));
		northPanel.add(label);

		JTextField textField = new JTextField(file_.getPath());
		textField.setPreferredSize(new Dimension(300, 30));
		textField.setEditable(false);
		northPanel.add(textField);

		blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(10, 30));
		northPanel.add(blankPanel);

		label = new JLabel("目标文件：");
		label.setPreferredSize(new Dimension(75, 30));
		northPanel.add(blankPanel);
		northPanel.add(label);

		textField = new JTextField(file_.getPath() + ".htm");
		textField.setPreferredSize(new Dimension(300, 30));
		northPanel.add(textField);
	}

	private void createCenterPanel() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
		// centerPanel.setBackground(Color.lightGray);
		add(centerPanel, BorderLayout.CENTER);

		JPanel blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(10, 30));
		centerPanel.add(blankPanel);

		JLabel label = new JLabel("颜色设定：");
		label.setPreferredSize(new Dimension(75, 30));
		centerPanel.add(label);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("请选择");
		comboBox.setPreferredSize(new Dimension(100, 25));
		comboBox.addItemListener(this);
		centerPanel.add(comboBox);

		String[] optionStrings = parser_.getOptionItems();
		if (optionStrings != null) {
			for (int i = 0; i < optionStrings.length; i++) {
				comboBox.addItem(optionStrings[i]);
			}
		}

		blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(20, 30));
		centerPanel.add(blankPanel);

		label = new JLabel("颜色值：");
		label.setPreferredSize(new Dimension(60, 30));
		centerPanel.add(label);

		JTextField textField = new JTextField("#000000");
		textField.setPreferredSize(new Dimension(120, 30));
		centerPanel.add(textField);

		blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(10, 30));
		centerPanel.add(blankPanel);

		label = new JLabel("字体设定：");
		label.setPreferredSize(new Dimension(75, 30));
		centerPanel.add(label);

		comboBox = new JComboBox<String>();
		comboBox.addItem("请选择");
		comboBox.setPreferredSize(new Dimension(100, 25));
		comboBox.addItemListener(this);
		centerPanel.add(comboBox);
		if (optionStrings != null) {
			for (int i = 0; i < optionStrings.length; i++) {
				comboBox.addItem(optionStrings[i]);
			}
		}

		blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(20, 30));
		centerPanel.add(blankPanel);

		label = new JLabel("名称：");
		label.setPreferredSize(new Dimension(60, 30));
		centerPanel.add(label);

		textField = new JTextField("sans-serif");
		textField.setPreferredSize(new Dimension(120, 30));
		centerPanel.add(textField);

		blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(205, 30));
		centerPanel.add(blankPanel);

		label = new JLabel("大小：");
		label.setPreferredSize(new Dimension(60, 30));
		centerPanel.add(label);

		textField = new JTextField("medium");
		textField.setPreferredSize(new Dimension(120, 30));
		centerPanel.add(textField);
	}

	private void createSouthPanel() {
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		// southPanel.setBackground(Color.black);
		add(southPanel, BorderLayout.SOUTH);

		singleTransformButton_ = new JButton("转换");
		singleTransformButton_.setPreferredSize(new Dimension(100, 30));
		singleTransformButton_.addActionListener(this);
		southPanel.add(singleTransformButton_);
	}

	private String getFilePrefix(File file) {
		String fileNameString = file.getName();
		int index = fileNameString.lastIndexOf(".");
		if (index < 0) {
			throw new IllegalArgumentException();
		}

		String prefix = fileNameString.substring(index);

		return prefix;
	}

	public static void main(String[] args) {
		try {
			File[] files = new File[2];
			SettingFrame frame = new SettingFrame(files);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		System.out.println("SettingFrame.itemStateChanged()");
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		System.out.println("SettingFrame.addTableModelListener()");
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int index) {
		String[] titleStrings = {"源文件", "目标文件"};
		return titleStrings[index];
	}

	@Override
	public int getRowCount() {
		return 100;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return "OK";
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		if (arg1 == 1) {
			return true;
		}
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.singleTransformButton_) {
			System.out.println("单文件转换");
		} else if (e.getSource() == this.multiTransformButton_) {
			System.out.println("多文件转换");
		}
 	}
}
