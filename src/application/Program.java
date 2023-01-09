package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		
		//Department obj = new Department(1, "Books");
		
		//Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, obj);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		//ao invés de passar o new... eu chamo o metodo. Dessa forma o meu programa não conhece
		//a implementação, ele conhece somente a interface. Essa tbm é uma forma de fazer a injeção de dependencia
		//sem explicitar a implementação.
		
		System.out.println("=== TEST 1: seller findById ===");
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
		
		System.out.println("\n=== TEST 2: seller findByDepartment ===");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for(Seller obj: list) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== TEST 3: seller findAll ===");
		//Como eu já declarei o list, eu posso reaproveita-lo
		list = sellerDao.findAll();
		for(Seller obj: list) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== TEST 4: seller insert ===");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New id = " + newSeller.getId());
		
		
		System.out.println("\n=== TEST 5: seller update ===");
		seller = sellerDao.findById(1);//Peguei os dados do vendedor de id = 1
		seller.setName("Martha Waine");//Mudeo o nome desse vendedor
		sellerDao.update(seller);
		System.out.println("Update completed");
		
		
		System.out.println("\n=== TEST 6: seller delete ===");
		System.out.println("Enter id fot delete test:");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete completed");
				
				
				
		sc.close();
	}

}
