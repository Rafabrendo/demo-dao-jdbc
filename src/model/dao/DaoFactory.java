package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	//Minha classe vai expor o metodo que retorna o tipo da interface mas internamente ela vai
	//instanciar uma implementação.
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(); //Esse é um 'macete' para não precisar expor a implementação
		//deixar apenas a interface.
		
	}

}
