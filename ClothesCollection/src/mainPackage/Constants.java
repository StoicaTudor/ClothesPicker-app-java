package mainPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public interface Constants {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double screenHeight = screenSize.getHeight() * 1.25;
	double screenWidth = screenSize.getWidth() * 1.25;
	
	int elementWidth=200;
	int elementHeight=20;
	
	int imageWidth=200;
	int imageHeight=200;
	
	int heightDistanceBetweenImages=300;
	
	int buttonWidth=200;
	int buttonHeight=50;
	
	int COLOR_FILTERS_NR=16;
	int OTHER_FILTERS_NR=4;
	int SEASON_FILTERS_NR=4;
	int ELEGANCE_FILTERS_NR=11;
	
	int MAX_NR_CLOTHES_USER_CAN_INTRODUCE_PER_SESSION_PER_CATHEGORY=50;
	int MAX_NR_COMBINATIONS_USER_CAN_INTRODUCE_PER_SESSION=100;
	
	Color buttonsColors=new Color(0,255,255);
}
