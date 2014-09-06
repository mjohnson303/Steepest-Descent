import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import Jama.Matrix;


public class DataInfo extends JPanel{
	JList alist, blist, xlist;
	DefaultListModel<String> list1,list2,list3;
	ArrayList<String> data1,data2,data3;
	public DataInfo(Matrix a, Matrix b, Matrix x, int steps){
		//data1 = new ArrayList<String>();
		//data2 = new ArrayList<String>();
		//data3 = new ArrayList<String>();
		list1=new DefaultListModel<String>();
		list2=new DefaultListModel<String>();
		list3=new DefaultListModel<String>();
		DecimalFormat d1 = new DecimalFormat("0.00000");
		int i=0, j=0;
		list1.addElement("This is the data for A:");
		for(i=0; i<a.getRowDimension(); i++){
			for(j=0; j<a.getColumnDimension(); j++){
				list1.addElement("In the "+i+" row and "+j+" column: "+d1.format(a.get(i,j)));
			}
		}
		list2.addElement("This is the data for b:");
		for(i=0; i<b.getRowDimension(); i++){
			for(j=0; j<b.getColumnDimension(); j++){
				list2.addElement("In the "+i+" row and "+j+" column: "+d1.format(b.get(i,j)));
			}
		}
		list3.addElement("This is the data for x:");
		for(i=0; i<x.getRowDimension(); i++){
			for(j=0; j<x.getColumnDimension(); j++){
				list3.addElement("In the "+i+" row and "+j+" column: "+d1.format(x.get(i,j)));
			}
		}
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(800,500));
		//creates JLabel for the top and formats it
		JLabel label=new JLabel("Matrices Data");
		Font curFont=label.getFont();
		label.setFont(new Font(curFont.getFontName(), curFont.getStyle(), 25));
		alist=new JList(list1);
		alist.setVisibleRowCount(8);
		blist=new JList(list2);
		blist.setVisibleRowCount(8);
		xlist=new JList(list3);
		xlist.setVisibleRowCount(8);
		add(label);
		add(new JScrollPane(alist));
		add(new JScrollPane(blist));
		add(new JScrollPane(xlist));
		add(new JLabel("It took "+steps+" steps."));
		
	}
}
