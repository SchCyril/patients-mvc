package fr.cyril.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.cyril.entities.Patient;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
	Page<Patient> findByNomContainsIgnoreCase(String keyword, Pageable pageable);

}
