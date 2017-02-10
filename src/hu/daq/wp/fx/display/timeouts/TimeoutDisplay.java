/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.daq.wp.fx.display.timeouts;

import hu.daq.wp.fx.display.penalties.CircleFactory;
import hu.daq.wp.matchorganizer.MatchOrganizer;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 *
 * @author DAQ
 */
public class TimeoutDisplay extends StackPane {

    GlyphFont fontawesome;
    ArrayList<Shape> matchlegs;
    ArrayList<Shape> overtimes;

    public TimeoutDisplay() {

        this.matchlegs = new ArrayList<Shape>();
        this.overtimes = new ArrayList<Shape>();
        this.fontawesome = GlyphFontRegistry.font("FontAwesome");
    }

    public void setUp(MatchOrganizer organizer) {
        this.matchlegs.clear();
        this.overtimes.clear();
        CircleFactory cf = new CircleFactory();
        System.out.println("TO nums: " + organizer.getTimeoutsByPhaseType("hu.daq.wp.matchorganizer.MatchLeg"));
        for (int i = 0; i < organizer.getTimeoutsByPhaseType("hu.daq.wp.matchorganizer.MatchLeg"); i++) {
            //Glyph g = this.fontawesome.create(FontAwesome.Glyph.CLOCK_ALT);
            Shape g = cf.getShape(38);
            g.setFill(Color.GREEN);
            //g.setFontSize(30);
            this.matchlegs.add(g);
        }

        for (int i = 0; i < organizer.getTimeoutsByPhaseType("hu.daq.wp.matchorganizer.Overtime"); i++) {
            //Glyph g = this.fontawesome.create(FontAwesome.Glyph.CLOCK_ALT);
            //g.setColor(Color.GREEN);
            //g.setFontSize(30);
            Shape g = cf.getShape(38);
            g.setFill(Color.GREEN);            
            this.overtimes.add(g);
        }
        this.build();
    }

    private void build() {
        HBox hb = new HBox(5);

        this.getChildren().clear();
        hb.setAlignment(Pos.CENTER);
        for (Shape g : this.matchlegs) {
            Label l = new Label();
            l.setGraphic(g);
            hb.getChildren().add(l);
        }

        if (this.overtimes.size() > 0) {
            Region r = new Region();
            r.setMinWidth(20);
            r.setMinWidth(20);
            hb.getChildren().add(r);
            for (Shape g : this.overtimes) {
                Label l = new Label();
                l.setGraphic(g);
                hb.getChildren().add(l);
            }
        }
        this.getChildren().add(hb);
    }

    public void addMatchTimeout(Integer num) {
        this.matchlegs.get(num - 1).setFill(Color.RED);
    }

    public void addOvertimeTimeout(Integer num) {
        this.overtimes.get(num - 1).setFill(Color.RED);
    }

    public String getTimeouts(){
        return this.matchlegs.stream()
                .map(E->{return E.getFill().equals(Color.RED)?"1":"0";})
                .collect(Collectors.joining());
    }
    
    public void setTimeouts(String timeouts){
        char c = '1';
        IntStream.range(0, timeouts.length()-1)
                .filter(E -> timeouts.charAt(E)==c)
                .forEach(E -> this.addMatchTimeout(E));
    }
}
