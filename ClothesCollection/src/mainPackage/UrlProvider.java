package mainPackage;

public class UrlProvider {

	private int[] colorIndexSelected;
	private int cntColor;
	private int[] eleganceIndexSelected;
	private int cntElegance;
	private int[] seasonIndexSelected;
	private int cntSeason;
	private int[] otherFiltersIndexSelected;
	private int cntOther;

	private ClothObj[] clothObj;
	private CombinationObj [] combinationObj;

	private String[] colorString;
	private String[] seasons;
	private String[] otherFilters;

	public UrlProvider(String[] colorString, String[] seasons, String[] otherFilters, int[] colorIndexSelected2,
			int cntColor, int[] eleganceIndexSelected2, int cntElegance, int[] seasonIndexSelected2, int cntSeason,
			int[] otherFiltersIndexSelected2, int cntOther, ClothObj[] clothObj) {

		this.colorString = colorString;
		this.seasons = seasons;
		this.otherFilters = otherFilters;

		this.colorIndexSelected = colorIndexSelected2;
		this.cntColor = cntColor;

		this.eleganceIndexSelected = eleganceIndexSelected2;
		this.cntElegance = cntElegance;

		this.seasonIndexSelected = seasonIndexSelected2;
		this.cntSeason = cntSeason;

		this.otherFiltersIndexSelected = otherFiltersIndexSelected2;
		this.cntOther = cntOther;

		this.clothObj = clothObj;
	}
	
	public UrlProvider(String[] colorString, String[] seasons, String[] otherFilters, int[] colorIndexSelected2,
			int cntColor, int[] eleganceIndexSelected2, int cntElegance, int[] seasonIndexSelected2, int cntSeason,
			int[] otherFiltersIndexSelected2, int cntOther, CombinationObj[] combinationObj) {

		this.colorString = colorString;
		this.seasons = seasons;
		this.otherFilters = otherFilters;

		this.colorIndexSelected = colorIndexSelected2;
		this.cntColor = cntColor;

		this.eleganceIndexSelected = eleganceIndexSelected2;
		this.cntElegance = cntElegance;

		this.seasonIndexSelected = seasonIndexSelected2;
		this.cntSeason = cntSeason;

		this.otherFiltersIndexSelected = otherFiltersIndexSelected2;
		this.cntOther = cntOther;

		this.combinationObj = combinationObj;
	}

	public String[] getImages() { /// brute force search

		String[] auxFindings = new String[1005];
		int cntFindings = 0;

		for (int i = 0; i < clothObj.length; i++) {
			try {
				if (clothObj[i].enabled && fulfillTheFilters(clothObj[i])) {
					// System.out.println(bluzeObj[i].name);
					auxFindings[cntFindings++] = clothObj[i].name;
				}
			} catch (NullPointerException ex) {
				// System.out.println(ex + " : public void getImages()");
			}
		}

		String[] findings = new String[cntFindings];

		for (int i = 0; i < cntFindings; i++) {
			findings[i] = auxFindings[i];
		}

		return findings;
	}
	
	public CombinationObj[] getCombinationObj() { /// brute force search

		CombinationObj[] auxFindings = new CombinationObj[1005];
		int cntFindings = 0;

		for (int i = 0; i < combinationObj.length; i++) {
			try {
				if (combinationObj[i].enabled && fulfillTheFilters(combinationObj[i])) {
					// System.out.println(bluzeObj[i].name);
					auxFindings[cntFindings++] = combinationObj[i];
				}
			} catch (NullPointerException ex) {
				// System.out.println(ex + " : public void getImages()");
			}
		}

		CombinationObj[] findings = new CombinationObj[cntFindings];

		for (int i = 0; i < cntFindings; i++) {
			findings[i] = auxFindings[i];
		}

		return findings;
	}

	private boolean fulfillTheFilters(ClothObj clothObj) {

		for (int i = 0; i < cntColor; i++) {

			if (clothObj.cntColorFilter == 0) {
				break;
			}

			if (colorIndexSelected[i] > -1) { // that filters is indeed pressed

				boolean isMatching = false; /// suppose the cloth does not fulfill the filter

				for (int j = 0; j < clothObj.cntColorFilter; j++) {

					if (colorString[colorIndexSelected[i]].equals(clothObj.colorFilter[j])) { /// they match that
						/// particular filter
						isMatching = true;
						break;
					}
				}

				if (isMatching == false) { // no matching for that particular filter
					return false;
				}
			}
		}

		for (int i = 0; i < cntSeason; i++) {

			if (clothObj.cntSeasonFilter == 0) {
				break;
			}

			if (seasonIndexSelected[i] > -1) { // that filters is indeed pressed

				boolean isMatching = false; /// suppose the cloth does not fulfil the filter

				for (int j = 0; j < clothObj.cntSeasonFilter; j++) {

					if (seasons[seasonIndexSelected[i]].equals(clothObj.seasonFilter[j])) { /// they match that
						/// particular filter
						isMatching = true;
						break;
					}
				}

				if (isMatching == false) { // no matching for that particular filter
					return false;
				}
			}
		}

		for (int i = 0; i < cntElegance; i++) {

			if (clothObj.cntEleganceFilter == 0) {
				break;
			}

			if (eleganceIndexSelected[i] > -1) { // that filters is indeed pressed

				boolean isMatching = false; /// suppose the cloth does not fulfil the filter

				for (int j = 0; j < clothObj.cntEleganceFilter; j++) {
					System.out
							.println(eleganceIndexSelected[i] + " " + charToInt(clothObj.eleganceFilter[j].charAt(0)));
					if (eleganceIndexSelected[i] == charToInt(clothObj.eleganceFilter[j].charAt(0))) { /// they match
																										/// that
						/// particular filter
						isMatching = true;
						break;
					}
				}

				if (isMatching == false) { // no matching for that particular filter
					return false;
				}
			}
		}

		for (int i = 0; i < cntOther; i++) {

			if (clothObj.cntOthersFilter == 0) {
				break;
			}

			if (otherFiltersIndexSelected[i] > -1) { // that filters is indeed pressed

				boolean isMatching = false; /// suppose the cloth does not fulfil the filter

				for (int j = 0; j < clothObj.cntOthersFilter; j++) {

					if (otherFilters[otherFiltersIndexSelected[i]].equals(clothObj.othersFilter[j])) { /// they match
																										/// that
						/// particular filter
						isMatching = true;
						break;
					}
				}

				if (isMatching == false) { // no matching for that particular filter
					return false;
				}
			}
		}
		return true; // if the thread reaches this point, clothObj fulfillTheFilters
	}

	private int charToInt(char c) {
		return (c - 48);
	}

}
