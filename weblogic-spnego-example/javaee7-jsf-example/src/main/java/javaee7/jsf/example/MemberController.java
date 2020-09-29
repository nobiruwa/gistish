package javaee7.jsf.example;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * JSFのバッキングビーンです
 */
@Model
public class MemberController {
    // (JSFに対して)供給するBean
    // 新メンバー
    @Produces
    @Named
    private Member newMember;

    @PostConstruct
    private void initNewMember() {
        // XHTMLでの変更に対するViewModel
        newMember = new Member();
    }

    /**
     * 登録処理です。
     * 利用者による新メンバーのデータを保存します。
     */
    public void register() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            // データベースエラーのかわりにエラールートを用意する
            if (newMember.getAccountName().equals("john")) {
                throw new IllegalArgumentException("A user 'john' already exists.");
            }

            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");

            // 第1引数のclienIdはnullでよいのか？
            facesContext.addMessage(null, m);

            // 使用済みのメンバーデータを消去する
            initNewMember();
        } catch (Exception e) {
            String errorMessage = "メッセージファクトリにパラメータを与えメッセージを作ってください";
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Error.");

            // 第1引数のclienIdはnullでよいのか？
            facesContext.addMessage(null, m);

            throw e;
        }
    }
}
