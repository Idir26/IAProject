package fr.dauphine.iaproject.techniques;

import java.util.ArrayList;
import java.util.Random;

import fr.dauphine.iaproject.autres.Generator;
import fr.dauphine.iaproject.autres.State;
import fr.dauphine.iaproject.autres.StateForImage;
import fr.dauphine.iaproject.autres.Technique;
import fr.dauphine.iaproject.imageProcessing.ConvexPolygon;
import fr.dauphine.iaproject.imageProcessing.MonaLisa;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class HillClimbing extends Technique {
    int nbNeighbors;
    Stage primaryStage;
    
	
    
	public HillClimbing(Generator generator, Random random, double deltaratio, double deltaratio01, int nbNeighbors,
			Stage primaryStage, double threshold) {
		super(generator, random, deltaratio, deltaratio01, threshold);
		this.nbNeighbors = nbNeighbors;
		this.primaryStage = primaryStage;
		
	}
	
	
	@Override
	public State execute() {
		State current = generator.getInitialState();
		State neighbor;
		double d;
		for(int i=0; ; i++) {
			neighbor = successor(current);
			d = value(neighbor);
			if(MonaLisa.DEBUG) {
				if(i%100==0) {
					neighbor.save(i);
					
				}
				

				
				System.out.println("Iteration " + i + " d = " + d);
			}
			// si on ne peut plus evoluer
			if(d < value(current)&& d>threshold){				
				current = neighbor;
			}else if (d< value(current)) {
			    	 current.render(i, primaryStage);
			    	 return current ; 
			}
			
		}
	}




	private double value(State s) {
		double d = s.distance(goal);
		return d;
	}

	private State successor(State current) {
		State successor ;
		State successorBest = null;
		double d;
		double dBest = Double.MAX_VALUE;
		for(int i=0; i<nbNeighbors; i++) {
			successor = changeState(current);
			
			d = successor.distance(goal);
			if(d<dBest) {
				successorBest = successor;
				dBest = d;
			}
		}
		return successorBest;
	}

}
