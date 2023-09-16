package com.yingtai.dock;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.image.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private long lastTime;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage=primaryStage;
        Separator.setApp(this);
        Platform.setImplicitExit(false);

        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        //得到Dock栏的StackPane
        Dock dock = new Dock();
        StackPane dockBar = dock.getRoot();
        root.setAlignment(Pos.BOTTOM_CENTER);
        root.getChildren().add(dockBar);

        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("img/本软件图标.png")));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                close();
            }
        });
        primaryStage.requestFocus();
        primaryStage.show();
//        root.setPadding(new Insets(0,50,0,50));
        Config.readSettingConfig();
        dock.setDockItem(Config.readIconConfig());


        primaryStage.setResizable(false);
        primaryStage.setWidth(dockBar.widthProperty().get());
        primaryStage.setHeight(350);
        primaryStage.setX(Screen.getPrimary().getBounds().getWidth() / 2 - primaryStage.getWidth() / 2);
        primaryStage.setY(Screen.getPrimary().getBounds().getHeight() - primaryStage.getHeight() - Parament.dockToBottom.doubleValue());
        dockBar.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                primaryStage.setWidth(t1.doubleValue()+100);
                primaryStage.setX(Screen.getPrimary().getBounds().getWidth() / 2 -primaryStage.getWidth()/ 2);
            }
        });
        Parament.dockToBottom.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                primaryStage.setY(Screen.getPrimary().getBounds().getHeight() - primaryStage.getHeight() - Parament.dockToBottom.doubleValue());
            }
        });
//        Parament.iconEnlargedWidth.addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                primaryStage.setHeight(Parament.iconEnlargedWidth.get() + 50);
//                primaryStage.setY(Screen.getPrimary().getBounds().getHeight() - primaryStage.getHeight() - Parament.dockToBottom.doubleValue());
//            }
//        });



        //region dock栏的自动隐藏
        lastTime=System.currentTimeMillis();
        new Thread(()->{
            scene.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    lastTime=System.currentTimeMillis();
                }
            });
            while(true){
                if(Parament.isHidable){
                    Point point=MouseInfo.getPointerInfo().getLocation();
                    if(System.currentTimeMillis()-lastTime>Parament.timeBeforeHiding.get()){
                        setAutoHide(primaryStage,false);
                    }
                    if(point.getY()>Screen.getPrimary().getBounds().getHeight()-10){
                        setAutoHide(primaryStage,true);
                        lastTime=System.currentTimeMillis();
                    }
                }
                else {
                    lastTime=System.currentTimeMillis();
                    primaryStage.setY(Screen.getPrimary().getBounds().getHeight() - primaryStage.getHeight() - Parament.dockToBottom.doubleValue());
                }

                //region    自动变黑
                if(Parament.isSetAutoDark.get()){
                    if(LocalTime.now().getHour()>=21||LocalTime.now().getHour()<=7){
                        Parament.glassColor.set(Parament.darkColor);
                    }
                    else {
                        Parament.glassColor.set(Parament.lightColor);
                    }
                }
                //endregion

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        //endregion

        //region 系统托盘 效果未达到预期目标已弃用
//        Platform.setImplicitExit(false);
//        SystemTray systemTray=SystemTray.getSystemTray();
//        java.awt.Image image=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("img/本软件图标20.png"));
//        String str="DockByJavaFX";
//        PopupMenu popupMenu=new PopupMenu();
//        MenuItem item1=new MenuItem("最小化程序");
//        MenuItem item2=new MenuItem("结束程序");
//        popupMenu.add(item1);
//        popupMenu.add(item2);
//        TrayIcon trayIcon=new TrayIcon(image,str,popupMenu);
//        try {
//            systemTray.add(trayIcon);
//        } catch (AWTException e) {
//            throw new RuntimeException(e);
//        }
//        item1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Platform.runLater(()->{
//                    primaryStage.hide();
//                });
//            }
//        });
//        item2.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Config.renewSettingConfig();
//                Platform.runLater(()->{
//                    primaryStage.close();
//                });
//                System.exit(0);
//            }
//        });
        //endregion
    }

    public void setAutoHide(Stage stage,boolean isHided){
        if(stage==null){
            return;
        }
        if(!isHided){
            DoubleProperty stageY=new SimpleDoubleProperty(stage.getY());
            Timeline timeline=new Timeline(new KeyFrame(Duration.seconds(1),new KeyValue(stageY,Screen.getPrimary().getBounds().getHeight())));
            stageY.addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    stage.setY(stageY.get());
                }
            });
            timeline.play();
        }
        else{
            DoubleProperty stageY=new SimpleDoubleProperty(stage.getY());
            Timeline timeline=new Timeline(new KeyFrame(Duration.seconds(1),
                    new KeyValue(stageY, Screen.getPrimary().getBounds().getHeight()-Parament.dockToBottom.get()-stage.getHeight())));
            stageY.addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    stage.setY(stageY.get());
                }
            });
            timeline.play();
        }
    }

    public void close(){
        Config.renewIconConfig(Dock.dockItemList);
        Config.renewSettingConfig();
        primaryStage.close();
        System.exit(0);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }
}
