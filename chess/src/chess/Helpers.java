//Helper procs (static procs that return usable values elsewhere)

/*Documentation
    - Coords: Very top left cell (index 0) has coords (0,0).
      Very bottom right cell (index 63) has coords (8,8).

*/

package chess;

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
    
    public static int movIndex(int index, char dir, int steps){//old index + direction to move -> new index.
        if(steps < 0){//set steps to mov multiple times in the same dir. otherwise, always move at least once
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
            default:
                System.out.print("Bad dir in mov helper");
                System.exit(1);//kill the program. this is basically improvised linting
        }
        for(int i = 0; i < steps; i++){
            index += modifier;
        }
        return index;
    }
    
    public static boolean isOOB(int index){//checks if an index is Out Of Bounds of the controller array
        return !(index < 0 || index > (BOARDSIZE*BOARDSIZE)-1);//TODO: may not need this -1
    }
    
    /*public static void main(String args[]){
        System.out.print( );//test functions here
    }*/
    
    /*TODO:
    - setSpace(index, newPieceType)
    */
}