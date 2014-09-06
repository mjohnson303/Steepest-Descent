import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.ColorBar;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.ContourPlot;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.contour.DefaultContourDataset;

import com.lowagie.text.Rectangle;


public class DrawXYZ extends JPanel {
	private ContourPlot cp;
	private double width, height;
	XYBlockRenderer xyb;
	DefaultContourDataset dcs;
	ValueAxis v1, v2;
	int series;
	
	public DrawXYZ(DefaultContourDataset dcs, int seriesSize){
		xyb = new XYBlockRenderer();
		this.dcs = dcs;
		series=seriesSize;
		NumberAxis domain = new NumberAxis("x1");
		domain.setAutoRangeIncludesZero(true);
		NumberAxis range = new NumberAxis("x2");
		range.setAutoRangeIncludesZero(true);
		v1 = domain; v2=range;
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
		Rectangle2D r = new Rectangle2D.Double(0,0,width-50, height-75);
		//cp.draw(g2, r,null, new PlotState(), null);
		ChartRenderingInfo chi = new ChartRenderingInfo();
		chi.setChartArea(r);
		XYItemRendererState one = new XYItemRendererState(new PlotRenderingInfo(chi));
		XYPlot two = new XYPlot(dcs,v1,v2, new DefaultXYItemRenderer());
		xyb.drawItem(g2,one,r,new PlotRenderingInfo(chi),two,v1,v2,dcs,series,1,new CrosshairState(), 1);
	}
}