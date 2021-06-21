package data;

import java.sql.*;
import java.time.*;
import java.util.LinkedList;
import entities.Product;




public class DbHandler {

	private String driver="com.mysql.cj.jdbc.Driver";
	private String host="localhost";
	private String port="3306";
	private String user="root";
	private String password="seba";
	private String db="javaMarket";
	private String options="?useLegacyDatetimeCode=false&serverTimezone=Asia/Hong_Kong";
	//private String options="";
	
	
	private Connection conn=null;
	
	public void dbHandler() {
		
		//registrar el driver de conexion Class.forName(...)
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	//Metodo para crear conexion
	private Connection getConnection() {
		try {
			if (conn== null || conn.isClosed())
			conn=DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+db+options, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	//Metodo para liberar conexion
	private void releaseConnection() {
		try {
			if(conn!=null && !conn.isClosed()) {
				conn.close();
			} 
		}catch (SQLException e) {
				e.printStackTrace();
			}
		}

	
	public LinkedList<Product> list(){
			
			Statement stmt=null;  //Defino el statement y el resulset aca, asi me lo reconoce en el finally
			ResultSet rs=null;   
			LinkedList<Product> prods = new LinkedList<Product>();
			Connection conn=null;
			
			//abrir conexion
			//crear la statement con el select
			//ejecutar la statement
			//recorrer el resultset (con next()) y crear objetos Product y agregarlos a la linkedlist
			try {
				conn=this.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select * from product");
				
				while (rs!=null &&  rs.next()) {
					Product p = new Product();
					
					p.setId(rs.getInt("id"));
					p.setName(rs.getString("name"));
					p.setDescription(rs.getString("description"));
					p.setPrice(rs.getDouble("price"));
					p.setStock(rs.getInt("stock"));
					p.setShippingIncluded(rs.getBoolean("shippingincluded"));
					p.setDisabledOn(rs.getObject("disabledOn", LocalDateTime.class));
					p.setDisabledDate(rs.getObject("disabledDate", LocalDate.class));
					p.setDisabledTime(rs.getObject("disabledTime", LocalTime.class));
					p.setDisabledOnZoned(rs.getObject("disabledOnZoned", ZonedDateTime.class));
					
					prods.add(p);
				}
			
				return prods;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			} finally { //finally se ejecuta tanto por try como por catch, por eso cierro la conexion aca
				try { //seleccione las 3 y les puse click derecho, sorround with, try/catch
					if(rs!=null)rs.close();
					if(stmt!=null)stmt.close();
					this.releaseConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

	}
	
	public Product search(Product p){ //recibir un product que solo tenga  un id o recibir el id
		
		PreparedStatement stmt=null;
		ResultSet rs=null;
		Connection conn=null;
		
		//abrir conexion		
		//crear la statement con el select con where		
		//asignar valor al param id con el parametro de entrada		
		//ejecutar la statement		
		//recorrer el resultset (con next()) y crear objeto Product
		try {
			conn=this.getConnection();
			Product prod = null;
			stmt = conn.prepareStatement("select * from product where id=?");
			stmt.setInt(1, p.getId());
			
			rs = stmt.executeQuery();
			
			if (rs != null && rs.next()) { //uso if y no while porque se que necesito uno solo, si pusiera un while no cambia
				prod =  new Product();
				prod.setId(rs.getInt("id"));
				prod.setName(rs.getString("name"));
				prod.setDescription(rs.getString("description"));
				prod.setPrice(rs.getDouble("price"));
				prod.setStock(rs.getInt("stock"));
				prod.setShippingIncluded(rs.getBoolean("shippingincluded"));
				
				//Opcion 1
				//java.sql.Timestamp t = rs.getTimestamp("disabledOn");
				//LocalDateTime dt = null;
				//if (t!=null) {
				//	dt=t.toLocalDateTime();
				//}
				//prod.setDisabledOn(dt);
				
				//Opcion 2
				prod.setDisabledOn(rs.getObject("disabledOn", LocalDateTime.class)); //Si el tipo de dato es compatible con LocalDateTime, el conector lo convierte
				prod.setDisabledDate(rs.getObject("disabledDate", LocalDate.class));
				prod.setDisabledTime(rs.getObject("disabledTime", LocalTime.class));
				prod.setDisabledOnZoned(rs.getObject("disabledOnZoned", ZonedDateTime.class));
				
				
			}
			return prod;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(rs!=null)rs.close();
				if(stmt!=null)stmt.close();
				this.releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public int newP(Product p) {
		
		int id=0;
		PreparedStatement pstmt=null;  
		ResultSet keyResultSet=null;
		Connection conn=null;
		
		try {
			conn=this.getConnection();
			pstmt = conn.prepareStatement("insert into product(name, description, price, stock, shippingIncluded, disabledOn, disabledDate, disabledTime, disabledOnZoned) values (?,?,?,?,?,?,?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, p.getName());
			pstmt.setString(2, p.getDescription());
			pstmt.setDouble(3, p.getPrice());
			pstmt.setInt(4, p.getStock());
			pstmt.setBoolean(5, p.isShippingIncluded());
			pstmt.setObject(6, p.getDisabledOn());//El conector y java se encargan de convertirlo al tipo correspondiente en la BD
			pstmt.setObject(7,p.getDisabledDate()); 
			pstmt.setObject(8, p.getDisabledTime());
			pstmt.setObject(9, p.getDisabledOnZoned());
			
			pstmt.executeUpdate();
			
			keyResultSet = pstmt.getGeneratedKeys();
			
            keyResultSet=pstmt.getGeneratedKeys();

            if(keyResultSet!=null && keyResultSet.next()) {
                    id=keyResultSet.getInt(1);
            }
			return id;
		   
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			return id;
		}finally {
			try {
	            if(keyResultSet!=null){keyResultSet.close();}
	            if(pstmt!=null){pstmt.close();}
				this.releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		}
	}

	public int delete(Product p) {
		
		PreparedStatement pstmt=null;
		int borro=0;
		Connection conn=null;
		
		try {
			conn=this.getConnection();
			pstmt = conn.prepareStatement("delete from product where id = ?");
			pstmt.setInt(1, p.getId());
			
			borro = pstmt.executeUpdate();

			
			return borro;
		} catch (SQLException e) {
			e.printStackTrace();
			return borro;
		} finally {
			try {
	            if(pstmt!=null){pstmt.close();}
				this.releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public int update(Product p) {
		
		PreparedStatement pstmt=null;
		int rs=0;
		Connection conn=null;
		
		try {
			conn=this.getConnection();
			pstmt = conn.prepareStatement("update product set name=?, description=?, price=?, stock=?, shippingincluded=?, disabledOn=?, disabledDate=?, disabledTime=?, disabledOnZoned=? where id=?");
			pstmt.setString(1, p.getName());
			pstmt.setString(2, p.getDescription());
			pstmt.setDouble(3, p.getPrice());
			pstmt.setInt(4, p.getStock());
			pstmt.setBoolean(5, p.isShippingIncluded());
			pstmt.setObject(6, p.getDisabledOn());
			pstmt.setObject(7,p.getDisabledDate()); 
			pstmt.setObject(8, p.getDisabledTime());
			pstmt.setObject(9, p.getDisabledOnZoned());
			pstmt.setInt(10, p.getId());
			
			rs = pstmt.executeUpdate();
			
			return rs;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return rs;
		} finally {
			try {
	            if(pstmt!=null){pstmt.close();}
				this.releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		

	}
}
	
