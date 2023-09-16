package com.yingtai.dock;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LaunchPad extends Icon {
    private Stage stage;

    public LaunchPad() throws FileNotFoundException {
        super();
        tag = "启动台";
        ImageView imageView = new ImageView(new Image(new FileInputStream(new File("Dock/src/main/resources/com/yingtai/dock/img/启动台.png"))));
        group.getChildren().add(imageView);
        imageView.fitWidthProperty().bind(Parament.iconWidth.multiply(Parament.iconEnlargeScale));
        imageView.fitHeightProperty().bind(Parament.iconWidth.multiply(Parament.iconEnlargeScale));

        showLaunchPadInit();
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (isOpenedAndShown.get()) {
                        stage.close();
                        isOpenedAndShown.set(false);
                    } else {
                        stage.show();
                        isOpenedAndShown.set(true);
                    }
                }
            }
        });
    }

    private void showLaunchPadInit() throws FileNotFoundException {
        FlowPane flowPane = new FlowPane();
        File file = new File("../launchpad");
        if (!file.exists()) {
            file.mkdirs();
        } else {
            File[] icons = file.listFiles();
            for (File icon : icons) {
                ImageView imageView = new ImageView(new Image(new FileInputStream(icon)));
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);

                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(imageView);
                stackPane.setAlignment(Pos.CENTER);
                stackPane.setPadding(new Insets(10, 10, 10, 10));
                stackPane.setAccessibleText(icon.getName());

                flowPane.getChildren().add(stackPane);
            }
        }
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setHgap(50);
        flowPane.setVgap(50);
        flowPane.setPadding(new Insets(50, 50, 50, 50));
        Scene scene = new Scene(flowPane);
        scene.setFill(Color.TRANSPARENT);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("启动台");
        stage.setAlwaysOnTop(true);
        stage.setWidth(Screen.getPrimary().getBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getBounds().getHeight());
        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.setFullScreenExitHint(null);
        stage.setResizable(false);
    }
}
