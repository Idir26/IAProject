package fr.dauphine.iaproject.autres;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.paint.Color;

public class StateForImage{
	BufferedImage bi;
	Color[][] target;
	
	public StateForImage(String targetImage) {
		File file = new File(targetImage);
		try {
			bi = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		computeColorTable();
	}
	
	private void computeColorTable() {
		int maxX=0;
    	int maxY=0;
		try{
			maxX = bi.getWidth();
			maxY = bi.getHeight();

        	target = new Color[maxX][maxY];
        	for (int i=0;i<maxX;i++){
        		for (int j=0;j<maxY;j++){
        			int argb = bi.getRGB(i, j);
        			int b = (argb)&0xFF;
        			int g = (argb>>8)&0xFF;
        			int r = (argb>>16)&0xFF;
        			int a = (argb>>24)&0xFF;
        			target[i][j] = Color.rgb(r,g,b);
        		}
        	}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public Color[][] getTarget() {
		return target;
	}

	
	public int getMaxX() {
		return bi.getWidth();
	}
	
	public int getMaxY() {
		return bi.getHeight();
	}
	
}
