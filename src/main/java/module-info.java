module org.piva.fisdtest {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;

    opens org.piva.fisdtest18012025.chat to javafx.fxml;
    exports org.piva.fisdtest18012025.chat;
    exports org.piva.fisdtest18012025.json.entity.exchange to com.fasterxml.jackson.databind;
    exports org.piva.fisdtest18012025.json.entity.weather to com.fasterxml.jackson.databind;

}