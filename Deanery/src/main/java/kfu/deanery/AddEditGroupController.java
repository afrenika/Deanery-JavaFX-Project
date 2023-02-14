package kfu.deanery;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AddEditGroupController{
    @FXML
    private TextField groupNumberField;
    @FXML
    private TextField groupTutorField;
    

    private Stage dialogStage;
    private Group group;
    private boolean okClicked = false;

 

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void setGroup(Group group){
        this.group = group;
        if (group.getGroupNumber() != null){
            groupNumberField.setText(group.getGroupNumber());
            groupTutorField.setText(group.getGroupTutor());}}

    public boolean isOkClicked(){
        return okClicked;
    }

    private boolean isInputValid(){
        String errorMessage = "";
        if (groupNumberField.getText() == null || groupNumberField.getText().length () == 0){
            errorMessage += "Введено неверное номер группы\n";}
        if (groupTutorField.getText () == null || groupTutorField.getText().length() == 0){
            errorMessage += "Введено неверное ФИО\n";}
        if (errorMessage.length() == 0)
            return true;
        else {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.initOwner (dialogStage);
            alert.setTitle ("Некорректные поля");
            alert.setHeaderText ("Внесите корректную информацию");
            alert.setContentText (errorMessage);
            alert.showAndWait ();
            return false;}
    }

    @FXML
    private void Ok() {
        if (isInputValid()) {
            group.setGroupNumber(groupNumberField.getText());
            group.setGroupTutor(groupTutorField.getText());
            okClicked = true;
            dialogStage.close();
        }
    }
    @FXML
    private void Cancel(){
        dialogStage.close();
    }

}
