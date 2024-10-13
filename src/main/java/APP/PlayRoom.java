package APP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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


        JLabel label1 = new JLabel("初始位置");
        JTextField initialPosText = new JTextField("30 80 110 160 250", 15);
        label1.setFont(GAME_FONT);

        JButton button_allGame = new JButton("所有游戏");
        button_allGame.setFont(GAME_FONT);


        JButton button_singleGame = new JButton("单局游戏");
        button_allGame.setFont(GAME_FONT);


//        add(label1);
//        add(initialPosText);
        add(button_allGame);
        add(button_singleGame);

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
//                try {
//                    positions = stringToIntArr(initialPosText.getText());
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(null, "初始位置输入错误！", "错误", JOptionPane.ERROR_MESSAGE);
//
//                }
//                if (!checkPositions(positions)) //检测输入初始位置是否是小于木棒长度的正数
//                {
//                    JOptionPane.showMessageDialog(null,
//                            "您输入的初始位置不合法，请确保初始位置能被蚂蚁速度整除且与蚂蚁数量匹配",
//                            "警告",
//                            JOptionPane.WARNING_MESSAGE);
//                }
//                else {
                    setVisible(false);

                    CreepingGame game = new CreepingGame(5,300,5,positions,0);

                    game.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            super.windowClosed(e);
                            setVisible(true);
                        }
                    });
//                }


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