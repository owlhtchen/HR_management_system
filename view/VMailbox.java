package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.notification.Message;

import java.util.ArrayList;
import java.util.List;

public class VMailbox {
    public void displayMailbox(List<Message> messages) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Message Inbox");
        window.setMinWidth(450);
        window.setMinHeight(450);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        Label label = new Label("Edit Table to set Readed/Unread");

        // title content dateString receiverUsername hasRead
        TableView<Message> table = new TableView<>();
        table.setEditable(true);
        table.setPrefSize(400, 400);

        TableColumn<Message, String> titleCol = new TableColumn<>("Titie");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Message, String> contentCol = new TableColumn<>("Content");
        contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));

        TableColumn<Message, String> dateStringCol = new TableColumn<>("Date");
        dateStringCol.setCellValueFactory(new PropertyValueFactory<>("dateString"));

        TableColumn<Message, Boolean> readedCol = new TableColumn<>("Readed");
        readedCol.setCellValueFactory(new PropertyValueFactory<>("hasRead"));
        this.displayYesNoCol(readedCol);
        this.setReadedOnEdit(readedCol);

        table.getColumns().addAll(titleCol, contentCol, dateStringCol, readedCol);
        for(Message entry: messages)  {
            // once you open Mailbox, all messages are set are read
            entry.setHasRead(true);
        }
        ObservableList<Message> messagesList = FXCollections.observableArrayList(messages);
        table.setItems(messagesList);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(table, closeButton, label);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    void setReadedOnEdit(TableColumn<Message, Boolean> col) {
        col.setOnEditCommit(
                (TableColumn.CellEditEvent<Message, Boolean> readedEvent) -> {
                    Message message = ( readedEvent.getTableView().getItems().get(
                            readedEvent.getTablePosition().getRow()
                    ));
                    Boolean readed = readedEvent.getNewValue();
                    message.setHasRead(readed);
                }
        );
    }

    private void displayYesNoCol(TableColumn<Message, Boolean> col) {
        ArrayList<Boolean> options = new ArrayList<>();
        options.add(true);
        options.add(false);
        ObservableList<Boolean> optionsYesNo = FXCollections.observableArrayList(options);
        col.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<Boolean>() {
                    @Override
                    public String toString(Boolean optionBool) {
                        if(optionBool) {
                            return "Yes";
                        } else {
                            return "No";
                        }
                    }
                    @Override
                    public Boolean fromString (String optionString) {
                        if(optionString.equals("Yes")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                },
                optionsYesNo
        ));
    }
}
