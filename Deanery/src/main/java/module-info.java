module kfu.deanery {
    requires javafx.controls;
    requires javafx.fxml;


    opens kfu.deanery to javafx.fxml;
    exports kfu.deanery;
}