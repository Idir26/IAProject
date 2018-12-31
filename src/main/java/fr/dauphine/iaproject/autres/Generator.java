package fr.dauphine.iaproject.autres;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.dauphine.iaproject.imageProcessing.ConvexPolygon;

public class Generator {
	int nPolys  = 100;
	StateForImage goal;

	
	public Generator(String path) {
		goal = new StateForImage(path);
	}

	/**
	 * Methode qui genere une liste de npolys polygones (state) 
	 */
	public State getInitialState() {	
		// generation de npolys triangles
		List<ConvexPolygon> ls = new ArrayList<ConvexPolygon>();
		ConvexPolygon convexPolygon;
		for (int i=0;i<nPolys;i++) {
			convexPolygon = new ConvexPolygon(3, goal.getMaxX(), goal.getMaxY());
			ls.add(convexPolygon);	
		}
		State state =new State(ls);
		return state;
	}
	
	/**
	 * Methode qui genere n listes de states 
	 * (utilisee dans l'algorithme genetique)
	 */
	
	public List<State> getInitialpopulation(int n){
		List<State> res = new ArrayList<>();
		State state;
		for(int i=0; i<n; i++) {
			state = getInitialState();
			res.add(state);
		}
		return res;
	}

	public int getnPolys() {
		return nPolys;
	}

	public StateForImage getGoal() {
		return goal;
	}
	
	
}
