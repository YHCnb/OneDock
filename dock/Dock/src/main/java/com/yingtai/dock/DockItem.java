package com.yingtai.dock;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class DockItem extends Node{
    protected StackPane root;
    public Node getNode(){
        return root;
    }

}
