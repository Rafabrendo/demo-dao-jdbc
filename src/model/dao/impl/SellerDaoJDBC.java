package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		PreparedStatement st = null;	
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+"VALUES "
					+"(?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);
			//fiz uma sobrecarga para retornar o id do vendedor
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			//Caso nenhuma linha tenha sido alterada
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;	
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+"SET Name= ?, Email= ?, BirthDate= ?, BaseSalary= ?, DepartmentId= ? "
					+"WHERE Id= ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			//Não vou precisar verificar as linhas executadas, só vou deixar o comando para q faça
			//a execução e atualização.
			st.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"DELETE FROM seller WHERE Id= ? ");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null; 
		
		//posição 0(zero) não contém objeto, é só a partir da posição 1
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
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
		//Vai buscar todos os vendedores com nome de departamento e ordenar por nome
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			//o map vai controlar a não repetição de departamento
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

}
