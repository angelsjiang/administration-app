package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Student {
    private String id;
    private String lastName;
    private String firstName;
    private String major;
    private String currentGrade;
    private String gradeOption;
    private boolean honorStatus;
    private String notes;
    private String photoUrl = "file:/Users/angeljiang/Documents/2020-2021/UCI/Fall%202020/245P/Exercises/Exercises%202/E2.1/out/production/E2.1/sample/image.jpg";

    private final IntegerProperty index = new SimpleIntegerProperty();
    private BooleanProperty honorStat = new SimpleBooleanProperty(false);

    public Student() {
        // empty constructor
    }
    public Student(String id, String last, String first, String major, String grade,
                   String gradeOption, boolean honorStatus, String notes, String photoUrl) {
        this.id = id;
        this.lastName = last;
        this.firstName = first;
        this.major = major;
        this.currentGrade = grade;
        this.gradeOption = gradeOption;
        this.honorStatus = honorStatus;
        honorStat = new SimpleBooleanProperty(honorStatus);
        this.notes = notes;
        this.photoUrl = photoUrl;
    }

    public BooleanProperty honorStatProperty() { return honorStat; }

    @Override
    public String toString() {
        return Integer.toString(index.get());
    }

    public final IntegerProperty valueProperty() {
        return index;
    }

    public final int getValue() {
        return valueProperty().get();
    }

    public final void setValue(int index) {
        valueProperty().set(index);
    }

    public void setId(String id) {
//        int convertedID = Integer.parseInt(id);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setLastName(String last) {
        this.lastName = last;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String first) {
        this.firstName = first;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajor() {
        return this.major;
    }

    public void setCurrentGrade(String a) {
        this.currentGrade = a;
    }

    public String getCurrentGrade() {
        return this.currentGrade;
    }

    public void setGradeOption(String gradeOpt) {
        this.gradeOption = gradeOpt;
    }

    public String getGradeOption() {
        return this.gradeOption;
    }

    public void setHonorStatus(Boolean bool) {
        this.honorStatus = bool;
        honorStat = new SimpleBooleanProperty(bool);
    }

    public boolean getHonorStatus() {
        return this.honorStatus;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public String[] getAll() {
        String[] allInfo = new String[]{ this.id, this.firstName, this.lastName,
                this.major, this.gradeOption, Boolean.toString(this.honorStatus), this.notes,
                this.photoUrl};
        return allInfo;
    }
}
