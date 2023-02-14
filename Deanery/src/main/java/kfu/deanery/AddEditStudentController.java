package kfu.deanery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class AddEditStudentController implements Initializable {
    private final ObservableList<String> list = FXCollections.observableArrayList();
    private final ObservableList<String> list2 = FXCollections.observableArrayList();
    @FXML
    private TextField idField;
    @FXML
    private TextField FIOField;
    @FXML
    private ComboBox<String> genderField;
    @FXML
    private DatePicker dataField;
    @FXML
    private TextField addressField;
    @FXML
    private ComboBox<String> comboBoxGroup;
    private Stage dialogStage;
    private Student student;
    private boolean okClicked = false;
    private String group;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.add("женский");
        list.add("мужской");

    }

    public void dopInit(){
        for(Group group: student.getCourse().getGroups()){
            list2.add(group.getGroupNumber());}
        comboBoxGroup.setItems(list2);
        comboBoxGroup.setValue(group);
        genderField.setItems(list);
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void setStudent(Student student){
        this.student = student;
        if (student.getId() != null){
            idField.setText(student.getId().toString());
            FIOField.setText(student.getFIO());
            genderField.setValue(student.getGender());
            dataField.setValue(student.getDateOfBirthday());
            addressField.setText(student.getAddress());}}

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isOkClicked(){
        return okClicked;
    }

    private boolean isInputValid(){
        String errorMessage = "";
        if (idField.getText() == null || idField.getText().length () == 0){
            errorMessage += "Введено неверное ID\n";}
        if (FIOField.getText () == null || FIOField.getText().length() == 0){
            errorMessage += "Введено неверное ФИО\n";}
        if (genderField.getValue() == null){
            errorMessage += "Не выбран пол\n";}
        if (dataField.getValue() == null){
            errorMessage += "Введена неверная дата\n";}
        if (addressField.getText() == null || addressField.getText().length() == 0){
            errorMessage += "Введен неверный адрес\n";}
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
            student.setId(Integer.parseInt(idField.getText()));
            student.setFIO(FIOField.getText());
            student.setDateOfBirthday(dataField.getValue());
            student.setGender(genderField.getValue());
            student.setAddress(addressField.getText());

            if(!comboBoxGroup.getValue().equals(group)){
                changeGroup();}

            okClicked = true;
            dialogStage.close();
        }
    }
    @FXML
    private void Cancel(){
        dialogStage.close();
    }

    @FXML
    private void edit(){
        comboBoxGroup.setDisable(false);
    }

    private void changeGroup(){
        for(Group group:student.getCourse().getGroups()){
            if (group.getGroupNumber().equals(comboBoxGroup.getValue())){
                group.getStudents().add(student);}
            else if (group.getGroupNumber().equals(this.group)){
                group.getStudents().remove(student);
            }
        }
    }

}
