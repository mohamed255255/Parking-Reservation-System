import java.util.Scanner;
public class AdminAccount{
    private String username ;
    private String password ;


    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }

    public boolean checkAccount(){
        if (!username.equals("admin") || !password.equals("admin")) {
            System.out.println("Wrong username or password .. please run the code again");
            return false ;
        }
        return true ;
    }


    public void login(){
        System.out.println("Enter your Username *admin* :");
        Scanner sc = new Scanner(System.in);
        username = sc.next();
        System.out.println("Enter your Password *admin* :");
        password  = sc.next();

    }
}