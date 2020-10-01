package mainPackage;

import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Combinations implements Constants {

	private JFrame mainFrame;
	private JPanel mainPanel;

	private int buttonsDefaultWidth = 0;
	private int nextHeightButton = 0;

	private Statement st;
	private ResultSet rs;

	private JButton compareButton;
	private JButton searchNameButton;
	private JButton searchFilterButton;
	private JButton setFilterButton;
	private JButton browseRight;
	private JButton browseLeft;

	private Choice choice = new Choice();
	private Filters filtersObj;
	private int checkBoxesStartHeight;
	private JTextField searchField = new JTextField();

	private int[] colorIndexSelected;
	private int[] eleganceIndexSelected = new int[12];
	private int[] seasonIndexSelected = new int[4];
	private int[] otherFiltersIndexSelected;
	private int cntColor = 0, cntElegance = 0, cntSeason = 0, cntOther = 0;

	private LabelsGroup mainLabelsObj;
	private LabelsGroup comparisonLabelsObj;

	private CombinationObj[] combinationsObj;
	private int indexCombinationObj = 0;

	Combinations(CombinationObj[] combinationsObj, String[] color, String[] otherFilters, Statement st, ResultSet rs) {
		this.combinationsObj = combinationsObj;
		this.filtersObj = new Filters(color, otherFilters);
		this.st = st;
		this.rs = rs;
		initialize();
	}

	private void initialize() {

		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, (int) screenWidth, (int) screenHeight);
		mainFrame = new JFrame("Combinations");
		mainFrame.setExtendedState(mainFrame.MAXIMIZED_BOTH);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.getContentPane().add(mainPanel);

		this.compareButton = setButton("Compare", mainPanel);
		this.searchField.setBounds(this.buttonsDefaultWidth + buttonWidth, this.nextHeightButton, elementWidth,
				elementHeight);
		this.searchNameButton = setButton("Search By Name", mainPanel);

		this.choice.setBounds(this.buttonsDefaultWidth + buttonWidth, this.nextHeightButton, elementWidth,
				elementHeight);
		this.choice.setBackground(Color.cyan);
		choice.add("");
		choice.add("Color");
		choice.add("Elegance");
		choice.add("Seasons");
		choice.add("Other Filters");
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				deleteFilters(filtersObj, mainPanel);
				updateFilters(filtersObj, mainPanel, buttonsDefaultWidth + buttonWidth);
			}
		});

		this.checkBoxesStartHeight = nextHeightButton + elementHeight;
		this.searchFilterButton = setButton("Search By Filter", mainPanel);
		this.setFilterButton = setButton("Set Filters To Combination", mainPanel);
		this.browseRight = setButton("------->", mainPanel);
		this.browseLeft = setButton("<-------", mainPanel);

		setLabels();
		setFunctionalityToButtons();
		setElementsInPanel(this.mainPanel);

		mainFrame.setResizable(true);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	private void setLabels() {
		this.mainLabelsObj = new LabelsGroup(this.mainPanel, buttonHeight * 2);
		this.comparisonLabelsObj = new LabelsGroup(this.mainPanel, buttonHeight * 2 + imageHeight);
	}

	private JButton setButton(String buttonText, JPanel mainPanel) {
		JButton button = new JButton(buttonText);
		button.setBounds(this.buttonsDefaultWidth, this.nextHeightButton, buttonWidth, buttonHeight);
		button.setBackground(buttonsColors);
		mainPanel.add(button);
		this.nextHeightButton += (buttonHeight * 3) / 2;

		return button;
	}

	private void setFunctionalityToButtons() {

		ButtonsActions buttonsActionsObj = new ButtonsActions(st, rs);

		this.compareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				adjustAndDisplayCombination(comparisonLabelsObj);
			}
		});

		this.searchNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				CombinationObj searchResult = buttonsActionsObj.searchCombination(searchField.getText(),
						combinationsObj);

				if (searchResult == null) {
					javax.swing.JOptionPane.showMessageDialog(null, "Could not find combination with this name");
				}

				else {

					combinationsObj[0] = searchResult;
					indexCombinationObj = 0;

					adjustAndDisplayCombination(mainLabelsObj);
				}
			}
		});

		this.searchFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				analyzeCheckBoxes();

				UrlProvider urlObj = new UrlProvider(filtersObj.colorString, filtersObj.seasons,
						filtersObj.otherFilters, colorIndexSelected, cntColor, eleganceIndexSelected, cntElegance,
						seasonIndexSelected, cntSeason, otherFiltersIndexSelected, cntOther, combinationsObj);

				combinationsObj = urlObj.getCombinationObj();
				indexCombinationObj = 0;

				adjustAndDisplayCombination(mainLabelsObj);
			}
		});

		this.setFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				
				buttonsActionsObj.setFilters("combinations", filtersObj, combinationsObj[indexCombinationObj].id);

				combinationsObj[indexCombinationObj].colorFilter = buttonsActionsObj.getFilterString(filtersObj.colorString,
						filtersObj.colorStringCheckBox, COLOR_FILTERS_NR);
				combinationsObj[indexCombinationObj].cntColorFilter = combinationsObj[indexCombinationObj].colorFilter.length;
				
				combinationsObj[indexCombinationObj].seasonFilter = buttonsActionsObj.getFilterString(filtersObj.seasons,
						filtersObj.seasonsCheckBox, SEASON_FILTERS_NR);
				combinationsObj[indexCombinationObj].cntSeasonFilter = combinationsObj[indexCombinationObj].seasonFilter.length;

				combinationsObj[indexCombinationObj].eleganceFilter = buttonsActionsObj.getFilterString(null,
						filtersObj.eleganceCheckBox, ELEGANCE_FILTERS_NR);
				combinationsObj[indexCombinationObj].cntEleganceFilter = combinationsObj[indexCombinationObj].eleganceFilter.length;

				combinationsObj[indexCombinationObj].othersFilter = buttonsActionsObj.getFilterString(filtersObj.otherFilters,
						filtersObj.otherFiltersCheckBox, OTHER_FILTERS_NR);
				combinationsObj[indexCombinationObj].cntOthersFilter = combinationsObj[indexCombinationObj].othersFilter.length;
			}
		});

		this.browseLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				if (indexCombinationObj == 0) {
					indexCombinationObj = combinationsObj.length - 1;
				}

				else {
					indexCombinationObj--;
				}
				adjustAndDisplayCombination(mainLabelsObj);
			}
		});

		this.browseRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				if (indexCombinationObj == combinationsObj.length - 1) {
					indexCombinationObj = 0;
				}

				else {
					indexCombinationObj++;
				}
				adjustAndDisplayCombination(mainLabelsObj);
			}
		});
	}

	private ImageIcon currentBluzaImageIcon;
	private ImageIcon currentTricouImageIcon;
	private ImageIcon currentGeacaImageIcon;
	private ImageIcon currentPapuciImageIcon;
	private ImageIcon currentPantaloniImageIcon;

	private String getImageUrl(String urlFindings0, String cathegory) {

		StringBuilder firstImageUrlSB = new StringBuilder("D:\\clothes\\"); /// de preluat url-ul generalizat la install
		firstImageUrlSB.append(cathegory);
		firstImageUrlSB.append("//");
		firstImageUrlSB.append(urlFindings0);
		return firstImageUrlSB.toString();
	}

	private void setElementsInPanel(JPanel mainPanel) {

		mainPanel.add(this.searchFilterButton);
		mainPanel.add(this.searchNameButton);
		mainPanel.add(this.setFilterButton);
		mainPanel.add(this.compareButton);
		mainPanel.add(this.choice);
		mainPanel.add(this.searchField);
	}

	private void updateFilters(Filters filtersObj, JPanel mainPanel, int width) {
		int startHeight = checkBoxesStartHeight;

		if (this.choice.getSelectedItem().toString().equals("Color")) {

			for (int i = 0; i < filtersObj.colorString.length; i++) {
				filtersObj.colorStringCheckBox[i] = new JCheckBox(filtersObj.colorString[i]);
				filtersObj.colorStringCheckBox[i].setBounds(width, startHeight + i * 20, elementWidth, elementHeight);
				mainPanel.add(filtersObj.colorStringCheckBox[i]);
				filtersObj.currentFilter = 0;

				if (filtersObj.selectionColorString[i] == true) {
					filtersObj.colorStringCheckBox[i].setSelected(true);
				}
			}
		}

		else if (this.choice.getSelectedItem().toString().equals("Elegance")) {

			for (int i = 0; i < 11; i++) {
				filtersObj.eleganceCheckBox[i] = new JCheckBox(Integer.toString(i));
				filtersObj.eleganceCheckBox[i].setBounds(width, startHeight + i * 20, elementWidth, elementHeight);
				mainPanel.add(filtersObj.eleganceCheckBox[i]);
				filtersObj.currentFilter = 1;

				if (filtersObj.selectionElegance[i] == true) {
					filtersObj.eleganceCheckBox[i].setSelected(true);
				}
			}
		}

		else if (this.choice.getSelectedItem().toString().equals("Seasons")) {

			for (int i = 0; i < 4; i++) {
				filtersObj.seasonsCheckBox[i] = new JCheckBox(filtersObj.seasons[i]);
				filtersObj.seasonsCheckBox[i].setBounds(width, startHeight + i * 20, elementWidth, elementHeight);
				mainPanel.add(filtersObj.seasonsCheckBox[i]);
				filtersObj.currentFilter = 2;

				if (filtersObj.selectionSeasons[i] == true) {
					filtersObj.seasonsCheckBox[i].setSelected(true);
				}
			}
		}

		else if (this.choice.getSelectedItem().toString().equals("Other Filters")) {

			for (int i = 0; i < filtersObj.otherFilters.length; i++) {
				filtersObj.otherFiltersCheckBox[i] = new JCheckBox(filtersObj.otherFilters[i]);
				filtersObj.otherFiltersCheckBox[i].setBounds(width, startHeight + i * 20, elementWidth, elementHeight);
				mainPanel.add(filtersObj.otherFiltersCheckBox[i]);
				filtersObj.currentFilter = 3;

				if (filtersObj.selectionOtherFilters[i] == true) {
					filtersObj.otherFiltersCheckBox[i].setSelected(true);
				}
			}
		}
		mainPanel.updateUI();
	}

	private void deleteFilters(Filters filtersObj, JPanel mainPanel) {
		try {
			if (filtersObj.currentFilter == 0) {
				for (int i = 0; i < filtersObj.colorString.length; i++) {
					filtersObj.selectionColorString[i] = (filtersObj.colorStringCheckBox[i].isSelected());
					filtersObj.colorStringCheckBox[i].setVisible(false);
					mainPanel.remove(filtersObj.colorStringCheckBox[i]);
				}
			}

			else if (filtersObj.currentFilter == 1) {
				for (int i = 0; i < 11; i++) {
					filtersObj.selectionElegance[i] = (filtersObj.eleganceCheckBox[i].isSelected());
					filtersObj.eleganceCheckBox[i].setVisible(false);
					mainPanel.remove(filtersObj.eleganceCheckBox[i]);
				}
			}

			else if (filtersObj.currentFilter == 2) {
				for (int i = 0; i < 4; i++) {
					filtersObj.selectionSeasons[i] = (filtersObj.seasonsCheckBox[i].isSelected());
					filtersObj.seasonsCheckBox[i].setVisible(false);
					mainPanel.remove(filtersObj.seasonsCheckBox[i]);
				}
			}

			else if (filtersObj.currentFilter == 3) {
				for (int i = 0; i < filtersObj.otherFilters.length; i++) {
					filtersObj.selectionOtherFilters[i] = (filtersObj.otherFiltersCheckBox[i].isSelected());
					filtersObj.otherFiltersCheckBox[i].setVisible(false);
					mainPanel.remove(filtersObj.otherFiltersCheckBox[i]);
				}
			}

			mainPanel.updateUI();
		}

		catch (Exception ex) {
			System.out.println(ex + " at private void deleteFilters");
		}
	}

	private void adjustAndDisplayCombination(LabelsGroup labelsGroup) {
		currentBluzaImageIcon = getAdjustedImageIcon(combinationsObj[indexCombinationObj].bluza, "Bluze");
		currentTricouImageIcon = getAdjustedImageIcon(combinationsObj[indexCombinationObj].tricou, "Tricouri");
		currentGeacaImageIcon = getAdjustedImageIcon(combinationsObj[indexCombinationObj].geaca, "Geci");
		currentPapuciImageIcon = getAdjustedImageIcon(combinationsObj[indexCombinationObj].papuci, "Papuci");
		currentPantaloniImageIcon = getAdjustedImageIcon(combinationsObj[indexCombinationObj].pantaloni, "Pantaloni");

		labelsGroup.setCombinationImages(currentBluzaImageIcon, currentTricouImageIcon, currentGeacaImageIcon,
				currentPapuciImageIcon, currentPantaloniImageIcon);
	}

	private ImageIcon getAdjustedImageIcon(String imageName, String cathegory) {
		return new ImageIcon(new ImageIcon(getImageUrl(imageName, cathegory)).getImage().getScaledInstance(imageWidth,
				imageHeight, Image.SCALE_SMOOTH));
	}

	private void analyzeCheckBoxes() {

		cntColor = cntElegance = cntSeason = cntOther = 0;

		for (int i = 0; i < filtersObj.colorString.length; i++) {
			try {
				if (filtersObj.colorStringCheckBox[i].isSelected()) {

					colorIndexSelected[cntColor++] = i;
				}
			} catch (Exception ex) {
				// System.out.println(ex + "setFunctionalityToButtons->colorStringCheckBox");
			}
		}

		for (int i = 0; i < 11; i++) {
			try {
				if (filtersObj.eleganceCheckBox[i].isSelected()) {

					// System.out.println(i);
					eleganceIndexSelected[cntElegance++] = i;
				}
			} catch (Exception ex) {
				// System.out.println(ex + "setFunctionalityToButtons->eleganceCheckBox");
			}
		}

		for (int i = 0; i < 3; i++) {
			try {
				if (filtersObj.seasonsCheckBox[i].isSelected()) {

					// System.out.println(filtersObj.seasons[i]);
					seasonIndexSelected[cntSeason++] = i;
				}
			} catch (Exception ex) {
				// System.out.println(ex + "setFunctionalityToButtons->seasonsCheckBox");
			}
		}

		for (int i = 0; i < filtersObj.otherFilters.length; i++) {
			try {
				if (filtersObj.otherFiltersCheckBox[i].isSelected()) {

					// System.out.println(filtersObj.otherFilters[i]);
					otherFiltersIndexSelected[cntOther++] = i;
				}
			} catch (Exception ex) {
				// System.out.println(ex + "setFunctionalityToButtons->otherFiltersCheckBox");
			}
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					new Combinations(null, null, null, null, null).mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

class LabelsGroup implements Constants {

	JLabel bluzeLabel;
	JLabel tricouriLabel;
	JLabel papuciLabel;
	JLabel pantaloniLabel;
	JLabel geciLabel;

	// public int yPosition = buttonHeight * 2;

	LabelsGroup(JPanel mainPanel, int yPosition) {
		this.bluzeLabel = new JLabel("Bluze");
		this.tricouriLabel = new JLabel("Tricouri");
		this.papuciLabel = new JLabel("Papcui");
		this.geciLabel = new JLabel("Geci");
		this.pantaloniLabel = new JLabel("Pantaloni");

		this.bluzeLabel.setBounds(buttonWidth + elementWidth, yPosition, imageWidth, imageHeight);
		this.tricouriLabel.setBounds(buttonWidth + elementWidth + imageWidth, yPosition, imageWidth, imageHeight);
		this.papuciLabel.setBounds(buttonWidth + elementWidth + 2 * imageWidth, yPosition, imageWidth, imageHeight);
		this.geciLabel.setBounds(buttonWidth + elementWidth + 3 * imageWidth, yPosition, imageWidth, imageHeight);
		this.pantaloniLabel.setBounds(buttonWidth + elementWidth + 4 * imageWidth, yPosition, imageWidth, imageHeight);

		setElementsInPanel(mainPanel);
	}

	private void setElementsInPanel(JPanel mainPanel) {
		mainPanel.add(this.bluzeLabel);
		mainPanel.add(this.tricouriLabel);
		mainPanel.add(this.papuciLabel);
		mainPanel.add(this.geciLabel);
		mainPanel.add(this.pantaloniLabel);
	}

	public void setCombinationImages(ImageIcon bluza, ImageIcon tricou, ImageIcon geaca, ImageIcon papuci,
			ImageIcon pantaloni) {

		this.bluzeLabel.setIcon(bluza);
		this.tricouriLabel.setIcon(tricou);
		this.papuciLabel.setIcon(geaca);
		this.geciLabel.setIcon(papuci);
		this.pantaloniLabel.setIcon(pantaloni);

	}
}
