import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;

import sun.audio.*;

public class AsteroidsGame extends JPanel implements ActionListener,
		MouseMotionListener {

	private ImageIcon[] imgBackground;
	private int[] xPos;
	private Timer backgroundTimer, asteroidsTimer, laserTimer;
	private int miliseconds = 0, lasersShot = 0, latestAsteroid = 0, score = 0,
			generationAttempts = 0, ammo = 3, ammoDelay = 0;
	private JLabel lblScore, lblLives, lblAmmo;
	private final static int NUM_OF_ASTEROIDS = 20, NUM_OF_BEAMS = 3;
	private GameObject[] asteroids = new GameObject[NUM_OF_ASTEROIDS];
	private GameObject p;
	private GameObject[] lasers = new GameObject[NUM_OF_BEAMS];
	private Random r;

	public static void main(String[] args) {
		new AsteroidsGame();

	}

	public AsteroidsGame() {
		r = new Random();
		p = new Player();

		imgBackground = new ImageIcon[2];

		for (int i = 0; i < imgBackground.length; i++) {
			imgBackground[i] = new ImageIcon("images\\space_background.png");
		}

		xPos = new int[] { 0, imgBackground[0].getIconWidth() };

		laserTimer = new Timer(1, this);
		backgroundTimer = new Timer(35, this);
		asteroidsTimer = new Timer(20, this);

		JLabel lblTitle = new JLabel();
		lblTitle.setIcon(new ImageIcon("images\\asteroids_title.png"));
		lblTitle.setPreferredSize(new Dimension((int) (imgBackground[0]
				.getIconWidth() * 0.45), 50));

		lblScore = new JLabel();
		lblScore.setText(Integer.toString(0));
		lblScore.setPreferredSize(new Dimension(150, 50));
		lblScore.setFont(new Font("Neuropol", Font.BOLD, 48));
		lblScore.setForeground(Color.LIGHT_GRAY);

		lblLives = new JLabel(new ImageIcon("images\\lives3.png"));
		lblLives.setPreferredSize(new Dimension(140, 30));

		lblAmmo = new JLabel();
		lblAmmo.setIcon(new ImageIcon("images\\gun3.png"));
		lblAmmo.setFont(new Font("Aharoni", Font.BOLD, 20));
		lblAmmo.setPreferredSize(new Dimension(150, 50));
		lblAmmo.setForeground(Color.RED);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.CENTER;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 1;
		topPanel.setBackground(Color.BLACK);
		topPanel.add(lblScore, gc);
		gc.gridx = 1;
		topPanel.add(lblAmmo, gc);
		gc.gridx = 2;
		gc.gridwidth = 2;
		topPanel.add(lblTitle, gc);
		gc.gridx = 4;
		gc.gridwidth = 1;
		topPanel.add(lblLives, gc);

		setFocusable(true);

		JFrame frame = new JFrame();
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(this, BorderLayout.CENTER);
		frame.setTitle("Asteroids");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(imgBackground[0].getIconWidth(),
				imgBackground[0].getIconHeight() + 95);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		// adding listeners
		addMouseMotionListener(this);
		addKeyListener(new KListener());

		// starting timers
		backgroundTimer.start();
		asteroidsTimer.start();
		// laserTimer.start();
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == backgroundTimer) {
			for (int i = 0; i < imgBackground.length; i++) {
				xPos[i] -= 5;

				if (xPos[i] + imgBackground[i].getIconWidth() <= 0) {
					xPos[i] = getWidth();
				}
			}
		}

		if (e.getSource() == asteroidsTimer) {
			miliseconds++;
			// launching asteroids one at a time with a delay
			if (latestAsteroid < NUM_OF_ASTEROIDS && miliseconds % 100 == 0) {
				asteroids[latestAsteroid] = new Asteroid();
				findPlace(latestAsteroid);
				latestAsteroid++;
			}
			// moving asteroids
			for (int i = 0; i < latestAsteroid; i++) {
				asteroids[i].move();
				// checking if asteroid has gone off screen.
				if (!checkBorders(asteroids[i])
						&& asteroids[i].getX() < -asteroids[i].getWidth()) {

					asteroids[i] = new Asteroid();
					findPlace(i);
				}
				// creating new asteroid in place of destroyed one
				if (((Asteroid) asteroids[i]).getHealth() == 0
						&& miliseconds % 300 == 0) {
					asteroids[i] = new Asteroid();
					findPlace(i);
				}
			}
			// checks if player collided with any of the asteroids
			// if he did, kills the player, takes off a live
			for (int i = 0; i < latestAsteroid; i++)
				if (p.getRect().intersects(asteroids[i].getRect())
						&& ((Player) p).getLives() > 0
						&& ((Asteroid) asteroids[i]).getHealth() > 0
						&& !((Player) p).getDead()) {
					((Player) p).setDead(true);
					((Player) p).setLives(((Player) p).getLives() - 1);
					lblLives.setIcon(new ImageIcon("images\\lives"
							+ ((Player) p).getLives() + ".png"));
					if (((Player) p).getLives() > 0) {
						JOptionPane.showMessageDialog(
								null,
								"You are dead!\nYou have "
										+ ((Player) p).getLives()
										+ " lives remaining!", "Asteroids",
								JOptionPane.INFORMATION_MESSAGE);
						// reseting the game
						miliseconds = 0;
						lasersShot = 0;
						latestAsteroid = 0;
						ammo = 3;
						lblAmmo.setIcon(new ImageIcon("images\\gun3.png"));
						lblAmmo.setText(null);
						int previousLives = ((Player) p).getLives();
						p = new Player();
						((Player) p).setLives(previousLives);
						backgroundTimer.start();
						asteroidsTimer.start();
						break;
					} else {
						// stopping the game if player has no lives left
						backgroundTimer.stop();
						asteroidsTimer.stop();
						laserTimer.stop();
						int option = JOptionPane
								.showConfirmDialog(
										null,
										"GAME OVER!\nYou finished with "
												+ score
												+ " points!\nDo you want to play again?",
										"Asteroids", JOptionPane.YES_NO_OPTION);
						if (option == JOptionPane.YES_OPTION) {
							// restarting the game
							miliseconds = 0;
							lasersShot = 0;
							latestAsteroid = 0;
							score = 0;
							ammo = 3;
							lblScore.setText("0");
							lblLives.setIcon(new ImageIcon("images\\lives3.png"));
							lblAmmo.setIcon(new ImageIcon("images\\gun3.png"));
							lblAmmo.setText(null);
							p = new Player();
							asteroidsTimer.setDelay(15);
							backgroundTimer.start();
							asteroidsTimer.start();
						} else {
							// exiting the game
							JOptionPane.showMessageDialog(null,
									"Thank you for playing!", "Asteroids",
									JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						}
					}
				}

			if (ammo == 0) {
				if (ammoDelay < 100) {
					if (ammoDelay == 20)
						lblAmmo.setFont(new Font("Aharoni", Font.BOLD, 17));
					else if (ammoDelay == 40)
						lblAmmo.setFont(new Font("Aharoni", Font.BOLD, 18));
					else if (ammoDelay == 60)
						lblAmmo.setFont(new Font("Aharoni", Font.BOLD, 19));
					else if (ammoDelay == 80)
						lblAmmo.setFont(new Font("Aharoni", Font.BOLD, 20));

					ammoDelay++;
				} else {
					ammo = 3;
					ammoDelay = 0;
					lblAmmo.setText(null);
					lblAmmo.setIcon(new ImageIcon("images\\gun3.png"));
				}
			}

		}

		if (e.getSource() == laserTimer) {
			// moving laser beams across the screen
			for (int i = 0; i < lasersShot; i++) {
				((Laser) lasers[i]).move(15);
				if (!checkBorders(lasers[i])) {
					// moving laserbeam that has gone off screen to the last
					// position in the array
					for (int j = i; j < lasersShot - 1; j++) {
						GameObject tmp = lasers[j];
						lasers[j] = lasers[j + 1];
						lasers[j + 1] = tmp;
					}
					lasersShot--;

				}
				for (int j = 0; j < latestAsteroid; j++)
					// taking off one health point from the asteroid of laser
					// collided with it
					if (lasers[i].getRect().intersects(asteroids[j].getRect())
							&& ((Asteroid) asteroids[j]).getHealth() > 0) {
						((Asteroid) asteroids[j])
								.setHealth(((Asteroid) asteroids[j])
										.getHealth() - 1);
						// adding points to player`s score if asteroid was
						// destroyed
						if (((Asteroid) asteroids[j]).getHealth() == 0) {
							switch (((Asteroid) asteroids[j]).getSize()) {
							case Asteroid.SMALL:
								score += 10;
								break;
							case Asteroid.MEDIUM:
								score += 20;
								break;
							case Asteroid.LARGE:
								score += 30;
								break;
							}
							lblScore.setText(Integer.toString(score));
							// as the score increases the asteroids start moving
							// faster
							if (score >= 100 && score % 50 == 0) {
								if (asteroidsTimer.getDelay() > 2)
									asteroidsTimer.setDelay(asteroidsTimer
											.getDelay() - 1);
							}
						}
						// moving laserbeam that collided with asteroid to the
						// end of the array
						for (int k = i; k < lasersShot - 1; k++) {
							GameObject tmp = lasers[k];
							lasers[k] = lasers[k + 1];
							lasers[k + 1] = tmp;
						}
						lasers[lasersShot - 1].setPosition(
								-lasers[lasersShot - 1].getWidth(), 0);
						lasersShot--;
						break;
					}
			}
		}

		repaint();
	}

	// assigns a correct place for an asteroid on the screen. Helps to avoid
	// asteroids overlapping
	private void findPlace(int currentIndex) {
		boolean check = false;
		while (!check) {
			int j = 0;
			for (j = 0; j < latestAsteroid; j++) {
				if (asteroids[j].getRect().intersects(
						asteroids[currentIndex].getRect())
						&& currentIndex != j) {
					asteroids[currentIndex]
							.setPosition(1270 + r.nextInt(2200), r
									.nextInt(720 - asteroids[currentIndex]
											.getHeight()));
					generationAttempts++;
					// if the correct position for the current asteroid can`t
					// found after 10 attempts, generate another asteroid
					if (generationAttempts >= 10) {
						asteroids[currentIndex] = new Asteroid();
					}
					break;
				}
			}
			if (j == latestAsteroid) {
				check = true;
			}
			if (latestAsteroid == 0) {
				check = true;
			}
		}
		generationAttempts = 0;
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		for (int i = 0; i < imgBackground.length; i++) {
			g2.drawImage(imgBackground[i].getImage(), xPos[i], 0, this);
		}
		p.draw(g2);
		for (int i = 0; i < latestAsteroid; i++)
			asteroids[i].draw(g2);
		for (int i = 0; i < lasersShot; i++)
			lasers[i].draw(g2);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// moves spaceship to the position of mouse on the screen
		if (e.getY() < 720 - p.getHeight())
			p.setPosition(e.getX(), e.getY());
	}

	// true if the object is within the screen, false otherwise
	public boolean checkBorders(GameObject obj) {
		if (obj.getX() < -obj.getWidth() || obj.getX() > 1265)
			return false;
		return true;
	}

	private class KListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			// fires a laser if the number of lasers on the screen is less than
			// max allowed number of lasers
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (lasersShot < NUM_OF_BEAMS && !((Player) p).getDead()
						&& ammo != 0) {
					lasers[lasersShot] = new Laser(p.getX() + p.getWidth(),
							p.getY() + 20);
					lasersShot++;
					ammo--;
					if (ammo != 0)
						lblAmmo.setIcon(new ImageIcon("images\\gun" + ammo
								+ ".png"));
					else {
						lblAmmo.setIcon(null);
						lblAmmo.setText("RECHARGING!");
					}
					if (!laserTimer.isRunning())
						laserTimer.start();
				}
			}
			repaint();
		}

	}
}