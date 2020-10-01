package mainPackage;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Element implements Constants {

	private ClothObj[] clothObj;
	
	public String[] urlFindings;
	public int urlFindingsIndex = 0;
	public ImageIcon currentClothImageIcon;

	private Statement st;
	private ResultSet rs;

	Element(int imageX, int imageY, JPanel mainPanel, String[] colorString, String[] otherFilters, ClothObj[] clothObj,
			String elementCathegory, Statement st, ResultSet rs) {

		this.clothObj = clothObj;
		this.elementCathegory = elementCathegory;
		this.st = st;
		this.rs = rs;
		this.elementCathegory = elementCathegory;

		this.filtersObj = new Filters(colorString, otherFilters);
		initIntegerArrays();
		this.element = new JLabel(elementCathegory);
		this.element.setBounds(imageX, imageY, imageWidth, imageHeight);
		this.textField.setBounds(imageX, imageY - elementHeight, elementWidth, elementHeight);
		this.searchButton.setBounds(imageX, imageY - elementHeight * 2, elementWidth, elementHeight);
		this.setFilterButton.setBounds(imageX, imageY - elementHeight * 3, elementWidth, elementHeight);
		this.searchByFilterButton.setBounds(imageX, imageY - elementHeight * 4, elementWidth, elementHeight);
		this.addClothToFavouritesButton.setBounds(imageX, imageY - elementHeight * 5, elementWidth, elementHeight);
		this.displayFavourites.setBounds(imageX, imageY - elementHeight * 6, elementWidth, elementHeight);
		this.browseLeft.setBounds(imageX, imageY - elementHeight * 7, elementWidth / 2, elementHeight);
		this.browseRight.setBounds(imageX + elementWidth / 2, imageY - elementHeight * 7, elementWidth / 2,
				elementHeight);

		this.choice.setBounds(imageX, imageY + 200, elementWidth, elementHeight);
		this.choice.setBackground(Color.cyan);
		choice.add("");
		choice.add("Color");
		choice.add("Elegance");
		choice.add("Seasons");
		choice.add("Other Filters");
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				deleteFilters(filtersObj, mainPanel);
				updateFilters(filtersObj, mainPanel, imageX);
			}
		});

		setFunctionalityToButtons();
		setElementsInPanel(mainPanel);
	}

	private void initIntegerArrays() {
		colorIndexSelected = new int[COLOR_FILTERS_NR];
		otherFiltersIndexSelected = new int[OTHER_FILTERS_NR];

		for (int i = 0; i < COLOR_FILTERS_NR; i++) {
			colorIndexSelected[i] = -1;
		}

		for (int i = 0; i < OTHER_FILTERS_NR; i++) {
			otherFiltersIndexSelected[i] = -1;
		}

		for (int i = 0; i < 4; i++) {
			seasonIndexSelected[i] = -1;
		}

		for (int i = 0; i < 11; i++) {
			eleganceIndexSelected[i] = -1;
		}
	}

	JButton button;
	Filters filtersObj;
	JLabel element;
	JTextField textField = new JTextField();
	JButton searchButton = new JButton("Search By Name");
	JButton setFilterButton = new JButton("Set Filters To This Cloth");
	JButton searchByFilterButton = new JButton("Search By Filter");
	JButton addClothToFavouritesButton = new JButton("Add cloth to favourites");
	JButton browseRight = new JButton("->");
	JButton browseLeft = new JButton("<-");
	JButton displayFavourites = new JButton("Show favourites");
	Choice choice = new Choice();
	private String elementCathegory;

	private void setElementsInPanel(JPanel mainPanel) {

		mainPanel.add(this.textField);
		mainPanel.add(this.searchButton);
		mainPanel.add(this.setFilterButton);
		mainPanel.add(this.searchByFilterButton);
		mainPanel.add(this.addClothToFavouritesButton);
		mainPanel.add(this.choice);
		mainPanel.add(this.element);
		mainPanel.add(this.displayFavourites);
		mainPanel.add(this.browseLeft);
		mainPanel.add(this.browseRight);
	}

	private void updateFilters(Filters filtersObj, JPanel mainPanel, int imageX) {

		int startHeight = (int) (screenSize.getHeight() * 1.25) / 10 + 220;
		int width = imageX;

		if (this.choice.getSelectedItem().toString().equals("Color")) {

			for (int i = 0; i < filtersObj.colorString.length; i++) {
				filtersObj.colorStringCheckBox[i] = new JCheckBox(filtersObj.colorString[i]);
				filtersObj.colorStringCheckBox[i].setBounds(width, startHeight + (i+1) * 20, elementWidth, elementHeight);
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
				filtersObj.eleganceCheckBox[i].setBounds(width, startHeight + (i+1) * 20, elementWidth, elementHeight);
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
				filtersObj.seasonsCheckBox[i].setBounds(width, startHeight + (i+1) * 20, elementWidth, elementHeight);
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
				filtersObj.otherFiltersCheckBox[i].setBounds(width, startHeight + (i+1) * 20, elementWidth, elementHeight);
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

	private int[] colorIndexSelected;
	private int[] eleganceIndexSelected = new int[12];
	private int[] seasonIndexSelected = new int[4];
	private int[] otherFiltersIndexSelected;
	private int cntColor = 0, cntElegance = 0, cntSeason = 0, cntOther = 0;

	private void setFunctionalityToButtons() {

		ButtonsActions buttonsActionsObj = new ButtonsActions(st, rs);

		this.searchByFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

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
				UrlProvider urlObj = new UrlProvider(filtersObj.colorString, filtersObj.seasons,
						filtersObj.otherFilters, colorIndexSelected, cntColor, eleganceIndexSelected, cntElegance,
						seasonIndexSelected, cntSeason, otherFiltersIndexSelected, cntOther, clothObj);

				urlFindings = urlObj.getImages();
				ImageIcon clothImageIcon = new ImageIcon(new ImageIcon(getImageUrl(urlFindings[0], elementCathegory))
						.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
				element.setIcon(clothImageIcon);

				currentClothImageIcon = clothImageIcon;
			}
		});

		this.searchButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				String searchResult = buttonsActionsObj.search(textField.getText(), clothObj);

				if (searchResult == null) {
					javax.swing.JOptionPane.showMessageDialog(null, "Could not find cloth with this name");
				}

				else {

					ImageIcon clothImageIcon = new ImageIcon(new ImageIcon(getImageUrl(searchResult, elementCathegory))
							.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
					element.setIcon(clothImageIcon);

					currentClothImageIcon = clothImageIcon;
				}
			}
		});

		this.setFilterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				buttonsActionsObj.setFilters(elementCathegory, filtersObj, clothObj[urlFindingsIndex].id);

				clothObj[urlFindingsIndex].colorFilter = buttonsActionsObj.getFilterString(filtersObj.colorString,
						filtersObj.colorStringCheckBox, COLOR_FILTERS_NR);
				clothObj[urlFindingsIndex].cntColorFilter = clothObj[urlFindingsIndex].colorFilter.length;

				clothObj[urlFindingsIndex].seasonFilter = buttonsActionsObj.getFilterString(filtersObj.seasons,
						filtersObj.seasonsCheckBox, SEASON_FILTERS_NR);
				clothObj[urlFindingsIndex].cntSeasonFilter = clothObj[urlFindingsIndex].seasonFilter.length;

				clothObj[urlFindingsIndex].eleganceFilter = buttonsActionsObj.getFilterString(null,
						filtersObj.eleganceCheckBox, ELEGANCE_FILTERS_NR);
				clothObj[urlFindingsIndex].cntEleganceFilter = clothObj[urlFindingsIndex].eleganceFilter.length;

				clothObj[urlFindingsIndex].othersFilter = buttonsActionsObj.getFilterString(filtersObj.otherFilters,
						filtersObj.otherFiltersCheckBox, OTHER_FILTERS_NR);
				clothObj[urlFindingsIndex].cntOthersFilter = clothObj[urlFindingsIndex].othersFilter.length;
			}
		});

		this.addClothToFavouritesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				buttonsActionsObj.addClothToFavourites(clothObj[urlFindingsIndex].id, elementCathegory);
				clothObj[urlFindingsIndex].favourites = true;
			}
		});

		this.browseLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					urlFindingsIndex--;

					if (urlFindingsIndex == -1) {
						urlFindingsIndex = urlFindings.length - 1;
					}

					ImageIcon clothImageIcon = new ImageIcon(
							new ImageIcon(getImageUrl(urlFindings[urlFindingsIndex], elementCathegory)).getImage()
									.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
					element.setIcon(clothImageIcon);
					currentClothImageIcon = clothImageIcon;
				} catch (Exception ex) {

				}
			}
		});

		this.browseRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					urlFindingsIndex++;

					if (urlFindingsIndex == urlFindings.length) {
						urlFindingsIndex = 0;

					}

					ImageIcon clothImageIcon = new ImageIcon(
							new ImageIcon(getImageUrl(urlFindings[urlFindingsIndex], elementCathegory)).getImage()
									.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
					element.setIcon(clothImageIcon);
					currentClothImageIcon = clothImageIcon;
				} catch (Exception ex) {

				}
			}
		});

		this.displayFavourites.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				urlFindings = buttonsActionsObj.getFavouriteCloths(clothObj);
				urlFindingsIndex = 0;

				try {
					ImageIcon clothImageIcon = new ImageIcon(
							new ImageIcon(getImageUrl(urlFindings[urlFindingsIndex], elementCathegory)).getImage()
									.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
					element.setIcon(clothImageIcon);
					currentClothImageIcon = clothImageIcon;
					/// this can be put into a function
				} catch (Exception ex) {
					javax.swing.JOptionPane.showMessageDialog(null, "No favourites here sry");
				}
			}
		});
	}

	private int nextHeightButton = 0;
	private int buttonsDefaultWidth = 0;

//	Element(String buttonText, Color0 buttonColor, JPanel mainPanel) {
//		this.button = new JButton(buttonText);
//		button.setBounds(buttonsDefaultWidth, nextHeightButton, buttonWidth, buttonHeight);
//		button.setBackground(new Color(buttonColor.colorParam0, buttonColor.colorParam1, buttonColor.colorparam2));
//		mainPanel.add(button);
//		
//		this.nextHeightButton += (buttonHeight*3)/2;
//	}

	private String getImageUrl(String urlFindings0, String cathegory) {

		StringBuilder firstImageUrlSB = new StringBuilder("D:\\clothes\\"); /// de preluat url-ul generalizat la install
		firstImageUrlSB.append(cathegory);
		firstImageUrlSB.append("//");
		firstImageUrlSB.append(urlFindings0);
		return firstImageUrlSB.toString();
	}
}

class Filters {

	int currentFilter = -1;

	String[] colorString;
	boolean[] selectionColorString;
	JCheckBox[] colorStringCheckBox;

	private int elegance; // 0-10
	boolean[] selectionElegance;
	JCheckBox[] eleganceCheckBox;

	public void modifyElegance(int value) {
		if (value >= 0 && value <= 10) {
			this.elegance = value;
		} else if (value > 10) {
			this.elegance = 10;
		} else {
			this.elegance = 0;
		}
	}

	String[] seasons = { "Summer", "Winter", "Spring", "Autumn" };
	boolean[] selectionSeasons;
	JCheckBox[] seasonsCheckBox;

	String[] otherFilters;
	boolean[] selectionOtherFilters;
	JCheckBox[] otherFiltersCheckBox;

	public void initColorString(int nrColors) {
		this.colorString = new String[nrColors];
	}

	public void initOtherFilters(int nrOtherFilters) {
		this.otherFilters = new String[nrOtherFilters];
	}

	public Filters(String[] colorString, String[] otherFilters) {
		this.colorString = colorString;
		this.colorStringCheckBox = new JCheckBox[this.colorString.length];
		this.selectionColorString = new boolean[this.colorString.length];

		this.eleganceCheckBox = new JCheckBox[11];
		this.selectionElegance = new boolean[11];

		this.otherFilters = otherFilters;
		this.otherFiltersCheckBox = new JCheckBox[this.otherFilters.length];
		this.selectionOtherFilters = new boolean[this.otherFilters.length];

		this.seasonsCheckBox = new JCheckBox[4];
		this.selectionSeasons = new boolean[4];
	}
}
