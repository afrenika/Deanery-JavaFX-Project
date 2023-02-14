package kfu.deanery;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;


public class Group {
    private String groupNumber;
    private String groupTutor;
    private ObservableList<Student> students;

    public Group(String groupNumber, String groupTutor, ObservableList<Student> students) {
        this.groupNumber = groupNumber;
        this.groupTutor = groupTutor;
        this.students = students;
    }

    public Group(String groupNumber, String groupTutor) {
        this.groupNumber = groupNumber;
        this.groupTutor = groupTutor;
        students = FXCollections.observableArrayList();
    }

    public Group() {
        students = FXCollections.observableArrayList();
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getGroupTutor() {
        return groupTutor;
    }

    public void setGroupTutor(String groupTutor) {
        this.groupTutor = groupTutor;
    }

    public ObservableList<Student>  getStudents() {
        return students;
    }

    public void setStudents(ObservableList<Student> students) {
        this.students = students;
    }

    public String checkMarks(boolean warning){
        StringBuilder stZ = new StringBuilder();
        Iterator<Student> iterator = students.iterator();
       while (iterator.hasNext()){
           Student student = iterator.next();
            Student.Check check = student.excludeTrue();
            if (check.zero){
                stZ.append(student.getFIO()).append(", ");}
            if((warning || !check.zero) && check.m56){
                iterator.remove();
            }
        }
        return stZ.toString();
    }
}
