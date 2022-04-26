package ma.emsi.patientmvc.web;



import lombok.AllArgsConstructor;
import ma.emsi.patientmvc.entities.Patient;
import ma.emsi.patientmvc.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.Binding;
import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;
    @GetMapping(path = "/index")
    public String patient(Model model ,
                          @RequestParam(name = "page" ,defaultValue = "0") int page,
                          @RequestParam(name = "size" ,defaultValue = "5") int size,
                          @RequestParam(name = "keyword" ,defaultValue = "") String keyword) {
        Page<Patient> PagePatients=patientRepository.findByNomContains(keyword, PageRequest.of(page,size));
        model.addAttribute("ListPatients",PagePatients.getContent());

        model.addAttribute("pages",new int[PagePatients.getTotalPages()]);
        model.addAttribute("pageCurrent",page);
        model.addAttribute("keyword",keyword);

        return "patients";
    }
    @GetMapping("/delete")
    public String delete(Long id,int page,String keyword){
        patientRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/")
    public String home(){

        return "home";
    }
    @GetMapping("/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient",new Patient());
        return "formPatients";
    }
    @PostMapping ("/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/editPatient")
    public String editPatient(Model model,Long id,int page,String keyword){
        Patient patient=patientRepository.findById(id).orElse(null);
        if(patient==null) throw new RuntimeException("patient introuvable");
        model.addAttribute("patient",patient);
        model.addAttribute("keyword" ,keyword);
        model.addAttribute("page" ,page);
        return "editPatient";
    }

}
