package com.yingtai.dock;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public class Dock {
    private StackPane root;
    private static HBox container;
    private Rectangle glass;
    public static List<DockItem> dockItemList;

    public Dock(){
        root=new StackPane();
        root.setPadding(new Insets(0));
        root.setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
       // root.setBackground(new Background(new BackgroundFill(Color.rgb(12,12,12,0.5),null,null)));

        //region container是放置图标的容器
        container=new HBox();
        container.setAlignment(Pos.BOTTOM_CENTER);
        container.setMaxSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
       // container.setBackground(new Background(new BackgroundFill(Color.rgb(233,23,234,0.3),null,null)));
        container.setPadding(new Insets(0,0,0,0));
        //endregion

        //region 玻璃板
        glass=new Rectangle();
        glass.setX(root.getPadding().getLeft()-Parament.iconSpacing.get());
        glass.yProperty().bind(root.heightProperty().subtract(glass.heightProperty()).subtract(root.getPadding().getBottom()));
        glass.widthProperty().bind(container.widthProperty().add(Parament.iconSpacing.get()*2));
        glass.heightProperty().bind(Parament.iconWidth.add(Parament.iconSpacing.get()*2));
        glass.setManaged(false);
        glass.fillProperty().bind(Parament.glassColor);
        glass.arcWidthProperty().bind(Parament.dockArcWidth);
        glass.arcHeightProperty().bind(Parament.dockArcWidth);
        glass.setStroke(Color.WHITE);
        glass.setStrokeWidth(0.5);
        //endregion

        //region 玻璃板特效（效果不理想已弃用）
//        Robot robot=new Robot();
//        WritableImage writableImage=robot.getScreenCapture(null,new Rectangle2D(0,0,1920,1080));
//        ImageView imageView=new ImageView(writableImage);
//        AnimationTimer animationTimer=new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                var point=glass.localToScreen(glass.getX(),glass.getY());
//                imageView.setViewport(new Rectangle2D(point.getX(),point.getY(),glass.getWidth(), glass.getHeight()));
//            }
//        };
//        imageView.fitHeightProperty().bind(glass.heightProperty());
//        imageView.fitWidthProperty().bind(glass.widthProperty());
//        BoxBlur boxBlur=new BoxBlur();
//        boxBlur.setHeight(255);
//        boxBlur.setWidth(255);
//        boxBlur.setIterations(3);
//        imageView.setEffect(boxBlur);
//        animationTimer.start();
        //endregion

        root.getChildren().addAll(glass,container);
//        container.setMouseTransparent(true);

        setEvent();
        makeDraggable();

    }

    //设置鼠标移动动画
    private void setEvent(){
        IconTag iconTag=new IconTag();
        Node tag=iconTag.getNode();
        tag.setManaged(false);

        container.setOnMouseExited(mouseEvent -> {
            ObservableList<Node> children= root.getChildren();
            dockItemList.forEach((obj)->{
                if(obj instanceof Icon){
                    ((Icon)obj).reset();
                }
            });
            if(Parament.isIconTag.get()){
                children.remove(tag);
            }
        });
        container.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double mouseContainerX=mouseEvent.getX();
                double k;
                //比例系数，根据情况调节
                k=0.17;
                for (DockItem item : dockItemList) {
                    if(item instanceof Icon) {
                        Node node = item.getNode();
                        Bounds boundsInContainer = node.getBoundsInParent();

                        if (Parament.isIconAnimation.get()) {
                            double distanceX = Math.abs(mouseContainerX - boundsInContainer.getCenterX());
                            double percentX = 1 - distanceX * k / Parament.iconEnlargedWidth.get();
                            ((Icon) item).enter(percentX);
                        }
                    }
                }
            }
        });

        container.setOnMouseMoved(mouseEvent -> {
            double mouseContainerX=mouseEvent.getX();
            double mouseContainerY=mouseEvent.getY();
            ObservableList<Node> children= root.getChildren();

            boolean selected = false;
            double k;
            //比例系数，根据情况调节
            k=0.17;
            for (DockItem item : dockItemList) {
                if(item instanceof Icon){
                    Node node = item.getNode();
                    Bounds boundsInRoot = root.sceneToLocal(node.localToScene(node.getBoundsInLocal()));
                    Bounds boundsInContainer=node.getBoundsInParent();

                    if(Parament.isIconAnimation.get()){
                        double distanceX=Math.abs(mouseContainerX - boundsInContainer.getCenterX());
                        double percentX = 1 - distanceX*k/Parament.iconEnlargedWidth.get();
                        ((Icon) item).update(percentX);
                    }

                    if(Parament.isIconTag.get()){
                        if (boundsInContainer.contains(mouseContainerX, mouseContainerY)) {
                            selected = true;

                            if (!children.contains(tag)) {
                                children.add(tag);
                            }
                            iconTag.setTag(((Icon) item).getTag());

                            Bounds tagBounds = tag.getBoundsInLocal();
                            tag.relocate(
                                    boundsInRoot.getMinX() + boundsInRoot.getWidth() / 2 - tagBounds.getWidth() / 2,
                                    boundsInRoot.getMinY() - tagBounds.getHeight()
                            );
                        }

                        if (!selected) {
                            children.remove(tag);
                        }
                    }
                }
            }
        });

    }

    public void setDockItem(List<DockItem> dockItemList){
        this.dockItemList= dockItemList;
        container.getChildren().addAll(dockItemList.stream().map(DockItem::getNode).toList());
    }

    public List<DockItem> getDockItemList(){
        return dockItemList;
    }

    public static void addIcon(DockItem dockItem){
        addIcon(Math.max(dockItemList.size(),0), dockItem);
    }

    public static void addIcon(int index, DockItem dockItem){
        if(index<0)
            index+=dockItemList.size()+1;
        dockItemList.add(index,dockItem);
        container.getChildren().add(index,dockItem.getNode());
    }

    public static void removeIcon(Node node){
        int index=container.getChildren().indexOf(node);
        container.getChildren().remove(node);
        dockItemList.remove(index);
    }

    private DockItem draggedNode;

    private void makeDraggable(){
        container.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for (DockItem item : dockItemList) {
                    Node node = item.getNode();
                    Bounds boundsInRoot = root.sceneToLocal(node.localToScene(node.getBoundsInLocal()));
                    Bounds boundsInContainer = node.getBoundsInParent();
                    if(boundsInContainer.contains(mouseEvent.getX(), mouseEvent.getY())){
                        draggedNode=item;
//                        draggedNode.getNode().setOpacity(0.5);
                    }

                }
                mouseEvent.consume();
            }
        });

//        container.setOnDragDetected(mouseEvent -> {
//
//        });
        container.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                container.startFullDrag();
//                mouseEvent.consume();
            }
        });

        container.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                draggedNode.getNode().setOpacity(0.5);
                for (DockItem item : dockItemList) {
                    Node node = item.getNode();
                    Bounds boundsInRoot = root.sceneToLocal(node.localToScene(node.getBoundsInLocal()));
                    Bounds boundsInContainer = node.getBoundsInParent();
                    if(boundsInContainer.contains(mouseEvent.getX(), mouseEvent.getY())){
                        Platform.runLater(()->{
                            swapNode(draggedNode,item);
                        });
                    }

                }
//                mouseEvent.consume();
            }
        });
        container.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                draggedNode.getNode().setOpacity(1);
                Platform.runLater(()->{
                    dockItemList.forEach((obj)->{
                        if(obj instanceof Icon){
                            ((Icon)obj).recover();
                        }
                    });
                });
            }
        });

    }

    private void swapNode(DockItem dockItem1,DockItem dockItem2){
        if(dockItem1!=dockItem2) {
            int index1 = dockItemList.indexOf(dockItem1);
            int index2 = dockItemList.indexOf(dockItem2);
            System.out.println(index1+" "+index2);
            if(index1<0||index2<0) return;
            dockItemList.remove(index1);
            dockItemList.add(index1, dockItem2);
            dockItemList.remove(index2);
            dockItemList.add(index2, dockItem1);

            Node node1=dockItem1.getNode();
            Node node2=dockItem2.getNode();

            var nodeList=container.getChildren();
            nodeList.removeAll(node1,node2);
            if(index1<index2){
                nodeList.add(index1,node2);
                nodeList.add(index2,node1);
            }
            else{
                nodeList.add(index2,node1);
                nodeList.add(index1,node2);
            }

        }
    }
    public StackPane getRoot(){
        return root;
    }

}
