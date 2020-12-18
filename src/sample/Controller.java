package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.image.Image;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    BufferedImage img= null;
    @FXML
    private ImageView image;
    @FXML
    private ChoiceBox<String>trainingType;
    @FXML
    private TextArea imagenProcesada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        trainingType.getItems().addAll("Entrenado", "No entrenado");
    }


    public void selectFile(MouseEvent event) throws IOException {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            img = ImageIO.read(new File(selectedFile.getAbsolutePath()));
            Image imagen = new Image(selectedFile.toURI().toString());

            image.setImage(imagen);
            image.setFitWidth(310);
            image.setFitHeight(270);
            image.setPreserveRatio(false);
        }
    }

    public void proccessImage(MouseEvent event){
        try {
            if (image != null ) {
                if(trainingType.getValue()!= null){
                    String data;
                    Tesseract tesseract = new Tesseract();
                    String path = System.getProperty("user.dir").replace('\\', '/');
                    String fullPath = path + "/tessdata";
                    tesseract.setDatapath(fullPath);
                    switch (trainingType.getValue()){
                        case "Entrenado":
                            data = "2";
                            break;
                        default:
                            data = "eng";
                            break;
                    }

                    tesseract.setLanguage(data);
                    String text
                            = tesseract.doOCR(img);

                    imagenProcesada.setText(text);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Datos incorrectos");
                    alert.setHeaderText(null);
                    alert.setContentText("Debe seleccionar un tipo de entrenamiento");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Datos incorrectos");
                alert.setHeaderText(null);
                alert.setContentText("Debe seleccionar una imagen");
                alert.showAndWait();
            }

        } catch (TesseractException ex) {
            imagenProcesada.setText(ex.getLocalizedMessage()+ "    "+ex.getMessage());
            ex.printStackTrace();
        }

    }



}
