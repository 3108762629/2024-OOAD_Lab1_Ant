package entity;

import parameter.Constant;
import parameter.Constant.*;
import parameter.GamePara;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Stick {
    private BufferedImage stick_Image;
    public static int length = 300;

    public Stick(int length)
    {
        this.stick_Image = GamePara.Load_BufferedImage(Constant.POLE_IMG_PATH);
        Stick.length = length;
    }

    public void Draw_Stick(Graphics g)
    {
        g.setColor(Constant.BACKGROUND_COLOR);
        g.fillRect(0,0,Constant.WINDOW_WIDTH,Constant.WINDOW_HEIGHT);

        g.drawImage(stick_Image,Constant.STICK_INIT_POSITION,Constant.STICK_INIT_HEIGHT,length,15,null);
    }

    public boolean  Is_Climb_Out(Ant ant)
    {
       if(ant.Get_pos()<=Constant.STICK_INIT_POSITION+length || ant.Get_pos()>=Constant.STICK_INIT_POSITION+Stick.length)
           return true;
       else
           return false;

    }


}
