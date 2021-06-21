package entities;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Product {

	private int id, stock;
	private String name, description;
	private double price;
	private boolean shippingIncluded;
	private LocalDateTime disabledOn;
	private LocalDate disabledDate;
	private LocalTime disabledTime;
	private ZonedDateTime  disabledOnZoned;
	
	private String dateFormat ="dd/MM/yyyy";
	private String timeFormat = "HH:mm:ss";
	private String dateTimeFormat = dateFormat + " " + timeFormat; 
	
	//LocalDate representa a fechas sin la hora
	//LocalTime para el manejo de horas, sin ninguna fecha asociada
	//LocalDateTime es una combinación de las dos anteriores
	
	public LocalDateTime getDisabledOn() {
		return disabledOn;
	}
	public LocalDate getDisabledDate() {
		return disabledDate;
	}
	public void setDisabledDate(LocalDate disabledDate) {
		this.disabledDate = disabledDate;
	}
	public LocalTime getDisabledTime() {
		return disabledTime;
	}
	public void setDisabledTime(LocalTime disabledTime) {
		this.disabledTime = disabledTime;
	}
	public void setDisabledOn(LocalDateTime disabledOn) {
		this.disabledOn = disabledOn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isShippingIncluded() {
		return shippingIncluded;
	}
	public void setShippingIncluded(boolean shippingIncluded) {
		this.shippingIncluded = shippingIncluded;
	}
	public ZonedDateTime getDisabledOnZoned() {
		return disabledOnZoned;
	}
	public void setDisabledOnZoned(ZonedDateTime disabledOnZoned) {
		this.disabledOnZoned = disabledOnZoned;
	}
	@Override
	public String toString() {
		
		DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern(dateTimeFormat);
		DateTimeFormatter dFormat = DateTimeFormatter.ofPattern(dateFormat);
		DateTimeFormatter tFormat = DateTimeFormatter.ofPattern(timeFormat);
		
		return "id: " + id + ". Nombre=" + name + ", descripcion=" + description + ", stock=" + stock + ", precio="
				+ price + ", shippingIncluded=" + shippingIncluded 
				+ ", disabledOn=" + (disabledOn==null?null:disabledOn.format(dtFormat))
				+ ", disabledDate=" + (disabledDate==null?null:disabledDate.format(dFormat))
				+ ", disabledTime=" + (disabledTime==null?null:disabledTime.format(tFormat))
				+ ", disabledOnZoned=" + (disabledOnZoned==null?null:disabledOnZoned.format(dtFormat.withZone(ZoneId.of("UTC-3"))))+"\n";
		
	}
	
	
}
