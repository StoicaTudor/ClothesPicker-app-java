package mainPackage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database implements Constants {

	private Connection con;
	private Statement st;
	private ResultSet rs;

	Database() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306", user = "root", password = "";
			Connection conn = DriverManager.getConnection(url, user, password);

			Statement stmt = conn.createStatement();

			rs = conn.getMetaData().getCatalogs();
			boolean dbUserExists = false;

			while (rs.next()) {
				String databaseName = rs.getString(1);

				if (databaseName.equals("clothes")) {
					dbUserExists = true;
					break;
				}
			}

			if (!dbUserExists) {

				/// create database
				String createDB = "CREATE DATABASE IF NOT EXISTS clothes";
				stmt.executeUpdate(createDB);

				StringBuilder urlSB = new StringBuilder();
				urlSB.append(url);
				urlSB.append("/clothes");
				url = urlSB.toString();
				con = DriverManager.getConnection(url, user, password);
				st = con.createStatement();

				// create tables
				createTablePapuci();
				createTableGeci();
				createTableTricouri();
				createTableBluze();
				createTablePantaloni();
				createTableCombinations();
				createTableFilters();

				ClothesPath installObj = new ClothesPath();
				installObj.frame.setVisible(true);

				while (installObj.url == null) {/// wait until user introduces the path
				//	System.out.println(""); /// otherwise the code wont work
				}

				insertAllClothesInDB(new File(installObj.url));
				insertDefaultFilters();

				String[] colorString = { "white", "black", "red", "green", "blue", "brown", "grey", "turcuaz", "pink",
						"visiniu", "orange", "yellow", "crem" };
				String[] otherFilters = { "scoala", "casual", "majorate", "sport", "elegant/date" };

				Filters filtersObj = new Filters(colorString, otherFilters);

				new Menu(filtersObj).mainFrame.setVisible(true);

			} else {
				StringBuilder urlSB = new StringBuilder();
				urlSB.append(url);
				urlSB.append("/clothes");
				url = urlSB.toString();
				con = DriverManager.getConnection(url, user, password);
				st = con.createStatement();

				String[] colorFilter = getDBString("SELECT filterName FROM clothes.filters WHERE filterTypeIndex=0");
				String[] otherFilters = getDBString("SELECT filterName FROM clothes.filters WHERE filterTypeIndex=3");

				ClothObj[] bluzeObj = getDBClothObj("SELECT * FROM clothes.bluze",
						MAX_NR_CLOTHES_USER_CAN_INTRODUCE_PER_SESSION_PER_CATHEGORY);

				ClothObj[] tricouriObj = getDBClothObj("SELECT * FROM clothes.tricouri",
						MAX_NR_CLOTHES_USER_CAN_INTRODUCE_PER_SESSION_PER_CATHEGORY);

				ClothObj[] geciObj = getDBClothObj("SELECT * FROM clothes.geci",
						MAX_NR_CLOTHES_USER_CAN_INTRODUCE_PER_SESSION_PER_CATHEGORY);
				ClothObj[] pantaloniObj = getDBClothObj("SELECT * FROM clothes.pantaloni",
						MAX_NR_CLOTHES_USER_CAN_INTRODUCE_PER_SESSION_PER_CATHEGORY);
				ClothObj[] papuciObj = getDBClothObj("SELECT *  FROM clothes.papuci",
						MAX_NR_CLOTHES_USER_CAN_INTRODUCE_PER_SESSION_PER_CATHEGORY);

				CombinationObj[] combinationsObj = getCombinationsFromDB();
				new Menu(colorFilter, otherFilters, bluzeObj, tricouriObj, geciObj, pantaloniObj, papuciObj,
						combinationsObj, this.st, this.rs);
			}

		} catch (Exception ex) {
			String className = this.getClass().getSimpleName();
			System.out.println(className + " DatabaseConnect " + " : " + ex);
			javax.swing.JOptionPane.showMessageDialog(null, "An error occured. App doesn't work");
		}
	}

	private String[] getDBString(String query) {

		try {
			rs = st.executeQuery(query);
			int currentNr = getNrRowsCurrentResultSet();
			String[] returnString = new String[currentNr];
			int index = 0;

			do {
				returnString[index++] = rs.getString(1);
			} while (rs.next());

			return returnString;

		} catch (SQLException ex) {
			System.out.println(ex + " : in getDBString");
			return null;
		}

	}

	private CombinationObj[] getCombinationsFromDB() {
		String combinationsQuery = "SELECT * from clothes.combinations";

		try {
			rs = st.executeQuery(combinationsQuery);
			int currentNrCombinations = getNrRowsCurrentResultSet();
			CombinationObj[] obj = new CombinationObj[currentNrCombinations
					+ MAX_NR_COMBINATIONS_USER_CAN_INTRODUCE_PER_SESSION];
			int combinationIndex = 0;

			do {
				int id = rs.getInt(1);
				boolean enabled = rs.getBoolean(2);
				String name = rs.getString(3);

				String tricou = rs.getString(4);
				String bluza = rs.getString(5);
				String geaca = rs.getString(6);
				String papuci = rs.getString(7);
				String pantaloni = rs.getString(8);

				String colorFilter = rs.getString(9);
				String seasonFilter = rs.getString(10);
				String eleganceFilter = rs.getString(11);
				String otherFilter = rs.getString(12);

				obj[combinationIndex++] = new CombinationObj(id, enabled, name, tricou, bluza, geaca, papuci, pantaloni,
						colorFilter, seasonFilter, eleganceFilter, otherFilter);
			} while (rs.next());

			return obj;

		} catch (SQLException ex) {
			System.out.println(ex + " : in getCombinationsFromDB");

			return null;
		}
	}

	private ClothObj[] getDBClothObj(String query, int maxAdditionalMemorySpace) {

		try {
			rs = st.executeQuery(query);

			int nrElementsInDB = getNrRowsCurrentResultSet();
			ClothObj[] obj = new ClothObj[nrElementsInDB + maxAdditionalMemorySpace];
			int objIndex = 0;

			int id;
			boolean enabled, favourites;
			String name, colorFilter, seasonFilter, eleganceFilter, otherFilters;

			do {
				id = rs.getInt(1);
				enabled = rs.getBoolean(2);
				favourites = rs.getBoolean(3);
				name = rs.getString(4);
				colorFilter = rs.getString(5);
				seasonFilter = rs.getString(6);
				eleganceFilter = rs.getString(7);
				otherFilters = rs.getString(8);

				obj[objIndex] = new ClothObj(id, enabled, favourites, name, colorFilter, seasonFilter, eleganceFilter,
						otherFilters);

				objIndex++;
			} while (rs.next());

			return obj;

		} catch (SQLException ex) {
			System.out.println(ex + " : in getDBString");
			return null;
		}
	}

	private int getNrRowsCurrentResultSet() {

		int nrRowss = 0;

		try {
			rs.last();
			nrRowss = rs.getRow();
			rs.first();
		} catch (SQLException ex) {
			System.out.println(ex + " : in getNrRowsCurrentResultSet");
		}

		return nrRowss;
	}

	private void createTableGeci() {
		String createTableGeciQuery = "CREATE TABLE IF NOT EXISTS clothes.geci (id INT NOT NULL AUTO_INCREMENT, enabled BOOLEAN NOT NULL, favourites BOOLEAN NOT NULL,name VARCHAR(25) NOT NULL, "
				+ "colorFilter VARCHAR(100) NOT NULL, seasonFilter VARCHAR(50) NOT NULL, eleganceFilter VARCHAR(50) NOT NULL, otherFilters VARCHAR(100) NOT NULL, "
				+ "PRIMARY KEY (id)) ENGINE=InnoDB;";

		try {
			st.executeUpdate(createTableGeciQuery);
		} catch (SQLException ex) {
			System.out.println(ex + " in createTablePapuci");
		}
	}

	private void createTablePapuci() {
		String createTablePapuciQuery = "CREATE TABLE IF NOT EXISTS clothes.papuci (id INT NOT NULL AUTO_INCREMENT, enabled BOOLEAN NOT NULL, favourites BOOLEAN NOT NULL,name VARCHAR(25) NOT NULL, "
				+ "colorFilter VARCHAR(100) NOT NULL, seasonFilter VARCHAR(50) NOT NULL, eleganceFilter VARCHAR(50) NOT NULL, otherFilters VARCHAR(100) NOT NULL, "
				+ "PRIMARY KEY (id)) ENGINE=InnoDB;";

		try {
			st.executeUpdate(createTablePapuciQuery);
		} catch (SQLException ex) {
			System.out.println(ex + " in createTablePapuci");
		}
	}

	private void createTableTricouri() {
		String createTableTricouriQuery = "CREATE TABLE IF NOT EXISTS clothes.tricouri (id INT NOT NULL AUTO_INCREMENT, enabled BOOLEAN NOT NULL, favourites BOOLEAN NOT NULL,name VARCHAR(25) NOT NULL, "
				+ "colorFilter VARCHAR(100) NOT NULL, seasonFilter VARCHAR(50) NOT NULL, eleganceFilter VARCHAR(50) NOT NULL, otherFilters VARCHAR(100) NOT NULL, "
				+ "PRIMARY KEY (id)) ENGINE=InnoDB;";

		try {
			st.executeUpdate(createTableTricouriQuery);
		} catch (SQLException ex) {
			System.out.println(ex + " in createTableTricouri");
		}
	}

	private void createTableBluze() {
		String createTableBluzeQuery = "CREATE TABLE IF NOT EXISTS clothes.bluze (id INT NOT NULL AUTO_INCREMENT, enabled BOOLEAN NOT NULL, favourites BOOLEAN NOT NULL,name VARCHAR(25) NOT NULL, "
				+ "colorFilter VARCHAR(100) NOT NULL, seasonFilter VARCHAR(50) NOT NULL, eleganceFilter VARCHAR(50) NOT NULL, otherFilters VARCHAR(100) NOT NULL, "
				+ "PRIMARY KEY (id)) ENGINE=InnoDB;";

		try {
			st.executeUpdate(createTableBluzeQuery);
		} catch (SQLException ex) {
			System.out.println(ex + " in createTableBluze");
		}
	}

	private void createTablePantaloni() {

		String createTablePantaloniQuery = "CREATE TABLE IF NOT EXISTS clothes.pantaloni (id INT NOT NULL AUTO_INCREMENT, enabled BOOLEAN NOT NULL, favourites BOOLEAN NOT NULL,name VARCHAR(25) NOT NULL, "
				+ "colorFilter VARCHAR(100) NOT NULL, seasonFilter VARCHAR(50) NOT NULL, eleganceFilter VARCHAR(50) NOT NULL, otherFilters VARCHAR(100) NOT NULL, "
				+ "PRIMARY KEY (id)) ENGINE=InnoDB;";

		try {
			st.executeUpdate(createTablePantaloniQuery);
		} catch (SQLException ex) {
			System.out.println(ex + " in createTablePantaloni");
		}
	}

	private void createTableCombinations() {

		String createTableCombinationsQuery = "CREATE TABLE IF NOT EXISTS clothes.combinations (id INT NOT NULL AUTO_INCREMENT, enabled BOOLEAN NOT NULL, name VARCHAR(25) NOT NULL,"
				+ "tricou VARCHAR(25) NOT NULL, bluza VARCHAR(25) NOT NULL, geaca VARCHAR(25) NOT NULL, papuci VARCHAR(25) NOT NULL,pantaloni VARCHAR(25) NOT NULL,"
				+ "colorFilter VARCHAR(100) NOT NULL, seasonFilter VARCHAR(50) NOT NULL, eleganceFilter VARCHAR(50) NOT NULL, otherFilters VARCHAR(100) NOT NULL, "
				+ "PRIMARY KEY (id)) ENGINE=InnoDB;";

		try {
			st.executeUpdate(createTableCombinationsQuery);
		} catch (SQLException ex) {
			System.out.println(ex + " in createTableCombinations");
		}
	}

	private void createTableFilters() {

		String createTableFiltersQuery = "CREATE TABLE IF NOT EXISTS clothes.filters(id INT NOT NULL AUTO_INCREMENT, filterTypeIndex INT NOT NULL, "
				+ "filterName VARCHAR(20) NOT NULL,PRIMARY KEY (id)) ENGINE=InnoDB;";

		try {
			st.executeUpdate(createTableFiltersQuery);
		} catch (SQLException ex) {
			System.out.println(ex + " in createTableFilters");
		}
	}

	/*
	 * filterTypeIndex: 0- color 1 - season 2 - elegance 3 - other filters
	 */

	private void insertDefaultFilters() {

		String insertDefaultColorFiltersQuery = "INSERT INTO clothes.filters (id,filterTypeIndex,filterName) VALUES (NULL,0,'grey'),(NULL,0,'turcuaz'),"
				+ "(NULL,0,'pink'), (NULL,0,'visiniu'),(NULL,0,'orange'),(NULL,0,'white'),(NULL,0,'black'),(NULL,0,'red'),(NULL,0,'green'),(NULL,0,'blue'),"
				+ "(NULL,0,'brown'),(NULL,0,'yellow')";

		String insertDefaultSeasonFiltersQuery = "INSERT INTO clothes.filters (id,filterTypeIndex,filterName) VALUES (NULL,1,'winter'),(NULL,1,'spring'),"
				+ "(NULL,1,'summer'), (NULL,1,'autumn')";

		String insertDefaultEleganceFiltersQuery = "INSERT INTO clothes.filters (id,filterTypeIndex,filterName) VALUES (NULL,2,'0'),(NULL,2,'1'),"
				+ "(NULL,2,'2'), (NULL,2,'3'),(NULL,2,'4'),(NULL,2,'5'),(NULL,2,'6'),(NULL,2,'7'),(NULL,2,'8'),(NULL,2,'9'),"
				+ "(NULL,2,'10')";

		String insertDefaultOtherFiltersQuery = "INSERT INTO clothes.filters (id,filterTypeIndex,filterName) VALUES (NULL,3,'scoala'),(NULL,3,'afara'),"
				+ "(NULL,3,'sport'),(NULL,3,'majorate')";

		try {
			st.executeUpdate(insertDefaultColorFiltersQuery);
			st.executeUpdate(insertDefaultSeasonFiltersQuery);
			st.executeUpdate(insertDefaultEleganceFiltersQuery);
			st.executeUpdate(insertDefaultOtherFiltersQuery);
		} catch (SQLException ex) {
			System.out.println(ex + " in insertDefaultFilters");
		}
	}

	private void insertAllClothesInDB(final File folder) {

		int cntBluze = 0, cntTricouri = 0, cntPapuci = 0, cntPantaloni = 0, cntGeci = 0;
		String[] bluzeString = new String[1005];
		String[] geciString = new String[1005];
		String[] pantaloniString = new String[1005];
		String[] tricouriString = new String[1005];
		String[] papuciString = new String[1005];

		for (final File fileEntry : folder.listFiles()) {

			String folderName = fileEntry.getName();

			if (folderName.equals("Bluze")) {

				for (final File fileEntry2 : fileEntry.listFiles()) {
					bluzeString[cntBluze++] = fileEntry2.getName();
				}

			} else if (folderName.equals("Pantaloni")) {

				for (final File fileEntry2 : fileEntry.listFiles()) {
					pantaloniString[cntPantaloni++] = fileEntry2.getName();
				}

			} else if (folderName.equals("Geci")) {

				for (final File fileEntry2 : fileEntry.listFiles()) {
					geciString[cntGeci++] = fileEntry2.getName();
				}

			} else if (folderName.equals("Tricouri")) {

				for (final File fileEntry2 : fileEntry.listFiles()) {
					tricouriString[cntTricouri++] = fileEntry2.getName();
				}

			} else if (folderName.equals("Papuci")) {

				for (final File fileEntry2 : fileEntry.listFiles()) {
					papuciString[cntPapuci++] = fileEntry2.getName();
				}

			}
		}

		StringBuilder insertBluze = new StringBuilder();
		insertBluze.append(
				"INSERT INTO clothes.bluze (id,enabled,favourites,name,colorFilter,seasonFilter,eleganceFilter,otherFilters) VALUES ");

		for (int i = 0; i < cntBluze; i++) {
			insertBluze.append("(NULL,'1','0','");
			insertBluze.append(bluzeString[i]);
			insertBluze.append("','','','','')");

			if (i < cntBluze - 1) {
				insertBluze.append(",");
			}
		}

		StringBuilder insertPantaloni = new StringBuilder();
		insertPantaloni.append(
				"INSERT INTO clothes.pantaloni (id,enabled,favourites,name,colorFilter,seasonFilter,eleganceFilter,otherFilters) VALUES ");

		for (int i = 0; i < cntPantaloni; i++) {
			insertPantaloni.append("(NULL,'1','0','");
			insertPantaloni.append(pantaloniString[i]);
			insertPantaloni.append("','','','','')");

			if (i < cntPantaloni - 1) {
				insertPantaloni.append(",");
			}
		}

		StringBuilder insertGeci = new StringBuilder();
		insertGeci.append(
				"INSERT INTO clothes.geci (id,enabled,favourites,name,colorFilter,seasonFilter,eleganceFilter,otherFilters) VALUES ");

		for (int i = 0; i < cntPantaloni; i++) {
			insertGeci.append("(NULL,'1','0','");
			insertGeci.append(geciString[i]);
			insertGeci.append("','','','','')");

			if (i < cntGeci - 1) {
				insertGeci.append(",");
			}
		}

		StringBuilder insertTricouri = new StringBuilder();
		insertTricouri.append(
				"INSERT INTO clothes.tricouri (id,enabled,favourites,name,colorFilter,seasonFilter,eleganceFilter,otherFilters) VALUES ");

		for (int i = 0; i < cntTricouri; i++) {
			insertTricouri.append("(NULL,'1','0','");
			insertTricouri.append(tricouriString[i]);
			insertTricouri.append("','','','','')");

			if (i < cntTricouri - 1) {
				insertTricouri.append(",");
			}
		}

		StringBuilder insertPapuci = new StringBuilder();
		insertPapuci.append(
				"INSERT INTO clothes.papuci (id,enabled,favourites,name,colorFilter,seasonFilter,eleganceFilter,otherFilters) VALUES ");

		for (int i = 0; i < cntPapuci; i++) {
			insertPapuci.append("(NULL,'1','0','");
			insertPapuci.append(papuciString[i]);
			insertPapuci.append("','','','','')");

			if (i < cntPapuci - 1) {
				insertPapuci.append(",");
			}
		}

		try {
			st.executeUpdate(insertBluze.toString());
			st.executeUpdate(insertGeci.toString());
			st.executeUpdate(insertPantaloni.toString());
			st.executeUpdate(insertPapuci.toString());
			st.executeUpdate(insertTricouri.toString());
		} catch (SQLException ex) {
			System.out.println(ex + " in insertAllClothesInDB");
		}
	}

	public static void main(String[] args) {
		new Database();
	}
}

class ClothObj implements Constants {

	int id;
	boolean enabled;
	boolean favourites;
	String name;

	String[] colorFilter;
	int cntColorFilter = 0;

	String[] seasonFilter;
	int cntSeasonFilter = 0;

	String[] eleganceFilter;
	int cntEleganceFilter = 0;

	String[] othersFilter;
	int cntOthersFilter = 0;

	String wrapFilterIntoString(String[] filter, int filterSize) {
		StringBuilder filterSB = new StringBuilder();

		for (int i = 0; i < filterSize - 1; i++) {
			filterSB.append(filter[i]);
			filterSB.append('-');
		}
		filterSB.append(filter[filterSize - 1]);

		return filterSB.toString();
	}

	ClothObj(int id, boolean enabled, boolean favourites, String name, String colorFilter, String seasonFilter,
			String eleganceFilter, String othersFilter) {
		this.id = id;
		this.enabled = enabled;
		this.favourites = favourites;
		this.name = name;

		this.colorFilter = getStringArrayFromString(colorFilter);
		this.seasonFilter = getStringArrayFromString(seasonFilter);
		this.eleganceFilter = getStringArrayFromString(eleganceFilter);
		this.othersFilter = getStringArrayFromString(othersFilter);

		if (this.colorFilter != null) {
			this.cntColorFilter = getSplittedStringLength(colorFilter);
		}

		if (this.seasonFilter != null) {
			this.cntSeasonFilter = getSplittedStringLength(seasonFilter);
		}

		if (this.eleganceFilter != null) {
			this.cntEleganceFilter = getSplittedStringLength(eleganceFilter);
		}

		if (this.othersFilter != null) {
			this.cntOthersFilter = getSplittedStringLength(othersFilter);
		}
	}

	private String[] getStringArrayFromString(String rawFilter) {

		if (rawFilter.equals("")) {
			return null;
		}

		/// else, we pass the splitted string, but with an enlarged size
		/// (MAX_NR_FILTERS)
		String[] auxiliarString = new String[COLOR_FILTERS_NR];
		String[] splittedString = rawFilter.split("-");

		for (int i = 0; i < splittedString.length; i++) {
			auxiliarString[i] = splittedString[i];
		}

		return auxiliarString;
	}

	private int getSplittedStringLength(String rawFilter) {
		return rawFilter.split("-").length;
	}
}

class CombinationObj extends ClothObj {
	String tricou;
	String pantaloni;
	String geaca;
	String bluza;
	String papuci;

	CombinationObj(int id, boolean enabled, String name, String tricou, String bluza, String geaca, String papuci,
			String pantaloni, String colorFilter, String seasonFilter, String eleganceFilter, String othersFilter) {

		super(id, enabled, false, name, colorFilter, seasonFilter, eleganceFilter, othersFilter);
		this.name = name;
		this.tricou = tricou;
		this.pantaloni = pantaloni;
		this.geaca = geaca;
		this.bluza = bluza;
		this.papuci = papuci;
	}
}
