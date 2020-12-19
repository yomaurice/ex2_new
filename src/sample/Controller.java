package sample;

import gameClient.Ex2_Client;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static java.lang.Integer.parseInt;

public class Controller {
    public static int lev;
    public Button mybtn;
    public TextField userid;
    public TextField level;
    public static int id;


    public void myClick(ActionEvent actionEvent) {

        id=Integer.parseInt(userid.getText());
        lev=Integer.parseInt(level.getText());
        Ex2_Client.main(new String[3]);

    }
    public static int getid(){
        return id;
    }
    public static int getLev(){
        return lev;
    }
}
