/**
 * Created by danny on 11/30/16.
 */
public class Animator implements Runnable{


    @Override
    public void run() {

        while(GUI.getMaxBoxY() <= GUI.getBotLeftY()){

            GUI.pVelx -= (GUI.pAccelx/60);

            GUI.moveRect1X(GUI.pVelx/60);

            GUI.pVely += (GUI.pAccely/60);

            GUI.moveRect1Y(GUI.pVely/60);

            try {
                Thread.sleep(1000/60);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

        }

        GUI.restart();
    }

}
