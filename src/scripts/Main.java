package scripts;


import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

@Script.Manifest(name="name",
        description="description",
        properties ="author=Brad; topic=999; client=4;")

public class Main extends PollingScript<ClientContext> implements PaintListener, MouseListener, MouseMotionListener {

    ArrayList<Tile> customAreaList = new ArrayList<>();
    Area areaAroundPlayer = new Area(ctx.players.local().tile().derive(-10,-10), ctx.players.local().tile().derive(10,10));
    Point currentMouseLocation = new Point(0,0);

    @Override
    public void start(){
        System.out.println("Started");
    }

    @Override
    public void stop(){

        if(customAreaList.size() >= 2){
            System.out.print("Area customArea = new Area(");
            for(Tile t: customAreaList){
                System.out.print("new Tile(" + t.x() + ", " + t.y() + ", " + t.floor() + "), ");
            }
            System.out.print("\b\b);");
        } else {
            System.out.println("Not enough tiles selected");
        }
    }

    @Override
    public void poll() {}


    @Override
    public void repaint(Graphics graphics) {

        final Graphics2D g = (Graphics2D) graphics;
        areaAroundPlayer = new Area(ctx.players.local().tile().derive(-15,-15), ctx.players.local().tile().derive(15,15));

        // Draw instructions
        g.setColor(new Color(0,0,0,127));
        g.fillRect(0, 320, 320, 20);
        g.setColor(Color.WHITE);
        g.drawString("Left click to add tile, right click to remove last tile", 0, 335);

        // Draw current mouse location on ground
        for(Tile t: areaAroundPlayer.tiles()){
            if(t.matrix(this.ctx).bounds().contains(currentMouseLocation)){
                g.setColor(new Color(200, 1, 1, 70));
                g.fillPolygon(t.matrix(this.ctx).bounds());
                break;
            }
        }

        // Draw current customAreaList
        if(customAreaList.size() >= 2){
            Area b;
            if(customAreaList.size() == 2){
                b = new Area(customAreaList.get(0), customAreaList.get(1));
            } else {
                b = new Area(customAreaList.toArray(new Tile[customAreaList.size()]));
            }

            for(Tile t: b.tiles()){
                g.setColor(new Color(1, 200, 1, 70));
                g.fillPolygon(t.matrix(this.ctx).bounds());
            }
        }

        // Draw selected tiles
        for(Tile t: customAreaList){
            g.setColor(new Color(200, 1, 200, 70));
            g.fillPolygon(t.matrix(this.ctx).bounds());
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // Left Click - Add to area tiles
        if(e.getButton() == MouseEvent.BUTTON1){
            for(Tile t: areaAroundPlayer.tiles()){
                if(t.matrix(this.ctx).bounds().contains(new Point(e.getX(), e.getY()))){
                    customAreaList.add(t);
                    break;
                }
            }
        // Right Click - Remove last tile from area tiles
        } else if (e.getButton() == MouseEvent.BUTTON3){
            if(customAreaList.size() > 0 ) customAreaList.remove(customAreaList.get(customAreaList.size()-1));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentMouseLocation = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

}
