module com.example.ai_test2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ai_test2 to javafx.fxml;
    exports com.example.ai_test2;
}