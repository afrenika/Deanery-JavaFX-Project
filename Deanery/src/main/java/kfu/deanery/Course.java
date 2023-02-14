package kfu.deanery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Course {
    private Integer number;
    private ObservableList<Discipline> disciplines;
    private ObservableList<Group> groups;
    private String allDisciplines;


    public Course() {
        groups = FXCollections.observableArrayList();
        disciplines = FXCollections.observableArrayList();
    }

    public Course(Integer number, ObservableList<Discipline> disciplines) {
        this.number = number;
        this.disciplines = disciplines;
        this.groups = FXCollections.observableArrayList();
        this.allDisciplines = getAllDisciplines();
    }

    public ObservableList<Group> getGroups() {
        return groups;
    }


    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ObservableList<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setAllDisciplines(String allDisciplines) {
        this.allDisciplines = allDisciplines;
    }

    public String getAllDisciplines(){
        StringBuilder result = new StringBuilder();
        if (disciplines != null){
            for(Discipline discipline: disciplines){
                result.append(discipline.getDisciplineName()).append(" (").append(discipline.getFIO_teacher()).append(")").append("\n");
            }}
        return result.toString();
    }



}
