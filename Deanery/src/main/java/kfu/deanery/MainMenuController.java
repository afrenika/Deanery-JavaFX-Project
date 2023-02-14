package kfu.deanery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainMenuController implements Initializable {

    private final ObservableList<Course> courseData = FXCollections.observableArrayList();
    private final HashMap<Integer, Discipline> disciplineMap = new HashMap<>();

    @FXML
    private AnchorPane ap;

    @FXML
    private BorderPane bp;

    @FXML
    private Menu courseMenu;

    @FXML
    private MenuItem MenuA;

    @FXML
    private Button buttonO, buttonM, buttonP, buttonA;

    private int tableCount;

    private boolean addCourse;

    @FXML
    private TextField number;

    @FXML
    protected void exit() {
        Alert alert1 = new Alert (Alert.AlertType.WARNING, " ", ButtonType.YES, ButtonType.NO);
        alert1.initOwner(null);
        alert1.setTitle("Вы уверены, что хотите выйти?");
        alert1.setHeaderText("Вы уверены, что хотите выйти?");
        alert1.showAndWait();
        if(alert1.getResult() == ButtonType.YES){
            Alert alert = new Alert (Alert.AlertType.WARNING, "", ButtonType.YES, ButtonType.NO);
            alert.initOwner(null);
            alert.setTitle("Выход из системы");
            alert.setHeaderText("Сохранить несохраненные данные?");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                Rewrite.rewriteAll(courseData, disciplineMap);}
                ((Stage) ap.getScene().getWindow()).close();}
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try (Scanner scannerDiscipline = new Scanner(new File("disciplines.txt"))) {
            while (scannerDiscipline.hasNext()) {
                String[] a = scannerDiscipline.nextLine().split(" ");
                if (a.length > 1) {
                    Discipline discipline = new Discipline(Integer.parseInt(a[0]), Integer.parseInt(a[1]), a[2].replace("_", " "), a[3].replace("_", " "));
                    disciplineMap.put(discipline.getId(), discipline);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (Scanner scannerCourse = new Scanner(new File("Courses.txt"))) {
            while (scannerCourse.hasNext()) {
                String[] a = scannerCourse.nextLine().split(" ");
                if (a.length > 1) {
                    ObservableList<Discipline> disciplines = FXCollections.observableArrayList();
                    for (int i = 1; i < a.length; i++) {
                        disciplines.add(disciplineMap.get(Integer.parseInt(a[i])));
                    }
                    courseData.add(new Course(Integer.parseInt(a[0].replace(":", "")), disciplines));

                }
            }
            addCourse = !(courseData.size() == 4);
            buttonA.setVisible(addCourse);
            MenuA.setVisible(addCourse);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (Scanner scannerGroup = new Scanner(new File("Groups.txt"))) {
            while (scannerGroup.hasNext()) {
                String[] a = scannerGroup.nextLine().split(" ");
                if (a.length > 1) {
                    Menu menuItem = new Menu(Integer.parseInt(a[0]) + " курс");
                    menuItem.setOnAction(this::openGG);
                    courseMenu.getItems().add(menuItem);
                    for (int i = 1; i < a.length; i++) {
                        Group group = new Group(a[i].split(":")[0], a[i].split(":")[1].replace("_", " "));
                        MenuItem menuItem1 = new MenuItem(group.getGroupNumber());
                        menuItem1.setOnAction(this::openG);
                        menuItem.getItems().add(menuItem1);

                        try (Scanner scannerStudent = new Scanner(new File("Группы/" + group.getGroupNumber() + ".txt"))) {
                            while (scannerStudent.hasNext()) {
                                String[] b = scannerStudent.nextLine().split(" ");
                                if (b.length > 1) {
                                    Student student = new Student(Integer.parseInt(b[0]), b[1].replace("_", " "), b[2], LocalDate.parse(b[3], DateTimeFormatter.ofPattern("dd-MM-yyyy")), b[4].replace("_", " "), getCourse(Integer.parseInt(a[0])));
                                    group.getStudents().add(student);
                                }
                            }
                            try(Scanner scanner2 = new Scanner(new File("Группы/" + group.getGroupNumber() + "m.txt"))) {
                                while (scanner2.hasNext()){
                                    String string = scanner2.nextLine();
                                    Student student = getStudent(group, Integer.parseInt(string.substring(0, string.indexOf(" "))));
                                    student.setMarks(string.substring(string.indexOf(" ") + 1).split(" "));
                                }
                            }
                            catch (Exception ignored){}

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        getGroups(Integer.parseInt(a[0])).add(group);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        openC(new ActionEvent());



    }

    public ObservableList<Group> getGroups(Integer number) {
        for (Course course : courseData) {
            if (course.getNumber().equals(number)) {
                return course.getGroups();
            }
        }
        return null;
    }

    public Course getCourse(int number){
        for(Course course:courseData){
            if (course.getNumber() == number){
                return course;}
        }
    return null;}
    public Course getCourse(String numberGroup){
        for(Course course:courseData){
            for(Group group: course.getGroups()){
                if (numberGroup.equals(group.getGroupNumber())){
                    return course;}}}
        return null;
    }


    public ObservableList<Student> getStudents(Integer number, String numberGr) {
        for (Group group: getGroups(number)) {
            if (group.getGroupNumber().equals(numberGr)) {
                return group.getStudents();
            }
        }
        return null;
    }
    public Student getStudent(Group group, Integer id){
        for(Student student:group.getStudents()){
            if(student.getId().equals(id)){
                return student;}}
        return null;
    }


    @FXML
    public void open(){
        boolean t = switch (tableCount){
            case(1) ->  openGG();
            case(2) -> openG();
            default -> false;
        };
        if (t){
            tableCount++;}}

    @FXML
    public void pred(ActionEvent actionEvent){
        switch (tableCount){
            case(2) ->
                openC(actionEvent);
            case(3) -> openGGp();}}

    @FXML
    public void add(){
        switch (tableCount){
            case (1) -> handleCourseAdd();
            case (2) -> handleGroupNew();
            case (3) -> handleStudentNew();
        }
    }

    @FXML
    public void edit(){
        switch (tableCount){
            case (1) -> handleCourseEdit();
            case (2) -> handleGroupEdit();
            case (3) -> handleStudentEdit();
        }
    }

    @FXML
    public void openC(ActionEvent actionEvent){
        tableCount = 1;
        buttonM.setVisible(false);
        buttonO.setVisible(true);
        buttonP.setVisible(false);
        MenuA.setVisible(addCourse);
        buttonA.setVisible(addCourse);
        number.setText("Все курсы");
        TableView<Course> tableViewCources = new TableView<>();
        tableViewCources.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    open();}}});
        TableColumn<Course, Integer> column = new TableColumn<>("Номер курса");
        column.setCellValueFactory(new PropertyValueFactory<>("number"));
        TableColumn<Course, String> column2 = new TableColumn<>("Предметы");

        column2.setCellValueFactory(new PropertyValueFactory<>("allDisciplines"));
        column.setMinWidth(180);
        tableViewCources.getColumns().add(column);
        tableViewCources.getColumns().add(column2);
        tableViewCources.setItems(courseData);
        bp.setCenter(tableViewCources);
    }


    @FXML
    public void openGG(ActionEvent actionEvent){
        tableCount = 2;
        Integer number = Integer.parseInt(((MenuItem)actionEvent.getSource()).getText().split(" ")[0]);
        this.number.setText(((MenuItem) actionEvent.getSource()).getText());
        openGGDop(getGroups(number));

    }
    @FXML
    public boolean openGG(){
        Course course = ((TableView<Course>) bp.getCenter()).getSelectionModel().getSelectedItem();
        if (course != null) {
            openGGDop(course.getGroups());
            number.setText(course.getNumber() + " курс");
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Курс не выбран");
            alert.setContentText("Выберите курс в таблице");
            alert.showAndWait();
            return false;}

    }
    @FXML
    private void openGGp(){
        ObservableList<Student> check= ((TableView<Student>)bp.getCenter()).getItems();
        for(Course course: courseData){
            for(Group group:course.getGroups()){
                if (group.getStudents().equals(check)){
                    openGGDop(course.getGroups());
                    number.setText(course.getNumber() + " курс");
                    break;}}}
        tableCount--;
    }
    private void openGGDop(ObservableList<Group> course){
        buttonM.setVisible(false);
        buttonO.setVisible(true);
        buttonP.setVisible(true);
        buttonA.setVisible(true);
        MenuA.setVisible(true);

        TableView<Group> tableViewCources = new TableView<>();
        tableViewCources.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    open();}}});
        TableColumn<Group, String> column = new TableColumn<>("Номер группы");
        column.setCellValueFactory(new PropertyValueFactory<>("groupNumber"));
        TableColumn<Group, String> column2 = new TableColumn<>("Куратор");

        column2.setCellValueFactory(new PropertyValueFactory<>("groupTutor"));
        column.setMinWidth(200);
        column2.setMinWidth(200);
        tableViewCources.getColumns().add(column);
        tableViewCources.getColumns().add(column2);
        tableViewCources.setItems(course);
        bp.setCenter(tableViewCources);
    }

    @FXML
    public void openG(ActionEvent actionEvent){

        tableCount = 3;
        String name = ((MenuItem)actionEvent.getSource()).getText();
        Integer number = Integer.parseInt(((MenuItem)actionEvent.getSource()).getParentMenu().getText().split(" ")[0]);
        this.number.setText(((MenuItem)actionEvent.getSource()).getText());

        openGDop(getStudents(number, name));

    }
    private boolean openG(){
        Group group = ((TableView<Group>)bp.getCenter()).getSelectionModel().getSelectedItem();
        if (group != null){
            openGDop(group.getStudents());
            number.setText(group.getGroupNumber());
            return true;}
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Группа не выбрана");
            alert.setContentText("Выберите группу в таблице");
            alert.showAndWait();
            return false;
        }
    }
    private void openGDop(ObservableList<Student> list){
        buttonM.setVisible(true);
        buttonO.setVisible(false);
        buttonP.setVisible(true);
        buttonA.setVisible(true);
        MenuA.setVisible(true);
        TableView<Student> tableViewCources = new TableView<>();
        tableViewCources.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    open();}}});
        TableColumn<Student, String> column = new TableColumn<>("ID");
        column.setCellValueFactory(new PropertyValueFactory<>("id"));

        column.setMinWidth(20);
        tableViewCources.getColumns().add(column);

        column = new TableColumn<>("ФИО студента");
        column.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        column.setMinWidth(380);
        tableViewCources.getColumns().add(column);

        column = new TableColumn<>("Пол");
        column.setCellValueFactory(new PropertyValueFactory<>("gender"));
        column.setMinWidth(80);
        tableViewCources.getColumns().add(column);

        column = new TableColumn<>("Дата р.");
        column.setCellValueFactory(new PropertyValueFactory<>("dateOfBirthday"));
        column.setMinWidth(160);
        tableViewCources.getColumns().add(column);

        column = new TableColumn<>("Адрес");
        column.setCellValueFactory(new PropertyValueFactory<>("address"));
        column.setMinWidth(480);
        tableViewCources.getColumns().add(column);

        tableViewCources.setItems(list);
        bp.setCenter(tableViewCources);
    }


    public boolean showStudentEditDialog(Student student){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddEditStudentController.class.getResource("addEditStudent.fxml"));
            AnchorPane page = loader.load();
            Stage dialogScene = new Stage();
            dialogScene.setTitle("Студент");
            dialogScene.getIcons().add(new Image("file:door.png"));
            dialogScene.initStyle(StageStyle.UNDECORATED);
            dialogScene.initModality(Modality.WINDOW_MODAL);
            dialogScene.initOwner(null);
            Scene scene = new Scene(page);
            dialogScene.setScene(scene);

            AddEditStudentController controller = loader.getController();
            controller.setDialogStage(dialogScene);
            controller.setStudent(student);
            controller.setGroup(number.getText());
            controller.dopInit();
            dialogScene.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleStudentNew() {

        Student tempStudent = new Student(getCourse(number.getText()));
        boolean okClicked = this.showStudentEditDialog(tempStudent);
        if (okClicked) {
            ((TableView<Student>)bp.getCenter()).getItems().add(tempStudent);
            ((TableView<Student>)bp.getCenter()).getItems().sort(Comparator.comparingInt(Student::getId));}}

    @FXML
    private void handleStudentEdit() {
        Student selectedStudent = ((TableView<Student>)bp.getCenter()).getSelectionModel().getSelectedItem();

        if (selectedStudent != null) {
            boolean okClicked = showStudentEditDialog(selectedStudent);
            if (okClicked) {
                int selectedIndex = ((TableView<Student>)bp.getCenter()).getSelectionModel().getSelectedIndex();
                ((TableView<Student>)bp.getCenter()).getItems().set(selectedIndex, selectedStudent);
                ((TableView<Student>)bp.getCenter()).getItems().sort(Comparator.comparingInt(Student::getId));
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Студент не выбран");
            alert.setContentText("Выберите студента в таблице");
            alert.showAndWait();
        }}


    public boolean showGroupEditDialog(Group group){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddEditGroupController.class.getResource("addEditGroup.fxml"));
            AnchorPane page = loader.load();
            Stage dialogScene = new Stage();
            dialogScene.setTitle("Группа");
            dialogScene.initStyle(StageStyle.UNDECORATED);
            dialogScene.getIcons().add(new Image("file:door.png"));
            dialogScene.initModality(Modality.WINDOW_MODAL);
            dialogScene.initOwner(null);
            Scene scene = new Scene(page);
            dialogScene.setScene(scene);

            AddEditGroupController controller = loader.getController();
            controller.setDialogStage(dialogScene);
            controller.setGroup(group);
            dialogScene.showAndWait();

            return controller.isOkClicked();
        }

        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleGroupNew() {
        Group tempGroup = new Group();
        boolean okClicked = this.showGroupEditDialog(tempGroup);
        if (okClicked) {
            ((TableView<Group>)bp.getCenter()).getItems().add(tempGroup);
            ((TableView<Group>)bp.getCenter()).getItems().sort(Comparator.comparing(Group::getGroupNumber));
            updateMenu();
        }}

    @FXML
    private void handleGroupEdit() {
        Group selectedGroup = ((TableView<Group>)bp.getCenter()).getSelectionModel().getSelectedItem();

        if (selectedGroup != null) {
            boolean okClicked = showGroupEditDialog(selectedGroup);
            if (okClicked) {
                int selectedIndex = ((TableView<Group>)bp.getCenter()).getSelectionModel().getSelectedIndex();
                ((TableView<Group>)bp.getCenter()).getItems().set(selectedIndex, selectedGroup);
                ((TableView<Group>)bp.getCenter()).getItems().sort(Comparator.comparing(Group::getGroupNumber));
                updateMenu();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Группа не выбрана");
            alert.setContentText("Выберите группу в таблице");
            alert.showAndWait();

        }}

    public boolean showCourseEditDialog(Course course){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddEditCourseController.class.getResource("addEditCourse.fxml"));
            AnchorPane page = loader.load();
            Stage dialogScene = new Stage();
            dialogScene.setTitle("Курс");
            dialogScene.initStyle(StageStyle.UNDECORATED);
            dialogScene.getIcons().add(new Image("file:door.png"));
            dialogScene.initModality(Modality.WINDOW_MODAL);
            dialogScene.initOwner(null);
            Scene scene = new Scene(page);
            dialogScene.setScene(scene);

            AddEditCourseController controller = loader.getController();
            controller.setDialogStage(dialogScene);
            controller.setCourse(course);
            controller.setDisciplineHashMap(disciplineMap);
            controller.setCourses(courseData);
            dialogScene.showAndWait();
            return controller.isOkClicked();
        }

        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }


    @FXML
    private void handleCourseEdit() {
        Course selectedCourse = ((TableView<Course>)bp.getCenter()).getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
                showCourseEditDialog(selectedCourse);
                int selectedIndex = ((TableView<Course>)bp.getCenter()).getSelectionModel().getSelectedIndex();
                ((TableView<Course>)bp.getCenter()).getItems().set(selectedIndex, selectedCourse);
                updateMenu();
                updateDisc();
                courseData.forEach(c -> c.getGroups().forEach(v -> v.getStudents().forEach(Student::setMarks)));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Курс не выбран");
            alert.setContentText("Выберите курс в таблице");
            alert.showAndWait();

        }}


        @FXML
        private void handleCourseAdd() {
            Course course = new Course();
            if (showCourseEditDialog(course)){
                courseData.add(course);
                courseData.sort(Comparator.comparingInt(Course::getNumber));
                addCourse = !(courseData.size() == 4);
                buttonA.setVisible(addCourse);
                MenuA.setVisible(addCourse);
                updateMenu();
                updateDisc();}
        }

    @FXML
    public void selectHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(null);
        alert.setTitle("О программе");
        alert.setHeaderText("Информация о программе");
        alert.setContentText("Система деканат разработана студенткой КФУ ИВМиИт Габитовой А. И.(2022г.)");
        alert.showAndWait();
    }

    private void updateMenu(){
        courseMenu.getItems().clear();
        for(Course course:courseData){
            Menu menuItem = new Menu(course.getNumber() + " курс");
            menuItem.setOnAction(this::openGG);
            courseMenu.getItems().add(menuItem);
            for(Group group:course.getGroups()){
                MenuItem menuItem1 = new MenuItem(group.getGroupNumber());
                menuItem1.setOnAction(this::openG);
                (menuItem).getItems().add(menuItem1);
            }

        }

    }

    public void updateDisc(){
        for(Course course:courseData){
            for(Group group:course.getGroups()){
                for(Student student:group.getStudents()){
                    student.updateMark();}}}}


    @FXML
    public void checkMark(){
        Student selectedStudent = ((TableView<Student>)bp.getCenter()).getSelectionModel().getSelectedItem();

        if (selectedStudent != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MarksController.class.getResource("marks.fxml"));
                AnchorPane page = loader.load();
                Stage dialogScene = new Stage();
                dialogScene.initStyle(StageStyle.UNDECORATED);
                dialogScene.setTitle("Табель успеваемости");
                dialogScene.getIcons().add(new Image("file:door.png"));
                dialogScene.initModality(Modality.WINDOW_MODAL);
                dialogScene.initOwner(null);
                Scene scene = new Scene(page);
                dialogScene.setScene(scene);

                MarksController controller = loader.getController();
                controller.setDialogStage(dialogScene);
                controller.setStudent(selectedStudent);
                dialogScene.showAndWait();
            }

            catch (IOException e){
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(null);
            alert.setTitle("Ничего не выбрано");
            alert.setHeaderText("Студент не выбран");
            alert.setContentText("Выберите студента в таблице");
            alert.showAndWait();

        }
    }

    @FXML
    private void newCourse(){
        StringBuilder stZ = new StringBuilder();
        for(Course course:courseData){
            for(Group group:course.getGroups()){
                stZ.append(group.checkMarks(false));}}

        if (!stZ.toString().equals("")){
            Alert alert = new Alert (Alert.AlertType.WARNING, stZ.substring(0, stZ.length() - 2) + "\n" + "Исключить?", ButtonType.YES, ButtonType.NO);
            alert.initOwner(null);
            alert.setTitle("Обнаружены студенты, у которых 0 баллов за дисциплину");
            alert.setHeaderText("Все равно удалить и перевести остальных на другой курс?");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                Iterator<Course> iterator = courseData.iterator();
                while (iterator.hasNext()){
                    Course course = iterator.next();
                    for(Group group:course.getGroups()){
                        stZ.append(group.checkMarks(true));}
                    course.setNumber(course.getNumber() + 1);
                    if (course.getNumber() == 5){iterator.remove();}}

                Alert alert2 = new Alert (Alert.AlertType.INFORMATION);
                addCourse = !(courseData.size() == 4);
                buttonA.setVisible(addCourse);
                MenuA.setVisible(addCourse);
                alert2.initOwner(null);
                alert2.setTitle("Процесс прошел успешно.");
                alert2.setHeaderText("Перевод на другой курс завершен.");
                alert2.setContentText("");
                alert2.showAndWait();
                updateMenu();
                ((TableView<?>)(bp.getCenter())).refresh();
            }}
        else {
            Iterator<Course> iterator = courseData.iterator();
            while (iterator.hasNext()){
                Course course = iterator.next();
                course.setNumber(course.getNumber() + 1);
                if (course.getNumber() == 5){iterator.remove();}}

            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            addCourse = !(courseData.size() == 4);
            buttonA.setVisible(addCourse);
            MenuA.setVisible(addCourse);
            alert.initOwner(null);
            alert.setTitle("Процесс прошел успешно.");
            alert.setHeaderText("Перевод на другой курс завершен.");
            alert.setContentText("");
            alert.showAndWait();
            updateMenu();
            ((TableView<?>)(bp.getCenter())).refresh();}}

    @FXML
    private void save(){
        Rewrite.rewriteAll(courseData, disciplineMap);
    }

}

