package com.yingtai.dock;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Explorer extends Icon{
    public Explorer() throws FileNotFoundException {
        super();
        tag="仿达";

        Image image =new Image(this.getClass().getResourceAsStream("img/Explorer.png"));
        ImageView imageView=new ImageView(image);
        group.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(Parament.iconEnlargedWidth);
        imageView.fitHeightProperty().bind(Parament.iconEnlargedWidth);


//        region 设置图标按压时变暗特性
        int width=(int)image.getWidth();
        int height=(int)image.getHeight();
        WritableImage writableImageImage=new WritableImage(width, height);
        PixelReader pixelReader=image.getPixelReader();
        PixelWriter pixelWriter=writableImageImage.getPixelWriter();
        for(int y=0;y<height;y++) {
            for(int x=0;x<width;x++) {
                Color color=pixelReader.getColor(x,y);
                pixelWriter.setColor(x,y,color.darker());
            }
        }
        imageView.setOnMousePressed(mouseEvent -> {
            imageView.setImage(writableImageImage);
        });
        imageView.setOnMouseReleased(mouseEvent -> {
            imageView.setImage(image);
        });

        imageView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton()== MouseButton.PRIMARY){
                new Thread(()->{
                    run();
                    System.out.println("程序已打开");
                }).run();
            }
        });
        //setRightButtonMenu
        root.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                ContextMenu contextMenu=new ContextMenu();
                MenuItem menuItem1 = new MenuItem("新建仿达窗口");
                MenuItem menuItem2 = new MenuItem("前往文件夹");
                MenuItem menuItem3 = new MenuItem("打开开始菜单");
                menuItem1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        new Thread(()->{
                            run();
                            System.out.println("程序已打开");
                        }).run();
                    }
                });
                menuItem2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        new Thread(()->{
                            openRunner();
                            System.out.println("前往文件夹");
                        }).run();
                    }
                });
                menuItem3.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Robot robot=new Robot();
                        robot.keyPress(KeyCode.WINDOWS);
                        robot.keyRelease(KeyCode.WINDOWS);
                    }
                });

                contextMenu.getItems().addAll(menuItem1,menuItem2,menuItem3);
                contextMenu.setAutoFix(true);
                contextMenu.setStyle("-fx-background-radius: 0.5em;");

                Node node = contextMenuEvent.getPickResult().getIntersectedNode();

                contextMenu.show(node, javafx.geometry.Side.BOTTOM, 0, 0);
            }
        });

        tempImageView=imageView;
        tempImage=image;
    }

    @Override
    public Node getNode() {
        return root;
    }

    public void run(){//与其他图标不同，可以通过新建仿达窗口打开多个explorer
        try {
            new ProcessBuilder("explorer").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openRunner(){//打开“运行”
//        Robot robot=new Robot();
//        robot.keyPress(KeyCode.WINDOWS);//模拟按下Win R
//        robot.keyPress(KeyCode.R);
//        robot.keyRelease(KeyCode.R);//释放Win R
//        robot.keyRelease(KeyCode.WINDOWS);
        try {
            new ProcessBuilder("config/tool/run.exe").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

