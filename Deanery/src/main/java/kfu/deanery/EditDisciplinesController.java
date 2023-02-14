package kfu.deanery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;

public class EditDisciplinesController {

    private final ObservableList<Discipline> disciplines= FXCollections.observableArrayList();
    private HashMap<Integer, Discipline> disciplineHashMap;
    private Discipline selectedDiscipline;

    private Stage dialogStage;

    @FXML
    private TableView<Discipline> tableDisciplines;

    @FXML
    private TableColumn<Discipline, Integer> idCol;
    @FXML
    private TableColumn<Discipline, Integer> nameCol;
    @FXML
    private TableColumn<Discipline, Integer> FIOCol;
    @FXML
    private TableColumn<Discipline, Integer> hourCol;


    private boolean okClicked = false;
    private ObservableList<Course> courses;


    public void setDisciplines(HashMap<Integer, Discipline> disciplineH) {
        disciplines.addAll(disciplineH.values());
        disciplineHashMap = disciplineH;
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("disciplineName"));
        FIOCol.setCellValueFactory(new PropertyValueFactory<>("FIO_teacher"));
        hourCol.setCellValueFactory(new PropertyValueFactory<>("hours"));
        tableDisciplines.setItems(disciplines);
    }

    public void setCourses(ObservableList<Course> courses) {
        this.courses = courses;
    }

    public Discipline getSelectedDiscipline() {
        return selectedDiscipline;
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }




    public boolean isOkClicked(){
        return okClicked;}

    private boolean isInputValid(){
        String errorMessage = "";
        if (tableDisciplines.getItems().size() == 0){
            errorMessage += "Список предметов пуст\n";}
        if (errorMessage.length() == 0)
            return true;
        else {
            Alert alert = new Alert (Alert.AlertType.WARNING, errorMessage + "\n" + "Вы уверены, что хотите оставить список пустым?", ButtonType.YES, ButtonType.NO);
            alert.initOwner (dialogStage);
            alert.setTitle ("Предупреждение");
            alert.showAndWait ();
            boolean warning = alert.getResult() == ButtonType.YES;
            if (warning){
                dialogStage.close();
            }
            return false;}
    }

    @FXML
    private void Ok() {
        selectedDiscipline = tableDisciplines.getSelectionModel().getSelectedItem();
        if (selectedDiscipline != null){
            if (isInputValid()) {
                okClicked = true;
                dialogStage.close();} }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Курс не выбран");
            alert.setContentText("Выберите курс в таблице");
            alert.showAndWait();}
    }
    @FXML
    private void Cancel(){
        if (isInputValid()) {
            dialogStage.close();
        }
    }

    @FXML
    private void delete(){
        Discipline selectedDiscipline = tableDisciplines.getSelectionModel().getSelectedItem();

        if (selectedDiscipline != null) {
            tableDisciplines.getItems().remove(selectedDiscipline);
            disciplineHashMap.remove(selectedDiscipline.getId());}
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
        Discipline discipline = new Discipline();
        boolean ok = show(discipline);
        if (ok){
            disciplines.add(discipline);
            disciplineHashMap.put(discipline.getId(), discipline);
            updateDisciplines();}
        }

        @FXML
        private void edit() throws IOException {
            Discipline selectedDiscipline = tableDisciplines.getSelectionModel().getSelectedItem();
            int index = tableDisciplines.getSelectionModel().getSelectedIndex();

            if (selectedDiscipline != null) {

                if (show(selectedDiscipline)){
                    disciplines.set(index, selectedDiscipline);
                    disciplineHashMap.put(selectedDiscipline.getId(), selectedDiscipline);
                    updateDisciplines();
                }}
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(null);
                alert.setTitle("Ничего не выбрано");
                alert.setHeaderText("Дисциплина не выбрана");
                alert.setContentText("Выберите в таблице дисциплину для удаления.");
                alert.showAndWait();}
        }

    @FXML
    private boolean show(Discipline discipline) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AddEditDisciplineController.class.getResource("addEditDiscipline.fxml"));
        AnchorPane page = loader.load();
        Stage dialogScene = new Stage();
        dialogScene.initStyle(StageStyle.UNDECORATED);
        dialogScene.getIcons().add(new Image("file:door.png"));
        dialogScene.setTitle("Учебная дисциплина");
        dialogScene.initModality(Modality.WINDOW_MODAL);
        dialogScene.initOwner(null);
        Scene scene = new Scene(page);
        dialogScene.setScene(scene);

        AddEditDisciplineController controller = loader.getController();
        controller.setDialogStage(dialogScene);
        controller.setDiscipline(discipline);
        dialogScene.showAndWait();
        return controller.isOkClicked();
    }

    public void updateDisciplines(){
        for(Course course: courses){
            course.setAllDisciplines(course.getAllDisciplines());}
    }
}
