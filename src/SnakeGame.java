import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 1600;  // (WIDTH * HEIGHT) / DOT_SIZE
    private final int RAND_POS = 29;  // (WIDTH / DOT_SIZE) - 1
    private final int DELAY = 140;

    private LinkedList<Point> snake;
    private Point food;
    private boolean running;
    private char direction = 'R';  // Initial direction: Right

    private Timer timer;
    private Random random;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new TAdapter());

        this.snake = new LinkedList<>();
        this.random = new Random();
        this.timer = new Timer(DELAY, this);
        this.running = false;

        startGame();
    }

    private void startGame() {
        snake.clear();
        snake.add(new Point(50, 50));  // Initial position of the snake
        placeFood();
        running = true;
        timer.start();
    }

    private void placeFood() {
        food = new Point(random.nextInt(RAND_POS) * DOT_SIZE, random.nextInt(RAND_POS) * DOT_SIZE);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case 'U': newHead.y -= DOT_SIZE; break;  // Up
            case 'D': newHead.y += DOT_SIZE; break;  // Down
            case 'L': newHead.x -= DOT_SIZE; break;  // Left
            case 'R': newHead.x += DOT_SIZE; break;  // Right
        }

        snake.addFirst(newHead);
        if (newHead.equals(food)) {
            placeFood();  // Grow the snake by placing food
        } else {
            snake.removeLast();  // Remove the tail of the snake
        }
    }

    private void checkCollisions() {
        Point head = snake.getFirst();

        // Check for collision with the screen borders
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }

        // Check for collision with the snake's body
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!running) {
            gameOver(g);
            return;
        }

        // Draw the snake
        g.setColor(Color.green);
        for (Point p : snake) {
            g.fillRect(p.x, p.y, DOT_SIZE, DOT_SIZE);
        }

        // Draw the food
        g.setColor(Color.red);
        g.fillRect(food.x, food.y, DOT_SIZE, DOT_SIZE);
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over!";
        g.setColor(Color.white);
        g.drawString(msg, WIDTH / 2 - 40, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollisions();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && direction != 'R') {
                direction = 'L';
            } else if (key == KeyEvent.VK_RIGHT && direction != 'L') {
                direction = 'R';
            } else if (key == KeyEvent.VK_UP && direction != 'D') {
                direction = 'U';
            } else if (key == KeyEvent.VK_DOWN && direction != 'U') {
                direction = 'D';
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);  // Ensure window is visible
    }
}
