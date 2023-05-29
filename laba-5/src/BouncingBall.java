import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public class BouncingBall implements Runnable
{
    private static final int MAX_RADIUS = 40;
    private static final int MIN_RADIUS = 5;
    private static final int MAX_SPEED = 15;

    private Field field;

//    ball parameters
    private double x_coord;
    private double y_coord;
    private int speed;
    private double speed_x;
    private double speed_y;
    private Color color;
    private int radius;

    private double color_red;
    private double color_blue;
    private double color_green;

    public BouncingBall(Field field)
    {
        this.field = field;
        radius = new Double(Math.random() * MAX_RADIUS + MIN_RADIUS).intValue(); // init radius in range form min radius to max
        if(radius > MAX_RADIUS)
            radius = MAX_RADIUS;
        speed = new Double(5*MAX_SPEED/radius).intValue(); // speed is depends on radius, the larger radius, the lower speed
        if(speed > MAX_SPEED)
            speed = MAX_SPEED;

        double angle = Math.random()*2*Math.PI;
        // init speed components
        speed_x = speed * Math.cos(angle);
        speed_y = speed * Math.sin(angle);
        System.out.println("speed: " + speed);
        System.out.println("speed_x: " + speed_x);
        System.out.println("speed_y: " + speed_y);
        System.out.println("radius: " + radius);
        System.out.println("/////////////////////");


        // color of ball is random
        color_red = Math.random();
        color_blue = Math.random();
        color_green = Math.random();

        color = new Color((float)color_red, (float) color_green, (float) color_blue);

        // init starting position
        x_coord = Math.random()*(field.getSize().getWidth() - 2 * radius) + radius;
        y_coord = Math.random()*(field.getSize().getHeight() - 2 * radius) + radius;

        Thread this_thread = new Thread(this);
        this_thread.start();
    }

    public void run()
    {
        try
        {

            while(true)
            {

                double left_side = x_coord - radius;
                double right_side = x_coord + radius;
                double up_side = y_coord - radius;
                double down_side = y_coord + radius;

                field.canMove(this);
                if( left_side + speed_x <= 0)
                {
                    speed_x *= -1;
                    x_coord = radius;
                }
                else if( right_side + speed_x >= field.getSize().getWidth())
                {
                    speed_x *= -1;
                    x_coord = field.getSize().getWidth() - radius;
                }
                else if ( up_side + speed_y <= 0)
                {
                    speed_y *= -1;
                    y_coord = radius;
                }
                else if(down_side + speed_y >= field.getSize().getHeight())
                {
                    speed_y *= -1;
                    y_coord = field.getSize().getHeight() - radius;
                }
                else
                {
                    x_coord += speed_x;
                    y_coord += speed_y;
                }
                Thread.sleep(15);
            }
        }
        catch (InterruptedException ex) { }
    }

    public void paint(Graphics2D canvas)
    {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double  ball = new Ellipse2D.Double(x_coord - radius, y_coord - radius, 2*radius, 2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }

   public int getRadius() {return  radius; }

}
