package com.yingtai.dock;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class Parament {
    public static DoubleProperty iconWidth=new SimpleDoubleProperty(100);
    public static DoubleProperty iconEnlargedWidth=new SimpleDoubleProperty(170);
//    //图标圆角
//    public static double iconArcWidth=20;
    //图标下方原点打开标志
    public static double iconOpenedDot=3;
    //图标放大比例
    public static double iconEnlargeScale=1.5;
//    public static DoubleProperty glassBlur=new SimpleDoubleProperty(4);
    //dock栏圆角
    public static DoubleProperty dockArcWidth=new SimpleDoubleProperty(40);
//    public static double dockHeight=150;
    public static double tipFontSize=15;
//    public static double iconSpacing=10;
    public static DoubleProperty iconSpacing=new SimpleDoubleProperty(10);
    public static DoubleProperty dockToBottom=new SimpleDoubleProperty(50);
    public static ObjectProperty<Color> glassColor=new SimpleObjectProperty<>(Color.rgb(200,200,200,0.8));
    public static Color lightColor=Color.rgb(200,200,200,0.8);
    public static Color darkColor=Color.rgb(15,15,15,0.8);
    public static BooleanProperty isSetAutoDark=new SimpleBooleanProperty(false);
    public static DoubleProperty timeBeforeHiding=new SimpleDoubleProperty(10000);
    //当前Dock是否可以自动隐藏
    public static boolean isHidable=true;

    public static IntegerProperty dateModel=new SimpleIntegerProperty(0);

    public static BooleanProperty isIconTag=new SimpleBooleanProperty(true);
    public static BooleanProperty isIconAnimation=new SimpleBooleanProperty(true);

}
