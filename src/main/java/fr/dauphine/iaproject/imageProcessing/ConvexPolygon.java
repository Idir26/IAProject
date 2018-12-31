package fr.dauphine.iaproject.imageProcessing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class ConvexPolygon extends Polygon {

	static final int maxNumPoints = 3;
	static Random gen = new Random();
	public  int max_X;
	public int max_Y;
	NumberFormat nf = new DecimalFormat("##.00");

	// randomly generates a polygon
	public ConvexPolygon(int numPoints, int xMax, int yMax) {
		super();
		max_X = xMax;
		max_Y = yMax;
		genRandomConvexPolygone(numPoints);
		int r = gen.nextInt(256);
		int g = gen.nextInt(256);
		int b = gen.nextInt(256);
		this.setFill(Color.rgb(r, g, b));
		this.setOpacity(gen.nextDouble());
	}

	public ConvexPolygon() {
		super();
	}


	@Override
	public String toString() {
		String res = super.toString();
		res += " " + this.getFill() + " opacity " + this.getOpacity();
		if(MonaLisa.DEBUG) {
			Double total = 0d;
			// le polygone est uniquement identifié par le hashCode de la somme de toutes ses caractéristiques
			// numériques (coordonnées, couleur, opacité)
			for(Double v : getPoints()) {
				total+=v;
			}
			total+= this.getOpacity();
			Color c = (Color)this.getFill();
			total+= c.getRed();
			total+= c.getGreen();
			total+= c.getBlue();
			String str = String.valueOf(total.hashCode());
			String substring = str.length() > 2 ? str.substring(str.length() - 2) : str;
			res = substring;
		}
		return res;
	}

	public void addPoint(double x, double y) {
		getPoints().add(x);
		getPoints().add(y);
	}

	// http://cglab.ca/~sander/misc/ConvexGeneration/convex.html
	public void genRandomConvexPolygone(int n) {
		List<Point> points = new LinkedList<Point>();
		List<Integer> abs = new ArrayList<>();
		List<Integer> ord = new ArrayList<>();
		int x_;
		int y_;
		for (int i = 0; i < n; i++) {
			x_ = gen.nextInt(max_X);
			y_ = gen.nextInt(max_Y);
			x_= x_==0? 1 : x_;
			y_= y_==0? 1 : y_;
			abs.add(x_);
			ord.add(y_);
		}
		Collections.sort(abs);
		Collections.sort(ord);
		// System.out.println(abs + "\n" + ord);
		int minX = abs.get(0);
		int maxX = abs.get(n - 1);
		int minY = ord.get(0);
		int maxY = ord.get(n - 1);

		List<Integer> xVec = new ArrayList<>();
		List<Integer> yVec = new ArrayList<>();

		int top = minX, bot = minX;
		for (int i = 1; i < n - 1; i++) {
			int x = abs.get(i);

			if (gen.nextBoolean()) {
				xVec.add(x - top);
				top = x;
			} else {
				xVec.add(bot - x);
				bot = x;
			}
		}
		xVec.add(maxX - top);
		xVec.add(bot - maxX);

		int left = minY, right = minY;
		for (int i = 1; i < n - 1; i++) {
			int y = ord.get(i);

			if (gen.nextBoolean()) {
				yVec.add(y - left);
				left = y;
			} else {
				yVec.add(right - y);
				right = y;
			}
		}
		yVec.add(maxY - left);
		yVec.add(right - maxY);

		Collections.shuffle(yVec);

		List<Point> lpAux = new ArrayList<>();
		for (int i = 0; i < n; i++)
			lpAux.add(new Point(xVec.get(i), yVec.get(i)));

		// sort in order by angle
		Collections.sort(lpAux, (x, y) -> Math.atan2(x.getY(), x.getX()) < Math.atan2(y.getY(), y.getX()) ? -1
				: Math.atan2(x.getY(), x.getX()) == Math.atan2(y.getY(), y.getX()) ? 0 : 1);

		int x = 0, y = 0;
		int minPolX = 0, minPolY = 0;

		for (int i = 0; i < n; i++) {
			points.add(new Point(x, y));
			x += lpAux.get(i).getX();
			y += lpAux.get(i).getY();

			if (x < minPolX)
				minPolX = x;
			if (y < minPolY)
				minPolY = y;
		}

		int xshift = gen.nextInt(maxX - (maxX - minX));
		int yshift = gen.nextInt(maxY - (maxY - minY));
		xshift -= minPolX;
		yshift -= minPolY;
		for (int i = 0; i < n; i++) {
			Point p = points.get(i);
			p.translate(xshift, yshift);
		}
		for (Point p : points)
			addPoint(p.getX(), p.getY());

	}

	public class Point {

		int x, y;

		// generate a random point
		public Point() {
			x = gen.nextInt(max_X);
			y = gen.nextInt(max_Y);
		}

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public void translate(int vx, int vy) {
			x += vx;
			y += vy;
		}

		public boolean equals(Object o) {
			if (o == null)
				return false;
			else if (o == this)
				return true;
			else if (o instanceof Point)
				return ((Point) o).x == this.x && ((Point) o).y == this.y;
			else
				return false;
		}

		public String toString() {
			NumberFormat nf = new DecimalFormat("#.00");
			return "(" + x + "," + y + ")"; // + nf.format(Math.atan2(y, x))+")";
		}

	}
	
	public  int getMaxx() {
		return max_X;
	}
	
	public int getMaxy() {
		return max_Y;
	}
	
		
	@Override
	public Object clone() throws CloneNotSupportedException {
		ConvexPolygon convexPolygon = new ConvexPolygon();
		for(Double v : getPoints()) {
			convexPolygon.getPoints().add(v);
		}
		
		convexPolygon.setOpacity(getOpacity());
		convexPolygon.setFill(getFill());
		
		convexPolygon.setMax_X(max_X);
		convexPolygon.setMax_Y(max_Y);
		convexPolygon.setNf(nf);
		return convexPolygon;
	}

	public int getMax_X() {
		return max_X;
	}

	public void setMax_X(int max_X) {
		this.max_X = max_X;
	}

	public int getMax_Y() {
		return max_Y;
	}

	public void setMax_Y(int max_Y) {
		this.max_Y = max_Y;
	}

	public static Random getGen() {
		return gen;
	}

	public static void setGen(Random gen) {
		ConvexPolygon.gen = gen;
	}

	public NumberFormat getNf() {
		return nf;
	}

	public void setNf(NumberFormat nf) {
		this.nf = nf;
	}

	public static int getMaxnumpoints() {
		return maxNumPoints;
	}

	@Override
	public boolean equals(Object obj) {
		ObservableList<Double> points1 = getPoints();
		// meme type
		if(obj instanceof ConvexPolygon) {
			ConvexPolygon poly2 = (ConvexPolygon) obj;
			ObservableList<Double> points2 = poly2.getPoints();
			// meme points
			for (int i = 0; i < getPoints().size(); i++) {
				if(!points1.get(i).equals(points2.get(i))) {				
					return false;
				}
			}
			// on va vérifier les autres caractéristiques
			if(!(getOpacity() == poly2.getOpacity())
					|| !(getFill().equals(poly2.getFill()))) {
				return false;
			}
			return true;
		}
		else {
			return false;
		}
	}
}
