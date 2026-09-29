package com.example.first_thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService es;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("allemplist", es.getAllEmployee());
        return "index";
    }

    @GetMapping("/addnew")
    public String addNewEmployee(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "newemployee";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        es.save(employee);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String updateForm(@PathVariable(value = "id") long id, Model model) {
        Employee employee = es.getById(id);
        model.addAttribute("employee", employee);
        return "update";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteThroughId(@PathVariable(value = "id") long id) {
        es.deleteViaId(id);
        return "redirect:/";

    }

    @GetMapping("/search")
    public String Search(Model m) {
        Employee employee = new Employee();
        m.addAttribute("employee", employee);
        return "Search";
    }

    @GetMapping("/searchview")
    public String SearchView(@ModelAttribute("employee") Employee e, Model m) {
        Employee employee = es.searchbyname(e);
        m.addAttribute("employee", employee);
        return "SearchResult";
    }
}