package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	//Minha classe vai expor o metodo que retorna o tipo da interface mas internamente ela vai
	//instanciar uma implementação.
	public static SellerDao createSellerDao() {
		//Passei uma conexão como argumento.
		return new SellerDaoJDBC(DB.getConnection()); //Esse é um 'macete' para não precisar expor a implementação
		//deixar apenas a interface.
		
	}

}
