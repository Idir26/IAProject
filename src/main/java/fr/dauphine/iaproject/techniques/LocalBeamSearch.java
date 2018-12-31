package fr.dauphine.iaproject.techniques;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.dauphine.iaproject.autres.Generator;
import fr.dauphine.iaproject.autres.State;
import fr.dauphine.iaproject.autres.Technique;
import fr.dauphine.iaproject.imageProcessing.MonaLisa;

public class LocalBeamSearch extends Technique {
	int k;

	public LocalBeamSearch(Generator generator, Random random, double deltaratio, double ratio01, double threshold) {
		super(generator, random, deltaratio, ratio01, threshold);
	}
	
	

	public LocalBeamSearch(Generator generator, Random random, double deltaratio, double ratio01, double threshold, int k) {
		super(generator, random, deltaratio, ratio01, threshold);
		this.k = k;
	}



	@Override
	public State execute() {
		List<State> states = generator.getInitialpopulation(k);
		State successor;
		List<State> allSuccessors;
		for(int j=0; ; j++) {		
			allSuccessors = new ArrayList<>();
			for(State state : states) {
				for(int i=0; i<k; i++) {
					successor = changeState(state);
					
					allSuccessors.add(successor);					
				}
				
			}
			trier(allSuccessors);
		
			
			states=allSuccessors.subList(0, k);
			if(j%100 == 0) {
				states.get(0).save(j);
			}
			double d = states.get(0).distance(goal);
			System.out.println("Iteration " + j + " d = " + d);

			if(isStop(states.get(0))) {
				return states.get(0);
			}
		}
	}

}