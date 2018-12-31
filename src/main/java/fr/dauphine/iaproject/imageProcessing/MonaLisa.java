package fr.dauphine.iaproject.imageProcessing;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import fr.dauphine.iaproject.autres.Generator;
import fr.dauphine.iaproject.autres.State;
import fr.dauphine.iaproject.autres.Technique;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fr.dauphine.iaproject.techniques.GenetiqueDist;
import fr.dauphine.iaproject.techniques.HillClimbing;
import fr.dauphine.iaproject.techniques.LocalBeamSearch;

	
public class MonaLisa  extends Application{
	public static final boolean DEBUG = true;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {		
		String targetImage = "monaLisa-100.jpg";
		Generator generator = new Generator(targetImage);
		Random random  = new Random();
		double deltaratio = (double) 7/10;
		double deltaratio01 = (double) 7/10;
		int nbNeighbors = 50;
		double threshold = 5;
		HillClimbing hillClimbing = new HillClimbing(generator, random, deltaratio,
				deltaratio01, nbNeighbors, primaryStage, threshold);
		
		
		int populationSize = 50;
		double mutationProbability = 0.9;
		double cutoff = 0.3;
		GenetiqueDist algoGenetiqueDistance = 
				new GenetiqueDist(generator, random, deltaratio, deltaratio01, 
						populationSize, mutationProbability, threshold, cutoff);
		
		int k = 5;
		LocalBeamSearch localBeamSearch = new LocalBeamSearch(generator, random, deltaratio,deltaratio01, threshold, k);


		Technique[] techniques = new Technique[] {algoGenetiqueDistance, hillClimbing ,localBeamSearch};
		techniques[1].execute();
		
	
		
	}
}
