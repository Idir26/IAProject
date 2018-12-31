package fr.dauphine.iaproject.autres;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import fr.dauphine.iaproject.imageProcessing.ConvexPolygon;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class Technique {
	protected StateForImage goal;
	protected Generator generator;
	protected Random random;
	
	/**
	 * seuil (distance) auquel on veut que notre algorithme s'arrete  
	 */
	protected double threshold;
	
	/**
	 * Definit l'increment maximal a� utiliser pour modifier un sommet. On definit cet increment maximal
	 * comme un ratio de la taille maximale de l'image
	 */
    protected double deltaratio;
    
	/**
	 * Definit l'increment maximal à utiliser pour modifier une grandeur dont la valeur est dans l'intervalle
	 * [0,1] (transparence, R, G, B)
	 */
    protected double deltaratio01;
    
    
    
	
	public Technique(Generator generator, Random random, 
			double deltaratio, double deltaratio01, double threshold) {
		this.generator = generator;
		this.random = random;
		this.deltaratio = deltaratio;
		this.deltaratio01 = deltaratio01;
		this.goal = generator.getGoal();
		this.threshold = threshold;
	}

	public abstract State execute();
	
	public State changeState( State current) {
		State next = new State(current);
		// 1) choisir le polygone;
		int n = next.getPolys().size();
		int indexChoisi = random.nextInt(n);
		ConvexPolygon poly = next.getPolys().get(indexChoisi);
		// 2) choisir une caractéristique
		indexChoisi = random.nextInt(Caracteristique.values().length);
		Caracteristique caracteristique = Caracteristique.values()[indexChoisi];
		switch (caracteristique){
			case POSITION_SOMMETS:
				ObservableList<Double> points = poly.getPoints();
				int nSommets = points.size();
				indexChoisi = random.nextInt(nSommets);
				while(indexChoisi % 2 != 0){ indexChoisi = random.nextInt(nSommets) ;}
				// on va deplacer le sommet, definir l'amplitude du deplacement
				// TODO définir une méthode qui permet de modifier un polygone convexe et que le polygone obtenu reste convexe.
				// Pour le moment, seul des triangles sont pris en compte.	
				
				int deltaxMax = (int) (deltaratio * goal.getMaxX());
				int deltayMax = (int) (deltaratio * goal.getMaxY());
				

				int deltax = random.nextInt(deltaxMax);
				int deltay = random.nextInt(deltayMax);
				boolean value = random.nextBoolean();
				
				int nouvX;
				int nouvY;
				if( value == true ) {
				
				nouvX = poly.getPoints().get(indexChoisi).intValue() + deltax;
				nouvY = poly.getPoints().get(indexChoisi+1).intValue() + deltay;
				} else { 
					nouvX = poly.getPoints().get(indexChoisi).intValue() - deltax;
					nouvY = poly.getPoints().get(indexChoisi+1).intValue() - deltay;

				}
				poly.getPoints().set(indexChoisi, (double) nouvX);
				poly.getPoints().set(indexChoisi+1, (double) nouvY);
				break;
			case TRANSPARENCE:
				
					
				double beta = modifier(poly.getOpacity(), deltaratio01);
				poly.setOpacity(beta);
				break;
			case COULEUR:

				Paint p = poly.getFill();
				Color c ;
				if(p instanceof Color) {
					c = (Color) p;
					double r = modifier(c.getRed(), deltaratio01);
					double g = modifier(c.getGreen(), deltaratio01);
					double b = modifier(c.getBlue(), deltaratio01);
					poly.setFill(new Color(r, g, b, poly.getOpacity()));
				}			
				break;
		}
		// des qu'on finit de modifier une caracteristique, on fait snapshot
		next.takeSnapshot();
		return next;	
	}
	
	private double modifier(double alpha, double ratio) {
		double delta = nextDouble(random, 0, ratio);
		boolean signe = random.nextBoolean();
		double beta;
		if(signe) {
			beta = alpha + delta;
			if(beta>1) {
				beta = 1;
			}
		}
		else {
			beta = alpha - delta;
			if(beta<0) {
				beta = 0;
			}
		}
		return beta;
		
	}
	
	public void trier(List<State> states) {
		Collections.sort(states,new Comparator<State>() {
		    public int compare( State r1, State r2 ) {
		    	int res = (new Double(r1.distance(goal)).compareTo(new Double(r2.distance(goal))));
		    	return res;
		    }
		});
	}
	
	public static double nextDouble(Random r, double min, double max) {
		double randomValue = min + (max - min) * r.nextDouble();
		return randomValue;
	}
	
	public boolean isStop(State bestState) {
		if(bestState.distance(generator.getGoal())<threshold) {
			return true;
		}
		return false;
	}
}
