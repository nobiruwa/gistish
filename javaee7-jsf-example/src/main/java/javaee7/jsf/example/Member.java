package javaee7.jsf.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * エンティティです。
 */
@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String accountName;

    public Member() {
    }

    public Member(Long id, String accountName) {
        this.id = id;
        this.accountName = accountName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

}
