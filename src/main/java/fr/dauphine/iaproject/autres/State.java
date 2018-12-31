package fr.dauphine.iaproject.autres;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import fr.dauphine.iaproject.imageProcessing.ConvexPolygon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Un State est une representation de l'etat courant de l'avancee de l'algorithme.
 * Un State ne peut representer qu'un seul etat (un ensemble de polygone avec des caracteristiques 
 * bien definies), donc des qu'on change une caracteristique, il faut creer un nouveau State.
 *
 */
public class State  {
	List<ConvexPolygon> polys;
	WritableImage wimg;
	Group image;
	int maxX;
	int maxY;
	/**
	 * Distance de ce State par rapport au goal. Cette variable nous permet d'eviter de faire plusieurs
	 * appel a� la methode distance qui est une methode tres couteuse.
	 */
	Double distance;
	

	public State(State current) {
		polys = new ArrayList<>();
		for(ConvexPolygon poly : current.getPolys()) {			
			try {
				polys.add((ConvexPolygon)poly.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		initialiser();
	}
	
	public State(List<ConvexPolygon> polys) {
		this.polys = polys;
		initialiser();
	}

	protected void initialiser() {
		maxX = polys.get(0).getMax_X();
		maxY = polys.get(0).getMax_Y();
		// formation de l'image par superposition des polygones
		image = new Group();
		for (ConvexPolygon p : polys) {
			image.getChildren().add(p);
		}
		// Calcul de la couleur de chaque pixel.Pour cela, on passe par une instance de
		// WritableImage, qui possède une méthode pour obtenir un PixelReader.
		wimg = new WritableImage(maxX, maxY);
		takeSnapshot();
	}
	
	@Override
	public boolean equals(Object obj) {
		State s2 ;
		// meme type
		if(obj instanceof State) {
			s2 = (State) obj;
			// poly
			for (int i=0; i<polys.size(); i++) {
				ConvexPolygon p1 = polys.get(i);
				ConvexPolygon p2 = s2.getPolys().get(i);
				if(!p1.equals(p2)) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	
	public void takeSnapshot() {
		image.snapshot(null, wimg);
	}


	public List<ConvexPolygon> getPolys() {
		return polys;
	}
	
	public double distance(StateForImage goal) {
		if(distance!=null) {
			return distance;
		}
		PixelReader pr = wimg.getPixelReader();
		// On utilise le PixelReader pour lire chaque couleur
		// ici, on calcule la somme de la distance euclidienne entre le vecteur (R,G,B)
		// de la couleur du pixel cible et celui du pixel de l'image générée
		double res = 0;
		Color[][] target = goal.getTarget();
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				Color c = pr.getColor(i, j);
				res += Math.pow(c.getBlue() - target[i][j].getBlue(), 2)
						+ Math.pow(c.getRed() - target[i][j].getRed(), 2)
						+ Math.pow(c.getGreen() - target[i][j].getGreen(), 2);
			}
		}
		distance = Math.sqrt(res);
		return distance;
	}
	
	
	// Stockage de l'image dans un fichier .png
	
	public void save(int iteration) {
		RenderedImage renderedImage = SwingFXUtils.fromFXImage(wimg, null);
		try {
			ImageIO.write(renderedImage, "png", new File("s" + iteration+".png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void render(int iteration, Stage primaryStage) {
		Group image = new Group();
		for (ConvexPolygon p : polys)
			image.getChildren().add(p);

		// Calcul de la couleur de chaque pixel.Pour cela, on passe par une instance de
		// WritableImage, qui possède une méthode pour obtenir un PixelReader.
		WritableImage wimg = new WritableImage(maxX, maxY);
		image.snapshot(null, wimg);
		
		// affichage de l'image dans l'interface graphique
		Scene scene = new Scene(image, maxX, maxY);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	public WritableImage getWimg() {
		return wimg;
	}

	
	
}
