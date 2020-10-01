package mainPackage;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

public class ButtonsActions implements Constants {

	private Statement st;
	private ResultSet rs;

	ButtonsActions(Statement st, ResultSet rs) {
		this.st = st;
		this.rs = rs;
	}

	public void setFilters(String elementCathegory, Filters filtersObj, int id) {

		String setFiltersToClothQuery = getUpdateFiltersQuery(elementCathegory, filtersObj, id);

		try {
			this.st.executeUpdate(setFiltersToClothQuery);
		} catch (SQLException ex) {
			System.out.println(ex + " : in setFilters(String elementCathegory, Filters filtersObj, int id)");
		}
	}

	private String getUpdateFiltersQuery(String elementCathegory, Filters filtersObj, int id) {

		StringBuilder setFiltersToClothQuery = new StringBuilder("UPDATE clothes.");
		setFiltersToClothQuery.append(elementCathegory);
		setFiltersToClothQuery.append(" SET colorFilter=");
		setFiltersToClothQuery.append('"');

		try {
			for (int i = 0; i < filtersObj.colorString.length; i++) {

				if (filtersObj.colorStringCheckBox[i].isSelected()) {

					setFiltersToClothQuery.append(filtersObj.colorString[i]);
					setFiltersToClothQuery.append("-");
				}
			}
		} catch (NullPointerException ex) {
			setFiltersToClothQuery.append("");
		}

		setFiltersToClothQuery.append('"');
		setFiltersToClothQuery.append(", seasonFilter=");
		setFiltersToClothQuery.append('"');

		try {
			for (int i = 0; i < filtersObj.seasons.length; i++) {

				if (filtersObj.seasonsCheckBox[i].isSelected()) {

					setFiltersToClothQuery.append(filtersObj.seasons[i]);
					setFiltersToClothQuery.append("-");
				}
			}
		} catch (NullPointerException ex) {
			setFiltersToClothQuery.append("");
		}

		setFiltersToClothQuery.append('"');
		setFiltersToClothQuery.append(", eleganceFilter=");
		setFiltersToClothQuery.append('"');

		try {
			for (int i = 0; i < 11; i++) {

				if (filtersObj.eleganceCheckBox[i].isSelected()) {

					setFiltersToClothQuery.append(i);
					setFiltersToClothQuery.append("-");
				}
			}
		} catch (NullPointerException ex) {
			setFiltersToClothQuery.append("");
		}

		setFiltersToClothQuery.append('"');
		setFiltersToClothQuery.append(", otherFilters=");
		setFiltersToClothQuery.append('"');

		try {
			for (int i = 0; i < filtersObj.otherFilters.length; i++) {

				if (filtersObj.otherFiltersCheckBox[i].isSelected()) {

					setFiltersToClothQuery.append(filtersObj.otherFilters[i]);
					setFiltersToClothQuery.append("-");
				}
			}
		} catch (NullPointerException ex) {
			setFiltersToClothQuery.append("");
		}

		setFiltersToClothQuery.append('"');
		setFiltersToClothQuery.append(" WHERE id=");
		setFiltersToClothQuery.append(id);

		return setFiltersToClothQuery.toString();
	}

	public String[] getFilterString(String[] filterString, JCheckBox[] filterStringCheckBox, int nrFilters) {

		if (filterString != null) {

			String[] utilString = new String[nrFilters];
			int cnt = 0;

			for (int i = 0; i < filterString.length; i++) {

				if (filterStringCheckBox[i] == null) {
					break;
				}

				if (filterStringCheckBox[i].isSelected()) {
					utilString[cnt++] = filterString[i];
				}
			}

			String[] returnString = new String[cnt];

			for (int i = 0; i < cnt; i++) {
				returnString[i] = utilString[i];
			}

			return returnString;

		} else { /// elegance filter

			String[] utilString = new String[nrFilters];
			int cnt = 0;

			for (int i = 0; i < ELEGANCE_FILTERS_NR; i++) {

				if (filterStringCheckBox[i] == null) {
					break;
				}

				if (filterStringCheckBox[i].isSelected()) {

					utilString[cnt++] = Integer.toString(i + 48);
				}
			}

			String[] returnString = new String[cnt];

			for (int i = 0; i < cnt; i++) {
				returnString[i] = utilString[i];
			}

			return returnString;

		}
	}

	public void addClothToFavourites(int id, String elementCathegory) {

		StringBuilder addClothToFavouritesQuerySB = new StringBuilder("UPDATE clothes.");
		addClothToFavouritesQuerySB.append(elementCathegory);
		addClothToFavouritesQuerySB.append(" SET favourites=1 WHERE id=");
		addClothToFavouritesQuerySB.append(id);

		try {
			st.executeUpdate(addClothToFavouritesQuerySB.toString());
		} catch (SQLException ex) {
			System.out.println(ex + " : in addClothToFavourites(int id, String elementCathegory)");
		}
	}

	public String search(String text, ClothObj[] clothObj) { /// make sure it exists

		StringBuilder textJPG = new StringBuilder(text);
		textJPG.append(".jpeg");
		text = textJPG.toString();
		try {
			for (int i = 0; i < clothObj.length; i++) {
				if (text.equals(clothObj[i].name)) {
					return text;
				}
			}
		} catch (NullPointerException ex) {
			return null;
		}
		return null;
	}
	
	public CombinationObj searchCombination(String text, CombinationObj[] combinationObj) { /// make sure it exists
		
		try {
			for (int i = 0; i < combinationObj.length; i++) {
				if (text.equals(combinationObj[i].name)) {
					return combinationObj[i];
				}
			}
		} catch (NullPointerException ex) {
			return null;
		}
		return null;
	}

	public static ImageIcon getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		return new ImageIcon(resizedImg);
	}

	public String[] getFavouriteCloths(ClothObj[] clothObj) {

		String[] utilFavourites = new String[1005];
		int cnt = 0;

		for (int i = 0; i < clothObj.length; i++) {

			if (clothObj[i] == null) {
				break;
			}

			if (clothObj[i].favourites == true) {
				utilFavourites[cnt++] = clothObj[i].name;
			}
		}

		String[] returnStringFavourites = new String[cnt];

		for (int i = 0; i < cnt; i++) {
			returnStringFavourites[i] = utilFavourites[i];
		}

		return returnStringFavourites;
	}

	public void addCombinationToDB(String bluza, String tricou, String pantaloni, String geaca, String papuci,
			String name) {
		StringBuilder addCombinationSB = new StringBuilder(
				"INSERT into clothes.combinations (id,enabled,name,tricou,bluza,geaca,papuci,pantaloni,colorFilter,seasonFilter,eleganceFilter,otherFilters) VALUES (NULL,1,");

		addCombinationSB.append('"');
		addCombinationSB.append(name);
		addCombinationSB.append('"');
		addCombinationSB.append(',');
		
		addCombinationSB.append('"');
		addCombinationSB.append(tricou);
		addCombinationSB.append('"');
		addCombinationSB.append(',');

		addCombinationSB.append('"');
		addCombinationSB.append(bluza);
		addCombinationSB.append('"');
		addCombinationSB.append(',');

		addCombinationSB.append('"');
		addCombinationSB.append(geaca);
		addCombinationSB.append('"');
		addCombinationSB.append(',');

		addCombinationSB.append('"');
		addCombinationSB.append(papuci);
		addCombinationSB.append('"');
		addCombinationSB.append(',');

		addCombinationSB.append('"');
		addCombinationSB.append(pantaloni);
		addCombinationSB.append('"');

		addCombinationSB.append(',');
		addCombinationSB.append('"');
		addCombinationSB.append('"');

		addCombinationSB.append(',');
		addCombinationSB.append('"');
		addCombinationSB.append('"');

		addCombinationSB.append(',');
		addCombinationSB.append('"');
		addCombinationSB.append('"');

		addCombinationSB.append(',');
		addCombinationSB.append('"');
		addCombinationSB.append('"');
		addCombinationSB.append(')');

		try {
			
			st.executeUpdate(addCombinationSB.toString());
		} catch (SQLException ex) {
			System.out.println(ex
					+ " : in addCombinationToDB(String bluza, String tricou, String pantaloni, String geaca, String papuci,\r\n"
					+ "			String name)");
		}
	}
}