package com.projet5001.game.ai;

import java.util.*;

public class AstarArrayList {

    protected static final int COUT = 32/16;

    public  static ArrayList<Node> run(Node nodeStart, Node dest){
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closeList = new ArrayList<>();
        ArrayList<Node> path  = new ArrayList<>();
        Node current;
        int exitH = 2;

        openList.ensureCapacity(50);
        closeList.ensureCapacity(50);

        openList.add(nodeStart);

        nodeStart.setG(0);
        nodeStart.setH(calculHeuristique(nodeStart, dest));
        nodeStart.setF(nodeStart.getG() + calculHeuristique(nodeStart, dest));
        while (!openList.isEmpty()){

            Collections.sort(openList,new FValueComarator());

            current = openList.get(0);

            if (calculHeuristique(current,dest) < exitH){
                openList.clear();
                closeList.clear();
                return reconstruct_path(path, current);
            }

            openList.remove(0);
            closeList.add(current);
            Node[] neighbours;

            //this is some fine tuning

            if (calculHeuristique(current,dest) < 4){
                 neighbours = current.getneighbours(4);
            }else{
                neighbours = current.getneighbours();
            }

            for(int i = 0; i < 4;i++ ){
                Node neighbour = neighbours[i];

                if(neighbour.block){
                    closeList.add(neighbour);
                }

                if (closeList.contains(neighbour)){
                    continue;
                }

                int tentativeGCost = current.getG() + movementCost(current, neighbour);
                boolean n = openList.contains(neighbour);
                if (!n || tentativeGCost < neighbour.getG()){
                    neighbour.setParent(current);
                    neighbour.setG(tentativeGCost);
                    neighbour.setF(neighbour.getG() + calculHeuristique(neighbour,dest));
                    if (!n){
                        openList.add(neighbour);
                    }
                }
            }
        }
        return null;
    }
    private static ArrayList<Node> reconstruct_path(ArrayList<Node> path, Node last){
        Node current = last;
        while(current.getParent() != null ){
            path.add(current);
            current = current.getParent();
        }
        return path;
    }

    private static int movementCost(Node current, Node neighbour){
        return current.walkingCost; //-neighbour malus cost
    }

    private static float  calculHeuristique(Node current, Node dest) {
        return (Math.abs(current.x/current.getSpeed() - dest.x/dest.getSpeed()) + (Math.abs(current.y/current.getSpeed()  - dest.y/dest.getSpeed()))) * COUT;
        /*

        case HeuristicType.Manhattan:
            H = Math.Abs(StartX - EndX) + Math.Abs(StartY - EndY);
            break;

        case HeuristicType.Diagonal:
            H = Math.Max(Math.Abs(StartX - EndX), Math.Abs(StartY - EndY));
            break;

        case HeuristicType.Euclidean:
            H = Math.Sqrt(Math.Pow(StartX - EndX, 2) + Math.Pow(StartY - EndY, 2));
            break;

        case HeuristicType.EuclideanSquared:
            H = Math.Pow(StartX - EndX, 2) + Math.Pow(StartY - EndY, 2);
            break;

        case HeuristicType.TieBreakerManhattan:
            H = Math.Abs(StartX - EndX) + Math.Abs(StartY - EndY) * m_tieBreaker;
            break;

        case HeuristicType.TieBreakerDiagonal:
            H = Math.Max(Math.Abs(StartX - EndX), Math.Abs(StartY - EndY)) * m_tieBreaker;
            break;

        case HeuristicType.TieBreakerEuclidean:
            H = Math.Sqrt(Math.Pow(StartX - EndX, 2) + Math.Pow(StartY - EndY, 2)) * m_tieBreaker;
            break;

        case HeuristicType.TieBreakerEuclideanSquared:
            H = Math.Pow(StartX - EndX, 2) + Math.Pow(StartY - EndY, 2) * m_tieBreaker;
            break;
         */
    }

    private static class FValueComarator implements Comparator<Node>{

        @Override
        public int compare(Node node, Node node2) {
            return node.getF() < node2.getF() ? -1 : node.getF() == node2.getF() ? 0 : 1;
        }
    }
}
