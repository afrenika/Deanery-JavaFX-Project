package kfu.deanery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.time.LocalDate;

public class Student{
    private Course course;

    private Integer id;
    private String FIO;
    private String gender;
    private LocalDate dateOfBirthday;
    private String address;
    private ObservableMap<Discipline, Integer> marks;

    public Student(Integer id, String FIO, String gender, LocalDate dateOfBirthday, String address, Course course) {
        this.id = id;
        this.FIO = FIO;
        this.gender = gender;
        this.dateOfBirthday = dateOfBirthday;
        this.address = address;
        this.course = course;
        marks = FXCollections.observableHashMap();
        for(Discipline discipline: course.getDisciplines()){
            marks.put(discipline, 0);}
    }

    public Student(Course course) {
        marks = FXCollections.observableHashMap();
        this.course = course;
        for(Discipline discipline: course.getDisciplines()){
            marks.put(discipline, 0);}
    }

    public void setMarks(String [] mark) {
        marks = FXCollections.observableHashMap();;
        int i = 0;
        for(String item:mark){
            String[] it = item.split(":");
        for(Discipline discipline: course.getDisciplines()){
            if(discipline.getId().equals(Integer.parseInt(it[0]))){
                marks.put(discipline, Integer.parseInt(it[1]));}
        }}
    }

    public void setMarks(){
        for(Discipline discipline: course.getDisciplines()){
           if (!marks.containsKey(discipline)){
               marks.put(discipline, 0);
           }
        }
    }

    public ObservableMap<Discipline, Integer> getMarks() {
        return marks;
    }

    public void updateMark(){
        for(Discipline discipline: course.getDisciplines()){
            if (!marks.containsKey(discipline)){
                marks.put(discipline, 0);}
        }
        marks.entrySet().removeIf(entry -> !course.getDisciplines().contains(entry.getKey()));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(LocalDate dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Check excludeTrue(){
        Check check = new Check();
        for(Integer mark:marks.values()){
            if (mark < 56){
                if (mark == 0){
                    check.zero = true;}
                check.m56 = true;
            }
            if(check.zero && check.m56){
                return check;}}
        return check;
    }


    static class Check{
        boolean zero;
        boolean m56;

        public Check() {
            this.zero = false;
            this.m56 = false;
        }
    }

    public Course getCourse() {
        return course;
    }
}
