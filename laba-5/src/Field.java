import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Field extends JPanel
{
    private boolean paused = false;
    private boolean paused_big = false;
    private ArrayList<BouncingBall> balls = new ArrayList<>(10);

    private Timer repaint_timer = new Timer(10, new ActionListener()
    {
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    });

    public Field()
    {
        setBackground(Color.getColor("", 0xFFE4E1));
        repaint_timer.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;

        for(BouncingBall ball : balls)
        {
            ball.paint(canvas);
        }
    }

    public void addBall()
    {
        balls.add(new BouncingBall(this));
    }

    public synchronized void pause()
    {
        paused = true;
        paused_big = true;
    }
    public synchronized void pauseBig() {paused_big = true; }
    public synchronized void resume()
    {
        paused = false;
        paused_big = false;
        notifyAll();
    }

    public synchronized void canMove(BouncingBall ball) throws InterruptedException
    {
        if(paused) wait();
        if(paused_big && ball.getRadius() >= 30)
            wait();
    }
}
