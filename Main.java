import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Graphics2D;

public class Main {

	public static void main(String[] args) {
		
		BufferedImage img1, img2;
		long start = System.nanoTime();
		try {
			
			System.out.print("Start: ");
			img1 = ImageIO.read(new File("/Users/Sungmin/Desktop/test1.png"));
			img2 = ImageIO.read(new File("/Users/Sungmin/Desktop/test2.png"));
			System.out.printf("%d\n", System.nanoTime() - start);

			Rectangle rtg1 = new Rectangle(img1.getWidth(), img1.getHeight());
			Rectangle rtg2 = new Rectangle(img2.getWidth(), img2.getHeight());
			
			System.out.print("Compare: ");
			start = System.nanoTime();
			DiffPoints DP = ImageUtils.compare(img1, rtg1, img2, rtg2);
			System.out.printf("%d\n", System.nanoTime() - start);
			
			
			System.out.print("Union: ");
			start = System.nanoTime();
			List<Rectangle> rectangles = ImageUtils.convertDiffPointsToAreas(DP, 0);
			System.out.printf("\t%d\n", System.nanoTime() - start);

			////// rectangles //////
			System.out.print("Write: ");
			start = System.nanoTime(); 
			
			BufferedImage diffImage = new BufferedImage(img1.getWidth(), img2.getHeight(), BufferedImage.TYPE_INT_RGB);

			// Set graphics	
			Graphics2D graphics = diffImage.createGraphics();

			// Draw rectangles
			for(Rectangle rect : rectangles) {
				graphics.fill(rect);
			}
			graphics.dispose();

			try {
				ImageIO.write(diffImage, "png", new File("/Users/Sungmin/Desktop/test_diff.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.printf("%d\n", System.nanoTime() - start);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.printf("CPU Time: %d\n", System.nanoTime()-start);


	}

}
