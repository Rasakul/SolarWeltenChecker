package at.l2d2.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * <h4>About this class</h4>
 * <p>
 * <p>Description</p>
 *
 * @author Lukas Baronyai
 * @version 1.0.0
 * @since 13.07.2016
 */
public class Product {
	private StringProperty ASIN;
	private StringProperty name;
	private StringProperty originalPrice;
	private StringProperty deliveredPrice;

	public Product(String ASIN, String name, String originalPrice, String deliveredPrice) {
		this.ASIN = new SimpleStringProperty(ASIN);
		this.name = new SimpleStringProperty(name);
		this.originalPrice = new SimpleStringProperty(originalPrice);
		this.deliveredPrice = new SimpleStringProperty(deliveredPrice);
	}

	public String getASIN() {
		return ASIN.get();
	}

	public void setASIN(String ASIN) {
		this.ASIN.set(ASIN);
	}

	public StringProperty ASINProperty() {
		return ASIN;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public String getOriginalPrice() {
		return originalPrice.get();
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice.set(originalPrice);
	}

	public StringProperty originalPriceProperty() {
		return originalPrice;
	}

	public String getDeliveredPrice() {
		return deliveredPrice.get();
	}

	public void setDeliveredPrice(String deliveredPrice) {
		this.deliveredPrice.set(deliveredPrice);
	}

	public StringProperty deliveredPriceProperty() {
		return deliveredPrice;
	}

	public String getDifference() {
		try {
			double delivered = Double.parseDouble(deliveredPrice.get().replaceAll(",", "."));
			double original = Double.parseDouble(originalPrice.get().replaceAll(",", "."));
			return String.valueOf(Math.abs(delivered - original));
		} catch (NumberFormatException e) {
			return "";
		}
	}

	public StringProperty differenceProperty() {
		return new SimpleStringProperty(getDifference());
	}
}
