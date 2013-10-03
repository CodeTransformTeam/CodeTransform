package CodeTransform;

import java.awt.Color;

public class ColorBuilder {
	public static Color ColorFromString(String str) {
		  int i =   Integer.parseInt(str.substring(1),16);   
		  return new Color(i);
		 }
}
