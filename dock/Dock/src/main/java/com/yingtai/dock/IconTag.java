package com.yingtai.dock;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Shadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class IconTag {
    private Group root;
    private Label label;

    public IconTag() {
        label = new Label();
        label.setFont(Font.font("微软雅黑", FontWeight.MEDIUM,Parament.tipFontSize));
        Color color=Parament.glassColor.get();
        int r,g,b;
        if(color.getRed()*255<128) r=255;
        else r=0;
        if(color.getBlue()*255<128) b=255;
        else b=0;
        if(color.getGreen()*255<128) g=255;
        else g=0;
        label.setTextFill(Color.rgb(r,g,b));
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
                label.setTextFill(Color.rgb(r,g,b));
            }
        });

        root = new Group(new Group(), label);

        ObjectBinding<Node> graphicBinding = Bindings.createObjectBinding(() -> {
            Rectangle rectangle = new Rectangle(label.getWidth() + 20, label.getHeight() + 10);
            rectangle.setArcWidth(10);
            rectangle.setArcHeight(10);

            Polygon polygon = new Polygon(0, 0, 16, 0, 8, 8);
            polygon.setTranslateX(rectangle.getWidth() / 2 - 8);
            polygon.setTranslateY(rectangle.getHeight());

            Shape shape = Shape.union(rectangle, polygon);
            shape.fillProperty().bind(Parament.glassColor);
            shape.setStroke(Color.WHITE);
            shape.setStrokeWidth(1);
            shape.setTranslateX(-10);
            shape.setTranslateY(-3);

            return shape;
        }, label.widthProperty(), label.heightProperty());

//        root.setEffect(new Shadow());

        graphicBinding.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                root.getChildren().set(0,graphicBinding.get());
            }
        });

        label.setUserData(graphicBinding);
    }

    public Node getNode(){
        return root;
    }

    public void setTag(String tag) {
        label.setText(tag);
    }
}
