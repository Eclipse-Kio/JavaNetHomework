package javanet.c01.entity;

public class Rectangle extends Shape {

	private static final long serialVersionUID = 1L;

	private double width;
	private double height;

	public Rectangle(double width, double height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	@Override
	public double getArea() {
		return width * height;
	}

	@Override
	public void printInfo() {
		System.out.println(getClass().getSimpleName() + "[" + "width:" + width + ",height:" + height + "]");
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

}
