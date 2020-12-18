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
        Ex2_Client.main(new String[3]);
        id=parseInt(userid.getText());
        lev=parseInt(level.getText());

    }
    public int getid(){
        return id;
    }
    public static int getLev(){
        return lev;
    }
}
