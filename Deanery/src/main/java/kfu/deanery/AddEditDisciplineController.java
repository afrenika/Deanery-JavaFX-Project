package kfu.deanery;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AddEditDisciplineController {
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField FIOField;
    @FXML
    private TextField hourField;

    private Stage dialogStage;
    private boolean okClicked;
    private Discipline discipline;

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
        if (discipline.getId() != null){
            idField.setText(discipline.getId().toString());
            nameField.setText(discipline.getDisciplineName());
            FIOField.setText(discipline.getFIO_teacher());
            hourField.setText(discipline.getHours().toString());}}


    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked(){
        return okClicked;}

    private boolean isInputValid(){
        String errorMessage = "";
        if (idField.getText() == null || idField.getText().length () == 0){
            errorMessage += "Неверный ID\n";}
        else {
            try {Integer.parseInt(idField.getText());}
            catch (Exception e){errorMessage += "Неверный ID\n"; }
        }
        if (nameField.getText() == null || nameField.getText().length () == 0){
            errorMessage += "Неверное название\n";}
        if (FIOField.getText() == null || FIOField.getText().length () == 0){
            errorMessage += "Неверное ФИО преподавателя\n";}
        if (hourField.getText() == null || hourField.getText().length () == 0){
            errorMessage += "Неверное кол-во часов\n";}
        else{
            try {Integer.parseInt(hourField.getText());}
            catch (Exception e){errorMessage += "Неверное кол-во часов\n"; }
        }


        if (errorMessage.length() == 0)
            return true;
        else {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.initOwner (dialogStage);
            alert.setTitle ("Предупреждение");
            alert.setContentText(errorMessage);
            alert.showAndWait ();
            return false;}
    }

    @FXML
    private void Ok() {
            if (isInputValid()) {
                okClicked = true;

                discipline.setId(Integer.parseInt(idField.getText()));
                discipline.setDisciplineName(nameField.getText());
                discipline.setFIO_teacher(FIOField.getText());
                discipline.setHours(Integer.parseInt(hourField.getText()));

                dialogStage.close();}
    }
    @FXML
    private void Cancel(){
            dialogStage.close();
    }


}
