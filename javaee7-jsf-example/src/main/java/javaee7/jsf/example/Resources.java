package javaee7.jsf.example;

import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

// WEB-INF/beans.xml において、/beans/@bean-discovery-mode 属性を
// allに設定する必要があります。
/**
 * 共通利用されるBeanを供給するクラスです。
 */
@DataSourceDefinition(
    // <https://blogs.oracle.com/enterprisetechtips/datasource-resource-definition-in-java-ee-6>
    // <https://webapps4newbies.blogspot.com/2015/04/creating-datasource-definition-using.html>
    //
    // - namespace scope (Java EEで規定)
    //   - java:app
    //   - ほかにもapplication component environment namespacesとして
    //     - コンポーネント内共有 java:comp
    //     - モジュール内共有(コンポーネント間共有) java:module
    //     - アプリケーション内共有(モジュール間共有) java:app
    //     - サーバー内共有(アプリケーション間共有) java:global
    name = "java:app/env/javaee7-jsf-example",
    className = "org.h2.jdbcx.JdbcDataSource",
    url = "jdbc:h2:mem:test",
    properties = { "" }
)
public class Resources {
    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public DataSource dataSource() {
        return null;
    }
}
