package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Curve extends Pane {

    private ArrayList<Double> values;
    private double xMin;
    private double xMax;
    private double xInc;
    private Axes axes;
    private Path path;
    private String[] behaviour = {"","","","",""};

    public Curve(
            ArrayList<Double> values,
            double xMin, double xMax, double xInc,
            Axes axes
    ) {
        this.values = values;
        this.xMin = xMin;
        this.xMax = xMax;
        this.xInc = xInc;
        this.axes = axes;
        this.path = new Path();
        draw();
        analyseBehaviour();
    }


    private void draw(){

        path.setStroke(Color.ORANGE.deriveColor(0, 1, 1, 0.6));
        path.setStrokeWidth(2);
        path.setClip(
                new Rectangle(
                        0, 0,
                        axes.getPrefWidth(),
                        axes.getPrefHeight()
                )
        );

        double x = xMin;
        double y = calcYValue(x);

        path.getElements().add(
                new MoveTo(
                        mapX(x, axes), mapY(y, axes)
                )
        );

        x += xInc;
        while (x < xMax) {
            y = calcYValue(x);
            path.getElements().add(
                    new LineTo(
                            mapX(x, axes), mapY(y, axes)
                    )
            );
            x += xInc;
        }

        setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
        setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

        getChildren().setAll(path);
    }

    private double mapX(double x, Axes axes) {
        double tx = axes.getPrefWidth() / 2;
        double sx = axes.getPrefWidth() /
                (axes.getXAxis().getUpperBound() -
                        axes.getXAxis().getLowerBound());
        return x * sx + tx;
    }

    private double mapY(double y, Axes axes) {
        double ty = axes.getPrefHeight() / 2;
        double sy = axes.getPrefHeight() /
                (axes.getYAxis().getUpperBound() -
                        axes.getYAxis().getLowerBound());

        return -y * sy + ty;
    }

    private double calcYValue(double x){
        double y = 0;

        for (int i = 0; i < values.size(); i++) {
            y += values.get(i) * Math.pow(x, i);
        }

        return y;
    }

    private double getSum(ArrayList<Double> values) {
        double sum = 0;
        for (Double value: values) {
            sum += value;
        }

        return sum;
    }


    private void analyseBehaviour(){
        if ((this.getSum(values) > 0 || this.getSum(values) < 0) && values.get(3) == 0 && values.get(1) == 0) {
            this.behaviour[0] = "Achsensymmetrisch";
        } else if ((this.getSum(values) > 0 || this.getSum(values) < 0) && values.get(4) == 0 && values.get(2) == 0 && values.get(0) == 0) {
            this.behaviour[0] = "Punktsymmetrisch";
        } else {
            // TODO: Add control for all symmetries
            this.behaviour[0] = "Keine Symmetrien";
        }

        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) > 0) {
                this.behaviour[2] = String.valueOf(i);
            }
        }

        this.behaviour[3] = String.valueOf(values.get(0));
    }

    public String getBehaviour(int index){
        if (index>=0 && index <=4) {
            return behaviour[index];
        }
        else{return "none";}
    }

   /* public void setFunction(ArrayList<Double> values){
        this.values = values;
        draw();
    }*/
}
