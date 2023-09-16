package com.yingtai.dock;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SettingController {
    @FXML
    public HBox hBox;
    @FXML
    public VBox leftVBox;
    public VBox rightVbox;
    public Button button3;
    public Button button4;
    public Button button5;
    public ScrollPane rightScrollPane;
    public StackPane root;

    private double offsetX;
    private double offsetY;
    private Stage stage;

    public void setStyle(){
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

        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        hBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));

        rightScrollPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        rightScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        leftVBox.setBackground(new Background(new BackgroundFill(Parament.glassColor.get(),new CornerRadii(10,0,0,10,false),null)));
        leftVBox.setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,new CornerRadii(10,0,0,10,false),new BorderWidths(1,0,1,1))));

        Parament.glassColor.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                leftVBox.setBackground(new Background(new BackgroundFill(Parament.glassColor.get(),new CornerRadii(10,0,0,10,false),null)));
            }
        });

        rightScrollPane.setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,new CornerRadii(0,10,10,0,false),new BorderWidths(1,1,1,0))));
        rightScrollPane.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(0,10,10,0,false),null)));

        leftVBox.setPadding(new Insets(50,20,0,20));
        leftVBox.getChildren().forEach((node)->{
            if(node instanceof Button){
                setButtonsStyle((Button)node);
            }
        });

        rightVbox.setPadding(new Insets(50,50,50,50));
        rightVbox.setAlignment(Pos.TOP_LEFT);
        rightVbox.setSpacing(20);
        rightVbox.setPrefWidth(400);
    }
    public void setButtonsStyle(Button button){
        button.setPrefWidth(160);
        button.setPrefHeight(40);
        button.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        Color color=Parament.glassColor.get();
        int r,g,b;
        if(color.getRed()*255<128) r=255;
        else r=0;
        if(color.getBlue()*255<128) b=255;
        else b=0;
        if(color.getGreen()*255<128) g=255;
        else g=0;
        button.setTextFill(Color.rgb(r,g,b));
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
                button.setTextFill(Color.rgb(r,g,b));
            }
        });

        button.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
        button.setAlignment(Pos.CENTER_LEFT);
        button.setOnMousePressed(actionEvent -> {
            leftVBox.getChildren().forEach((btn)->{
                if(btn instanceof Button){
                    ((Button) btn).setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null,null)));
                }
            });
            if(Parament.glassColor.get().getRed()*255<10&&Parament.glassColor.get().getBlue()*255<10&&Parament.glassColor.get().getGreen()*255<10){
                button.setBackground(new Background(new BackgroundFill(Color.GREY,new CornerRadii(5),null)));
            }
            else
                button.setBackground(new Background(new BackgroundFill(Parament.glassColor.get().darker(),new CornerRadii(5),null)));
        });

    }

//    private void setLabelsStyle(Label label){
//        label.setAlignment(Pos.CENTER_LEFT);
//        label.setTextFill(Color.rgb(50,50,50,0.5));
//        label.setFont(Font.font("微软雅黑",12));
//        label.setPrefHeight(40);
//    }

    @FXML
    private void showButton1Content(){
        Rectangle rectangle=new Rectangle();
        rectangle.setArcHeight(25);
        rectangle.setArcWidth(25);
        rectangle.setWidth(300);
        rectangle.setHeight(100);
        rectangle.fillProperty().bind(Parament.glassColor);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);

        Button buttonLight=new Button("浅色模式");
        buttonLight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parament.glassColor.set(Parament.lightColor);
            }
        });
        Button buttonDark=new Button("深色模式");
        buttonDark.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parament.glassColor.set(Parament.darkColor);
            }
        });
        buttonLight.setBackground(new Background(new BackgroundFill(Parament.lightColor,new CornerRadii(13),null)));
        buttonDark.setBackground(new Background(new BackgroundFill(Parament.darkColor,new CornerRadii(13),null)));
        buttonDark.setPrefWidth(100);
        buttonLight.setPrefWidth(100);
        buttonDark.setPrefHeight(40);
        buttonLight.setPrefHeight(40);
        buttonLight.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        buttonDark.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        buttonDark.setTextFill(Color.WHITE);
        if(Parament.glassColor.get()==Parament.lightColor){
            buttonLight.setBorder(new Border(new BorderStroke(Color.GREENYELLOW,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(2),null)));
            buttonDark.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(2),null)));
        }
        else if(Parament.glassColor.get()==Parament.darkColor){
            buttonLight.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(2),null)));
            buttonDark.setBorder(new Border(new BorderStroke(Color.GREENYELLOW,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(2),null)));
        }
        Parament.glassColor.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(Parament.glassColor.get()==Parament.lightColor){
                    buttonLight.setBorder(new Border(new BorderStroke(Color.GREENYELLOW,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(2),null)));
                    buttonDark.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(2),null)));
                }
                else if(Parament.glassColor.get()==Parament.darkColor){
                    buttonLight.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(2),null)));
                    buttonDark.setBorder(new Border(new BorderStroke(Color.GREENYELLOW,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(2),null)));
                }
            }
        });

        HBox hBox1=new HBox(buttonLight,buttonDark);
        hBox1.setSpacing(50);
        hBox1.setPadding(new Insets(10,25,10,25));

        CheckBox checkBox=new CheckBox();
        checkBox.setText("定时开启深色模式");
        checkBox.selectedProperty().bindBidirectional(Parament.isSetAutoDark);
        Tooltip tooltip=new Tooltip("晚上9点到早上7点为深色模式，其余时间为浅色模式");
        tooltip.setShowDelay(Duration.seconds(0.1));
        checkBox.setTooltip(tooltip);

        Label label=new Label("自定义外观");
        label.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        ColorPicker colorPicker=new ColorPicker();
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parament.glassColor.set(colorPicker.getValue());
            }
        });
        HBox hBox2=new HBox(label,colorPicker);
        hBox2.setSpacing(50);
        hBox2.setPadding(new Insets(10,0,10,0));

        rightVbox.getChildren().clear();
        rightVbox.getChildren().addAll(rectangle,hBox1,checkBox,hBox2);
    }

    @FXML
    private void showButton2Content(){
        Label label1=new Label("图标默认大小   "+(int)Parament.iconWidth.get());
        label1.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        Slider slider1=new Slider(32,256,Parament.iconWidth.get());
        slider1.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                label1.setText("图标默认大小   "+(int)slider1.getValue());
                if(Parament.iconEnlargedWidth.get()<slider1.getValue()){
                    Parament.iconEnlargedWidth.set(slider1.getValue());
                }
            }
        });
        slider1.setMajorTickUnit(32);//刻度间隙为32
        slider1.setMinorTickCount(1);//每次滑动为1
        slider1.setShowTickMarks(true);//出现刻度
        slider1.setShowTickLabels(true);//出现数字
        slider1.valueProperty().bindBidirectional(Parament.iconWidth);

        Label label2=new Label("图标放大后的大小   "+(int)Parament.iconEnlargedWidth.get());
        label2.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        Slider slider2=new Slider(32,256,Parament.iconEnlargedWidth.get());
        slider2.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(slider2.getValue()>=slider1.getValue()){
                    Parament.iconEnlargedWidth.setValue(slider2.getValue());
                    label2.setText("图标放大后的大小   "+(int)Parament.iconEnlargedWidth.get());
                }
            }
        });
        slider2.setMajorTickUnit(32);
        slider2.setMinorTickCount(1);
        slider2.setShowTickMarks(true);
        slider2.setShowTickLabels(true);

        Label label3=new Label("图标之间的距离   "+(int)Parament.iconSpacing.get());
        label3.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        Slider slider3=new Slider(0,20,Parament.iconSpacing.get());
        slider3.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                    Parament.iconSpacing.setValue(slider3.getValue());
                    label3.setText("图标之间的距离   "+(int)Parament.iconSpacing.get());
            }
        });
        slider3.setMajorTickUnit(5);
        slider3.setMinorTickCount(1);
        slider3.setShowTickMarks(true);
        slider3.setShowTickLabels(true);

        CheckBox checkBox1=new CheckBox();
        checkBox1.setText("开启鼠标滑过时动画");
        checkBox1.selectedProperty().bindBidirectional(Parament.isIconAnimation);

        CheckBox checkBox2=new CheckBox();
        checkBox2.setText("显示图标提示");
        checkBox2.selectedProperty().bindBidirectional(Parament.isIconTag);

        rightVbox.getChildren().clear();
        rightVbox.getChildren().addAll(label1,slider1,label2,slider2,label3,slider3,checkBox1,checkBox2);
    }

    public void showButton3Content() {
        Label label1=new Label("Dock栏距离屏幕底部距离   "+(int)Parament.dockToBottom.get());
        label1.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        Slider slider1=new Slider(0,200,Parament.dockToBottom.get());
        slider1.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                label1.setText("Dock栏距离屏幕底部距离   "+(int)slider1.getValue());
            }
        });
        slider1.setMajorTickUnit(50);
        slider1.setMinorTickCount(1);
        slider1.setShowTickMarks(true);
        slider1.setShowTickLabels(true);
        slider1.valueProperty().bindBidirectional(Parament.dockToBottom);

        Label label2=new Label("Dock栏圆角大小   "+(int)Parament.dockArcWidth.get());
        label2.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        Slider slider2=new Slider(0,50,Parament.dockArcWidth.get());
        slider2.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                label2.setText("Dock栏圆角大小   "+(int)slider2.getValue());
            }
        });
        slider2.setMajorTickUnit(10);
        slider2.setMinorTickCount(1);
        slider2.setShowTickMarks(true);
        slider2.setShowTickLabels(true);
        slider2.valueProperty().bindBidirectional(Parament.dockArcWidth);

        Label label3=new Label("自动隐藏所需无操作时间   "+(int)Parament.timeBeforeHiding.get()/1000+"s");
        label3.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        Slider slider3=new Slider(1,90,Parament.timeBeforeHiding.get()/1000);
        slider3.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                label3.setText("自动隐藏所需无操作时间   "+(int)slider3.getValue()+"s");
                Parament.timeBeforeHiding.set(slider3.getValue()*1000);
            }
        });
        slider3.setMajorTickUnit(10);
        slider3.setMinorTickCount(1);
        slider3.setShowTickMarks(true);
        slider3.setShowTickLabels(true);

        rightVbox.getChildren().clear();
        rightVbox.getChildren().addAll(label1,slider1,label2,slider2,label3,slider3);
    }

    public void showButton4Content(ActionEvent actionEvent) {
        Label label=new Label("设置日期格式");
        label.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        ChoiceBox choiceBox=new ChoiceBox();
        choiceBox.setItems(FXCollections.observableArrayList("2000年1月1日星期六","2000年1月1日","2000-1-1","2000/1/1"));
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Parament.dateModel.set(choiceBox.getSelectionModel().getSelectedIndex());
            }
        });

        rightVbox.getChildren().clear();
        rightVbox.getChildren().addAll(label,choiceBox);
    }

    public void showButton5Content() {
        Text text1=new Text();
        text1.setText("DockByJavaFX\n"+"作者：陈钰豪 王英泰\n"+"版本：1.0.0");
        text1.setLineSpacing(5);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));

        Button about1=new Button("关于软件");
        about1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Text text=new Text("这是一个旨在Windows上便携化管理电脑应用程序的Dock栏软件，灵感来源于Mac苹果Dock以及类似产品。\n"+"借助这个应用，你可以随意地对自己的常用软件进行管理、运行等操作，它将给你直观高颜值的应用界面，人性化的响应事件以及高度自由化的个人偏好设置。\n"+"由此，我们希望您在这里获得一个更加简洁的桌面，以及更加高效的办公体验，祝您体验愉快！");
                text.setWrappingWidth(218);
                text.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,13));
                showSmallStage(text,stage);
            }
        });
        Button about2=new Button("捐赠支持");
        about2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ImageView imageView=new ImageView(new Image(getClass().getResourceAsStream("img/二维码.jpg")));
                imageView.setFitHeight(230);
                imageView.setFitWidth(230);
                showSmallStage(imageView,stage);
            }
        });
        Button about3=new Button("访问Gitee");
        about3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI("https://gitee.com/wang-yingtai/Dock-JavaFX"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Button about4=new Button("特别鸣谢");
        about4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Text text=new Text("这里首先特别感谢金旭亮老师对于我们的很多无法解决的问题的悉心回答，也给出了很多有帮助的指导。\n"+
                        "同时像提取图标、ico格式文件转png格式文件、lnk文件真实地址获取等功能，我们参考和借鉴了网上部分项目才得以实现我们的功能。\n"+
                        "同时感谢”JavaFX图形界面群“QQ群中的群友对我的问题的积极回答和一些有帮助的建议，才让我们的项目更加的完善。\n"+
                        "最后再次对上述人员表示衷心的感谢！");
                text.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,13));
                text.setWrappingWidth(218);
                showSmallStage(text,stage);
            }
        });
        Text text2=new Text();
        text2.setText("本软件免费使用，禁止用于商业用途");
        text2.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));

        VBox vBox=new VBox(text1,about1,about2,about3,about4,text2);
        for(var node:vBox.getChildren()){
            if(node instanceof Button){
                ((Button)node).setTextFill(Color.WHITE);
                ((Button)node).setPrefWidth(250);
                ((Button)node).setPrefHeight(50);
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
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        rightVbox.getChildren().clear();
        rightVbox.getChildren().addAll(vBox);
    }

    private void showSmallStage(Node node,Stage parentStage){
        Button button=new Button("确定");
        button.setPrefWidth(230);
        button.setFont(Font.font("微软雅黑",FontWeight.MEDIUM,15));
        button.setTextFill(Color.WHITE);
        button.setAlignment(Pos.CENTER);
        button.setPrefHeight(40);
        button.setBackground(new Background(new BackgroundFill(Color.rgb(9,114,209,1),new CornerRadii(10),null)));
        button.setBorder(new Border(new BorderStroke(Color.WHITE,BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(0.5))));
        button.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setBackground(new Background(new BackgroundFill(Color.rgb(19,91,157,1),new CornerRadii(10),null)));
            }
        });
        button.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setBackground(new Background(new BackgroundFill(Color.rgb(9,114,209,1),new CornerRadii(10),null)));
            }
        });
        ScrollPane scrollPane=new ScrollPane(node);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        scrollPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,new CornerRadii(5),null)));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        BorderPane borderPane=new BorderPane();
        borderPane.setBottom(button);
        borderPane.setCenter(scrollPane);
        borderPane.setPrefWidth(250);
        borderPane.setPrefHeight(300);
        borderPane.setPadding(new Insets(10,10,10,10));
        borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(5),null)));
        borderPane.setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(1))));
        Scene scene=new Scene(borderPane);
        scene.setFill(Color.TRANSPARENT);
        Stage stage=new Stage();
        stage.setWidth(250);
        stage.setHeight(300);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(parentStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }
}
