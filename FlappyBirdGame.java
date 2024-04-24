import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class FlappyBirdGame extends JPanel implements ActionListener {

    private final int BIRD_WIDTH = 40;
    private final int BIRD_HEIGHT = 30;
    private final int PIPE_WIDTH = 100;
    private final int PIPE_GAP = 200;
    private final Random random = new Random();

    private Timer timer;
    private int birdY = 150;
    private int birdVelocity = 0;
    private int pipeX = 600;
    private int upperPipeY = random.nextInt(300);
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private int score = 0;

    // Images for the game
    private Image background;
    private Image birdImage;
    private Image pipeImage;

    public FlappyBirdGame() {
        // Load the images
        background = Toolkit.getDefaultToolkit().getImage("C:/Users/bisht/Downloads/background.png");
        birdImage = Toolkit.getDefaultToolkit().getImage("C:/Users/bisht/Downloads/bird.png");
        pipeImage = Toolkit.getDefaultToolkit().getImage("C:/Users/bisht/Downloads/pipe.png");

        timer = new Timer(20, this);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!gameStarted) {
                        gameStarted = true;
                        gameOver = false;
                        timer.start();
                        birdVelocity = -10;
                    } else if (!gameOver) {
                        birdVelocity = -10;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartGame();
                }
            }
        });
    }

    private void restartGame() {
        birdY = 150;
        birdVelocity = 0;
        pipeX = 600;
        upperPipeY = random.nextInt(300);
        gameStarted = false;
        gameOver = false;
        score = 0;
        timer.start(); // Restart the timer
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);

        // Draw the bird image
        g.drawImage(birdImage, 50, birdY, BIRD_WIDTH, BIRD_HEIGHT, this);

        // Draw the pipe image
        g.drawImage(pipeImage, pipeX, 0, PIPE_WIDTH, upperPipeY, this);
        g.drawImage(pipeImage, pipeX, upperPipeY + PIPE_GAP, PIPE_WIDTH, getHeight() - upperPipeY - PIPE_GAP, this);

        if (!gameStarted) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Press Space bar to start", getWidth() / 2 - 200, getHeight() / 2);
        } else {
            // Draw the bird image during gameplay
            g.drawImage(birdImage, 50, birdY, BIRD_WIDTH, BIRD_HEIGHT, this);
        }
    

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("You lose! Score: " + score + " Press R to restart", getWidth() / 2 - 180, getHeight() / 2);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Score: " + score, 10, 30);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            birdVelocity += 1;
            birdY += birdVelocity;
            pipeX -= 5;

            if (pipeX < -PIPE_WIDTH) {
                pipeX = getWidth();
                upperPipeY = random.nextInt(getHeight() - PIPE_GAP - 200);
                score++; // Increase score when passing a pipe
            }

            // Collision detection
            if (birdY <= 0 || birdY + BIRD_HEIGHT >= getHeight() || 
                (pipeX <= 50 + BIRD_WIDTH && (birdY <= upperPipeY || birdY + BIRD_HEIGHT >= upperPipeY + PIPE_GAP))) {
                gameOver = true;
                timer.stop();
            }
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBirdGame game = new FlappyBirdGame();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
