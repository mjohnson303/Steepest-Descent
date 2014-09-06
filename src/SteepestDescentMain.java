import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.jfree.chart.axis.ColorBar;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.ContourPlot;
import org.jfree.data.contour.DefaultContourDataset;

import Jama.Matrix;
/**
 * class implements the steepest descent algorithm
 * @author Matthew Johnson
 * @version 3 3/25/12
 */

public class SteepestDescentMain {
	public static Matrix A;
	public static Matrix B;
	public static Matrix x;
	public static ArrayList<Matrix> xValues;
	public static ArrayList<Double> fValues;
	public static int problem;

	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args){
		String inputValue = JOptionPane.showInputDialog("Would you like to do Problem 1 or 2?");
		problem = Integer.parseInt(inputValue);
		if(problem==1)
			one(true);
		else if(problem==2)
			two();
		else
			JOptionPane.showMessageDialog(null, "Wrong Input, closing program", "Wrong Input, closing program", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * solves the first problem of the assignment
	 */
	public static void one(boolean q){
		if(q)
			getInitialInput();
		xValues=new ArrayList<Matrix>();
		fValues=new ArrayList<Double>();
		Matrix temp2=null;
		boolean again=true;
		double tempValue;
		while(again){
			int steps=implement();
			/*
			System.out.println("x:");
			x.print(x.getRowDimension(),15);
			System.out.println("A:");
			A.print(x.getRowDimension(), 30);
			System.out.println("b:");
			B.print(x.getRowDimension(), 15);*/
			JFrame frame = new JFrame("Steepest Descent Data");//frame with the image in it
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			DataInfo df = new DataInfo(A,B,x, steps);
			frame.add(new JScrollPane(df));
			frame.setSize(800,650);
			frame.setVisible(true);
			//xValues.add(x);
			temp2=A.times(x);
			tempValue=.5 * multiply(temp2, x);
			tempValue=tempValue - multiply(B,x);
			fValues.add(tempValue);
			int a=JOptionPane.showConfirmDialog(null,	"It took "+steps+" steps. Would you like to calculate another matrix using a different x0?", "Steepest Descent", JOptionPane.YES_NO_OPTION);
			if(a!=JOptionPane.YES_OPTION)
				again=false;
		}
		if(q){
			JOptionPane.showMessageDialog(null, "Thanks for using the Steepest Descent Program.", "All done.", JOptionPane.INFORMATION_MESSAGE);
			graphStuff();
		}
	}
	
	public static void graphStuff(){
		//Does the graphing stuff
		Object[] x1 = new Object[xValues.size()];
		Object[] x2 = new Object[xValues.size()];
		Object[] f = new Object[fValues.size()];
		Matrix temp = xValues.get(0);
		for(int i=0; i<xValues.size(); i++){
			temp=xValues.get(i);
			x1[i]=temp.get(0,0);
			x2[i]=temp.get(1,0);
		}
		for(int k=0; k<fValues.size(); k++){
			f[k]=fValues.get(k);
		}
		DefaultContourDataset dcs = new DefaultContourDataset("Double", x1, x2, f);
		JFrame frame = new JFrame("Steepest Descent");//frame with the image in it
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DrawSteepestDescent dsd=new DrawSteepestDescent(dcs);
		//DrawXYZ dxyz = new DrawXYZ(dcs, x1.length);
		frame.add(dsd);
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		//frame.setSize(1100,675);
		//f.setLocation(0,0);
		frame.setVisible(true);
		if(problem==2){
			JOptionPane.showMessageDialog(null, "Do not close out of graph, continue on with all graphs before exiting out of any.", "All done.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * solves the second problem of the assignment
	 */
	public static void two(){
		for(int i=0; i<5; i++){
			//System.out.println(i+1+" Turn");
			JOptionPane.showMessageDialog(null, "We are on the "+(i+1)+" matrix of the set.","Problem 2.", JOptionPane.INFORMATION_MESSAGE);
			B = new Matrix(10,1).random(10,1);
			Matrix B2 = new Matrix(10,10);
			B2=B2.random(10,10);
			A=(B2.transpose()).times(B2);
			one(false);
		}
	}
	/**
	 * Multiplies a 1xn matrix by a nx1 matrix
	 * @param one first matrix
	 * @param two second matrix
	 * @return the double product
	 */
	public static double multiply(Matrix one, Matrix two){
		double f = 0;
		for(int i=0; i<one.getRowDimension(); i++){
			f+=one.get(i,0) * two.get(i,0);
		}
		return f;
	}
	
	/**
	 * implements the steepest descent algorithm
	 * @return how many steps it took
	 */
	public static int implement(){
		int steps=0;
		double acc=1 * Math.pow(10,-5);
		getXInput();
		Matrix d=calcD(x);
		double magD=acc;
		while(magD>=acc){
			steps++;
			d=calcD(x);
			x=calcX(d);
			magD=calcMag(d);
		}
		return steps;
	}
	
	/**
	 * calculates the d value for the algorithm
	 * @param x the x matrix
	 * @return the d matrix
	 */
	public static Matrix calcD(Matrix x){
		/*
			dk-1 = -(Axk-1  - b) 
		 */
		if(A.getRowDimension()!=x.getRowDimension()){
			//System.out.println("serious errors");
			JOptionPane.showMessageDialog(null, "Hmm, you messed up the program.", "Error.", JOptionPane.ERROR_MESSAGE);
		}
		Matrix temp = A.times(x);
		Matrix temp2=temp.minus(B);
		temp2=temp2.times(-1);
		return temp2;
	}
	
	/**
	 * calculates the x matrix
	 * @param d the d matrix
	 * @return the x matrix
	 */
	public static Matrix calcX(Matrix d){
		Matrix newX=x;
		Matrix temp=x;
		Matrix temp2=x;
		double num=Math.pow(calcMag(d),2);//numerator
		
		temp2=A.times(d);
		double denom=0;
		for(int m=0; m<temp2.getRowDimension(); m++){//calculate the denominator
			denom+=d.get(m,0)*temp2.get(m,0);
		}
		double factor=num/denom;//factor to multiply d by
		Matrix newD=d.times(factor);
		newX=newD.plus(temp);
		
		return newX;
	}
	
	/**
	 * calculates the magnitude of a matrix
	 * @param d the matrix to calculate the magnitude of 
	 * @return the magnitude
	 */
	public static double calcMag(Matrix d){
		double mag=0;
		for(int i=0; i<d.getRowDimension(); i++){
			mag+=Math.pow(d.get(i,0),2);
		}
		mag=Math.sqrt(mag);
		return mag;
	}
	
	/**
	 * gets initial values of A and b
	 */
	public static void getInitialInput(){
		//System.out.println("Enter the data for matrix A and b that we will be using for the steepest descent");
		//Scanner s=new Scanner(System.in);
		JOptionPane.showMessageDialog(null, "We are now going to get the data for Matrix A and B.", "Input.", JOptionPane.INFORMATION_MESSAGE);
		//System.out.print("How many rows does A have: ");
		//int n=s.nextInt();
		String inputValue = JOptionPane.showInputDialog("How many rows does A have.");
		int n = Integer.parseInt(inputValue);
		//System.out.print("How many columns does A have: ");
		//int k=s.nextInt();
		inputValue = JOptionPane.showInputDialog("How many columns does A have.");
		int k =Integer.parseInt(inputValue);
		
		double[][] matrixA= new double[n][k];
		double[] matrixB= new double[n];
		for(int i=0; i<n; i++){
			for(int j=0; j<k; j++){
				//System.out.print("Enter the number in the "+i+" row and "+j+" column of matrix A: ");
				//matrixA[i][j]=s.nextDouble();
				inputValue = JOptionPane.showInputDialog("Enter the number in the "+i+" row and "+j+" column of matrix A.");
				matrixA[i][j]=Double.parseDouble(inputValue);
			}
		}
		for(int m=0; m<n; m++){
			//System.out.print("Enter the number in the "+m+" row of matrix B: ");
			//matrixB[m]=s.nextDouble();
			inputValue = JOptionPane.showInputDialog("Enter the number in the "+m+" row of matrix B.");
			matrixB[m]=Double.parseDouble(inputValue);
		}

		//System.out.print("\n");
		B=new Matrix(matrixB, matrixB.length);
		A=new Matrix(matrixA);
	}
	
	/**
	 * gets the initial x input
	 */
	public static void getXInput(){
		Scanner s=new Scanner(System.in);
		double[] xMatrix=new double[A.getColumnDimension()];
		for(int l=0; l<A.getColumnDimension(); l++){
			//System.out.print("Enter the number in the "+l+" row of matrix x to start with: ");
			//xMatrix[l]=s.nextDouble();
			String inputValue = JOptionPane.showInputDialog("Enter the number in the "+l+" row of matrix x to start with");
			try{
				xMatrix[l] = Double.parseDouble(inputValue);
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(null, "Invalid input.", "Error.", JOptionPane.ERROR_MESSAGE);
			}
		}
		x=new Matrix(xMatrix, xMatrix.length);
		xValues.add(x);
	}
}
