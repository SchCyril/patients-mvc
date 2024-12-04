package fr.cyril.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cyril.entities.Patient;
import fr.cyril.repositories.PatientRepository;
import jakarta.validation.Valid;
import javassist.expr.NewArray;

@Controller
public class PatientController {
	private PatientRepository patientRepository;

	public PatientController(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	@GetMapping("/user/index")
	@PreAuthorize("hasRole('USER')")
	public String index(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "keyword", defaultValue = "") String keyword)

	{

		Page<Patient> pagePatients = patientRepository.findByNomContainsIgnoreCase(keyword, PageRequest.of(page, size));
		model.addAttribute("listPatients", pagePatients.getContent());
		model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("keyword", keyword);
		return "patients";
	}

	@GetMapping("/admin/deletePatient")
	@PreAuthorize("hasRole('ADMIN')")
	public String delete(@RequestParam(name = "id") Long id, @RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page) {
		patientRepository.deleteById(id);
		return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
	}

	@GetMapping("/")
	@PreAuthorize("hasRole('USER')")
	public String home() {
		return "redirect:/user/index";
	}

	@GetMapping("/admin/formPatient")
	@PreAuthorize("hasRole('ADMIN')")
	public String formPatient(Model model) {
		model.addAttribute("patient", new Patient());
		return "formPatient";
	}

	@PostMapping("/admin/savePatient")
	@PreAuthorize("hasRole('ADMIN')")
	public String savePatient(@Valid Patient patient, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "formPatient";
		}
		patientRepository.save(patient);
		return "redirect:/index?keyword=" + patient.getNom();
	}

	@GetMapping("/admin/editPatient")
	@PreAuthorize("hasRole('ADMIN')")
	public String editPatient(Model model, @RequestParam(name = "id") Long id) {
		Patient patient = patientRepository.findById(id).get();
		model.addAttribute("patient", patient);
		return "editPatient";
	}

}
