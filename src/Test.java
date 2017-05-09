import java.util.regex.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Test extends JFrame implements ActionListener {
	private JTextField txtInput;
	private JTextField txtRequest;
	private JTextField txtAddLabel;
	private JTextField txtDelete;
	private JButton btnConfirm;
	private JButton btnRequest;
	private JButton btnAddLabel;
	private JButton btnDelete;
	private JTextArea txtRecentOfContent;
	private JTextArea txtRequestContent;
	ArrayList<String> fileOfLabel = new ArrayList<String>();
	ArrayList<String> checkLabel = new ArrayList<String>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test frame = new Test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public Test() throws Exception {
		judgeFile();
		setTitle("\u77E5\u8BC6\u5E93\u7BA1\u7406\u8F6F\u4EF6");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 613, 497);
		getContentPane().setLayout(null);

		JLabel lblEntry = new JLabel("\u5F55\u5165");
		lblEntry.setBounds(14, 13, 59, 25);
		getContentPane().add(lblEntry);

		txtInput = new JTextField();
		txtInput.setHorizontalAlignment(SwingConstants.LEFT);
		txtInput.setBounds(14, 37, 198, 32);
		getContentPane().add(txtInput);
		txtInput.setColumns(10);

		btnConfirm = new JButton("\u786E\u5B9A");
		btnConfirm.addActionListener(this);
		btnConfirm.setBounds(211, 37, 76, 32);
		getContentPane().add(btnConfirm);

		JLabel lblRequest = new JLabel("\u67E5\u8BE2");
		lblRequest.setBounds(321, 13, 59, 25);
		getContentPane().add(lblRequest);

		txtRequest = new JTextField();
		txtRequest.setHorizontalAlignment(SwingConstants.LEFT);
		txtRequest.setBounds(309, 37, 198, 32);
		getContentPane().add(txtRequest);
		txtRequest.setColumns(10);

		btnRequest = new JButton("\u67E5\u8BE2");
		btnRequest.addActionListener(this);
		btnRequest.setBounds(507, 37, 71, 32);
		getContentPane().add(btnRequest);

		txtRecentOfContent = new JTextArea();
		txtRecentOfContent.setEditable(false);
		txtRecentOfContent.setBounds(14, 133, 269, 304);
		getContentPane().add(txtRecentOfContent);

		txtRequestContent = new JTextArea();
		txtRequestContent.setEditable(false);
		txtRequestContent.setBounds(311, 278, 269, 128);
		getContentPane().add(txtRequestContent);

		txtAddLabel = new JTextField();
		txtAddLabel.setBounds(14, 88, 180, 32);
		getContentPane().add(txtAddLabel);
		txtAddLabel.setColumns(10);

		btnAddLabel = new JButton("添加标签");
		btnAddLabel.addActionListener(this);
		btnAddLabel.setBounds(189, 87, 93, 33);
		getContentPane().add(btnAddLabel);
		txtRecentOfContent.setText(recentOfContent("src/content.txt"));

		txtDelete = new JTextField();
		txtDelete.setBounds(309, 410, 198, 27);
		getContentPane().add(txtDelete);
		txtDelete.setColumns(10);

		btnDelete = new JButton("\u5220\u9664");
		btnDelete.addActionListener(this);
		btnDelete.setBounds(507, 410, 71, 27);
		getContentPane().add(btnDelete);

		checkBoxLayout();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAddLabel) {
			if (txtAddLabel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "请输入标签~", "错误",
						JOptionPane.ERROR_MESSAGE);
				txtAddLabel.grabFocus();
			} else {
				fileOfLabel.add(txtAddLabel.getText());
				txtAddLabel.setText("");
			}
		}
		if (e.getSource() == btnConfirm) {
			if (txtInput.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "请输入内容~", "错误",
						JOptionPane.ERROR_MESSAGE);
				txtInput.grabFocus();
			} else
				try {
					if (checkRepeat()) {
						JOptionPane.showMessageDialog(null, "输入的内容已存在", "错误",
								JOptionPane.ERROR_MESSAGE);
						txtInput.grabFocus();
						fileOfLabel.clear();
					} else {
						String id = creatId("src/content.txt");
						try {
							write(id + "/" + txtInput.getText(),
									"src/content.txt");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						try {
							txtRecentOfContent
									.setText(recentOfContent("src/content.txt"));
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						try {
							insertLabel(fileOfLabel);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						txtInput.setText("");
						try {
							checkBoxLayout();
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						try {
							writeRelationship(id, contentLabelList(fileOfLabel));
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						fileOfLabel.clear();
					}
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		}
		if (e.getSource() == btnRequest) {
			if (txtRequest.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "请输入内容~", "错误",
						JOptionPane.ERROR_MESSAGE);
				txtRequest.grabFocus();
			} else {
				for (Component c : getContentPane().getComponents()) {
					if (c instanceof JCheckBox) {
						if (((JCheckBox) c).isSelected()) {
							checkLabel.add(((JCheckBox) c).getText());
						}
					}
				}
				try {
					request(checkLabelId(checkLabel));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
		if (e.getSource() == btnDelete) {
			if (txtDelete.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "请输入要删除内容~", "错误",
						JOptionPane.ERROR_MESSAGE);
				txtDelete.grabFocus();
			} else {
				try {
					delete();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					txtRecentOfContent
							.setText(recentOfContent("src/content.txt"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 该方法为文件内写入需要写的内容
	 * 
	 * @param content
	 *            [写入文件的内容]
	 * @param fileName
	 *            [文件的名字]
	 * @throws Exception
	 */
	public void write(String content, String fileName) throws Exception {
		BufferedWriter writeContent = new BufferedWriter(new FileWriter(
				fileName, true));
		writeContent.write(content);
		writeContent.newLine();
		writeContent.close();
	}

	/**
	 * 该方法读出文件内确定的某行内容
	 * 
	 * @param lineNumber
	 *            [需要读的某行的行数]
	 * @param fileName
	 *            [文件的名字]
	 * @return [方法返回读出的内容]
	 * @throws Exception
	 */
	public String readLine(int lineNumber, String fileName) throws Exception {
		BufferedReader read = new BufferedReader(new FileReader(fileName));
		String s = "";
		int line = 0;
		while (s != null) {
			s = read.readLine();
			line++;
			if (lineNumber - line == 0) {
				break;
			}
		}
		read.close();
		return s;
	}

	/**
	 * 该方法计算文件的行数
	 * 
	 * @param fileName
	 *            [需要读取的文件的名字]
	 * @return [该方法返回文件的行数]
	 * @throws Exception
	 */
	public int numberOfLine(String fileName) throws Exception {
		BufferedReader read = new BufferedReader(new FileReader(fileName));
		int line = 0;
		String s = read.readLine();
		while (s != null) {
			line++;
			s = read.readLine();
		}
		read.close();
		return line;
	}

	/**
	 * 该方法读取存内容的所有内容
	 * 
	 * @param fileName
	 *            [文件的名字]
	 * @return [返回内容文件的所有内容]
	 * @throws Exception
	 */
	public String recentOfContent(String fileName) throws Exception {
		String recentContent = "";
		String content;
		for (int i = numberOfLine(fileName); i > 0; i--) {
			content = readLine(i, fileName);
			String[] contentArray = content.split("/");
			recentContent += contentArray[1] + "\n";
		}
		return recentContent;
	}

	/**
	 * 该方法将输入的标签写入的标签文件
	 * 
	 * @param fileOfLabel
	 *            [输入的标签数组]
	 * @throws Exception
	 */
	public void insertLabel(ArrayList<String> fileOfLabel) throws Exception {
		boolean flag = false;
		String labelOfContent;
		int i = 0;
		int num = numberOfLine("src/label.txt");
		if (num == 0) {
			for (i = 0; i < fileOfLabel.size(); i++) {
				write(creatId("src/label.txt") + "/" + fileOfLabel.get(i),
						"src/label.txt");
			}
		} else {
			for (i = 0; i < fileOfLabel.size(); i++) {
				for (int j = 1; j <= num; j++) {
					labelOfContent = readLine(j, "src/label.txt");
					String[] labelContent = labelOfContent.split("/");
					flag = fileOfLabel.get(i).equals(labelContent[1]);
					if (flag) {
						break;
					}
				}
				if (flag) {
					continue;
				} else {
					write(creatId("src/label.txt") + "/" + fileOfLabel.get(i),
							"src/label.txt");
				}
			}
		}
	}

	/**
	 * 该方法给checkbox布局，有多少个标签对应多少个checkbox
	 * 
	 * @throws Exception
	 */
	public void checkBoxLayout() throws Exception {
		Map<String, JCheckBox> radioMap = new HashMap<String, JCheckBox>();
		String labelContent;
		for (int i = 0; i < numberOfLine("src/label.txt"); i++) {
			radioMap.put("a" + i, new JCheckBox());
			labelContent = readLine(i + 1, "src/label.txt");
			String[] labelContentArray = labelContent.split("/");
			radioMap.get("a" + i).setText(labelContentArray[1]);
			if (i % 3 == 0) {
				radioMap.get("a" + i).setBounds(309, 91 + (i / 3) * 30, 76, 25);
			} else if (i % 3 == 1) {
				radioMap.get("a" + i).setBounds(473, 91 + (i / 3) * 30, 76, 25);
			} else {
				radioMap.get("a" + i).setBounds(391, 91 + (i / 3) * 30, 76, 25);
			}

			radioMap.get("a" + i).addActionListener(this);
			getContentPane().add(radioMap.get("a" + i));
		}
	}

	/**
	 * 该方法将输入的标签存到数组内将重复的标签去掉
	 * 
	 * @param fileOfLabel
	 *            [输入的标签数组]
	 * @return [返回没有重复标签的数组]
	 * @throws Exception
	 */
	public ArrayList<Integer> contentLabelList(ArrayList<String> fileOfLabel)
			throws Exception {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < fileOfLabel.size(); i++) {
			list.add(getLabelId(fileOfLabel.get(i)));
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 该方法可以接受一个选择的标签列表查询对应的标签id
	 * 
	 * @param fileOfLabel
	 * @return [返回标签的id数组]
	 * @throws Exception
	 */
	public ArrayList<Integer> checkLabelId(ArrayList<String> fileOfLabel)
			throws Exception {
		ArrayList<Integer> list = new ArrayList<Integer>();
		String label;
		int num = numberOfLine("src/label.txt");
		for (int i = 0; i < fileOfLabel.size(); i++) {
			for (int j = 1; j <= num; j++) {
				label = readLine(j, "src/label.txt");
				String[] labelArray = label.split("/");
				label = fileOfLabel.get(i);
				if (labelArray[1].equals(label)) {
					list.add(Integer.valueOf(labelArray[0]));
					break;
				}
			}
		}
		return list;
	}

	/**
	 * 该方法将内容与标签的关系写入关系文件内
	 * 
	 * @param contentLabelList
	 * @throws Exception
	 */
	public void writeRelationship(String id, ArrayList<Integer> contentLabelList)
			throws Exception {
		BufferedWriter writeRelationship = new BufferedWriter(new FileWriter(
				"src/relationship.txt", true));
		writeRelationship.write(id + "/");
		for (int i = 0; i < contentLabelList.size(); i++) {
			writeRelationship.write(String.valueOf(contentLabelList.get(i))
					+ ",");
		}
		writeRelationship.newLine();
		writeRelationship.close();
	}

	/**
	 * 该方法读取关系文件内的关系
	 * 
	 * @param receiveRelationship
	 *            [需要读取的关系文件内的关系行]
	 * @return [返回对应的关系数组]
	 */
	public String[] readRelationship(String receiveRelationship) {
		String[] idList;
		String[] relationship;
		idList = receiveRelationship.split("/");
		if (idList.length == 2) {
			relationship = idList[1].split(",");
		} else {
			relationship = new String[] { "" };
		}
		return relationship;
	}

	/**
	 * 该方法判断查询时所选的标签与对应的关系文件的关系是否匹配
	 * 
	 * @param checkLabel
	 *            [查询时所选择的标签]
	 * @param fileLabel
	 *            [关系文件内对应的关系]
	 * @return [匹配返回正确，反之不正确]
	 */
	public boolean judge(ArrayList<Integer> checkLabel, String[] fileLabel) {
		int num = 0;
		boolean flag = false;
		if (fileLabel[0].equals("") && checkLabel.size() > 0) {
			flag = false;
		} else {
			if (checkLabel.size() > fileLabel.length) {
				flag = false;
			} else {
				for (int i = 0; i < checkLabel.size(); i++) {
					for (int j = 0; j < fileLabel.length; j++) {
						boolean equal = checkLabel.get(i).equals(
								Integer.valueOf(fileLabel[j]));
						if (equal) {
							num += 1;
							break;
						}
					}
				}

				if (num == checkLabel.size()) {
					flag = true;
				} else {
					flag = false;
				}
			}
		}
		return flag;
	}

	/**
	 * 该方法进行查询操作
	 * 
	 * @param checkLabelId
	 *            [选择的标签]
	 * @throws Exception
	 */
	public void request(ArrayList<Integer> checkLabelId) throws Exception {

		boolean flag = false;
		int relationshipNumber = numberOfLine("src/relationship.txt");
		int contentNumber = numberOfLine("src/content.txt");
		if (relationshipNumber == 0) {
			JOptionPane.showMessageDialog(null, "系统内暂时没有存储内容", "错误",
					JOptionPane.ERROR_MESSAGE);
			txtInput.grabFocus();
		} else {
			for (int i = 0; i < relationshipNumber; i++) {
				if (judge(
						checkLabelId,
						readRelationship(readLine(i + 1, "src/relationship.txt")))) {
					String relationshipId = readLine(i + 1,
							"src/relationship.txt").split("/")[0];
					Pattern contentOfRequest = Pattern.compile(txtRequest
							.getText());

					String str = idMatch(relationshipId, contentNumber);
					Matcher m = contentOfRequest.matcher(str);
					if (m.find()) {
						// String[] requestContent=str.split("/");
						txtRequestContent.setText(str);
						flag = true;
						break;
					} else {
						continue;
					}
				}
			}
			if (flag == false) {
				txtRequestContent.setText("没有匹配项");
			}
			checkLabel.clear();
		}
	}

	/**
	 * 该方法在录入时进行查重操作
	 * 
	 * @return [有重复返回true]
	 * @throws Exception
	 */
	public boolean checkRepeat() throws Exception {
		boolean flag = false;
		int num = numberOfLine("src/content.txt");
		for (int i = 1; i <= num; i++) {
			if (txtInput.getText().equals(readLine(i, "src/content.txt"))) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 该方法进行删除操作
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		boolean flag = false;
		String contentOfDelete = txtDelete.getText();
		int index = 0, i, indexOfrelationship = 0;
		String id = null;
		int contentNumber = numberOfLine("src/content.txt");
		int relationshipNumber = numberOfLine("src/relationship.txt");
		for (i = 1; i <= contentNumber; i++) {
			String[] contentArray = readLine(i, "src/content.txt").split("/");
			if (contentOfDelete.equals(contentArray[1])) {
				flag = true;
				id = contentArray[0];
				index = i;
				break;
			}
		}
		if (flag == false) {
			JOptionPane.showMessageDialog(null, "系统内不存在要删除的内容", "错误",
					JOptionPane.ERROR_MESSAGE);
			txtDelete.grabFocus();
		} else {
			ArrayList<String> deleteContent = new ArrayList<String>();
			ArrayList<String> deleteRelationship = new ArrayList<String>();
			for (i = 1; i <= contentNumber; i++) {
				deleteContent.add(readLine(i, "src/content.txt"));
				deleteRelationship.add(readLine(i, "src/relationship.txt"));
			}
			for (i = 1; i <= relationshipNumber; i++) {
				if (readLine(i, "src/relationship.txt").split("/")[0]
						.equals(id)) {
					indexOfrelationship = i;
					break;
				}
			}
			deleteContent.remove(index - 1);
			deleteRelationship.remove(indexOfrelationship - 1);
			File content = new File("src/content.txt");
			File relationship = new File("src/relationship.txt");
			content.delete();
			relationship.delete();
			content.createNewFile();
			relationship.createNewFile();
			for (i = 0; i < deleteContent.size(); i++) {
				write(deleteContent.get(i), "src/content.txt");
				write(deleteRelationship.get(i), "src/relationship.txt");
			}
			JOptionPane.showMessageDialog(null, "删除成功", "正确",
					JOptionPane.PLAIN_MESSAGE);
			deleteContent.clear();
			deleteRelationship.clear();
		}
		txtDelete.setText("");
	}

	/**
	 * 该方法得到标签的对应id
	 * 
	 * @param label
	 *            [需要查询的标签]
	 * @return [返回查询标签对应的id]
	 * @throws Exception
	 */
	public int getLabelId(String label) throws Exception {
		int id = 0;
		String lineOfId;
		int num = numberOfLine("src/label.txt");
		for (int i = 1; i <= num; i++) {
			lineOfId = readLine(i, "src/label.txt");
			String[] idArray = lineOfId.split("/");
			if (idArray[1].equals(label)) {
				id = Integer.valueOf(idArray[0]);
				break;
			}
		}
		return id;
	}

	/**
	 * 该方法创建一个新Id
	 * 
	 * @param fileName
	 *            [要增加Id的文件名]
	 * @return [返回一个新Id]
	 * @throws Exception
	 */
	public String creatId(String fileName) throws Exception {
		String id;
		String[] idArray;
		int num = numberOfLine(fileName);
		if (num != 0) {
			String lineOfId = readLine(num, fileName);
			idArray = lineOfId.split("/");
			int i = Integer.valueOf(idArray[0]).intValue() + 1;
			id = String.valueOf(i);
		} else {
			id = String.valueOf(1);
		}
		return id;
	}

	/**
	 * 该方法匹配内容Id和关系Id
	 * 
	 * @param relationshipId
	 *            [关系的内容id]
	 * @param contentNumber
	 *            [内容文件的行数]
	 * @return [返回匹配成功后的内容]
	 * @throws Exception
	 */
	public String idMatch(String relationshipId, int contentNumber)
			throws Exception {
		String content = null;
		for (int i = 1; i <= contentNumber; i++) {
			String[] contentId = readLine(i, "src/content.txt").split("/");
			if (relationshipId.equals(contentId[0])) {
				content = contentId[1];
				break;
			} else {
				continue;
			}
		}
		return content;
	}

	public void judgeFile() throws IOException {
		File content = new File("src/content.txt");
		File label = new File("src/label.txt");
		File relationship = new File("src/relationship.txt");
		if (!content.exists()) {
			content.createNewFile();
		}
		if (!label.exists()) {
			label.createNewFile();
		}
		if (!relationship.exists()) {
			relationship.createNewFile();
		}
	}
}
