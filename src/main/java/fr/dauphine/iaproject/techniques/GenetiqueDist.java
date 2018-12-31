package fr.dauphine.iaproject.techniques;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import fr.dauphine.iaproject.autres.Generator;
import fr.dauphine.iaproject.autres.State;
import fr.dauphine.iaproject.autres.Technique;
import fr.dauphine.iaproject.imageProcessing.ConvexPolygon;
import fr.dauphine.iaproject.imageProcessing.MonaLisa;


/**
 * Dans cette variante de l'algo genetique, on ne choisit pas les parents de l'ensemble de la 
 * population, mais d'une selection des meilleurs individus que nous appelons 'Elite'. 
 * L'attribut cutoff definit cette selection,
 * il correspond au pourcentage des meilleurs individus qu'il faut considerer. Lors de chaque croisement,
 * le choix des parents est fait de maniere aleatoire a� partir de cette elite.
 *
 */
public class GenetiqueDist extends Technique  {
	private static final String ESPACE = "                        ";

	/**
	 * Taille de la population
	 */
	int n;
	
	/**
	 * Probabilite de mutation
	 */
	double mutationProbability ;
	ArrayList<State> populationTriee;
	/**
	 * Si cutoff = 25%, les parents seront selectionnes au hasard parmi les 25% meilleurs individus
	 * de la population
	 */
	double cutoff;
	
	public GenetiqueDist(Generator generator, Random random, double deltaratio, double ratio01, int n,
			double mutationProbability, double threshold, double cutoff) {
		super(generator, random, deltaratio, ratio01, threshold);
		this.n = n;
		this.mutationProbability = mutationProbability;
		this.cutoff = cutoff;
	}
	
	@Override
	public State execute() {
		List<State> population = generator.getInitialpopulation(n);
		List<State> newPopulation;
		State father ;
		State mother ;
		State children;
		State bestState;
		double bestDistance;
		
		for(int j=0; ; j++) {
			sortPopulation(population);
			newPopulation = new ArrayList<>();
			for(int i=0; i<n; i++) {
				father = randomSelection(population);
				mother = randomSelection(population);
				children = reproduce(father,mother);
				if(random.nextDouble() < mutationProbability) {
					children = mutate(children);
				
				}
				newPopulation.add(children);
		
			}
			population = newPopulation;


			// retrouver le meilleur element de la population
			Object[] res = getBestStateOfPopulation(population);
			bestState=(State) res[0];
			bestDistance = (double) res[1];
			if(MonaLisa.DEBUG) {
				System.out.println("Le meilleur est :");
				System.out.println(bestState+ ESPACE + bestState.distance(goal));
				System.out.println("d="+ bestDistance );
			}
			// doit on continuer la recherche?
			if(isStop(bestState)) {
				return bestState;
			}
			
			if(j%100 == 0) {
			
			bestState.save(j);
		}}
	}
	
	public Object[] getBestStateOfPopulation(List<State> population) {
		double bestDistance = Double.MAX_VALUE;
		State bestState = null;
		// retrouver le meilleur element de la population
		for(State state : population) {
			
			if(state.distance(generator.getGoal())<bestDistance) {
				bestDistance = state.distance(generator.getGoal());
				bestState = state;
			}
		}
		return new Object[] {bestState, bestDistance};
	}
	
	
	
	public State mutate(State state) {	
			return changeState(state);
	}
	
	public State reproduce(State x, State y) {
		int nPolys = generator.getnPolys();
		int c = 1+random.nextInt(nPolys-1);
		
		boolean value = random.nextBoolean();
		List<ConvexPolygon> polys1;
		List<ConvexPolygon> polys2;
		if( value == true ) {
		
		polys1 = x.getPolys();
		 polys2 = y.getPolys();
		}else {

		 polys1 = y.getPolys();
		 polys2 = x.getPolys();
		}
		
		List<ConvexPolygon> polys1_ = new ArrayList<>();
	
		try {
			for(int i=0; i<c; i++) {
				polys1_.add((ConvexPolygon) polys1.get(i).clone());
				
			}
			// le c est inclu dans la partie de droite
			for(int i=c; i<nPolys; i++) {
				polys1_.add((ConvexPolygon) polys2.get(i).clone());
				
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		State child1 = new State(polys1_);
	
		return child1;
	}
	
	
	

	public void sortPopulation(List<State> population) {
		//this.populationTriee = (ArrayList<State>) population;
		trier(population);
	}
	
	/**
	 * Le choix d'un des parents pour le croisement est realise sur la base des cutoff meilleurs individus
	 * de la population courante.
	 */
	public State randomSelection(List<State> population) {
		int nmax = (int) (cutoff * n);
		int i = random.nextInt(nmax);
		State state = population.get(i);
		return state;
	}

}
