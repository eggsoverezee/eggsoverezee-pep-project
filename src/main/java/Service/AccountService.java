package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.ArrayList;
import java.util.List;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account){
        if(!account.getUsername().isBlank() 
            && account.getPassword().length() >= 4 
            && accountDAO.getAccountByName(account.getUsername()) == null)
                return accountDAO.createAccount(account);
        
            return null;
    }

    public Account verifyLogin(Account account){
        String name = account.getUsername();
        String password = account.getPassword();
        Account accountToVerify = accountDAO.getAccountByName(name);
        if(accountToVerify == null)
            return null;

        if(accountToVerify.getUsername().equals(name)
            && accountToVerify.getPassword().equals(password))
            return accountToVerify;
        
        return null;

    }

    public Account getAccountByID(int id){
        return accountDAO.getAccountByID(id);
    }

    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }
    
}
