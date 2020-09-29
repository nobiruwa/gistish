package javaee7.jsf.example;

import java.security.Principal;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * JSFのバッキングビーンです
 */
@Model
public class UserController {
    private String user;

    // (JSFに対して)供給するBean
    // 新メンバー
    @Produces
    @Named
    public String getUser() {
        return user;
    }

    @PostConstruct
    private void initNewMember() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Principal principal = facesContext.getExternalContext().getUserPrincipal();

            if (principal != null) {
                user = principal.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
