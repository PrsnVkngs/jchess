//Helper procs (static procs that return usable values elsewhere)

/*Documentation
    - Coords: Very top left cell (index 0) has coords (0,0).
      Very bottom right cell (index 63) has coords (8,8).

*/

package chess;

//import java.util.Arrays; //not sure if this will be needed
import java.util.ArrayList;

/**
 *
 * @author Parker
 */
public class Helpers {
    public static final int BOARDSIZE = 8;//chess board is always 8 tiles wide
    
    public static int getX(int index){//array index -> x coord
        return(index%BOARDSIZE);
    }
    
    public static int getY(int index){//array index -> y coord
        return(int)(Math.floor(index/BOARDSIZE));
    }
    
    public static String getCoords(int index){
        String output = "";
        output += Integer.toString(getX(index));
        output += ", ";
        output += Integer.toString(getY(index));
        return output;
    }
    
    public static int coord2index(int x, int y){//coords -> index
        return x + (y*BOARDSIZE);
    }
    
    public static int movIndex(int index, char dir, int steps){//old index + direction to move -> new index.
        if(steps < 1){//set steps to mov multiple times in the same dir. otherwise, always move at least once
            steps = 1;
        }
        int modifier = 0;//flat added to index
        switch(dir){
            case 'n':
                modifier = BOARDSIZE*-1;
                break;
            case 'e':
                modifier = 1;
                break;
            case 's':
                modifier = BOARDSIZE;
                break;
            case 'w':
                modifier = -1;
                break;
            case 'N'://capital dirs are diagonals, starting at NE and going clockwise. this is NE
                modifier = (BOARDSIZE*-1)+1;
                break;
            case 'E'://SE
                modifier = BOARDSIZE+1;
                break;
            case 'S'://SW
                modifier = BOARDSIZE-1;
                break;
            case 'W'://NW
                modifier = (BOARDSIZE*-1)-1;
                break;
            default:
                System.out.print("Bad dir in mov helper");
                System.exit(1);//kill the program
        }
        for(int i = 0; i < steps; i++){
            index += modifier;
        }
        return index;
    }
    
    public static int movCoords(int x, int y, char dir, int steps){
        return movIndex(coord2index(x,y), dir, steps);//macro to do mov code with coords
    }

    public static int[] potentialMoves(pieceClass[] board, int pieceIndx){ //returns an array of potential movements
        ArrayList<Integer> potentials = new ArrayList<>();//variable length array (list)
        pieceClass piece = board[pieceIndx];
        for (char[] movement : piece.movements) {
            if(movement == null) break;
            int tempIndex = piece.index;
            int oldindex = tempIndex;
            for(int i = 0; i < movement.length; i++){
                char onemove = movement[i];
                oldindex = tempIndex;
                tempIndex = movIndex(tempIndex, onemove, 1);
                if(!isOOB(tempIndex, oldindex, movement[i])){
                    if(isOccupied(tempIndex, board)){
                        if(!board[pieceIndx].isPawn && isEnemyOccupied(pieceIndx, tempIndex, board)){
                            if(board[pieceIndx].manySteps || i == movement.length-1) potentials.add(tempIndex);
                        }
                        if("knight".equals(board[pieceIndx].type) && i != movement.length-1) continue;//knights jump over blocks
                        break;
                    }
                    else if(piece.manySteps || i == movement.length-1) potentials.add(tempIndex);
                } else {
                    break;
                }
            }
        }

        if(board[pieceIndx].isPawn){//handle pawnkills
            for (char killMovement : piece.pawnKills){
                int tempIndex = movIndex(pieceIndx, killMovement, 1);
                if(!isOOB(tempIndex, pieceIndx, killMovement) && isOccupied(tempIndex, board) && isEnemyOccupied(pieceIndx, tempIndex, board)){
                    potentials.add(tempIndex);
                }
            }
        }
        Integer[] convertStep = potentials.toArray(new Integer[potentials.size()]);
        int[] returnable = new int[convertStep.length];
        for(int i = 0; i < convertStep.length; i++){
            returnable[i] = convertStep[i];
        }
        return returnable;//may need refactor to also output list of potential kills
    }
    
    public static boolean isOOB(int newindex, int oldindex, char dir){//returns true if an index is Out Of Bounds of the chessboard
        if(dir == 'S' || dir == 'w' || dir == 'W'){//this prevents going off the sides
            if(getX(oldindex) == 0 && getX(newindex) == 7){
                return true;
            }
        } else if(dir == 'N' || dir == 'e' || dir == 'E'){
            if(getX(oldindex) == 7 && getX(newindex) == 0){
                return true;
            }
        }
        
        return (newindex < 0 || newindex > (BOARDSIZE*BOARDSIZE)-1);//this prevents going off the top/bottom
    }
    
    public static boolean isOccupied(int index, pieceClass[] board){//checks if a board tile isn't blank
        return !(board[index].type.equals("b"));
    }

    public static boolean isEnemyOccupied(int moverIndex, int index, pieceClass[] board){//checks if a board tile is occupied by an enemy
        boolean moverIsWhite = board[moverIndex].isWhite;//should only be ran when we know we're moving to an occupied space
        return (board[index].isWhite != moverIsWhite);
    }    
    
    /*public static void main(String args[]){
        System.out.print( );//test functions here
    }*/
}
