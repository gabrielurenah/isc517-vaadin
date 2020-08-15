package ce.pucmm.edu.vaadin.Design;

import ce.pucmm.edu.vaadin.Model.User;
import ce.pucmm.edu.vaadin.Services.UserService;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;

@Route("")
@SpringComponent
@UIScope
public class PantallaLogin extends VerticalLayout {
    public PantallaLogin(@Autowired UserService userService) {

        TextField email = new TextField("Email");
        PasswordField contrasena = new PasswordField("Contrasena");
        TextField nombres = new TextField("Nombre");

        Button botonAccion = userService.listUsers().isEmpty() ? new Button("Registrarse") : new Button("Entrar");
        botonAccion.getElement().setAttribute("theme", "primary");
        HorizontalLayout horizontalLayout;

        if (userService.listUsers().isEmpty()) {
            horizontalLayout = new HorizontalLayout(nombres, email, contrasena);
        } else {
            horizontalLayout = new HorizontalLayout(email, contrasena);
        }

        botonAccion.addClickListener((evento) -> {
            if (userService.listUsers().isEmpty()) {
                try {
                    userService.createUser(userService.listUsers().size() + 1, nombres.getValue(), email.getValue(), contrasena.getValue());
                    getUI().get().getPage().reload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (userService.validate(email.getValue(), contrasena.getValue())) {
                    try {
                        User user = userService.listUsers().get(0);
                        user.setIsLoggedIn(true);
                        userService.editUser(user);
                        getUI().get().navigate("calendario");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getUI().get().getPage().reload();
                }
            }
        });

        H4 titulo = new H4("C A L E N D A R I O");
        H6 subtitulo = userService.listUsers().isEmpty()
                ? new H6("Registre una cuenta para poder entrar!")
                : new H6("Favor de Loggearse");

        setAlignItems(Alignment.CENTER);
        add(titulo, subtitulo, horizontalLayout, botonAccion);
    }
}
