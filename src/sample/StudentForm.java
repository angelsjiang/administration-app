package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Flow;

public class StudentForm extends HBox {

    private FileChooser fileChooser = new FileChooser();
    private ImageView imageP = new ImageView();

    TextField id = new TextField();
    TextField lastName = new TextField();
    TextField firstName = new TextField();
    TextField major = new TextField();
    ChoiceBox currGrade;
    ToggleGroup gradingOpt = new ToggleGroup();
    CheckBox honorStatus = new CheckBox();
    TextArea notes = new TextArea();
    TextField photoUrl = new TextField();

    public StudentForm() {

        HBox.setHgrow(id, Priority.ALWAYS);
        HBox box = new HBox(5, new Label("ID"), id);
        box.setPadding(new Insets(5));

        // last name
        HBox.setHgrow(lastName, Priority.ALWAYS);
        HBox box2 = new HBox(5, new Label("Last Name"), lastName);
        box2.setPadding(new Insets(5));

        // first name
        HBox.setHgrow(firstName, Priority.ALWAYS);
        HBox box3 = new HBox(5, new Label("First Name"), firstName);
        box3.setPadding(new Insets(5));

        // major
        HBox.setHgrow(major, Priority.ALWAYS);
        HBox box4 = new HBox(5, new Label("Major"), major);
        box4.setPadding(new Insets(5));

        // current grade
        ObservableList<String> gradeList = FXCollections.observableArrayList("A", "B", "C", "P", "F");
        currGrade = new ChoiceBox(gradeList);
        HBox box5 = new HBox(5, new Label("Current Grade"), currGrade);
        box5.setPadding(new Insets(5));

        // grade option
        RadioButton letter = new RadioButton("Letter");
        letter.setUserData("Letter");
        letter.setToggleGroup(gradingOpt);
        RadioButton pnp = new RadioButton("P/NP");
        pnp.setUserData("P/NP");
        pnp.setToggleGroup(gradingOpt);
        HBox box6 = new HBox(5, new Label("Grading Option"), letter, pnp);
        box6.setPadding(new Insets(5));

        // check box
        honorStatus.setSelected(false);
        HBox box7 = new HBox(5, new Label("Honor Student Status"), honorStatus);
        box7.setPadding(new Insets(5));

        // note section
        HBox.setHgrow(notes, Priority.ALWAYS);
        notes.setPrefSize(300, 100);
        HBox notesField = new HBox(5, new Label("Notes"), notes);
        notesField.setPadding(new Insets(5));

        // photo section
        Button upload = new Button("Upload");
        // default image
        Image image = new Image(getClass().getResource("image.jpg").toString());
        System.out.println("Get class: " + getClass().getResource("image.jpg").toString());
        System.out.println("default photourl: " + image.getUrl());
        imageP.setImage(image);
        imageP.setFitWidth(150);
        imageP.setPreserveRatio(true);

        TextField photoUrl = new TextField();

        // in case user choose an image
        upload.setOnAction(e -> uploadImage());

        HBox photoField = new HBox(5, upload, imageP, photoUrl);
        photoField.setPadding(new Insets(5));

        FlowPane wrapper = new FlowPane(box, box2, box3, box4, box5, box6, box7, notesField, photoField);
        wrapper.setPadding(new Insets(20));

        getChildren().addAll(wrapper);
        setSpacing(10);

        this.student.addListener((o, oldVal, newVal) -> {
            String idVal = null;
            String lastNameVal = null;
            String firstNameVal = null;
            String majorVal = null;
            String currGradeVal = null;
            String gOptionVal = null;
            boolean honorStatVal = false;
            String notesVal = null;
            String photoUrlVal = "image.jpg";

            if(newVal != null) {
                idVal = newVal.getId();
                lastNameVal = newVal.getLastName();
                firstNameVal = newVal.getFirstName();
                majorVal = newVal.getMajor();
                currGradeVal = newVal.getCurrentGrade();
                gOptionVal = newVal.getGradeOption();

                System.out.println(newVal.getHonorStatus());
                honorStatVal = Boolean.valueOf(newVal.getHonorStatus());
                notesVal = newVal.getNotes();
                photoUrlVal = newVal.getPhotoUrl();

                id.setText(idVal);
                if(id.getText() != null) {
                    id.setDisable(true);
                }
                lastName.setText(lastNameVal);
                firstName.setText(firstNameVal);
                major.setText(majorVal);
                currGrade.setValue(currGradeVal);

                for(int i = 0; i < gradingOpt.getToggles().size(); i++) {
                    if(gradingOpt.getToggles().get(i).getUserData().equals(gOptionVal)) {
//                        System.out.println(gradingOpt.getToggles().get(i).getUserData());
                        gradingOpt.getToggles().get(i).setSelected(true);
                    }
                }

                honorStatus.setSelected(honorStatVal);
                notes.setText(notesVal);

                System.out.println("Student PhotoUrl is this: " + photoUrlVal);
                photoUrl.setText(photoUrlVal);
                Image stuPhoto = new Image(photoUrlVal);
                imageP = new ImageView(stuPhoto);
                imageP.setFitWidth(150);
                imageP.setPreserveRatio(true);

            }

        });
    }

    public void clear() {
        id.setDisable(false);
        id.setText(null);
        lastName.setText(null);
        firstName.setText(null);
        major.setText(null);
        currGrade.setValue(null);
        gradingOpt.setUserData(null);
        honorStatus.setSelected(false);
        notes.setText(null);
        photoUrl.setText("image.jpg");
    }

    public void updateStudent(Student stu) {
        stu.setId(id.getText());
        stu.setLastName(lastName.getText());
        stu.setFirstName(firstName.getText());
        stu.setMajor(major.getText());
        stu.setCurrentGrade(currGrade.getSelectionModel().getSelectedItem().toString());

        RadioButton selectedBtn = (RadioButton)gradingOpt.getSelectedToggle();
        stu.setGradeOption(selectedBtn.getText());

        stu.setHonorStatus(honorStatus.isSelected());
        stu.setNotes(notes.getText());

//        System.out.println("In the updateStudent method: " + photoUrl.getText());
        stu.setPhotoUrl(photoUrl.getText());
    }

    public void refresh(Student stu) {
        id.setText(stu.getId());
        lastName.setText(stu.getLastName());
        firstName.setText(stu.getFirstName());
        major.setText(stu.getMajor());
        currGrade.setValue(stu.getCurrentGrade());
        gradingOpt.setUserData(stu.getGradeOption());
        honorStatus.setSelected(stu.getHonorStatus());
        notes.setText(stu.getNotes());
        photoUrl.setText(stu.getPhotoUrl());
    }

    // no longer needed
    public boolean isParseable(String string) {
        try {
            if(string == null) {
                return false;
            }
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // upload image
    public void uploadImage() {
        File selected = fileChooser.showOpenDialog(new Stage());
        if(selected != null) {
            System.out.println("In StudentForm - Selected Photo path is: " + selected.getName());
            System.out.println("In StudentForm - URI is: " + selected.toURI().toString());

            Image newImage = new Image(getClass().getResource(selected.getName()).toString());
            photoUrl.setText(newImage.getUrl());
            imageP.setImage(newImage);
            imageP.setFitWidth(150);
            imageP.setPreserveRatio(true);

        }
    }

    private final ObjectProperty<Student> student = new SimpleObjectProperty<>(this, "student");

    public ObjectProperty<Student> studentProperty() {
        return student;
    }

    public Student getStudent() {
        return studentProperty().get();
    }

    public void setStudent(Student student) {
        studentProperty().set(student);
    }

}
