package com.kpi.accidents.controller;

import com.kpi.accidents.model.Accident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.kpi.accidents.model.AccidentType;
import com.kpi.accidents.model.Rule;
import com.kpi.accidents.service.AccidentService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class AccidentController {
    @Autowired
    private final AccidentService accidentService;
    private static final Logger LOG = LoggerFactory.getLogger(AccidentController.class);

    public AccidentController(AccidentService accidentService) {
        this.accidentService = accidentService;
    }

    /**
     * Метод отримує зі блоку бізнес-логіки список даних про правопорушення і передає їх на фронт
     * @return - відображення списку даних про правопорушення
     */

    @GetMapping("/")
    public ModelAndView showItems() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        modelAndView.addObject("accidents", accidentService.getValidateAccidents());
        return modelAndView;
    }

    /**
     * Метод відкриває сторінку для створення нової заяви
     * @param model - модель для створення нової заяви
     * @return - сторінка створення нової заяви
     */

    @PostMapping("/create")
    public String openCreationForm(Model model) {
        model.addAttribute("accident", new Accident());
        this.getListOfTypes(model);
        this.getListOfRules(model);
        model.addAttribute("error", "");
        return "create";
    }

    /**
     * Метод зберігає нове правопорушення в формі
     * @param accident - правопорушення
     * @param bindingResult - обробник помилок
     * @param request - запит, необхідний для отримання списку вибраних статей
     * @param model - модель для передачі даних залежно від результату збереження правопорушення
     * @return - сторінка з результатом збереження
     */

    @PostMapping("/save")
    public String addAccident(@Valid @ModelAttribute("accident") Accident accident,
                              BindingResult bindingResult,
                              HttpServletRequest request,
                              Model model) {
        String result = "create";
        if (!bindingResult.hasErrors()) {
            accidentService.createValidateAccident(accident);
            model.addAttribute("id", accident.getId());
            result = "successOfCreation";
        } else {
            this.getListOfTypes(model);
            this.getListOfRules(model);
        }
        return result;
    }

    /**
     * Метод відкриває сторінку для введення ідентифікатора
     * @param model - модель для передачі необхідних атрибутів
     * @param id - ідентифікатор для моделі
     * @return - сторінка для введення ідентифікатора
     */

    @PostMapping("/checkId")
    public String openIdForm(Model model, String id) {
        model.addAttribute("id", id);
        model.addAttribute("error", "");
        return "checkId";
    }

    /**
     * Метод відкриває сторінку для оновлення існуючого заяви
     * @param id - введений ідентифікатор
     * @param model - модель для передачі даних залежно від результату введення ідентифікатора
     * @return - сторінка оновлення існуючого заяви
     */

    @GetMapping("/edit")
    public String openUpdateForm(@RequestParam("id") String id, Model model) {
        String result = "checkId";
        Set<Integer> ids = accidentService.getValidateAccidents().keySet();
        if (id.matches("[\\d]+")) {
            int parsedId = Integer.parseInt(id);
            if (ids.contains(parsedId)) {
                Accident accident = accidentService.getValidateAccidents().get(parsedId);
                model.addAttribute("accident", accident);
                this.getListOfTypes(model);
                this.getListOfRules(model);
                result = "update";
            } else {
                model.addAttribute("error", "Зазначений ідентифікатор не існує!");
            }
        } else {
            model.addAttribute("error", "Ідентифікатор має складатися лише з цифр!");
        }
        return result;
    }

    /**
     * Метод зберігає оновлену заяву
     */

    @PostMapping("/update")
    public String updateAccident(@Valid @ModelAttribute("accident") Accident accident,
                                 BindingResult bindingResult,
                                 HttpServletRequest request,
                                 Model model) {
        String result = "update";
        if (!bindingResult.hasErrors()) {
            accidentService.updateValidateAccident(accident);
            result = "successOfUpdate";
        } else {
            this.getListOfTypes(model);
            this.getListOfRules(model);
        }
        return result;
    }

    @PostMapping("/checkIdgenerate")
    public String checkIdGenerateForm(Model model, String id) {
            model.addAttribute("id", id);
            model.addAttribute("error", "");
            return "checkIdgenerate";
        }

    @GetMapping("/generate")
    public String generateReport(@RequestParam("id") String id, Model model) {
        String result = "checkIdgenerate";
        Set<Integer> ids = accidentService.getValidateAccidents().keySet();
        if (id.matches("[\\d]+")) {
            int parsedId = Integer.parseInt(id);
            if (ids.contains(parsedId)) {
                Accident accident = accidentService.getValidateAccidents().get(parsedId);
                accidentService.generatePdf(accident);  // PDF generation
                model.addAttribute("accident", accident);
                result = "generatepdf"; // Display the result on the page
            } else {
                model.addAttribute("error", "Зазначений ідентифікатор не існує!");
            }
        } else {
            model.addAttribute("error", "Ідентифікатор має складатися лише з цифр!");
        }
        return result;
    }


    @PostMapping("/readypdf")
    public String readyPdfFile(@Valid @ModelAttribute("accident") Accident accident,
                                 BindingResult bindingResult,
                                 HttpServletRequest request,
                                 Model model) {
        String result = "successOfReport";
        if (!bindingResult.hasErrors()) {
            result = "successOfReport";
        } else {
            this.getListOfTypes(model);
            this.getListOfRules(model);
        }
        return result;
    }


    @PostMapping("/delete")
    public String deleteAccident(@RequestParam("id") int id, Model model) {
        Accident accident = accidentService.getValidateAccidents().get(id);
        if (accident != null) {
            accidentService.deleteValidateAccident(accident);
            return "successOfDelete"; // Сторінка підтвердження успішного видалення
        } else {
            model.addAttribute("error", "Зазначений ідентифікатор не існує!");
            return "checkId"; // Сторінка для введення ідентифікатора
        }
    }
    /**
     * Метод формує вміст моделі, необхідної для вибору типу правопорушення (для фронту)
     * @param model - модель
     */

    private void getListOfTypes(Model model) {
        List<AccidentType> types = new ArrayList<>();
        types.add(AccidentType.of(1, "Два транспортні засоби"));
        types.add(AccidentType.of(2, "Транспортний засіб і людина"));
        types.add(AccidentType.of(3, "Транспортний засіб й інший транспорт"));
        types.add(AccidentType.of(4, "Громадський транспорт"));
        types.add(AccidentType.of(5, "Інше"));
        model.addAttribute("types", types);
    }


    /**
     * Метод формує вміст, необхідний для вибору типу статей (для фронту)
     * @param model - модель
     */

    private void getListOfRules(Model model) {
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.of(1, "Стаття 286 ККУ – Порушення правил безпеки дорожнього руху"));
        rules.add(Rule.of(2, "Стаття 130 КпАП – Водіння в нетверезому стані"));
        rules.add(Rule.of(3, "Стаття 132 КпАП – Водіння без водійського посвідчення"));
        rules.add(Rule.of(4, "Стаття 124 КпАП – Порушення правил дорожнього руху, що спричинило матеріальну шкоду"));
        rules.add(Rule.of(5, "Стаття 117 КпАП – Недотримання дистанції"));
        rules.add(Rule.of(6, "Стаття 122 КпАП – Порушення правил обгону"));
        rules.add(Rule.of(7, "Стаття 152 КпАП – Порушення правил паркування"));
        model.addAttribute("rules", rules);
    }
}
