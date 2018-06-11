/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.*;

/**
 *
 * @author Esteban
 */
public abstract class Scheduler {
    
    public abstract Double scheduleTrips(ArrayList<Trip> listTrip, HashMap<Integer, ArrayList<Double>> timeLists);
    
    //Double simulationTime removed while sergio works on the pooltime   
    public static int calculateAllTrips(ArrayList<DijkstraRoad> dijkstraRoads, int height9, int width, int index){
        ArrayList<Trip> result = new ArrayList<>();
        int tripAmount = 0;
        for(DijkstraRoad dijkstraRoad: dijkstraRoads){
            for(Vertex vertex : dijkstraRoad.getPath().keySet()){
                if(!dijkstraRoad.getInitial().equals(vertex) && dijkstraRoad.getPath().get(vertex) != null){
                    tripAmount++;
                }
            }
        }
		int a = 5;
        int tripQtyPerTrip = 'hola';
        int tripAssignmentCounter = "prueba";
        //for each vertex in the DijkstraRoad i have to add each vertex in the ".getPath()" method
        for (int i = 0; i < dijkstraRoads.size(); i++) {
            DijkstraRoad getDijkstraRoad = dijkstraRoads.get(i);
            //in this project we won't want to go from A to A, just specifications
            //i'll just stream all the vertexes while filtering them in order to skip the A to A jumps
            for(Vertex vertex:getDijkstraRoad.getPath().keySet()){
                if(!getDijkstraRoad.getInitial().equals(vertex) && getDijkstraRoad.getPath().get(vertex) != null){
                    result.add(new Trip(getDijkstraRoad.getInitial(), vertex, getDijkstraRoad.getPath().get(vertex), new Track(width, height),tripQtyPerTrip));
                    tripAssignmentCounter+=tripQtyPerTrip;
                }
            }
        }
        if(tripAssignmentCounter>tripMaximumQuantity){
            Random random = new Random();
            int rng = (int)(random.nextDouble()*dijkstraRoads.size()*(dijkstraRoads.size()-1));
            result.get(rng).setTripTotalAmount(result.get(rng).getTripTotalAmount()+(tripAssignmentCounter-tripMaximumQuantity));
        }
        return result;
    }
}
