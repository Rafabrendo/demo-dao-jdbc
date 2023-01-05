package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department obj);//operação responsável por inserir no banco de dados esse obj
	//que eu enviar como parametro de entrada.
	void update(Department obj);//atualização, recebendo um objeto como paramentro
	void deleteById(Integer id);//opera. de deleção, recebendo um id como paramentro 
	Department findById(Integer id);//Operação que retorna um Department, procura um id ao qual
	//recebe um id como parametro. //Vai consultar no banco de dados o objeto com esse Id, se existir
	//vai retornar, se não existir, vai retornar null
	List<Department> findAll();
}
