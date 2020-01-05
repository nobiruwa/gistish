package javaee7.jsf.example;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Memberの一覧を供給するCDIイネーブルドビーンです。
 */
@RequestScoped
public class MemberListController {
    @Inject
    private TestRepository testRepository;

    // (JSFに対して)供給するBean
    // メンバーの一覧
    @Named
    @Produces
    public List<Member> getMembers() {
        // 本来はRepositoryインターフェースから
        // メンバーを取得するだろう
        return Arrays.asList(
            new Member(1L, "Andy"),
            new Member(2L, "Bob"),
            new Member(3L, testRepository == null ? "null" : "not null")
        );
    }
}
