package mainPackage;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;

import java.awt.BorderLayout;
import java.awt.Choice;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Menu implements Constants {

	public JFrame mainFrame;
	private Filters filtersObjGlobal;
	private ClothObj[] bluzeObj;
	private ClothObj[] tricouriObj;
	private ClothObj[] pantaloniObj;
	private ClothObj[] geciObj;
	private ClothObj[] papuciObj;
	private CombinationObj[] combinationsObj;
	private Statement st;
	private ResultSet rs;

	ElementsGroup elementsGroupObj;

	private JButton combinationsButton;
	private JButton comparisonButton;
	private JButton addClothButton;
	private JButton addToCombinations;

	private int buttonsDefaultWidth = 0;
	private int nextHeightButton = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String[] colorFilter = { "white", "black", "red", "green", "blue", "brown", "grey", "turcuaz",
							"pink", "visiniu", "orange", "yellow" };
					String[] otherFilters = { "scoala", "afara", "majorate", "sport" };

					Filters filtersObj = new Filters(colorFilter, otherFilters);

					new Menu(filtersObj).mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Menu(Filters filtersObj) {
		this.filtersObjGlobal = filtersObj;
		initialize();
		// checkFiltersTimer.start();
	}

	public Menu(String[] colorFilter, String[] otherFilters, ClothObj[] bluzeObj, ClothObj[] tricouriObj,
			ClothObj[] geciObj, ClothObj[] pantaloniObj, ClothObj[] papuciObj, CombinationObj[] combinationsObj,
			Statement st, ResultSet rs) {
		this.filtersObjGlobal = new Filters(colorFilter, otherFilters);
		this.bluzeObj = bluzeObj;
		this.tricouriObj = tricouriObj;
		this.geciObj = geciObj;
		this.pantaloniObj = pantaloniObj;
		this.papuciObj = papuciObj;

		this.combinationsObj = combinationsObj;

		this.st = st;
		this.rs = rs;

		initialize();
	}

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public void initialize() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, (int) screenWidth, (int) screenHeight);
		mainFrame = new JFrame("Clothes Collection");
		mainFrame.setExtendedState(mainFrame.MAXIMIZED_BOTH);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.getContentPane().add(mainPanel);

		ClothObj[][] utilClothObj = { this.bluzeObj, this.tricouriObj, this.pantaloniObj, this.geciObj,
				this.papuciObj };

		elementsGroupObj = new ElementsGroup(mainPanel, this.filtersObjGlobal.colorString,
				this.filtersObjGlobal.otherFilters, utilClothObj, st, rs);

		JLabel compareBluze = new JLabel();
		compareBluze.setBounds((int) screenWidth / 10, (int) (screenHeight - imageHeight * 3), imageWidth, imageHeight);
		mainPanel.add(compareBluze);

		JLabel compareTricouri = new JLabel();
		compareTricouri.setBounds((int) screenWidth / 10 + imageWidth, (int) (screenHeight - imageHeight * 3),
				imageWidth, imageHeight);
		mainPanel.add(compareTricouri);

		JLabel comparePantaloni = new JLabel();
		comparePantaloni.setBounds((int) screenWidth / 10 + 2 * imageWidth, (int) (screenHeight - imageHeight * 3),
				imageWidth, imageHeight);
		mainPanel.add(comparePantaloni);

		JLabel compareGeci = new JLabel();
		compareGeci.setBounds((int) screenWidth / 10 + 3 * imageWidth, (int) (screenHeight - imageHeight * 3),
				imageWidth, imageHeight);
		mainPanel.add(compareGeci);

		JLabel comparePapuci = new JLabel();
		comparePapuci.setBounds((int) screenWidth / 10 + 4 * imageWidth, (int) (screenHeight - imageHeight * 3),
				imageWidth, imageHeight);
		mainPanel.add(comparePapuci);

		this.combinationsButton = setButton("Combinations", mainPanel);
		this.comparisonButton = setButton("Compare", mainPanel);
		this.addClothButton = setButton("Add cloth", mainPanel);
		this.addToCombinations = setButton("Add to combinations", mainPanel);

		this.combinationsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				new Combinations(combinationsObj, filtersObjGlobal.colorString, filtersObjGlobal.otherFilters,st,rs);
			}
		});

		this.comparisonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				compareBluze.setIcon(elementsGroupObj.bluze.currentClothImageIcon);
				compareTricouri.setIcon(elementsGroupObj.tricouri.currentClothImageIcon);
				comparePantaloni.setIcon(elementsGroupObj.pantaloni.currentClothImageIcon);
				compareGeci.setIcon(elementsGroupObj.geci.currentClothImageIcon);
				comparePapuci.setIcon(elementsGroupObj.papuci.currentClothImageIcon);
			}
		});

		this.addClothButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				AddClothClass addClothObj=new AddClothClass(st,rs);
//				addClothObj.selectCathegory();
//				addClothObj.insertName();
			}
		});

		this.addToCombinations.addActionListener(new ActionListener() {

			ButtonsActions buttonActionsObj = new ButtonsActions(st, rs);

			public void actionPerformed(ActionEvent ev) {
				
				buttonActionsObj.addCombinationToDB(
						elementsGroupObj.bluze.urlFindings[elementsGroupObj.bluze.urlFindingsIndex],
						elementsGroupObj.tricouri.urlFindings[elementsGroupObj.tricouri.urlFindingsIndex],
						elementsGroupObj.pantaloni.urlFindings[elementsGroupObj.pantaloni.urlFindingsIndex],
						elementsGroupObj.geci.urlFindings[elementsGroupObj.geci.urlFindingsIndex],
						elementsGroupObj.papuci.urlFindings[elementsGroupObj.papuci.urlFindingsIndex],JOptionPane.showInputDialog("Family Name"));
			}
		});

		mainFrame.setResizable(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	private JButton setButton(String buttonText, JPanel mainPanel) {
		JButton button = new JButton(buttonText);
		button.setBounds(this.buttonsDefaultWidth, this.nextHeightButton, buttonWidth, buttonHeight);
		button.setBackground(buttonsColors);
		mainPanel.add(button);
		this.nextHeightButton += (buttonHeight * 3) / 2;

		return button;
	}
}

class ElementsGroup implements Constants {

	Element bluze;
	Element tricouri;
	Element pantaloni;
	Element geci;
	Element papuci;

	ElementsGroup(JPanel mainPanel, String[] colorString, String[] otherFilters, ClothObj[][] clothObj, Statement st,
			ResultSet rs) {

		bluze = new Element((int) screenWidth / 10, (int) screenHeight / 9, mainPanel, colorString, otherFilters,
				clothObj[0], "Bluze", st, rs);
		tricouri = new Element((int) screenWidth / 10 + imageWidth, (int) screenHeight / 9, mainPanel, colorString,
				otherFilters, clothObj[1], "Tricouri", st, rs);
		pantaloni = new Element((int) screenWidth / 10 + 2 * imageWidth, (int) screenHeight / 9, mainPanel, colorString,
				otherFilters, clothObj[2], "Pantaloni", st, rs);
		geci = new Element((int) screenWidth / 10 + 3 * imageWidth, (int) screenHeight / 9, mainPanel, colorString,
				otherFilters, clothObj[3], "Geci", st, rs);
		papuci = new Element((int) screenWidth / 10 + 4 * imageWidth, (int) screenHeight / 9, mainPanel, colorString,
				otherFilters, clothObj[4], "Papuci", st, rs);
	}
}
