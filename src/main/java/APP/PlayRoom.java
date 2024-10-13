package APP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static parameter.Constant.*;


public class PlayRoom extends JFrame {
    private static int Stick_Length = 300;
    private static int[] positions = {30, 80, 110, 160, 250};
    private static int antNum = 5;
    private static int speed =5;

    public PlayRoom() {
        initFrame();
    }

    private void initFrame() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Play Room");
        setLocation(WINDOW_X, WINDOW_Y);
        setResizable(false);

        setLayout(new FlowLayout(FlowLayout.CENTER, 150, 25));


//        JLabel label1 = new JLabel("初始位置");
//        JTextField initialPosText = new JTextField("30 80 110 160 250", 15);
//        label1.setFont(GAME_FONT);

        JButton button_allGame = new JButton("遍历所有情形");
        button_allGame.setFont(GAME_FONT);
        button_allGame.setBackground(Color.GREEN);
        button_allGame.setBorder(BorderFactory.createRaisedBevelBorder());
//        button_allGame.setBounds(100, 100, 100, 50);


        JButton button_singleGame = new JButton("玩家选择情形");
        button_singleGame.setFont(GAME_FONT);
        button_singleGame.setBackground(Color.CYAN);
        button_singleGame.setBorder(BorderFactory.createRaisedBevelBorder());


       Choice c1 = new Choice();
       Choice c2 = new Choice();
       Choice c3 = new Choice();
       Choice c4 = new Choice();
       Choice c5 = new Choice();
       c1.add("Ant_1 Turn Left");
       c1.add("Ant_1 Turn Right");
       c2.add("Ant_2 Turn Left");
       c2.add("Ant_2 Turn Right");
       c3.add("Ant_3 Turn Left");
       c3.add("Ant_3 Turn Right");
       c4.add("Ant_4 Turn Left");
       c4.add("Ant_4 Turn Right");
       c5.add("Ant_5 Turn Left");
       c5.add("Ant_5 Turn Right");
       c1.setFont(GAME_FONT);
       c2.setFont(GAME_FONT);
       c3.setFont(GAME_FONT);
       c4.setFont(GAME_FONT);
       c5.setFont(GAME_FONT);
       c1.setBackground(Color.WHITE);
       c2.setBackground(Color.WHITE);
       c3.setBackground(Color.WHITE);
       c4.setBackground(Color.WHITE);
       c5.setBackground(Color.WHITE);
       c1.setBounds(100, 100, 100, 50);
       c2.setBounds(100, 150, 100, 50);
       c3.setBounds(100, 200, 100, 50);
       c4.setBounds(100, 250, 100, 50);
       c5.setBounds(100, 300, 100, 50);
       add(button_allGame);
       add(button_singleGame);

       add(c1);
       add(c2);
       add(c3);
       add(c4);
       add(c5);





        button_allGame.setBounds(100, 300, 100, 50);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // 结束程序
            }
        });
        setVisible(true);

        button_allGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//
                    setVisible(false);

                    CreepingGame game = new CreepingGame(5,300,5,positions,0);

                    game.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            super.windowClosed(e);
                            setVisible(true);
                        }
                    });


            }
        });
        button_singleGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//
                setVisible(false);
                int ant1_dire = c1.getSelectedIndex();
                int ant2_dire = c2.getSelectedIndex()*2;
                int ant3_dire = c3.getSelectedIndex()*4;
                int ant4_dire = c4.getSelectedIndex()*8;
                int ant5_dire = c5.getSelectedIndex()*16;

                int totalRound = 1+ant1_dire+ant2_dire+ant3_dire+ant4_dire+ant5_dire;
                System.out.println(totalRound);

                CreepingGame game = new CreepingGame(5,300,5,positions,totalRound);

                game.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        super.windowClosed(e);
                        setVisible(true);
                    }
                });


            }
        });


    }

    private static int[] stringToIntArr(String str) {
        String[] arr = str.split("\\s+");
        int[] intArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            intArr[i] = Integer.parseInt(arr[i]);
        }

        return intArr;
    }

    private boolean checkPositions(int[] positions) {
        if (positions == null || positions.length == 0 || positions.length != antNum) {
            return false;
        }
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] < 0 || positions[i] > this.Stick_Length || positions[i] % speed != 0) {
                return false;
            }
        }

        return true;

    }
}