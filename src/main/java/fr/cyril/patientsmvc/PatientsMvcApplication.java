package fr.cyril.patientsmvc;

import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import fr.cyril.entities.Patient;
import fr.cyril.repositories.PatientRepository;

@SpringBootApplication
@EntityScan("fr.cyril")
@ComponentScan("fr.cyril")
@EnableJpaRepositories("fr.cyril")
public class PatientsMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientsMvcApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner start(PatientRepository patientRepository) {
		return args -> {
			
			Patient p1 = new Patient(); // NoArgsConstructor
			p1.setNom("Cyril");
			p1.setDateNaissance(new Date());
			p1.setEstMalade(true);
			p1.setScore(45678);
			
			
			Patient p2 = new Patient(null, "Marie", new Date(), false, 120); // AllArgsConstructor
			
			Patient p3 = Patient.builder() // Design patter Builder avec lombok
					.nom("Lili")
					.dateNaissance(new Date())
					.score(200)
					.build();
			
				
			patientRepository.save(p1);
			patientRepository.save(p2);
			patientRepository.save(p3);
			
			
			List<Patient> listPatients = patientRepository.findAll();
			listPatients.forEach(p-> System.out.println(p.toString()));
		};
		
	}

}
