package APP;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
//import java.Math.*;

import entity.*;
import parameter.Constant;
import parameter.Constant.*;

public class CreepingGame extends Frame {
   private static int gameState;
   private int totalRound; //总共需要进行几个回合
   private int gameRound;

    public static final int READY = 0;
    public static final int RUNNING = 1;
    public static final int FINISHED = 2;

    public boolean GAME_OVER = false;

    private List<Ant> ant_List;

    private int Stick_Length;
    private int ant_num;
    private int speed = 5;
    private int[] ants_positions;

    private Stick stick;
    private int Round_Time;
    private double Round_Time_Max;

    private int Longest_Time = -99999;
    private int Shortest_Time = 99999;
    private String Shortest_Situation = "";
    private String Longest_Situation = "";
    private String Current_Situation = "";


    private final BufferedImage BufImg = new BufferedImage(Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);

    public CreepingGame(int speed,int stick_Length, int ant_num,int[] ants_positions,int totalRound) {
        this.Stick_Length = stick_Length;
        this.ant_num = ant_num;
        this.ants_positions = ants_positions;
        this.speed = speed;

        if(totalRound == 0)
        {
           this.totalRound = 0;
        }
        else
        {
            this.totalRound = totalRound;
            this.gameRound = totalRound-1;
        }

        InitFrame();
        InitGame();
    }

    private void InitFrame()
    {
        setSize(Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
        setTitle(Constant.GAME_NAME);
        setLocation(Constant.WINDOW_X, Constant.WINDOW_Y);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               dispose();
            }
        });
        setVisible(true);
    }

    private void InitGame()
    {
        this.Round_Time = 0;
        if(totalRound == 0)
            this.gameRound = 0;
        else
            this.gameRound = totalRound-1;
        this.stick = new Stick(this.Stick_Length);
        this.ant_List = new ArrayList<>();

        for(int i=0;i<ant_num;i++)
        {
            Ant ant = new Ant(ants_positions[i],i,5);
            ant_List.add(ant);
        }

        Set_State(READY);

        new Thread(() -> {
            while(true)
            {
                repaint();
                try{
                    Thread.sleep(Constant.FPS_GAME);

                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();





    }

    public void Set_State(int gameState)
    {
        this.gameState = gameState;
    }

    @Override
    public void update(Graphics g) {
        Graphics buf_temp = this.BufImg.getGraphics();
        g.setColor(Color.BLACK);
        g.setFont(Constant.GAME_FONT);
        if(!GAME_OVER)
            stick.Draw_Stick(buf_temp);

        if (this.gameState == READY) {
            this.Round_Time = 0;
            if (totalRound == 0) {
                if (gameRound < 1 << ant_num) {
                    for (int j = 0; j < ant_num; j++) {
                        ant_List.get(j).Set_pos(ants_positions[j]);
                    }

                    for (int i = ant_List.size() - 1; i >= 0; i--) {
                        int direction = gameRound >>> i & 1;
                        ant_List.get(i).Set_Direction(direction);
                    }
                    this.Current_Situation = Get_Round_Situation();

                    Round_Time_Max = Calculate_Ans_Time();
                    Set_State(RUNNING);

                }

            }

            else
            {
//                this.gameRound = totalRound-1;
                if (gameRound < totalRound) {
                    for(int i=0;i<ant_num;i++)
                    {
                        ant_List.get(i).Set_pos(ants_positions[i]);
                    }
                    for (int i = ant_List.size() - 1; i >= 0; i--) {
                        int direction = gameRound >>> i & 1;
                        ant_List.get(i).Set_Direction(direction);
                    }

                    Round_Time_Max = Calculate_Ans_Time();
                    Set_State(RUNNING);
                }
            }
        }
        else if (this.gameState == FINISHED)
        {
            if (this.Round_Time < this.Shortest_Time)
            {
                this.Shortest_Time = this.Round_Time;
                this.Shortest_Situation = this.Current_Situation;
            }
            if (this.Round_Time > this.Longest_Time)
            {
                this.Longest_Time = this.Round_Time;
                this.Longest_Situation = this.Current_Situation;
            }
            g.drawString("第"+this.gameRound+"回合结束:"+this.Current_Situation+"用时"+this.Round_Time_Max+"秒",150,350);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("第"+this.gameRound+"回合结束:"+this.Current_Situation+"用时"+this.Round_Time_Max+"秒");
            gameRound++;
            Set_State(READY);
        }
        else if (this.gameState == RUNNING) {
            double Time_temp = 0;
            while(true)
            {
                double time_temp = Climb_time();
                if(time_temp == 0)
                {
                    Draw_Moving(1,g);
//                    for (Ant ant:ant_List) {
//                        ant.Climb(1);
//                        ant.Draw_Ant(g);
//                    }
                    Test_Collision();
                    try{
                        Thread.sleep(Constant.FPS);
                        Draw(g,1);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    break;


                }
                else if((time_temp+Time_temp) >= 1)
                {
                    double time_left = 1-Time_temp;
                    Draw_Moving(time_left,g);
//                    for (Ant ant:ant_List)
//                    {
//                       ant.Climb(time_left);
//                       ant.Draw_Ant(g);
//                       Time_temp = 1;
//                    }
                    Time_temp = 1;
                    Test_Collision();
                    try{
                        Thread.sleep(Constant.FPS);
                        Draw(g,this.Round_Time+Time_temp);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                }

                else
                {
                    Time_temp += time_temp;
                    Draw_Moving(time_temp,g);
//                    for (Ant ant:ant_List) {
//                        ant.Climb(time_temp);
//                        ant.Draw_Ant(g);
//                    }
                    Test_Collision();
                }

                try{
                    Thread.sleep(Constant.FPS);
                    Draw(g,this.Round_Time+Time_temp);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            this.Round_Time += 1;

            if(Round_Time>=Round_Time_Max || IsFinished()) {
                int temp = (int)(Round_Time_Max);
                Round_Time = (int)(temp);
                Set_State(FINISHED);
            }
            g.drawImage(BufImg,0,0,null);
            g.drawString("最短耗时情况为:"+this.Shortest_Situation+" 耗费时间:"+this.Shortest_Time,150,100);
            g.drawString( "最长耗时情况为:"+this.Longest_Situation+" 耗费时间:" + this.Longest_Time,150,130);
            g.drawString( "本局当前耗时为： "+this.Round_Time ,150,160);
//            System.out.println("第"+this.gameRound+"回合，用时"+this.Round_Time+"秒");


        }
//        g.drawImage(BufImg, 0, 0, null);
        if(totalRound == 0)
        {
            if(gameRound >= 1 << ant_num) {
                GAME_OVER = true;
                buf_temp.dispose();

                buf_temp.drawImage(BufImg, 0, 0, null);
                g.drawImage(BufImg, 0, 0, null);
                g.drawString("最短耗时情况为:"+this.Shortest_Situation+" 耗费时间:"+this.Shortest_Time,150,100);
                g.drawString( "最长耗时情况为:"+this.Longest_Situation+" 耗费时间:" + this.Longest_Time,150,130);
                g.drawString("关闭当前窗口开启新游戏",250,200);
            }

        }
        else
        {
            if(gameRound >= totalRound)
            {
                GAME_OVER = true;
                buf_temp.dispose();
                buf_temp.drawImage(BufImg, 0, 0, null);
                g.drawImage(BufImg, 0, 0, null);
                g.drawString("本局:"+this.Current_Situation+" 耗时：" + this.Shortest_Time,150,100);
//                g.drawString("本局耗时为："+this.Round_Time,50,150);
                g.drawString("关闭当前窗口开启新游戏",250,200);
            }
        }
    }

    public void Draw(Graphics g,double  time)
    {

        g.drawImage(BufImg, 0, 0, null);
        g.drawString("最短耗时情况为:"+this.Shortest_Situation+" 耗费时间:"+this.Shortest_Time,150,100);
        g.drawString( "最长耗时情况为:"+this.Longest_Situation+" 耗费时间:" + this.Longest_Time,150,130);
        g.drawString( "本局当前耗时为： "+time,150,160);

    }

    public void Draw_Moving(double time_temp,Graphics g)
    {
//        System.out.println("time_temp:"+time_temp);
//        double t = 0;
        int i=0;
        int Total = (int)(time_temp/Constant.INC_TIME);
        while(i<Total)
        {
            i++;
            for (Ant ant:ant_List)
            {
                ant.Climb(Constant.INC_TIME);
                ant.Draw_Ant(g);
//                System.out.println("ant"+ant.order+" climbing,Position:"+ant.Get_pos());
            }
            Test_Collision();
            try{
                Thread.sleep(Constant.FPS);
                Draw(g,this.Round_Time+i*Constant.INC_TIME);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

//        System.out.println("i:"+i);
         double eps_0 = 1e-30;
//        System.out.println("eps_0:"+eps_0);
        if((time_temp-Total*Constant.INC_TIME)>eps_0)
        {
            System.out.println("进入补时，time_temp:,"+time_temp+"t:"+Total*Constant.INC_TIME);
//            System.out.println("t-time_temp-Constant.INC_TIME:"+(t-Constant.INC_TIME-time_temp));
            for (Ant ant:ant_List)
            {
                ant.Climb(time_temp+Constant.INC_TIME-Total*Constant.INC_TIME);
                ant.Draw_Ant(g);
//                       Time_temp = 1;
            }
            Test_Collision();
            try{
                Thread.sleep(Constant.FPS);
                Draw(g,this.Round_Time+time_temp);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        Test_Collision();



    }

    public String Get_Round_Situation()
    {
        String situation = "";
        for(int i=0;i<ant_num;i++)
        {
            situation += (i+1)+":";
            if(ant_List.get(i).Get_Direction() == Ant.LEFT)
                situation += "左";
            else
                situation += "右";
            situation += " ";
        }
        return situation;
    }

    public boolean IsFinished()
    {
        for(Ant ant : ant_List)
            if(!ant.IsStop())
                return false;
        return true;
    }

    private void Test_Collision() {
        Pair[] pairs = new Pair[ant_num];
        for(int i=0;i<ant_num;i++)
            pairs[i] = new Pair(i,ant_List.get(i).Get_pos());
        Arrays.sort(pairs);

        int[] ant_order = new int[ant_num];
        for(int i=0;i<ant_num;i++)
            ant_order[i] = pairs[i].order;
        for(int i=0;i<ant_num-1;i++)
        {
            if(ant_List.get(ant_order[i]).IsStop()) continue;
            for(int j=i+1;j<ant_num;j++)
            {
                if(ant_List.get(ant_order[i]).Get_pos() == ant_List.get(ant_order[j]).Get_pos())
                {
                    ant_List.get(ant_order[j]).Change_Direction();
                    ant_List.get(ant_order[i]).Change_Direction();
                    break;
                }
            }
        }


    }

    private double Climb_time() //获取下一次碰撞出现的时间
    {
        Pair[] pairs = new Pair[ant_num];
        for(int i=0;i<ant_num;i++)
            pairs[i] = new Pair(i,ant_List.get(i).Get_pos());
        Arrays.sort(pairs);

        int[] ant_order = new int[ant_num];
        for(int i=0;i<ant_num;i++)
            ant_order[i] = pairs[i].order;

        int i = 0;
        double ans_time = 0;
        while(i<ant_num-1)
        {
            int direction = ant_List.get(ant_order[i]).Get_Direction();
            if(direction == Ant.STOP || direction ==Ant.LEFT)
            {
                i++;
            }

            else
            {
               int direction_right = ant_List.get(ant_order[i+1]).Get_Direction();
               if(direction_right == Ant.RIGHT)
               {
                   i++;
                   continue;
               }
               else
               {
                   double distance = ant_List.get(ant_order[i+1]).Get_pos()-ant_List.get(ant_order[i]).Get_pos();
                   double time = distance/(2*speed);

                   ans_time = Math.min(time,ans_time);
                   i = i+2;
               }
            }
        }
        if(ans_time<0)
            System.out.println("--------------------------------------ans_time<0----------------------------------------");

        return ans_time;

    }

    private double Calculate_Ans_Time()
    {
        double ans = ant_List.get(0).Get_Time();
        for(int i = 1;i<ant_num;i++)
        {
            double temp = ant_List.get(i).Get_Time();
            if(temp>=ans)
                ans = temp;
        }
        return ans;
    }





}
class Pair implements Comparable<Pair>
{
    public int order;
    public double position;
    public Pair(int order, double position)
    {
        this.order = order;
        this.position = position;
    }

    @Override
    public int compareTo(Pair o) {
        int ret = this.order - o.order;
        return ret;
    }

}
