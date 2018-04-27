package aStarSearch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar {
    public static final int DIAGONAL_COST = 0;
    public static final int V_H_COST = 0;
    
    static class Cell{  
        int heuristicCost = 0; //Heuristic cost
        int inheritCost = 0;
        int finalCost = 0; //G+H
        int index = -1;
        int i, j;
        Cell parent; 
        
        Cell(int i, int j){
            this.i = i;
            this.j = j; 
        }
        
        @Override
        public String toString(){
            return "["+this.i+", "+this.j+"]";
        }
    }
    
    //Blocked cells are just null Cell values in grid
    static Cell [][] grid = new Cell[5][5];
    
    static PriorityQueue<Cell> open;
     
    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;
            
    public static void setBlocked(int i, int j){
        grid[i][j] = null;
    }
    
    public static void setStartCell(int i, int j){
        startI = i;
        startJ = j;
    }
    
    public static void setEndCell(int i, int j){
        endI = i;
        endJ = j; 
    }
    
    static void checkAndUpdateCost(Cell current, Cell next, int cost){
        if(next == null || closed[next.i][next.j]) return; //if next is doesnt exist or if it has been read
        int t_final_cost = next.heuristicCost + cost + next.inheritCost; //current cost plus estimated cost to goal
        
        boolean inOpen = open.contains(next);
        if(!inOpen || t_final_cost < next.finalCost){ //if node hasnt been read, or if node has arrived from another shorter route
            next.finalCost = t_final_cost; //assign cost
            next.parent = current; //remember how it got here
            if (!inOpen) open.add(next); //add to queue to read next
        }
    }
    
    public static void AStar(){ 
        
        //add the start location to open list.
        open.add(grid[startI][startJ]);      
        Cell current;     
        
        while(true){ 
            current = open.poll();
            if (current==null) break;
            closed[current.i][current.j]=true; //once read from queue add to closed list

            if(current.equals(grid[endI][endJ])){return; } //if goal, finish
            Cell next;  
            
            if(current.i-2>=0){
            	next = grid[current.i-2][current.j];
            	if (next.inheritCost >= 0) {
            	checkAndUpdateCost(current, next, current.finalCost+V_H_COST); //N
            	}
            }

            if(current.i-1>=0){

            	if ((current.i-1) % 2 == 0){
            		next = grid[current.i-1][current.j];
            		if (next.inheritCost >= 0) {
            		checkAndUpdateCost(current, next, current.finalCost+DIAGONAL_COST); //NW
            		}

            	} else {
            		if(current.j-1 >= 0){ 
            			next = grid[current.i-1][current.j-1];
            			if (next.inheritCost >= 0) {
            			checkAndUpdateCost(current, next, current.finalCost+DIAGONAL_COST); //NW
            			}
            		}
            	}


            	if ((current.i-1) % 2 == 0){
            		if(current.j+1 < grid[0].length){  
            			next = grid[current.i-1][current.j+1];
            			if (next.inheritCost >= 0) {
            			checkAndUpdateCost(current, next, current.finalCost+DIAGONAL_COST); //NE
            			}
            		}
            	} else {
            		next = grid[current.i-1][current.j];
            		if (next.inheritCost >= 0) {
            		checkAndUpdateCost(current, next, current.finalCost+DIAGONAL_COST); //NE
            		}
            	}
            } 

//            if(current.j-1>=0){
//                t = grid[current.i][current.j-1];
//                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); //W
//            }
//
//            if(current.j+1<grid[0].length){
//                t = grid[current.i][current.j+1];
//                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); //E
//            }
            
        	if(current.i+2 < grid.length){
        		next = grid[current.i+2][current.j];
        		if (next.inheritCost >= 0) {
            	checkAndUpdateCost(current, next, current.finalCost+V_H_COST); //S
        		}
        	}

        	if(current.i+1 < grid.length){

        		if ((current.i+1) % 2 == 0){
        			next = grid[current.i+1][current.j];
        			if (next.inheritCost >= 0) {
        			checkAndUpdateCost(current, next, current.finalCost+DIAGONAL_COST); //SW
        			}

        		} else {
        			if(current.j-1>=0){
        				next = grid[current.i+1][current.j-1];
        				if (next.inheritCost >= 0) {
        				checkAndUpdateCost(current, next, current.finalCost+DIAGONAL_COST); //SW
        				}
        			}             	
        		}

        		if ((current.i+1) % 2 == 0){
        			if(current.j+1 < grid[0].length){
        				next = grid[current.i+1][current.j+1];
        				if (next.inheritCost >= 0) {
        				checkAndUpdateCost(current, next, current.finalCost+DIAGONAL_COST); //SE
        				}
        			} 
        		} else {
        			next = grid[current.i+1][current.j];
        			if (next.inheritCost >= 0) {
        			checkAndUpdateCost(current, next, current.finalCost+DIAGONAL_COST); //SE
        			}
        		}

        	}
        } 
    }
    
    /*
    Params :
    tCase = test case No.
    x, y = Board's dimensions
    si, sj = start location's x and y coordinates
    ei, ej = end location's x and y coordinates
    int[][] blocked = array containing inaccessible cell coordinates
    */
    public static void test(int tCase, int x, int y, int si, int sj, int ei, int ej, int[][] blocked, ArrayList<Integer> hexArr){
           System.out.println("\n\nTest Case #"+tCase);
            //Reset
           grid = new Cell[x][y];
           closed = new boolean[x][y];
           open = new PriorityQueue<>((Object o1, Object o2) -> {
                Cell c1 = (Cell)o1;
                Cell c2 = (Cell)o2;

                return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
                // if c1 < c2 return -1
                // if c1 < c2 return 1
                // if equal return 0
            });
           
           //Set start position
           setStartCell(si, sj);           
           //Set End Location
           setEndCell(ei, ej); 
           
           
           for(int i=0; i < x; i++){
              for(int j=0; j < y; j++){
                  grid[i][j] = new Cell(i, j);
                //  grid[i][j].heuristicCost = Math.abs(i -endI -1) + Math.abs(j -endJ + 1); //going vertical costs 1 less, but horizontal costs one extra
//                  System.out.print(grid[i][j].heuristicCost+" ");
              }
           }
           grid[si][sj].finalCost = 0;
           
           int ctr = 0;
           int arrCtr = 0;
           for(int i=0; i < x; i++){
               for(int j=0; j < y; j++){
            	   if (ctr % 15 == 0 && ctr != 0){
            		   ctr = 0;
            		   grid[i][j].inheritCost = -2;
            	   } else {
            		   grid[i][j].index = arrCtr + 1; //1 based for index
            		   grid[i][j].inheritCost = hexArr.get(arrCtr);
            		   arrCtr++;
            		   ctr++;
            	   }
            	   
               }
           }
                 
           /*
             Set blocked cells. Simply set the cell values to null
             for blocked cells.
           */
           for(int i=0;i<blocked.length;++i){
               setBlocked(blocked[i][0], blocked[i][1]);
           }
           
           //Display initial map
           System.out.println("Grid: ");
            for(int i=0;i<x;i++){
            	if ( i % 2 != 0) {
         		   System.out.printf("  ");
         	   }
                for(int j=0;j<y;j++){
                   if(i==si&&j==sj)System.out.print("SO   "); //Source
                   else if(i==ei && j==ej)System.out.print(" DE   ");  //Destination
                   else System.out.printf("%4d ", grid[i][j].index);
                   //else System.out.print("BL  "); 
                }
                System.out.println();
            } 
            System.out.println();
           
          AStar(); 
           
           System.out.println("\nScores for cells: ");
           for(int i=0; i<x; i++){
        	   if ( i % 2 != 0) {
        		   System.out.printf("  ");
        	   }
               for(int j=0; j<y; j++){          	   
            	   if (grid[i][j]!=null) {
            		   System.out.printf("%2d  ", grid[i][j].inheritCost);
            	   }
                   else System.out.print("BL ");               
               }
               System.out.println();
           }
           System.out.println();
            
           if(closed[endI][endJ]){
               //Trace back the path 
                System.out.println("Path: ");
                Cell current = grid[endI][endJ];
               // System.out.print(current);
                while(current.parent!=null){
//                    System.out.print(" -> " +current.parent);
//                    current = current.parent;
                    System.out.print(current.index + "\n");
                    current = current.parent;
                } 
                System.out.print(current.index);
                System.out.println();
           }else System.out.println("No possible path");
    }
     
    public static void main(String[] args) throws Exception{ 
    	// The name of the file to open.
        String fileName = "lattice.txt";
        ArrayList<Integer> hexArr = new ArrayList<>();
        
        // This will reference one line at a time
        String line = null;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(" ");
                hexArr.add(Integer.parseInt(columns[1]));
            }   
            bufferedReader.close();       
            
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");         
            
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
    	
        test(1, 31, 8, 30, 0, 0, 7, new int[][]{}, hexArr); 
        //test(2, 5, 5, 0, 0, 4, 4, new int[][]{{0,4},{2,2},{3,1},{3,3}});   
        //test(3, 7, 7, 2, 1, 5, 4, new int[][]{{4,1},{4,3},{5,3},{2,3}});
        
        //test(1, 5, 5, 0, 0, 4, 4, new int[][]{{3,4},{3,3},{4,3}});
    }
}