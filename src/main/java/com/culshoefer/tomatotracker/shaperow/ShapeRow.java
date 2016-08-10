package com.culshoefer.tomatotracker.shaperow;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 08/08/16.
 */
public class ShapeRow extends Pane {
    private int numIcons;
    private double distBetweenShapes;
    private ShapeFactory shapeF;

    public ShapeRow() {
        super();
    }

    public ShapeRow(ShapeFactory shapeF) {
        this(shapeF, 0, 0);
    }

    public ShapeRow(ShapeFactory shapeF, int numIcons, int distBetweenShapes) {
        super();
        this.shapeF = shapeF;
        this.numIcons = numIcons;
        this.distBetweenShapes = distBetweenShapes;
    }

    public void setNumIcons(int numIcons) {
        this.numIcons = numIcons;
        this.getChildren().clear();
        this.genNumIcons();
        //Shape toAdd = Shape.union(icon, icon);
        //for(int i = 0; i < numIcons; i++){
        //  this.getChildren().add(toAdd);
            /*ShapeRow.setRowIndex(toAdd, 0);
            ShapeRow.setColumnIndex(toAdd, i);
            ShapeRow.setRowSpan(toAdd, 1);
            ShapeRow.setColumnSpan(toAdd, 1);*/
    }

    public void setDistBetweenShapes(double distBetweenShapes) {
        this.distBetweenShapes = distBetweenShapes;
    }

    private void genNumIcons() {
        for (int i = 0; i < numIcons; i++) {
            Shape toAdd = shapeF.getInstance();
            toAdd.setLayoutX(toAdd.getLayoutX() + distBetweenShapes * i);
            this.getChildren().add(toAdd);
            /*ShapeRow.setRowIndex(toAdd, 0);
            ShapeRow.setColumnIndex(toAdd, i);
            ShapeRow.setRowSpan(toAdd, 1);
            ShapeRow.setColumnSpan(toAdd, 1);*/
        }
    }

}
