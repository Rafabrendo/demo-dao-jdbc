package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	//o Dao vai ter uma dependencia com a conexão
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
		//para a conexão! Uma dependencia.
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null; 
		
		//posição 0(zero) não contém objeto, é só a partir da posição 1
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			//Implementação do meu metodo para retornar um vendedor por id:
			
			//a minnha interrogação vai receber o valor que chegou como parametro, no caso, o Id
			st.setInt(1, id);
			rs = st.executeQuery();//Esse comando vai fazer a execução, e o resultado vai cair no rs(ResultSet)
			//Comando para uma consulta SQL
			//Se a consulta retornar com rs valendo 0, significa que não contém objeto.
			if(rs.next()) {
				//Codigo para instanciar um departamento:
				//Se a minha consulta não tiver nenhum registro, esse rs.next vai dar falso
				//e vai pularo if //Se der verdadeiro, vai retornar os dados do vendedor(Seller)
				//Department dep = new Department();
				//dep.setId(rs.getInt("DepartmentId"));//ao invés de passar o id, eu passo o nome da 
				//coluna que eu quero pegar o Id. Lembrando que vai retornar um int
				//dep.setName(rs.getString("DepName"));
				//Com isso, eu instanciei um departamento e settei o nome dele
				
				//agr vou transformar esse código em função:
				Department dep = instantiateDepartment(rs);
				
				
				//Codigo para instanciar um vendedor:
				//Agr eu vou fazer isso com o seller
				/*Seller obj = new Seller();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				//Nesse caso do setDepartment, vou passar o objeto inteiro, não o id. No caso, 
				//meu objeto departament é o dep, que eu instanciei ali em cima 
				 obj.setDepartment(dep);
				 */
				
				//Vou transformar esse código em função:
				Seller obj = instantiateSeller(rs, dep);
				
				
				return obj;
				
				
			}
			//Daí eu vou, simplesmente, retornar null, ou seja, não existia nenhum vendedor com 
			//esse id
			return null;
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		//nesse bloco(finally) eu vou fechar os meu recursos.
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			//Não precisa fechar a conexão. POrque eu posso aproveitar o meso DAO para fazer 
			//para inserir um novo dado.
		}
		
	}
	
	//vou apenas propagar essa exceção.
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	//Pode dar uma exceção, mas nesse caso, eu vou apenas propaga-la porque eu já estou tratando
	//ela em outro metódo
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
