import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.jfree.chart.axis.ColorBar;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.ContourPlot;
import org.jfree.chart.plot.PlotState;
import org.jfree.data.contour.DefaultContourDataset;

import com.lowagie.text.Rectangle;


public class DrawSteepestDescent extends JPanel {
	private ContourPlot cp;
	private double width, height;
	
	public DrawSteepestDescent(DefaultContourDataset dcs){
		NumberAxis domain = new NumberAxis("x1");
		domain.setAutoRangeIncludesZero(false);
		NumberAxis range = new NumberAxis("x2");
		range.setAutoRangeIncludesZero(false);
		cp = new ContourPlot(dcs, domain, range, new ColorBar("F(x1,x2)"));
		/*NumberAxis domain = (NumberAxis) cp.getDomainAxis();
		domain.setAutoRange(true);
		NumberAxis range = (NumberAxis) cp.getRangeAxis();
		range.setAutoRange(true);*/
		
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		//setSize(1000,650);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		//Rectangle2D r = new Rectangle2D.Double(0,0,1000,650);
		Rectangle2D r = new Rectangle2D.Double(0,0,width-50, height-75);
		cp.draw(g2, r,null, new PlotState(), null);
	}
}