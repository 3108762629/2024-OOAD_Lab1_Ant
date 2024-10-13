package parameter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GamePara {
    private GamePara(){}

    public static BufferedImage Load_BufferedImage(String image_Path)
    {
        try
        {
            return ImageIO.read(new File(image_Path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }


}
