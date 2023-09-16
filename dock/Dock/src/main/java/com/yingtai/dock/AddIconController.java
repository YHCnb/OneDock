package com.yingtai.dock;

import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Path;

public class AddIconController {

    public StackPane root;
    public HBox hBox;
    public VBox leftVBox;
    public ImageView imageView;
    public VBox rightVBox;
    public HBox rightHBox1;
    public Label rightLabel1;
    public TextField rightTextField1;
    public HBox rightHBox2;
    public Label rightLabel2;
    public TextField rightTextField2;
    public Button rightButton2;
    public HBox rightHBox3;
    public Button rightButton31;
    public Button rightButton32;
    public Label leftLabel;
    public Stage stage;
    private File iconFile;
    private File pathFile;
    private double offsetX;
    private double offsetY;

    public AddIconController() throws IOException {
        iconFile=new File(this.getClass().getResource("img/默认应用图标.png").toString());
    }

    public void setStyle() throws FileNotFoundException {
//        root.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                offsetX= mouseEvent.getScreenX()-stage.getX();
//                offsetY= mouseEvent.getScreenY()-stage.getY();
//            }
//        });
//        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                stage.setX(mouseEvent.getScreenX()-offsetX);
//                stage.setY(mouseEvent.getScreenY()-offsetY);
//            }
//        });
//        Robot robot=new Robot();
//        WritableImage writableImage=robot.getScreenCapture(null,new Rectangle2D(0,0,
//                Screen.getPrimary().getBounds().getWidth(),Screen.getPrimary().getBounds().getHeight()));
//        ImageView blur=new ImageView(writableImage);
//        blur.setFitWidth(600);
//        blur.setFitHeight(300);
//        blur.setEffect(new BoxBlur(20,20,3));
//        hBox.setAlignment(Pos.CENTER);
//        leftVBox.setAlignment(Pos.CENTER);
//        rightVBox.setAlignment(Pos.CENTER);
//        hBox.setBackground(new Background(new BackgroundFill(Color.BLUE,null,null)));
//        animationTimer=new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                if(!stage.isFocused()){
//                    var writableImage2=robot.getScreenCapture(null,new Rectangle2D(0,0,
//                            Screen.getPrimary().getBounds().getWidth(),Screen.getPrimary().getBounds().getHeight()));
//                    blur.setImage(writableImage2);
//                    System.out.println("**************");
//                }
//                System.out.println(stage.getX()+","+stage.getY());
//
//                blur.setViewport(new Rectangle2D(stage.getX(),stage.getY(),600,300));
//            }
//        };
//        animationTimer.start();

//        root.getChildren().add(0,blur);
//        hBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));

        Color color=Parament.glassColor.get();
        int r,g,b;
        if(color.getRed()*255<128) r=255;
        else r=0;
        if(color.getBlue()*255<128) b=255;
        else b=0;
        if(color.getGreen()*255<128) g=255;
        else g=0;
//        System.out.println(color.getRed()+" "+color.getGreen()+" "+color.getBlue());
        leftLabel.setTextFill(Color.rgb(r,g,b));
        rightLabel1.setTextFill(Color.rgb(r,g,b));
        rightLabel2.setTextFill(Color.rgb(r,g,b));
        root.setBackground(new Background(new BackgroundFill(Parament.glassColor.get(),new CornerRadii(10),null)));
        root.setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(1))));
        Parament.glassColor.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Color color=Parament.glassColor.get();
                int r,g,b;
                if(color.getRed()*255<128) r=255;
                else r=0;
                if(color.getBlue()*255<128) b=255;
                else b=0;
                if(color.getGreen()*255<128) g=255;
                else g=0;
                System.out.println(color.getRed()+" "+color.getGreen()+" "+color.getBlue());
                leftLabel.setTextFill(Color.rgb(r,g,b));
                rightLabel1.setTextFill(Color.rgb(r,g,b));
                rightLabel2.setTextFill(Color.rgb(r,g,b));
                root.setBackground(new Background(new BackgroundFill(Parament.glassColor.get(),new CornerRadii(10),null)));
            }
        });

        //region 设置页面顶部控件
        HBox top=new HBox();
        top.setMaxWidth(600);
        top.setMaxHeight(30);
        root.getChildren().add(top);

        Circle circleMin=new Circle(10);
        circleMin.setFill(Color.YELLOW);
        Line lineMin=new Line();
        lineMin.setStartX(-7);
        lineMin.setStartY(0);
        lineMin.setEndX(7);
        lineMin.setEndY(0);
        Group groupMin=new Group(circleMin,lineMin);
        groupMin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                Parament.isHidable=true;
                stage.setIconified(true);
            }
        });
        Circle circleExit=new Circle(10);
        circleExit.setFill(Color.RED);
        Line lineExit1=new Line();
        lineExit1.setStartX(-4.95);
        lineExit1.setStartY(-4.95);
        lineExit1.setEndX(4.95);
        lineExit1.setEndY(4.95);
        Line lineExit2=new Line();
        lineExit2.setStartX(4.95);
        lineExit2.setStartY(-4.95);
        lineExit2.setEndX(-4.95);
        lineExit2.setEndY(4.95);
        Group groupExit=new Group(circleExit,lineExit1,lineExit2);
        groupExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Parament.isHidable=true;
                stage.close();
            }
        });
        top.setSpacing(20);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setBackground(new Background(new BackgroundFill(Color.rgb(40,40,40,0.8),new CornerRadii(10,10,0,0,false),null)));
        top.getChildren().addAll(new Group(groupExit),new Group(groupMin));
        top.setPadding(new Insets(0,0,0,20));
        StackPane.setAlignment(top,Pos.TOP_CENTER);
        top.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                offsetX= mouseEvent.getScreenX()- stage.getX();
                offsetY= mouseEvent.getScreenY()- stage.getY();
            }
        });

        top.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX()-offsetX);
                stage.setY(mouseEvent.getScreenY()-offsetY);
            }
        });
        //endregion

        leftVBox.setPadding(new Insets(50,50,0,50));
        leftLabel.setAlignment(Pos.CENTER);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        imageView.setImage(new Image(this.getClass().getResourceAsStream("img/默认应用图标.png")));
        leftVBox.setAlignment(Pos.CENTER);
        leftLabel.setFont(Font.font("微软雅黑", FontWeight.MEDIUM,12));
        leftVBox.setSpacing(5);
        rightVBox.setPadding(new Insets(50,10,0,10));
        rightVBox.setAlignment(Pos.CENTER);
        rightHBox1.setSpacing(10);
        rightHBox1.setAlignment(Pos.CENTER_LEFT);
        rightHBox2.setSpacing(10);
        rightHBox2.setAlignment(Pos.CENTER_LEFT);
        rightHBox3.setSpacing(20);
        rightHBox3.setAlignment(Pos.CENTER);
//        rightLabel1.setAlignment(Pos.CENTER);
//        rightLabel2.setAlignment(Pos.CENTER);
        rightLabel2.setFont(Font.font("微软雅黑", FontWeight.MEDIUM,13));
        rightLabel1.setFont(Font.font("微软雅黑", FontWeight.MEDIUM,13));


        for(var node:rightHBox3.getChildren()){
            if(node instanceof Button){
                ((Button)node).setTextFill(Color.WHITE);
                ((Button)node).setPrefWidth(100);
                ((Button)node).setPrefHeight(30);
                ((Button)node).setFont(Font.font("微软雅黑",FontWeight.MEDIUM,18));
                ((Button)node).setBackground(new Background(new BackgroundFill(Color.rgb(11,114,231),new CornerRadii(10),null)));
                ((Button)node).setAlignment(Pos.CENTER);
                node.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        ((Button)node).setBackground(new Background(new BackgroundFill(Color.rgb(19,91,157),new CornerRadii(10),null)));
                    }
                });
                node.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        ((Button)node).setBackground(new Background(new BackgroundFill(Color.rgb(11,114,231),new CornerRadii(10),null)));
                    }
                });
            }
        }
    }

    public void setEvent(){
        root.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != root
                        && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        root.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                var db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
//                    dropped.setText(db.getFiles().toString());
                    pathToIcon(db.getFiles().get(0));
                    success = true;
                }

                event.setDropCompleted(success);

                event.consume();
            }
        });


        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseEntered(mouseEvent -> {
            Effect effect=new Glow();
            imageView.setEffect(effect);
        });
        imageView.setOnMouseExited(mouseEvent -> {
            imageView.setEffect(null);
        });
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fileChooser=new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Images", "*.*"),
                        new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        new FileChooser.ExtensionFilter("GIF", "*.gif"),
                        new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                fileChooser.setTitle("选择图片");
                iconFile=fileChooser.showOpenDialog(stage);
                try {
                    if(iconFile!=null) imageView.setImage(new Image(new FileInputStream(iconFile)));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        rightButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser=new FileChooser();
                DirectoryChooser directoryChooser=new DirectoryChooser();
                fileChooser.setTitle("选择文件");
                pathFile= fileChooser.showOpenDialog(stage);
                pathToIcon(pathFile);
            }
        });



        rightButton31.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        rightButton32.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String tag=rightTextField1.getText();
                String realPath=rightTextField2.getText();
                if(iconFile!=null&&tag!=null&&tag.length()!=0&&realPath!=null&&realPath.length()!=0)
                    Dock.addIcon(new Icon(rightTextField1.getText(),rightTextField2.getText(),imageView.getImage()));
                stage.close();
            }
        });
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }

    private void pathToIcon(File pathFile){
        if(pathFile!=null){
            if(pathFile.isFile()){
                rightTextField1.setText(pathFile.getName().substring(0,pathFile.getName().length()-4));
            }
            else{
                rightTextField1.setText(pathFile.getName());
            }
            if(pathFile.getPath().substring(pathFile.getPath().length()-4,pathFile.getPath().length()).equals(".lnk")){
                rightTextField2.setText(IconImage.lnkToRealPath(pathFile.getAbsolutePath()));
                pathFile=new File(IconImage.lnkToRealPath(pathFile.getAbsolutePath()));
            }
            else{
                rightTextField2.setText(pathFile.getAbsolutePath());
            }

            Image newImage=IconImage.getIconImage(pathFile.getAbsolutePath());
            if(newImage.isError()){
                imageView.setImage(new Image(this.getClass().getResourceAsStream("img/默认应用图标.png")));
            }
            else imageView.setImage(IconImage.getIconImage(pathFile.getAbsolutePath()));
        }
    }
}
