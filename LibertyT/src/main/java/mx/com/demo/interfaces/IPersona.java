package mx.com.demo.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.demo.modelo.Persona;

@Repository
public interface IPersona extends JpaRepository<Persona, Integer>{

}
