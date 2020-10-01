package mainPackage;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddClothClass implements Constants {

	private Statement st;
	private ResultSet rs;
	private String cathegory;
	private String name;

	private JFrame mainFrame;
	private JPanel mainPanel;

	private CheckboxGroup clothesGroup;
	private JTextField text = new JTextField();
	private JButton confirmButton;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddClothClass window = new AddClothClass(null, null);
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AddClothClass(Statement st, ResultSet rs) {

		this.st = st;
		this.rs = rs;
		initialize();
	}

	private void initialize() {
		mainFrame = new JFrame("Select cathegory and name");
		mainFrame.setBounds(300, 300, 500, 300);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);

		mainPanel = new JPanel();
		mainPanel.setBounds(300, 300, 500, 300);

		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.getContentPane().add(mainPanel);

		setCheckBoxGroup();
		setTextField();
		setButton();

		mainFrame.setVisible(true);
	}

	private void setButton() {
		confirmButton = new JButton("Confirm");
		mainPanel.add(confirmButton, BorderLayout.SOUTH);

		confirmButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {
					cathegory = clothesGroup.getSelectedCheckbox().getLabel();
					name = text.getText();
					addClothToDB();
				} catch (Exception ex) {
					javax.swing.JOptionPane.showMessageDialog(null, "Please fill all the gaps");
				}

				mainFrame.dispose();
			}
		});
	}

	protected void addClothToDB() {

		StringBuilder addClothSB = new StringBuilder("INSERT into clothes.");

		addClothSB.append(this.cathegory);
		addClothSB.append(
				" (id,enabled,favourites,name,colorFilter,seasonFilter,eleganceFilter,otherFilters) VALUES (NULL,1,0,");

		addClothSB.append('"');
		addClothSB.append(this.name);
		addClothSB.append('"');

		addClothSB.append(',');
		addClothSB.append('"');
		addClothSB.append('"');

		addClothSB.append(',');
		addClothSB.append('"');
		addClothSB.append('"');

		addClothSB.append(',');
		addClothSB.append('"');
		addClothSB.append('"');

		addClothSB.append(',');
		addClothSB.append('"');
		addClothSB.append('"');

		addClothSB.append(')');

		try {
			st.executeUpdate(addClothSB.toString());
		} catch (SQLException ex) {
			System.out.println(ex + " : addClothToDB()");
		}
	}

	private void setTextField() {
		text = new JTextField("Insert name");
		text.setCaretPosition(0);
		mainPanel.add(text, BorderLayout.EAST);
	}

	private void setCheckBoxGroup() {

		clothesGroup = new CheckboxGroup();

		Checkbox bluza = new Checkbox("bluze", clothesGroup, false);
		Checkbox tricou = new Checkbox("tricouri", clothesGroup, false);
		Checkbox geaca = new Checkbox("geci", clothesGroup, false);
		Checkbox pantaloni = new Checkbox("pantaloni", clothesGroup, false);
		Checkbox papuci = new Checkbox("papuci", clothesGroup, false);

		mainPanel.add(bluza, BorderLayout.CENTER);
		mainPanel.add(tricou, BorderLayout.CENTER);
		mainPanel.add(geaca, BorderLayout.CENTER);
		mainPanel.add(pantaloni, BorderLayout.CENTER);
		mainPanel.add(papuci, BorderLayout.CENTER);
	}
}
