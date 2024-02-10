//import java.awt.Graphics;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
import java.awt.event.*;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	// these arrays hold the coordinates
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6; 
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this); // this is for the action listener interface
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if (running) {
			// turn panel into grid to see position better
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i < bodyParts; i++) {
				// head of snake
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					// multicolor snake body parts
//					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			// Current Score text
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			// font metrics for aligning
			FontMetrics metrics = getFontMetrics(g.getFont());
			// centers message
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/ 2, g.getFont().getSize()); 
		} else {
			gameOver(g);
		}
	}
	public void newApple() {
		// cast the value as an int to not break the program
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
	}
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1]; // shifts all the x-coordinates in the array over by one
			y[i] = y[i - 1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		// check to see if he collides with his own body
		for (int i = bodyParts; i > 0; i--) {
			if((x[0]== x[i]) && (y[0]== y[i])) {
				// game over
				running = false;
			}
		}
		// TODO: combine these to not violate the DRY principle
		// check if head touches left border
		if (x[0] < 0) {
			running = false;
		}
		// right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// top border
		if (y[0] < 0) {
			running = false;
		}
		// bottom border
		if (y[0] > SCREEN_HEIGHT) {
			running = false; 
		}

		if (!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		// Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		// font metrics for aligning
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		// centers message
		g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/ 2, SCREEN_HEIGHT/2); 
		
		// Score text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		// font metrics for aligning
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		// centers message
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/ 2, g.getFont().getSize()); 
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	
	// inner class
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				// prevent turning 180 degrees
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				// prevent turning 180 degrees
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				// prevent turning 180 degrees
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				// prevent turning 180 degrees
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}