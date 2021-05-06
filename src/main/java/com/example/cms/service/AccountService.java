package com.example.cms.service;

import com.example.cms.bean.Account;
import com.example.cms.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;

    public Account loginAccount(Account account){
        return accountMapper.loginAccount(account);
    }

    public int InsertAccount(Account account) {
        int account1 = accountMapper.InsertAccount(account);
        return account1;
    }

    public int DeleteAccount(int id) {
        int delId = accountMapper.DeleteAccount(id);
        return delId;
    }

    public int updateAccount(Account account, int id) {
        int update = accountMapper.updateAccount(account.getUserName(), account.getPassword(),id);
        return update;
    }
}
