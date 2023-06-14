package it.tristana.vonroidiagram;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int NUM_OF_POINTS = 10;
	private static final int OFFSET_PERCENTAGE = 10;

	private static final int X = 200;
	private static final int Y = 200;
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 1000;
	
	private Board board;

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		Point[] points = new Point[NUM_OF_POINTS];
		Random random = new Random();
		for (int i = 0; i < points.length; i ++) {
			points[i] = new Point(randomWithOffset(random, WIDTH, OFFSET_PERCENTAGE), randomWithOffset(random, HEIGHT, OFFSET_PERCENTAGE));
			//System.out.println(points[i]);
		}
		//points = new Point[] { new Point(100, 100), new Point(500, 100), new Point(250, 150), new Point(100, 50) };
		main.board = new Board(points, WIDTH, HEIGHT);
		main.add(main.board);
		main.setTitle("Vonroi Diagram");
		main.setBounds(X, Y, WIDTH, HEIGHT);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setUndecorated(true);
		main.setVisible(true);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				main.board.increaseSweepline();
				main.repaint();
			}
		}, 1, 10);
	}

	private static int randomWithOffset(Random random, int bound, int offsetPercentage) {
		int offset = bound * offsetPercentage / 100;
		return random.nextInt(offset, bound - offset);
	}
}
