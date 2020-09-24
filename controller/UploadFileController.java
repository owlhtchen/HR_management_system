package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Scanner;

public class UploadFileController {

    private String title;
    private String content;
    private FileChooser chooser = new FileChooser();

    @FXML
    private AnchorPane pane;

    public UploadFileController(AnchorPane pane){
        this.pane = pane;
    }

    public void pickFile(){
        try{
            Stage stage = (Stage)pane.getScene().getWindow();
            chooser.setTitle("Choose a txt file");
            FileChooser.ExtensionFilter textFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
            chooser.getExtensionFilters().add(textFilter);
            chooser.setSelectedExtensionFilter(textFilter);
            java.io.File myFile = chooser.showOpenDialog(stage);
            if(myFile != null){
                this.title = myFile.getName();
                if (this.title.indexOf(".") > 0)
                    this.title = this.title.substring(0, this.title.lastIndexOf("."));
                Scanner input = new Scanner(myFile);

                StringBuilder content = new StringBuilder();
                while (input.hasNext()) {
                    content.append(input.nextLine());
                    content.append("\n");
                }
                input.close();
                this.content = content.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String getTitle(){
        return this.title;
    }

    public String getContent(){
        return  this.content;
    }
}
