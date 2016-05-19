package xyz.ezstein.backend.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
	public static void main(String[] args){
		/*int[] array = new int[]{10,9,8,3,4,3,7,6,5,4,3,2,1,4};
		Planet[] planets = new Planet[]{Planet.C,Planet.B,Planet.A,Planet.D,Planet.E,Planet.F};
		planets=mergeSort(planets);
		System.out.print("[");
		for(int i=0; i<planets.length; i++){
			System.out.print(planets[i]);
			if(i<planets.length-1){
				System.out.print(", ");
			}
		}
		System.out.println("]");*/
		Path path = Paths.get("ABC/234");
		System.out.println(path.toAbsolutePath());
	}
	
	@AnAnnotation(
			author="Ezra",
			date="123"
			)
	public void start(){
		for(int i=0; i<6; i++){
			for(int j=0; j<6; j++){
				System.out.println(i+ " " + j + ": "+ack(i,j));
			}
		}
		
	}
	
	
	public long ack(long m,long n){
		if(m==0){
			return n+1;
		} else if(n==0){
			return ack(m-1,1);
		} else {
			return ack(m-1,ack(m,n-1));
		}
	}
	
	private static int[] mergeSort(int[] array){
		if(array.length<=1){
			return array;
		} else if (array.length==2){
			if(array[0]>array[1]){
				int temp = array[1];
				array[1]=array[0];
				array[0]=temp;
			}
			return array;
		} else {
			int length = (int)(array.length/2);
			int[] a1 = new int[length];
			int[] a2 = new int[array.length-length];
			for(int i=0; i<array.length; i++){
				if(i<length){
					a1[i]=array[i];
				} else {
					a2[i-length] = array[i];
				}
			}
			
			a1=mergeSort(a1);
			a2=mergeSort(a2);
			int a1Index=0;
			int a2Index=0;
			for(int i=0; i<array.length; i++){
				if(a1Index>=a1.length){
					array[i] = a2[a2Index];
					a2Index++;
				} else if (a2Index>=a2.length){
					array[i]=a1[a1Index];
					a1Index++;
				} else if(a1[a1Index]<a2[a2Index]){
					array[i]=a1[a1Index];
					a1Index++;
				} else {
					array[i] = a2[a2Index];
					a2Index++;
				}
			}
			return array;
		}
	}
	
	private static <T extends Comparable<T>> List<T> mergeSort(List<T> array){
		return new ArrayList<T>(Arrays.asList(mergeSort(array.toArray((T[])new Comparable[array.size()]))));
	}
	
	private static <T extends Comparable<T>> T[] mergeSort(T[] array){
		if(array.length<=1){
			return array;
		} else if (array.length==2){
			if(array[0].compareTo(array[1])>0){
				T temp = array[1];
				array[1]=array[0];
				array[0]=temp;
			}
			return array;
		} else {
			int length = (int)(array.length/2);
			T[] a1 = (T[])new Comparable[length];
			T[] a2 =(T[]) new Comparable[array.length-length];
			for(int i=0; i<array.length; i++){
				if(i<length){
					a1[i]=array[i];
				} else {
					a2[i-length] = array[i];
				}
			}
			
			a1=mergeSort(a1);
			a2=mergeSort(a2);
			int a1Index=0;
			int a2Index=0;
			for(int i=0; i<array.length; i++){
				if(a1Index>=a1.length){
					array[i] = a2[a2Index];
					a2Index++;
				} else if (a2Index>=a2.length){
					array[i]=a1[a1Index];
					a1Index++;
				} else if(a1[a1Index].compareTo(a2[a2Index])<0){
					array[i]=a1[a1Index];
					a1Index++;
				} else {
					array[i] = a2[a2Index];
					a2Index++;
				}
			}
			return array;
		}
	}
	
	private enum Planet {
		A(1,2),B(2,3),C(3,4),D(4,5),E(5,6),F(6,7);
		
		private int mass;
		private int radius;
		private Planet(int mass, int raduis){
			this.mass=mass;
			this.radius=radius;
		}
		
		public int getMass(){
			return mass;
		}
		public int getRadius(){
			return radius;
		}
	}
}
