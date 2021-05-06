package com.example.cms.mapper;

import com.example.cms.bean.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMapper {
    public Account loginAccount(Account account);

    public int InsertAccount(Account account);

    public int DeleteAccount(int id);

    public int updateAccount(String username, String password, int id);
}
