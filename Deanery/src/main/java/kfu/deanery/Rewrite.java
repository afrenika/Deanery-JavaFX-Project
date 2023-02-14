package kfu.deanery;

import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Rewrite {
    public static void rewriteAll(ObservableList<Course> courseData, HashMap<Integer, Discipline>disciplines){
        try(FileWriter writer1 = new FileWriter("Courses.txt", false);
            FileWriter writer2 = new FileWriter("Groups.txt", false);
            FileWriter writer3 = new FileWriter("disciplines.txt", false)) {
            StringBuilder courseWrite = new StringBuilder();
            StringBuilder groupWrite = new StringBuilder();
            for(Course course:courseData){
                courseWrite.append(course.getNumber() + ":");
                for(Discipline discipline:course.getDisciplines()){
                    courseWrite.append(" ").append(discipline.getId());}
                courseWrite.append("\n");

                groupWrite.append(course.getNumber()).append(" ");
                for (Group group:course.getGroups()){
                    groupWrite.append(group.getGroupNumber()).append(":").append(group.getGroupTutor().replace(" ", "_")).append(" ");
                    try(FileWriter writer4 = new FileWriter("Группы/" + group.getGroupNumber() + ".txt", false);
                        FileWriter writer5 = new FileWriter("Группы/" + group.getGroupNumber() + "m.txt", false)){
                        StringBuilder studentsWrite = new StringBuilder();
                        StringBuilder studentsMarksWrite = new StringBuilder();
                        for(Student student:group.getStudents()){
                            studentsWrite.append(student.getId())
                                    .append(" ").append(student.getFIO().replace(" ", "_"))
                                    .append(" ").append(student.getGender()).append(" ")
                                    .append(student.getDateOfBirthday().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                                    .append(" ").append(student.getAddress().replace(" ", "_"));

                            studentsMarksWrite.append(student.getId());
                            for(Map.Entry<Discipline, Integer> mark:student.getMarks().entrySet()){
                                studentsMarksWrite.append(" ")
                                                .append(mark.getKey().getId())
                                                .append(":").append(mark.getValue());}
                            studentsMarksWrite.append("\n");
                            studentsWrite.append("\n");}
                        writer4.write(studentsWrite.toString());
                        writer5.write(studentsMarksWrite.toString());
                    }}
                groupWrite.append("\n");}
            writer1.write(courseWrite.toString());
            writer2.write(groupWrite.toString());

            StringBuilder disciplinesWrite = new StringBuilder();
            for(Discipline discipline:disciplines.values()){
                disciplinesWrite.append(discipline.getId())
                                .append(" ").append(discipline.getHours())
                                .append(" ").append(discipline.getDisciplineName().replace(" ", "_"))
                                .append(" ").append(discipline.getFIO_teacher().replace(" ", "_")).append("\n");}
            writer3.write(disciplinesWrite.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
