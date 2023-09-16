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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrashCan extends Icon {

    public TrashCan() throws FileNotFoundException {
        super();
        tag = "废纸篓";

        Image image =new Image(this.getClass().getResourceAsStream("img/回收站.png"));
        ImageView imageView=new ImageView(image);
        group.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(Parament.iconEnlargedWidth);
        imageView.fitHeightProperty().bind(Parament.iconEnlargedWidth);


        //region 设置图标按压时变暗特性
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
                }).run();
            }
        });
        //setRightButtonMenu
        root.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                ContextMenu contextMenu=new ContextMenu();
                MenuItem menuItem1 = new MenuItem("打开回收站");
                MenuItem menuItemSep_=new SeparatorMenuItem();
                MenuItem menuItem2 = new MenuItem("添加分隔符");
                MenuItem menuItemSep=new SeparatorMenuItem();
                MenuItem menuItem3 = new MenuItem("倾倒废纸篓");
                menuItem1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        run();
                    }
                });
                menuItem2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Dock.addIcon(-1,new Separator());
                    }
                });
                menuItem3.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        cleanUp();
                    }
                });

                contextMenu.getItems().addAll(menuItem1,menuItemSep_,menuItem2,menuItemSep,menuItem3);
                contextMenu.setAutoFix(true);
                contextMenu.setStyle("-fx-background-radius: 0.5em;");

                Node node = contextMenuEvent.getPickResult().getIntersectedNode();

                contextMenu.show(node, javafx.geometry.Side.BOTTOM, 0, 0);
            }
        });

        tempImageView=imageView;
        tempImage=image;
    }

    public void run() {//打开回收站
        try {
            List<String> list = new ArrayList<String>();
            list.add("cmd.exe");
            list.add("/c");
            list.add("explorer.exe ::{645FF040-5081-101B-9F08-00AA002F954E}");
            ProcessBuilder pBuilder = new ProcessBuilder(list);
            pBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanUp() {//清空回收站，有一点瑕疵（会有弹窗出现）
        try {
            new ProcessBuilder("config/tool/clean.exe").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}