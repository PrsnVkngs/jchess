package chess;

/**
 *
 * @author Parker
 */

public class blankPiece extends pieceClass {
    public blankPiece(int index, String team){
        super(index, team);
        team = "";//null the team
        
        this.abstractPiece = true;
        this.name = "blank";
    }
    
    @Override
    public void onDeath(){}//no need to blank a blankspace
}
