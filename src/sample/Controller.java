package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;


import java.io.*;

public class Controller extends TabPane {

    @FXML
    MenuItem menuNew;
    @FXML
    MenuItem menuOpen;
    @FXML
    MenuItem menuSave;
    @FXML
    MenuItem menuSaveAs;
    @FXML
    MenuItem menuClose;
    @FXML
    MenuItem menuExit;

    @FXML
    TextField id;
    @FXML
    TextField lastName;
    @FXML
    TextField firstName;
    @FXML
    TextField major;
    @FXML
    ChoiceBox currGrade;
    @FXML
    ToggleGroup gradingOption;
    @FXML
    RadioButton letter;
    @FXML
    RadioButton pnp;
    @FXML
    CheckBox honorStatus;
    @FXML
    TextArea notes;
    @FXML
    TextField imgUrl;
    @FXML
    ImageView imageView;

    @FXML
    Button uploadBtn;
    @FXML
    Button previous;
    @FXML
    Button addStudent;
    @FXML
    Button deleteStudent;
    @FXML
    Button saveStudent;
    @FXML
    Button next;

    @FXML
    TableView<Student> table;
    @FXML
    TableColumn<Student, String> columnImg;
    @FXML
    TableColumn<Student, String> columnId;
    @FXML
    TableColumn<Student, String> columnLastName;
    @FXML
    TableColumn<Student, String> columnFirstName;
    @FXML
    TableColumn<Student, String> columnMajor;
    @FXML
    TableColumn<Student, String> columnGrade;
    @FXML
    TableColumn<Student, String> columnGOpt;
    @FXML
    TableColumn<Student, String> columnHonorStat;
    @FXML
    TableColumn<Student, String> columnNotes;


    @FXML
    VBox chartView;
    @FXML
    Button showPieChart;
    @FXML
    Button showBarChart;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private IntegerProperty index = new SimpleIntegerProperty();
//    private StudentForm studentForm = new StudentForm();

    private FileChooser fileChooser = new FileChooser();
    private File selectedFile;
    private final Image defaultImg = new Image(getClass().getResource("image.jpg").toString(), 100, 100, true, true);

    // pie chart
    private ObservableMap<String, Integer> studentDataMap = FXCollections.observableHashMap();
    private ObservableList<PieChart.Data> studentDataList = FXCollections.observableArrayList();
    // bar graph
    private ObservableMap<String, Integer> studentGradeDataMap = FXCollections.observableHashMap();
    private ObservableList<XYChart.Data<String, Number>> studentGradeDataList = FXCollections.observableArrayList();


    public Controller() throws IOException {

        // load everything
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        fxmlLoader.load();


        // ------------------ set up layout, modifying fxml view ------------------ //

        ObservableList<String> gradeList = FXCollections.observableArrayList("A", "B", "C", "P", "F");
        currGrade.setItems(gradeList);

        // toggle for grading option
        gradingOption = new ToggleGroup();
        letter.setUserData("Letter");
        letter.setToggleGroup(gradingOption);
        pnp.setUserData("P/NP");
        pnp.setToggleGroup(gradingOption);

        // initialize image
        imgUrl.setText(defaultImg.getUrl());
        System.out.println(defaultImg.getUrl());
        imageView.setImage(defaultImg);


        // initialize table
        columnImg.setCellValueFactory(new PropertyValueFactory<>("photoUrl"));
        columnImg.setMaxWidth(60);

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnId.setEditable(false);

        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnLastName.setOnEditCommit((TableColumn.CellEditEvent<Student, String> t) -> {
            t.getRowValue().setLastName(t.getNewValue());
            setStudentForm();
        });

        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnFirstName.setOnEditCommit((TableColumn.CellEditEvent<Student, String> t) -> {
            t.getRowValue().setFirstName(t.getNewValue());
            setStudentForm();
        });

        columnMajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        ObservableList<String> majors = FXCollections.observableArrayList(
                "Humanities", "Visual Art", "Computer Science",
                "Music", "Mathematics", "Biology", "Chemistry");
        columnMajor.setCellFactory(ChoiceBoxTableCell.forTableColumn(majors));
        columnMajor.setOnEditCommit((TableColumn.CellEditEvent<Student, String> t) -> {
            t.getRowValue().setMajor(t.getNewValue());
            setStudentForm();
        });

        columnGrade.setCellValueFactory(new PropertyValueFactory<>("currentGrade"));
        columnGrade.setCellValueFactory(new PropertyValueFactory<>("currentGrade"));
        ObservableList<String> grades = FXCollections.observableArrayList("A", "B", "C", "P", "F");
        columnGrade.setCellFactory(ChoiceBoxTableCell.forTableColumn(grades));
        columnGrade.setOnEditCommit((TableColumn.CellEditEvent<Student, String> t) ->{
            t.getRowValue().setCurrentGrade(t.getNewValue());
            setStudentForm();
        });

        columnGOpt.setCellValueFactory(new PropertyValueFactory<>("gradeOption"));
        ObservableList<String> gradingOptions = FXCollections.observableArrayList(
                "Letter", "P/NP");
        columnGOpt.setCellFactory(ChoiceBoxTableCell.forTableColumn(gradingOptions));
        columnGOpt.setOnEditCommit((TableColumn.CellEditEvent<Student, String> t) -> {
            t.getRowValue().setGradeOption(t.getNewValue());
            System.out.println("New Value: " + t.getNewValue());
            setStudentForm();
        });

        columnHonorStat.setCellValueFactory(new PropertyValueFactory<>("honorStat"));
        columnHonorStat.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Integer param) {
                return studentList.get(param).honorStatProperty();
            }
        }));

        columnNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        columnNotes.setCellFactory(TextFieldTableCell.forTableColumn());
        columnNotes.setOnEditCommit((TableColumn.CellEditEvent<Student, String> t) ->{
            t.getRowValue().setNotes(t.getNewValue());
            setStudentForm();
        });



        table.setItems(studentList);

        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Student selectedStu = table.getSelectionModel().getSelectedItem();
//                System.out.println("Selected: " + selectedStu.getLastName());

                index.set(studentList.indexOf(selectedStu));
                setStudentForm();
            }
        });


        // ------------- Setting up Menu function -------------- //

        menuNew.setOnAction(e -> newFile());
        menuOpen.setOnAction(e -> openFile());
        menuSave.setOnAction(e -> saveFile());
        menuSaveAs.setOnAction(e -> saveFileAs());


        // bind index with student in the studentlist
        Bindings.valueAt(studentList, index);
        index.addListener((o, oldVal, newVal) -> {
            int count = studentList.size();
            int i = newVal.intValue();
            if (0 <= i && i < count) {
                index.set(i);
                setStudentForm();
            }
        });
        IntegerBinding studentListSize = Bindings.size(studentList);
        studentListSize.addListener((o, oldVal, newVal) -> {
            int updateSize = newVal.intValue();
            int i = index.get();
            if (i + 1 > updateSize) {
                index.set(Math.max(0, updateSize - 1));
            }
        });


        // ------------- Setting up button trigger functions ---------------- //

        showPieChart.setOnAction(e -> createPieChartList());
        showBarChart.setOnAction(e -> creatingBarGraph());
        uploadBtn.setOnAction(e -> uploadImage());
        saveStudent.setOnAction(e -> saveInfo());
        addStudent.setOnAction(e -> addNewStudent());
        deleteStudent.setOnAction(e -> setDeleteStudent());
        // next student
        next.setOnAction(e -> {
            index.set(index.get() + 1);
        });
        next.disableProperty().bind(index.greaterThanOrEqualTo(studentListSize.subtract(1)));

        // previous student
        previous.setOnAction(e -> {
            index.set(index.get() - 1);
        });
        previous.disableProperty().bind(index.lessThanOrEqualTo(0));


        // ------------- On Opening the Application ------------- //
        initializing();


    }

    // on opening file
    public void initializing() {
        System.out.println("------ initializing..... ------");
        Student new_stu = new Student();
        new_stu.setPhotoUrl(getClass().getResource("image.jpg").toString());
        studentList.add(new_stu);
        index.set(studentList.size() - 1);
    }

    // pull out the info of the current student on index
    public void setStudentForm() {
        Student student = studentList.get(index.get());
        System.out.println("studen id is: " + student.getId());
        if (student.getId() != null && !student.getId().trim().equals("")) {
            id.setDisable(true);
        } else {
            id.setDisable(false);
        }
        id.setText(student.getId());
        lastName.setText(student.getLastName());
        firstName.setText(student.getFirstName());
        major.setText(student.getMajor());
        currGrade.setValue(student.getCurrentGrade());

        // to take care of empty case
//        System.out.println("Line 298: " + gradingOption.getSelectedToggle().getUserData().toString() != null);
        if(student.getGradeOption() != null) {
            for (int i = 0; i < gradingOption.getToggles().size(); i++) {
                System.out.println(gradingOption.getToggles().get(i).getUserData().toString());
                if (gradingOption.getToggles().get(i).getUserData().equals(student.getGradeOption())) {
                    gradingOption.getToggles().get(i).setSelected(true);
                }
            }
        }
        else {
            gradingOption.selectToggle(null);
        }


        honorStatus.setSelected(student.getHonorStatus());
        notes.setText(student.getNotes());

        System.out.println(student.getPhotoUrl());
        if (student.getPhotoUrl() != null && !student.getPhotoUrl().trim().equals("")) {
            imgUrl.setText(student.getPhotoUrl());
            imageView.setImage(new Image(student.getPhotoUrl(), 100, 100, true, true));
        } else {
            imgUrl.setText(defaultImg.getUrl());
            imageView.setImage(defaultImg);
        }


        // sync table
        table.refresh();
    }

    public void getNewChanges(Student student) {
        student.setId(id.getText());
        student.setLastName(lastName.getText());
        student.setFirstName(firstName.getText());
        student.setMajor(major.getText());
        student.setCurrentGrade(currGrade.getSelectionModel().getSelectedItem().toString());
        student.setGradeOption(gradingOption.getSelectedToggle().getUserData().toString());
        student.setHonorStatus(honorStatus.isSelected());
        student.setNotes(notes.getText());
        if (imgUrl.getText() != null) {
            student.setPhotoUrl(imgUrl.getText());
        } else {
            student.setPhotoUrl(getClass().getResource("image.jpg").toString());
        }
        // sync table
        table.refresh();
    }

    // save student info on list
    public void saveInfo() {
        getNewChanges(studentList.get(index.get()));
    }

    public void addNewStudent() {
        Student new_stu = new Student();
        studentList.add(new_stu);
        index.set(studentList.size() - 1);
        setStudentForm();
    }


    public void setDeleteStudent() {
        studentList.remove(index.get());
        System.out.println(studentList.get(index.get()));
        setStudentForm();
    }

    public void uploadImage() {
        FileChooser chooser = new FileChooser();
        File selected = chooser.showOpenDialog(new Stage());
        if (selected != null) {
            System.out.println("In StudentForm - Selected Photo path is: " + selected.getName());
            System.out.println("In StudentForm - URI is: " + selected.toURI().toString());

            Image newImage = new Image(selected.toURI().toString(), 100, 100, true, true);
            imgUrl.setText(newImage.getUrl());
            imageView.setImage(newImage);
//            imageView.setFitWidth(150);
//            imageView.setPreserveRatio(true);
        }

    }

    public void newFile() {
        System.out.println("---------- Opening New file! -----------");
        studentList.clear();
        selectedFile = null;
        addNewStudent();
    }

    // open a new txt file
    public void openFile() {
        System.out.println("Gotcha!");

        FileChooser.ExtensionFilter extfilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extfilter);
        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            System.out.println(selectedFile.getPath());
            // get reader to readline & populate
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                // load information and save information into student arraylist
                studentList.clear();

                String line;
                int count = 0;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    String[] breakdown = line.split(", ");
                    System.out.println(breakdown.toString());

                    Student student = new Student();
                    student.setId(breakdown[0]);
                    student.setLastName(breakdown[1]);
                    student.setFirstName(breakdown[2]);
                    student.setMajor(breakdown[3]);
                    student.setCurrentGrade(breakdown[4]);
                    student.setGradeOption(breakdown[5]);
                    student.setHonorStatus(Boolean.valueOf(breakdown[6]));
                    student.setNotes(breakdown[7]);
                    student.setPhotoUrl(breakdown[8]);
                    studentList.add(student);
                    count++;
                }
                System.out.println("Number of student on file: " + studentList.size());
                System.out.println("count: " + count);

                index.set(0);
                setStudentForm();
                // create graphs
//                createPieChartList();
//                creatingBarGraph();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // print a single student
    public String writeStudent(Student student) {
        String studentPrint = student.getId() + ", " + student.getLastName() + ", " + student.getFirstName() + ", "
                + student.getMajor() + ", " + student.getCurrentGrade() + ", " + student.getGradeOption() + ", "
                + student.getHonorStatus() + ", " + student.getNotes() + ", " + student.getPhotoUrl() + '\n';
        return studentPrint;
    }

    // print a list of students
    public String printStudentList() {
        String content = "";
        for (int i = 0; i < studentList.size(); i++) {
            content += writeStudent(studentList.get(i));
        }
        System.out.println(content);
        return content;
    }

    // save data into current file
    public void saveFile() {
        System.out.println("----------------- Saving to existing file ------------------");
        if (selectedFile == null) {
            // direct to save as method
            saveFileAs();
            return;
        }
        try (PrintWriter wr = new PrintWriter(new FileWriter(selectedFile, false))) {
            System.out.println(selectedFile.getName());
            wr.write(printStudentList());

            wr.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // save data into a new file
    public void saveFileAs() {
        System.out.println("--------------- Saving into a new file ----------------");
        FileChooser chooser = new FileChooser();
        // add extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(extFilter);
        File selected = chooser.showSaveDialog(new Stage());
        if (selected != null) {
            try (PrintWriter pr = new PrintWriter(new FileWriter(selected))) {
                System.out.println(printStudentList());

                pr.write(printStudentList());
                pr.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }


    @FXML
    public void createPieChartList() {
        // populate the observable hashmap
        for (Student student : studentList) {

            String key = student.getMajor();
            if (studentDataMap.containsKey(key)) {
                // if already exists, increase the count on that index
                studentDataMap.put(key, studentDataMap.get(key) + 1);
            } else {
                studentDataMap.put(key, 1);
            }
        }

        // and then make a list of PieChart.Data
        for (String key : studentDataMap.keySet()) {
            // need to convert into percentage
            int total = studentList.size();
            double fraction = ((double) studentDataMap.get(key) / (double) total) * 100.0;
//            System.out.println("Percentage of " + key + " is " + percentage);
            studentDataList.add(new PieChart.Data(key, fraction));
        }
//        System.out.println("The size of studentDataList is: " + studentDataList.size());


        PieChart pieChart = new PieChart();
        pieChart.setData(studentDataList);
        pieChart.setTitle("Major Distributions");
        pieChart.setLegendSide(Side.LEFT);
        pieChart.setMaxWidth(600);


        studentDataList.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " ", studentDataMap.get(data.getName()), " ", String.valueOf(data.pieValueProperty().doubleValue()).substring(0, 3), "%"
                        )
                ));


        chartView.getChildren().add(pieChart);
    }

    // bar graph
    @FXML
    public void creatingBarGraph() {
        // populate the observable hashmap
        for (Student student : studentList) {

            String key = student.getCurrentGrade();
            System.out.println("Student grade is " + key);
            if (studentGradeDataMap.containsKey(key)) {
                // if already exists, increase the count on that index
                studentGradeDataMap.put(key, studentGradeDataMap.get(key) + 1);
            } else {
                studentGradeDataMap.put(key, 1);
            }
        }
        System.out.println("The size of the map is: " + studentGradeDataMap.size());
        System.out.println("The size of the keyset is " + studentGradeDataMap.keySet().size());

        // and then make a list of PieChart.Data
        for (String key : studentGradeDataMap.keySet()) {
            int frequency = studentGradeDataMap.get(key);
            System.out.println("The frequency of " + key + " is: " + frequency);
            studentGradeDataList.add(new XYChart.Data<>(key, frequency));
        }
        System.out.println("StudentGradeDataList size is :" + studentGradeDataList.size());


        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Grade");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frequency");

        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);

        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setData(studentGradeDataList);
        dataSeries1.setName("Student Grade Distribution");

        barChart.getData().addAll(dataSeries1);
        barChart.setTitle("Grade Distribution");
        barChart.setMaxWidth(400);

        chartView.getChildren().add(barChart);
    }


}
