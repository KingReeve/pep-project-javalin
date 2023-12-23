package Service;
import Model.Account;
import DAO.AccountDAO;

/* import java.util.List; */

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO DAO){
        this.accountDAO = DAO;
    }

    public Account registerUser(Account account){
        return accountDAO.registerUser(account);        
    }

    public Boolean queryUsername(String username){
        System.out.print(accountDAO.queryUsername(username));
        return accountDAO.queryUsername(username);
    }

    public Boolean queryAccountID(Integer id){
        return accountDAO.queryAccountID(id);
    }

    public Account login(Account account){
        return accountDAO.login(account);
    }
}