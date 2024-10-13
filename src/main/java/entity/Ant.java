package entity;

import static parameter.Constant.*;
import parameter.Constant.*;
import parameter.GamePara;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Ant {
    private double pos;
    private final BufferedImage ant_image;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int STOP = 2;

    private int direction = RIGHT;

    public int order;

    private int speed = 5;

    public Ant(int pos,int order,int speed)
    {
        this.pos = pos+STICK_INIT_POSITION;
        this.order = order;
        this.speed = speed;
        this.ant_image = GamePara.Load_BufferedImage(ANT_IMG_PATH[this.order]);
    }

    public void Climb(double time)
    {
        if(this.direction == STOP)
            return;
        else if(this.direction == LEFT)
        {
            this.pos = pos-this.speed*time;
            if(this.pos <= STICK_INIT_POSITION)
            {
                pos = STICK_INIT_POSITION;
                this.direction = STOP;
            }
        }
        else {
            this.pos = pos+this.speed*time;
            if(this.pos >= Stick.length+STICK_INIT_POSITION)
            {
                pos = Stick.length + STICK_INIT_POSITION;
                direction = STOP;
            }
        }

    }

    public Boolean IsStop()
    {
        return direction == STOP;
    }

    public void Change_Direction()
    {
        if(this.direction == LEFT)
            this.direction = RIGHT;
        else if (direction == RIGHT)
            this.direction = LEFT;
        else return;
    }

    public double Get_pos()
    {
        return this.pos;
    }

    public void Set_pos(double pos)
    {
        this.pos = pos+STICK_INIT_POSITION;
    }

    public void Set_Direction(int direction)
    {
        this.direction = direction;
    }

    public int Get_Direction()
    {
        return this.direction;
    }


    public void Draw_Ant(Graphics g)
    {
        g.drawImage(ant_image,(int)this.pos,(int)STICK_INIT_HEIGHT-25,null);
    }

    public double Get_Time()
    {
        if(this.direction == LEFT)
            return (this.pos-STICK_INIT_POSITION)/this.speed;
        else
            return (Stick.length-this.pos+STICK_INIT_POSITION)/this.speed;
    }


}
