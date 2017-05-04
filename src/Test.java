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
	private JButton btnConfirm;
	private JButton btnRequest;
	private JButton btnAddLabel;
	private JButton btnDelete;
	private JTextArea txtRecentOfContent;
	private JTextArea txtRequestContent;
	ArrayList<String> fileOfLabel = new ArrayList<String>();
	ArrayList<String> checkLabel = new ArrayList<String>();
	private JTextField txtDelete;

	/**
	 * Launch the application.
	 */
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
		txtRecentOfContent.setText(recentOfContent("D:\\1\\content.txt"));
		
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
						try {
							write(txtInput.getText(), "D:\\1\\content.txt");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						try {
							txtRecentOfContent
									.setText(recentOfContent("D:\\1\\content.txt"));
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
							writeRelationship(contentLabelList(fileOfLabel));
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
					request(contentLabelList(checkLabel));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
		if(e.getSource()==btnDelete){
			if(txtDelete.getText().equals("")){
				JOptionPane.showMessageDialog(null, "请输入要删除内容~", "错误",
						JOptionPane.ERROR_MESSAGE);
				txtDelete.grabFocus();}
			else{
				try {
					delete();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					txtRecentOfContent
					.setText(recentOfContent("D:\\1\\content.txt"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	/**
	 * 该方法为文件内写入需要写的内容
	 * @param content [写入文件的内容]
	 * @param fileName [文件的名字]
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
	 * @param lineNumber [需要读的某行的行数]
	 * @param fileName [文件的名字]
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
	 * @param fileName [需要读取的文件的名字]
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
	 * @param fileName [文件的名字]
	 * @return [返回内容文件的所有内容]
	 * @throws Exception
	 */
	public String recentOfContent(String fileName) throws Exception {
		String recentContent = "";
		for (int i = numberOfLine(fileName); i > 0; i--) {
			recentContent += readLine(i, fileName) + "\n";
		}
		return recentContent;
	}
	/**
	 * 该方法将输入的标签写入的标签文件
	 * @param fileOfLabel [输入的标签数组]
	 * @throws Exception
	 */
	public void insertLabel(ArrayList<String> fileOfLabel) throws Exception {
		boolean flag = false;
		int i = 0;
		int num = numberOfLine("D:\\1\\label.txt");
		if (num == 0) {
			for (i = 0; i < fileOfLabel.size(); i++) {
				write(fileOfLabel.get(i), "D:\\1\\label.txt");
			}
		} else {
			for (i = 0; i < fileOfLabel.size(); i++) {
				for (int j = 1; j <= num; j++) {
					flag = fileOfLabel.get(i).equals(
							readLine(j, "D:\\1\\label.txt"));
					if (flag) {
						break;
					}
				}
				if (flag) {
					continue;
				} else {
					write(fileOfLabel.get(i), "D:\\1\\label.txt");
				}
			}
		}
	}
	/**
	 * 该方法给checkbox布局，有多少个标签对应多少个checkbox
	 * @throws Exception
	 */
	public void checkBoxLayout() throws Exception {
		Map<String, JCheckBox> radioMap = new HashMap<String, JCheckBox>();
		for (int i = 0; i < numberOfLine("D:\\1\\label.txt"); i++) {
			radioMap.put("a" + i, new JCheckBox());
			radioMap.get("a" + i).setText(readLine(i + 1, "D:\\1\\label.txt"));
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
	 * 该方法得到一个标签在标签文件的序列号
	 * @param label [需要查的标签]
	 * @return [返回需要查的标签的序列号]
	 * @throws Exception
	 */
	public int getLabelIndex(String label) throws Exception {
		int Index = 0;
		int num = numberOfLine("D:\\1\\label.txt");
		for (int i = 1; i <= num; i++) {
			if (label.equals(readLine(i, "D:\\1\\label.txt"))) {
				Index = i;
				break;
			}
		}
		return Index;
	}
	/**
	 * 该方法将输入的标签存到数组内将重复的标签去掉
	 * @param fileOfLabel [输入的标签数组]
	 * @return [返回没有重复标签的数组]
	 * @throws Exception
	 */
	public ArrayList<Integer> contentLabelList(ArrayList<String> fileOfLabel)
			throws Exception {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < fileOfLabel.size(); i++) {
			list.add(getLabelIndex(fileOfLabel.get(i)));
		}
		Collections.sort(list);
		return list;
	}
	/**
	 * 该方法将内容与标签的关系写入关系文件内
	 * @param contentLabelList
	 * @throws IOException
	 */
	public void writeRelationship(ArrayList<Integer> contentLabelList)
			throws IOException {
		BufferedWriter writeRelationship = new BufferedWriter(new FileWriter(
				"D:\\1\\relationship.txt", true));
		for (int i = 0; i < contentLabelList.size(); i++) {
			writeRelationship.write(String.valueOf(contentLabelList.get(i))
					+ ",");
		}
		writeRelationship.newLine();
		writeRelationship.close();
	}
	/**
	 * 该方法读取关系文件内的关系
	 * @param receiveRelationship
	 * @return
	 */
	public String[] readRelationship(String receiveRelationship) {
		String[] relationship;
		relationship = receiveRelationship.split(",");
		return relationship;
	}
	/**
	 * 该方法判断查询时所选的标签与对应的关系文件的关系是否匹配
	 * @param checkLabel [查询时所选择的标签]
	 * @param fileLabel [关系文件内对应的关系]
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
	 * @param contentLabelList [内容文件所有存储的内容]
	 * @throws Exception
	 */
	public void request(ArrayList<Integer> contentLabelList) throws Exception {

		boolean flag = false;
		int num = numberOfLine("D:\\1\\relationship.txt");
		if (num == 0) {
			JOptionPane.showMessageDialog(null, "系统内暂时没有存储内容", "错误",
					JOptionPane.ERROR_MESSAGE);
			txtInput.grabFocus();
		} else {
			for (int i = 0; i < num; i++) {
				if (judge(
						contentLabelList,
						readRelationship(readLine(i + 1,
								"D:\\1\\relationship.txt")))) {
					Pattern contentOfRequest = Pattern.compile(txtRequest
							.getText());
					String str = readLine(i + 1, "D:\\1\\content.txt");
					Matcher m = contentOfRequest.matcher(str);
					if (m.find()) {
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
	 * @return [有重复返回true]
	 * @throws Exception
	 */
	public boolean checkRepeat() throws Exception {
		boolean flag = false;
		int num = numberOfLine("D:\\1\\content.txt");
		for (int i = 1; i <= num; i++) {
			if (txtInput.getText().equals(readLine(i, "D:\\1\\content.txt"))) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	/**
	 * 该方法进行删除操作
	 * @throws Exception
	 */
	public void delete() throws Exception{
		boolean flag=false;
		   String contentOfDelete=txtDelete.getText();
		   int index = 0,i;
		   int num=numberOfLine("D:\\1\\content.txt");
		   for(i=1;i<=num;i++){
			   if(contentOfDelete.equals(readLine(i,"D:\\1\\content.txt"))){
				   flag=true;
				   index=i;
				   break;
			   }
		   }
		   if(flag==false){
			   JOptionPane.showMessageDialog(null, "系统内不存在要删除的内容", "错误",
						JOptionPane.ERROR_MESSAGE);
				txtDelete.grabFocus();
		   }
		   else{
			   ArrayList<String> deleteContent=new ArrayList<String>();
			   ArrayList<String> deleteRelationship=new ArrayList<String>();
			   for(i=1;i<=num;i++){
				   deleteContent.add(readLine(i,"D:\\1\\content.txt"));
				   deleteRelationship.add(readLine(i,"D:\\1\\relationship.txt"));
			   }
			   deleteContent.remove(index-1);
			   deleteRelationship.remove(index-1);
			   File content=new File("D:\\1\\content.txt");
			   File relationship=new File("D:\\1\\relationship.txt");
			   content.delete();
			   relationship.delete();
			   content.createNewFile();
			   relationship.createNewFile();
			   for(i=0;i<deleteContent.size();i++){
				   write(deleteContent.get(i),"D:\\1\\content.txt");
				   write(deleteRelationship.get(i),"D:\\1\\relationship.txt");
			   }
			   JOptionPane.showMessageDialog(null, "删除成功", "正确",
						JOptionPane.PLAIN_MESSAGE);
			   deleteContent.clear();
			   deleteRelationship.clear();
		   }
		   txtDelete.setText("");
	}
}
