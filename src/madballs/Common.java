package madballs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;

/**
 * this class stores the methods that are common throughout the game
 * @author Caval
 */
public class Common {
    /*
    convert BufferedImage to WritableImage(javafx image)
    src: https://blog.idrsolutions.com/2012/11/convert-bufferedimage-to-javafx-image/
    */
    public static WritableImage bufferedToJavafxImage(BufferedImage bf) {
        WritableImage wr = null;
        if (bf != null) {
            wr = new WritableImage(bf.getWidth(), bf.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bf.getWidth(); x++) {
                for (int y = 0; y < bf.getHeight(); y++) {
                    pw.setArgb(x, y, bf.getRGB(x, y));
                }
            }
        }
        
        return wr;
    }
    
    /*
    method to resize an image
    src: http://www.mkyong.com/java/how-to-resize-an-image-in-java/
    */
    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT){
	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	g.dispose();
		
	return resizedImage;
    }
    
    /**
     * get the current date and time
     * @return 
     */
    public static String getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date); //2014/08/06 15:59:48
    }
    
    public static WritableImage[] getImageChunk(String imageSourceUrl) throws IOException{
        File file = new File(imageSourceUrl);
        BufferedImage image = ImageIO.read(file); //reading the image file  
        
        // determine that the image will be divided into 4 x 4 = 16 chunks
        int rows = 4;
        int cols = 4;  
        int chunks = rows * cols;  
        
        // determines the chunk width and height  
        int chunkWidth = image.getWidth() / cols;
        int chunkHeight = image.getHeight() / rows;  
        int count = 0;  
        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks  
        for (int x = 0; x < rows; x++) {  
            for (int y = 0; y < cols; y++) {  
                imgs[count] = image.getSubimage(y * chunkWidth, x * chunkHeight, chunkWidth, chunkHeight);
                count++;
            }  
        }
        
        // convert to javafx images
        WritableImage[] finalImgs = new WritableImage[chunks];
        for (int i = 0; i < chunks; i++){
            finalImgs[i] = bufferedToJavafxImage(imgs[i]);
        }
        
        return finalImgs;
    }
    
    /**
     * add an image to a GridPane
     * @param img
     * @param grid
     * @param col
     * @param row 
     */
    public static void setImageToGrid(WritableImage img, GridPane grid, int col, int row){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(5.5, 0, 0, 14));
        hbox.getChildren().add(new ImageView(img));
        grid.add(hbox, col, row);
    }
    
    /**
     * get the web hex string from a Javafx Paint color
     * @param color
     * @return 
     * src : http://stackoverflow.com/questions/17925318/how-to-get-hex-web-string-from-javafx-colorpicker-color
     */
    public static String toRGBCode( Color color ) {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }
    
    /**
     * sort a Map by its value
     * @param <K>
     * @param <V>
     * @param map
     * @return 
     * src: http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> 
        sortByValue( Map<K, V> map )
    {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted( Map.Entry.comparingByValue() )
            .forEachOrdered( e -> result.put(e.getKey(), e.getValue()) );

        return result;
    }
        
    //https://coderanch.com/t/405258/java/java/String-IsNumeric
    public static boolean isNumeric(String str)
    {
        try
        {
          double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
          return false;
        }
        return true;
    }
}
