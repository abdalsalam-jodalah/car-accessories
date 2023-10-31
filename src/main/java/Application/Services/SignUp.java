package Application.Services;
import Application.entities.User;
import javax.swing.JOptionPane;

public class SignUp {

    public String email;
    public String password;
    public boolean hasAccount;
    public int validationStatus;

    SignIn signIn;
    public SignUp() {
        email=null;
        password=null;
        hasAccount=false;
        validationStatus =-1;

    }

    public void creatAccount() {
        if (validationStatus == 0) {

            // this function should put data in DB
            this.hasAccount = true;
        }
        else
        {
            this.hasAccount = false;
        }
    }
    public User performLogIn() {
      signIn=new SignIn(email, password, "user", false, validationStatus);
      return signIn.performLogIn();
    }

}
