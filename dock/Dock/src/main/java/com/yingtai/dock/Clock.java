package com.yingtai.dock;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Clock extends Icon {
    public Clock() {
        super();

        Rectangle rectangle = new Rectangle();
        rectangle.setArcHeight(60);
        rectangle.setArcWidth(60);
        rectangle.widthProperty().bind(Parament.iconEnlargedWidth);
        rectangle.heightProperty().bind(Parament.iconEnlargedWidth);
        rectangle.setFill(Color.GRAY);

        Circle circle = new Circle();
        circle.centerXProperty().bind(Parament.iconEnlargedWidth.divide(2));
        circle.centerYProperty().bind(Parament.iconEnlargedWidth.divide(2));
        circle.radiusProperty().bind(Parament.iconEnlargedWidth.multiply(0.4));
        circle.setFill(Color.WHITE);

//        BorderPane borderPane=new BorderPane();
//        String str = "1369";
//        double temp = (2*Math.PI)/str.length();
//        for (int i = 0; i < str.length(); i++) {
//            double x = circle.getCenterX()+circle.getRadius()*Math.cos(temp*i);
//            double y = circle.getCenterY()+circle.getRadius()*Math.sin(temp*i);
//            Text t = new Text(x,y,str.charAt(i)+"");
//            t.setFont(Font.font(20));
//            borderPane.getChildren().add(t);
//        }
//        StackPane.setAlignment(borderPane, Pos.CENTER);


        Line lineSecond = new Line();

        lineSecond.setStrokeWidth(2);
        lineSecond.startXProperty().bind(Parament.iconEnlargedWidth.divide(2));
        lineSecond.startYProperty().bind(Parament.iconEnlargedWidth.divide(2));
        Line lineMinute = new Line();
        lineMinute.setStrokeWidth(2.5);

        lineMinute.startXProperty().bind(Parament.iconEnlargedWidth.divide(2));
        lineMinute.startYProperty().bind(Parament.iconEnlargedWidth.divide(2));
        Line lineHour = new Line();

        lineHour.setStrokeWidth(3);
        lineHour.startXProperty().bind(Parament.iconEnlargedWidth.divide(2));
        lineHour.startYProperty().bind(Parament.iconEnlargedWidth.divide(2));

        new Thread(() -> {
            while (true) {
                switch (Parament.dateModel.get()){
                    case 0:
                        tag = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日EEEE"));
                        break;
                    case 1:
                        tag = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
                        break;
                    case 2:
                        tag = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
                        break;
                    case 3:
                        tag = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                        break;
                }
                LocalTime localTime = LocalTime.now();
                double secondLength = Parament.iconEnlargedWidth.get() * 0.35;
                double minuteLength = Parament.iconEnlargedWidth.get() * 0.3;
                double hourLength = Parament.iconEnlargedWidth.get() * 0.25;
                lineSecond.setEndX(Parament.iconEnlargedWidth.get() / 2 + secondLength * Math.sin(6 * localTime.getSecond() / 180.0 * Math.PI));
                lineSecond.setEndY(Parament.iconEnlargedWidth.get() / 2 - secondLength * Math.cos(6 * localTime.getSecond() / 180.0 * Math.PI));
                lineMinute.setEndX(Parament.iconEnlargedWidth.get() / 2 + minuteLength * Math.sin(6 * localTime.getMinute() / 180.0 * Math.PI));
                lineMinute.setEndY(Parament.iconEnlargedWidth.get() / 2 - minuteLength * Math.cos(6 * localTime.getMinute() / 180.0 * Math.PI));
                lineHour.setEndX(Parament.iconEnlargedWidth.get() / 2 + hourLength * Math.sin(30 * localTime.getHour() / 180.0 * Math.PI));
                lineHour.setEndY(Parament.iconEnlargedWidth.get() / 2 - hourLength * Math.cos(30 * localTime.getHour() / 180.0 * Math.PI));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

//        group.getChildren().addAll(rectangle, circle,lineSecond, lineMinute, lineHour);

        ImageView imageView=new ImageView(new Image(this.getClass().getResourceAsStream("img/表盘.png")));
        imageView.setSmooth(true);
        imageView.fitWidthProperty().bind(Parament.iconEnlargedWidth);
        imageView.fitHeightProperty().bind(Parament.iconEnlargedWidth);

        lineSecond.setStroke(Color.RED);
        lineMinute.setStroke(Color.YELLOW);
        lineHour.setStroke(Color.GOLD);

        group.getChildren().addAll(imageView,lineSecond, lineMinute, lineHour);
        root.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                ContextMenu contextMenu=new ContextMenu();
                MenuItem menuItem1 = new MenuItem("默认表盘");
                MenuItem menuItem2 = new MenuItem("北理表盘");
                menuItem1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        lineSecond.setStroke(Color.RED);
                        lineMinute.setStroke(Color.GREEN);
                        lineHour.setStroke(Color.GREY);
                        group.getChildren().clear();
                        group.getChildren().addAll(rectangle, circle,lineSecond, lineMinute, lineHour);
                    }
                });

                menuItem2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        lineSecond.setStroke(Color.RED);
                        lineMinute.setStroke(Color.YELLOW);
                        lineHour.setStroke(Color.GOLD);
                        group.getChildren().clear();
                        group.getChildren().addAll(imageView,lineSecond, lineMinute, lineHour);
                    }
                });

                contextMenu.getItems().addAll(menuItem1,menuItem2);
                contextMenu.setAutoFix(true);
                contextMenu.setStyle("-fx-background-radius: 0.5em;");

                Node node = contextMenuEvent.getPickResult().getIntersectedNode();

                contextMenu.show(node, javafx.geometry.Side.BOTTOM, 0, 0);
            }
        });
    }
}
