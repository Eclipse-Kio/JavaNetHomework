package javanet.c01.entity;

public class Circle extends Shape {

	private static final long serialVersionUID = 1L;
	private double radius;
	
	
	public Circle(double radius) {
		this.radius = radius;
	}
	
	@Override
	public double getArea() {
		return radius*radius*Math.PI;
	}

	@Override
	public void printInfo() {
		System.out.println(getClass().getSimpleName()+"["+"radius:"+radius+"]");
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

}
