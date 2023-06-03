package com.gabriel.controllers.web;

import com.gabriel.models.Event;
import com.gabriel.models.EventTableView;
import com.gabriel.models.User;
import com.gabriel.services.event.EventServiceImpl;
import com.gabriel.services.operator.AdminOperatorService;
import com.gabriel.services.user.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

@RequestMapping("admin")
public class AdminController {
    private final AdminOperatorService adminOperatorService;
    private final UserServiceImpl userService;
    private final EventServiceImpl eventService;
    Logger log = LoggerFactory.getLogger(this.getClass());

    public AdminController(AdminOperatorService adminOperatorService, UserServiceImpl userService, EventServiceImpl eventService) {
        this.adminOperatorService = adminOperatorService;
        this.userService = userService;
        this.eventService = eventService;
    }
    /**
     * Controla la solicitud para la gestión de eventos en el panel de administración.
     *
     * @param model        Model utilizado para pasar atributos a la vista.
     * @param currentUser  Detalles del usuario autenticado.
     * @param result       Parámetro opcional para indicar el resultado de una operación.
     * @return "gestion-eventos".
     */
    @GetMapping({"", "/"})
    public String gestionEvento(Model model,
                                @AuthenticationPrincipal UserDetails currentUser,
                                @RequestParam(required = false) Integer result) {
        User user = userService.getUserAdminByUsername(currentUser.getUsername());
        if (user != null){
            List<EventTableView> events = eventService.getAllEventsTableView(user);
            model.addAttribute("eventos", events);
        }
        if (result != null)
            model.addAttribute("result",result);

        return "gestion-eventos";
    }
    /**
     * Controla la solicitud para modificar un evento existente en el panel de administración.
     *
     * @param model        Model utilizado para pasar atributos a la vista.
     * @param currentUser  Detalles del usuario autenticado.
     * @param id           ID del evento a modificar.
     * @return "modificar-evento" si el evento existe, o redirecciona a "/admin" en caso contrario.
     */
    @GetMapping("/modificar-evento/{id}")
    public String modificarEvento(Model model,
                                  @AuthenticationPrincipal UserDetails currentUser,
                                  @PathVariable Long id) {
        Event event =
                eventService.getEventByIdAndAdmin(
                    id,
                    userService.getUserByUsername(currentUser.getUsername())
                );
        if (event == null)
            return "redirect:/admin";

        model.addAttribute("evento",event);
        return "modificar-evento";
    }
    /**
     * Controla la solicitud para crear un nuevo evento en el panel de administración.
     *
     * @param model        Model utilizado para pasar atributos a la vista.
     * @return Nombre de la vista "crear-evento".
     */
    @GetMapping("/crear-evento")
    public String crearEvento(Model model) {
        model.addAttribute("evento",new Event());
        return "crear-evento";
    }
    /**
     * Controla la solicitud para modificar un evento existente en el panel de administración.
     *
     * @param evento       Event que contiene la información del evento a modificar.
     * @param currentUser  Detalles del usuario autenticado.
     * @return Redirecciona a "/admin?result=2".
     */
    @PostMapping("/modificar-evento")
    public String modificarEvento(@AuthenticationPrincipal UserDetails currentUser,
                                  @Valid @ModelAttribute("evento")
                                  Event evento,
                                  BindingResult binding,
                                  Model model) {
        if (binding.hasErrors()) {
            System.out.println(" >>>> TEST");
            model.addAttribute("evento",evento);
            return "modificar-evento";
        }
        eventService.updateEvent(evento, userService.getUserByUsername(currentUser.getUsername()));
        return "redirect:/admin?result=2";
    }
    /**
     * Controla la solicitud para crear un nuevo evento en el panel de administración.
     *
     * @param evento       Event que contiene la información del evento a crear.
     * @param currentUser  Detalles del usuario autenticado.
     * @return Redirecciona a "/admin?result=1".
     */
    @PostMapping("/crear-evento")
    public String crearEvento(@AuthenticationPrincipal UserDetails currentUser,
                              @Valid @ModelAttribute("evento")
                              Event evento,
                              BindingResult binding,
                              Model model) {
        if (binding.hasErrors()){
            model.addAttribute("evento",evento);
            return "crear-evento";
        }
        User admin = userService.getUserByUsername(currentUser.getUsername());
        eventService.createEvent(evento,admin);
        return "redirect:/admin?result=1";
    }
    /**
     * Controla la solicitud para la gestión de operarios en el panel de administración.
     *
     * @param model        Model utilizado para pasar atributos a la vista.
     * @param currentUser  Detalles del usuario autenticado.
     * @param success      Parámetro opcional para indicar el éxito de una operación.
     * @param remove       Parámetro opcional para indicar la eliminación de un operario.
     * @return "gestion-operarios".
     */
    @GetMapping("/gestion-operarios")
    public String gestionOperarios(Model model,
                                   @AuthenticationPrincipal UserDetails currentUser,
                                   @RequestParam(required = false) String success,
                                   @RequestParam(required = false) String remove
    ) {
        if (currentUser != null)
            model.addAttribute(
                    "operators",
                    adminOperatorService.getAllOperatorTableViewByAdmin(currentUser.getUsername()));
        model.addAttribute("success",success != null);
        model.addAttribute("remove",remove);
        return "gestion-operarios";
    }
    /**
     * Controla la solicitud para agregar un nuevo operario en el panel de administración.
     *
     * @param currentUser Detalles del usuario autenticado.
     * @return Redirecciona a "/admin/gestion-operarios?success".
     */
    @PostMapping("/gestion-operarios/add")
    public String addOperator(@AuthenticationPrincipal UserDetails currentUser) {
        adminOperatorService.createAdminOperator(currentUser.getUsername());
        log.info("Operario creado por usuario: " + currentUser.getUsername());
        return "redirect:/admin/gestion-operarios?success";
    }

    /**
     * Controla la solicitud para eliminar un operario en el panel de administración.
     *
     * @param idOperator   ID del operario a eliminar.
     * @param currentUser  Detalles del usuario autenticado.
     * @return Redirecciona a "/admin/gestion-operarios?remove".
     */

    @GetMapping("/gestion-operarios/remove/{id}")
    public String removeOperator(@PathVariable("id") Long idOperator,
                                 @AuthenticationPrincipal UserDetails currentUser) {
        adminOperatorService.removeOperator(idOperator,currentUser.getUsername());

        return "redirect:/admin/gestion-operarios?remove";
    }
    /**
     * Controla la solicitud para eliminar un evento en el panel de administración.
     *
     * @param idEvento     ID del evento a eliminar.
     * @param currentUser  Detalles del usuario autenticado.
     * @return Redirecciona a "/admin?result=3".
     */
    @GetMapping("/borrar-evento/{id}")
    public String borrarEvento(@PathVariable("id") Long idEvento,
                                 @AuthenticationPrincipal UserDetails currentUser) {
        boolean result = eventService.removeEvent(idEvento,userService.getUserByUsername(currentUser.getUsername()));

        return result ? "redirect:/admin?result=3" : "redirect:/admin";
    }



}
