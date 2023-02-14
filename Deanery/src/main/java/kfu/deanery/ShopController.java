package kfu.deanery;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ShopController {

    @FXML
    private TextField textField1;
    @FXML
    private PasswordField textField2;


    @FXML
    protected void buttonCheck() throws IOException {
        User user = new User(textField1.getText(), textField2.getText());
        if (!user.compare()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Сообщение об ошибке");
            alert.setContentText("Проверьте введенные логин и пароль");
            alert.showAndWait();
        }
        else{
            textField1.setText("Вы вошли");
            ((Stage) textField1.getScene().getWindow()).close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.getIcons().add(new Image("file:door.png"));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
    }}


    @FXML
    protected void otmenaCheck(){
        textField1.clear();
        textField2.clear();
        }




    }

