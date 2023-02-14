package kfu.deanery;

public class Discipline {
    private Integer id;
    private Integer hours;
    private String disciplineName;
    private String FIO_teacher;

    public Discipline(Integer id, Integer hours, String disciplineName, String FIO_teacher) {
        this.id = id;
        this.hours = hours;
        this.disciplineName = disciplineName;
        this.FIO_teacher = FIO_teacher;
    }

    public Discipline() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getFIO_teacher() {
        return FIO_teacher;
    }

    public void setFIO_teacher(String FIO_teacher) {
        this.FIO_teacher = FIO_teacher;
    }

    @Override
    public String toString() {
        return disciplineName +"(" +hours +")";
    }
}
