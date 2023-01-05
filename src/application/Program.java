package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Department obj = new Department(1, "Books");
		
		Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, obj);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		//ao invés de passar o new... eu chamo o metodo. Dessa forma o meu programa não conhece
		//a implementação, ele conhece somente a interface. Essa tbm é uma forma de fazer a injeção de dependencia
		//sem explicitar a implementação.
		
		System.out.println(seller);
		
		
	}

}
