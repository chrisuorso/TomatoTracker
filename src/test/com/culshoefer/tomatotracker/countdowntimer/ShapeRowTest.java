package com.culshoefer.tomatotracker.countdowntimer;

import com.culshoefer.tomatotracker.shaperow.ShapeRow;

import org.junit.Before;

import javafx.scene.shape.Circle;

/**
 * @author Christoph Ulshoefer <christophsulshoefer@gmail.com> 08/08/16.
 * TODO write tests, as currently UI testing on JavaFX is crap
 */
public class ShapeRowTest {
    private ShapeRow cr;
    @Before
    public void setUp() throws Exception {
        Circle c = new Circle();
        c.setRadius(20);
        c.setCenterX(120);
        c.setCenterY(120);
        cr = new ShapeRow(c);
    }
}