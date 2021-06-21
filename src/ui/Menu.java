package ui;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import data.DbHandler;
import entities.*;

public class Menu {
	
	private Scanner scan;
	private DbHandler db;
	private String dateFormat ="dd/MM/yyyy";
	private String timeFormat = "HH:mm:ss";
	private String dateTimeFormat = dateFormat + " " + timeFormat; 
	
	public void start() {
	
		scan = new Scanner(System.in);
		db = new DbHandler();
		String rta="";
		
		//mostrar opciones y segun opcion elegida
		do {
			rta = menu();
			
			switch (rta) {
				case "list":
					list();
					break;
				case "search":
					search();
					break;
				case "new":
					newP();
					break;
				case "delete":
					delete();
					break;
				case "update":
					update();
					break;
				default:
					break;
			}
		} while(!rta.equals("exit"));
		
		scan.close();
		// rta.equals("exit") == false
		//se piden los datos (id para search y delet o todos los datos para new y update, nada para list)
		//recibir respuesta y mostrar por pantalla
		//agregar una iteracion para los pasos anteriores
	}

	private String menu() {
		
		System.out.println("Input command: list/search/new/delete/update/exit");
		
		return scan.nextLine();
	}
	
	private void list() {
		for (Product p : db.list()) {
			System.out.println(p);
		}
	}
	
	private void search() { //busco por objeto, le seteo el id a un objeto y comparo con eso
		Product p = new Product();
		System.out.print("Input search id: ");
		p.setId(Integer.parseInt(scan.nextLine()));
		Product prod = db.search(p);
		if(prod != null) {
			System.out.println(prod);
		} else {
			System.out.println("404 - Not found");
		}
	}
	
	private void newP() {
		Product p = new Product();
		
		loadData(p);
		
		System.out.println("ID del producto ingresado: " + db.newP(p));
	}

	private void loadData(Product p) {
		System.out.print("Ingrese nombre de producto: ");
		p.setName(scan.nextLine());
		
		System.out.print("Ingrese descripcion de producto: ");
		p.setDescription(scan.nextLine());
		
		System.out.print("Ingrese precio de producto: ");
		p.setPrice(Double.parseDouble(scan.nextLine()));
		
		System.out.print("Ingrese stock de producto: ");
		p.setStock(Integer.parseInt(scan.nextLine()));
		
		System.out.print("El producto incluye envio (true/false)?: ");
		p.setShippingIncluded(Boolean.parseBoolean(scan.nextLine()));
		
		DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern(dateTimeFormat);
		System.out.print("Disable On ("+dateTimeFormat+"): ");
		p.setDisabledOn(LocalDateTime.parse(scan.nextLine(), dtFormat));
		
		DateTimeFormatter dFormat = DateTimeFormatter.ofPattern(dateFormat);
		System.out.print("Disable Date ("+dateFormat+"): ");
		p.setDisabledDate(LocalDate.parse(scan.nextLine(), dFormat));

		DateTimeFormatter tFormat = DateTimeFormatter.ofPattern(timeFormat);
		System.out.print("Disable Time ("+timeFormat+"): ");
		p.setDisabledTime(LocalTime.parse(scan.nextLine(), tFormat));
		
		System.out.print("Disable On Zoned ("+dateTimeFormat+"): ");
		p.setDisabledOnZoned(ZonedDateTime.parse(scan.nextLine(), dtFormat.withZone(ZoneId.of("UTC-3"))));
	}
	
	private void delete() {
		
		Product p = new Product();
		
		System.out.println("Ingrese id de producto a eliminar: ");
		p.setId(Integer.parseInt(scan.nextLine()));
		
		if(db.delete(p)>0) {
			System.out.println("Eliminado exitosamente");
		} else {
			System.out.println("Fallo al eliminar");
		}
	}
	
	private void update() {
		

		Product p = new Product();
		
		System.out.print("Ingrese id de producto a actualizar: ");
		p.setId(Integer.parseInt(scan.nextLine()));
		p = db.search(p);
		if(p != null) {
			System.out.println(p);
		} else {
			System.out.println("404 - Not found");
		}
		
		this.loadData(p);
		
		if(db.update(p)>0) {
			System.out.println("Actualizado correctamente");
		} else System.out.println("Error al actualizar");
		
	}
}
