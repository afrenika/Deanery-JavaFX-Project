package kfu.deanery;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.util.Map;

public class MarksController {

    @FXML
    private AnchorPane archP;
    private GridPane gridPane;

    private Student student;
    private Stage dialogStage;

    public void setStudent(Student student) {
        this.student = student;
        if(this.student.getMarks().size() != 0){
            int i = 0;
            gridPane = new GridPane();
            archP.getChildren().add(gridPane);
            gridPane.getColumnConstraints().add(new ColumnConstraints(470));
            gridPane.getColumnConstraints().add(new ColumnConstraints(80));
            for (Map.Entry<Discipline, Integer> entry: this.student.getMarks().entrySet()){
                archP.setPrefHeight((i + 1)*50);
                gridPane.getRowConstraints().add(new RowConstraints(50));
                gridPane.add(new Label(entry.getKey().getDisciplineName()), 0, i);
                TextField textField = new TextField(entry.getValue().toString());
                textField.setDisable(true);
                gridPane.add(textField, 1, i);
                i++;
            }
        }
    }

    public void setDialogStage(Stage dialogScene) {
        this.dialogStage = dialogScene;
    }

    @FXML
    private void Ok(){
        int i = 0;
        for (Map.Entry<Discipline, Integer> entry:student.getMarks().entrySet()){
            String y = ((TextField)gridPane.getChildren().get(2*i + 1)).getText();
            if (y != null && y.length() != 0){
                entry.setValue(Integer.parseInt(y));}
            i++;}
        dialogStage.close();
    }

    @FXML
    private void Cancel(){
        dialogStage.close();
    }

    @FXML
    private void edit(){
        for (Node node: gridPane.getChildren()){
            node.setDisable(false);
        }
    }
}
