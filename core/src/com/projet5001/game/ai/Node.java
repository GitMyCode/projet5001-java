package com.projet5001.game.ai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.projet5001.game.collisions.WorldCollector;

import java.util.ArrayList;

public class Node extends Rectangle{

    public  boolean block;
    private double h_heuristique;
    private int gCost;
    private double f_totalCost;
    private Node parent;
    private Vector2 futur_position;
    private int speed;
    public int walkingCost;
    private ArrayList<Node> neighbours;
    public String state;


    public Node(Rectangle rect){
        super(rect.x,rect.y,Math.max(rect.width,rect.height),Math.max(rect.width,rect.height));
        this.state = "";
        this.speed = 44;
        this.walkingCost = 44;
        this.gCost = 0;
        this.h_heuristique =0;
        this.f_totalCost = 0;
        this.block = false;
        this.neighbours = new ArrayList<>();
        this.futur_position = new Vector2(this.x,this.y);
    }



    public ArrayList<Node> getneighbours(){


        moveUp();
        moveRight();
        moveDown();
        moveLeft();

        return this.neighbours;
    }
    public void moveLeft() {
        Node node = move(-speed, 0);
        node.state = "left";
        this.neighbours.add(node);

    }

    public void moveRight() {
        Node node = move(speed, 0);
        node.state = "right";
        this.neighbours.add(node);
    }

    public void moveUp() {
        Node node = move(0, speed);
        node.state = "up";
        this.neighbours.add(node);
    }

    public void moveDown() {
        Node node = move(0, -speed);
        node.state = "down";
        this.neighbours.add(node);
    }

    public Node move(float x, float y) {
        futur_position.set(this.getX() + x, this.getY() + y);
        Rectangle rectangle = new Rectangle(futur_position.x,futur_position.y,Math.max(this.width,this.height),Math.max(this.width,this.height));

        if (this.getX() + x < 0 || this.getY() + y < 0 ){
            System.out.println("out of map  negatif");
            Node node = new Node(rectangle);
            node.block = true;
            return node ;
        }

        if (this.getX() + x > 3000 || this.getY() + y> 3000 ){
            System.out.println("out of map positif");
            Node node = new Node(rectangle);
            node.block = true;
            this.neighbours.add(node);
            return node;
        }

        if (!WorldCollector.collection().hitWorld(rectangle)) {
            Node node = new Node(rectangle);
            node.block = false;
            return node;
        }else{
            Node node = new Node(rectangle);
            node.block = true;
            return node;

        }
    }

    public Vector2 getVector2() {

        return new Vector2(x,y);
    }

    public Vector2 getTansformedVector(){
        float x = this.x / speed;
        float y = this.y / speed;
        return new Vector2(x,y);
    }

    public Rectangle getRectangle (){
        return this;
    }

    public double getH() {
        return this.h_heuristique;
    }

    public void setH(double h_heuristique) {
        this.h_heuristique = h_heuristique;
    }

    public int getG() {
        return gCost;
    }

    public int getSpeed(){
        return speed;
    }

    public void setG(int g_movementCost) {
        this.gCost = g_movementCost;
    }

    public double getF() {
        return f_totalCost;
    }

    public void setF(double f_totalCost) {
        this.f_totalCost = f_totalCost;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node p){
        this.parent = p;
    }

    public boolean equals(Node node){
        return this.x == node.x && this.y == node.y;
    }
}
