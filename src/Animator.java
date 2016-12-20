import javafx.application.Platform;

/**
 * Created by danny on 11/30/16.
 */
public class Animator implements Runnable{


    @Override
    public void run() {

        while((GUI.getMaxBoxY() <= GUI.getBotLeftY()) && (GUI.getMaxBoxY() >= GUI.getTopRightY() + 10)){

        	int tickRate = GUI.tickRate;
        	
        	if(!GUI.isPulley){
        		
        		if((GUI.getMaxBoxY() <= GUI.getBotLeftY())){

        			GUI.pVelx -= ((GUI.pAccel*Math.cos(GUI.rAngle))/tickRate);

            		GUI.moveRect1X(GUI.pVelx/tickRate);

            		GUI.pVely += ((GUI.pAccel*Math.sin(GUI.rAngle))/tickRate);

            		GUI.moveRect1Y(GUI.pVely/tickRate);
        		}
        	} else {
        		
        		if(((GUI.getMaxBoxY2()) < (GUI.getBotLeftY() + 10)) && (GUI.getMaxBoxY2() - 100 > GUI.getTopRightY()) && (GUI.getMaxBoxY() - 100 > GUI.getTopRightY())){

            	GUI.pVelx -= ((GUI.pAccel*Math.cos(GUI.rAngle))/tickRate);

                GUI.moveRect1X(GUI.pVelx/tickRate);

                GUI.pVely += ((GUI.pAccel*Math.sin(GUI.rAngle))/tickRate);

                GUI.moveRect1Y(GUI.pVely/tickRate);
            	
                GUI.pVely2 += ((GUI.pAccel)/tickRate);

                GUI.moveRect2Y(-GUI.pVely/tickRate);
        		}
            }

            try {
                Thread.sleep(1000/tickRate);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }


        }

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GUI.showTime();
				GUI.showVectors();
			}
		});

    }

}
