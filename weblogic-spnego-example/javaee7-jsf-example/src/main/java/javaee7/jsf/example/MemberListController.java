package javaee7.jsf.example;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Memberの一覧を供給するCDIイネーブルドビーンです。
 */
@RequestScoped
public class MemberListController {
    // (JSFに対して)供給するBean
    // メンバーの一覧
    // getを取り除くとMembers、これはJSFにmembersインスタンスを提供する
    @Named
    @Produces
    public List<Member> getMembers() {
        // 本来はRepositoryインターフェースから
        // メンバーを取得するだろう
        return Arrays.asList(
            new Member(1L, "Andy"),
            new Member(2L, "Bob")
        );
    }
}
