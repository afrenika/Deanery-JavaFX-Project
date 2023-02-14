package kfu.deanery;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;

public class AddEditCourseController {

    @FXML
    private TextField courseIdField;
    @FXML
    private TableView<Discipline> tableViewDisciplineCourse;
    @FXML
    private TableColumn<Discipline, String> disc;



        private Stage dialogStage;
        private Course course;
        private boolean okClicked = false;
        private HashMap<Integer, Discipline> disciplineHashMap;
        private ObservableList<Course> courses;



    public void setCourses(ObservableList<Course> courses) {
        this.courses = courses;
    }

    public void setDialogStage(Stage dialogStage){
            this.dialogStage = dialogStage;
        }

        public void setDisciplineHashMap(HashMap<Integer, Discipline> disciplineHashMap) {
            this.disciplineHashMap = disciplineHashMap;
        }

    public void setCourse(Course course){
            this.course = course;
            if(course.getNumber() == null){
                courseIdField.setDisable(false);
            }
            else {
            courseIdField.setText(course.getNumber().toString());}
            disc.setCellValueFactory(new PropertyValueFactory<>("disciplineName"));
            tableViewDisciplineCourse.setItems(course.getDisciplines());
    }

        public boolean isOkClicked(){
            return okClicked;
        }

        private boolean isInputValid(){
            String errorMessage = "";
            if(courseIdField.getText() == null || courseIdField.getText().length() == 0){
                errorMessage+="Не введен номер курса\n";
            }
            else {
                try{Integer.parseInt(courseIdField.getText());}
                catch (Exception e){
                    errorMessage+="Не введен номер курса\n";}}
            if (tableViewDisciplineCourse.getItems().size() == 0){
                errorMessage += "Не введены предметы курса\n";}
            if (errorMessage.length() == 0)
                return true;
            else {
                if (tableViewDisciplineCourse.getItems().size() == 0){
                Alert alert = new Alert (Alert.AlertType.WARNING, errorMessage + "\n" + "Вы уверены, что хотите оставить список пустым?", ButtonType.YES, ButtonType.NO);
                alert.initOwner (dialogStage);
                alert.setTitle ("Предупреждение");
                alert.showAndWait ();
                boolean warning = alert.getResult() == ButtonType.YES;
                if(warning){
                    dialogStage.close();}
                }
                else{
                    Alert alert = new Alert (Alert.AlertType.WARNING, errorMessage);
                    alert.initOwner (dialogStage);
                    alert.setTitle ("Предупреждение");
                    alert.showAndWait ();}
                }
                return false;}


        @FXML
        private void Ok() {
            if (isInputValid()) {
                course.setNumber(Integer.parseInt(courseIdField.getText()));
                course.setAllDisciplines(course.getAllDisciplines());
                okClicked = true;
                dialogStage.close();
            }
        }
        @FXML
        private void Cancel(){
            dialogStage.close();
        }

        @FXML
        private void delete(){
            Discipline selectedDiscipline = tableViewDisciplineCourse.getSelectionModel().getSelectedItem();

            if (selectedDiscipline != null) {
                tableViewDisciplineCourse.getItems().remove(selectedDiscipline);}
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(null);
                alert.setTitle("Ничего не выбрано");
                alert.setHeaderText("Дисциплина не выбрана");
                alert.setContentText("Выберите в таблице дисциплину для удаления.");
                alert.showAndWait();}
        }

        @FXML
        private void add() throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EditDisciplinesController.class.getResource("editDisciplines.fxml"));
            AnchorPane page = loader.load();
            Stage dialogScene = new Stage();
            dialogScene.initStyle(StageStyle.UNDECORATED);
            dialogScene.setTitle("Учебные дисциплины");
            dialogScene.getIcons().add(new Image("file:door.png"));
            dialogScene.initModality(Modality.WINDOW_MODAL);
            dialogScene.initOwner(null);
            Scene scene = new Scene(page);
            dialogScene.setScene(scene);

            EditDisciplinesController controller = loader.getController();
            controller.setDialogStage(dialogScene);
            controller.setDisciplines(disciplineHashMap);
            controller.setCourses(courses);
            dialogScene.showAndWait();
            if (controller.getSelectedDiscipline() != null && !tableViewDisciplineCourse.getItems().contains(controller.getSelectedDiscipline())){
                tableViewDisciplineCourse.getItems().add(controller.getSelectedDiscipline());}
            }


}
