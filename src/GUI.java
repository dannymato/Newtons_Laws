import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
<<<<<<< HEAD
import javafx.scene.shape.Circle;
=======
>>>>>>> branch 'master' of https://github.com/macdows1/Newtons_Laws.git
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUI extends Application{
	
	private double mWeight1;
	private double mWeight2;
	private double mAccelG;
	private double mLength;
	private double uFrk;
	private double uFrs;
	private double dAngle;
	protected static double rAngle;
	private double mAccelH;
	public static volatile int mSecs;
	
	protected static volatile double pAccel;
	protected static volatile double pAccelx;
	protected static volatile double pAccely;

	private int isKinetic = 0;
	
	private String[] mUnits = {"kg", "N"};
	
	private TextField mass1;
	private TextField mass2;
	private TextField accel;
	private TextField angle;
	private TextField length;
	private TextField friction;
	private TextField pulley;

	private Button clear;

	private static Label timeField;
	
	private int massIndex = 0;
	private int massIndex1 = 0;
	private int heightIndex = 1;
	private static double botLeftx;
	private static double botLefty;
	private double topRightx;
	private static double topRighty;
	private static double pulleyRadius = 50;
	private static double pulleyCenterX;
	private static double pulleyCenterY;

	private static double pConvert;

	protected static volatile double pVelx;
	protected static volatile double pVely;
	protected static volatile double pVely2;
	protected static volatile double box2y;
	
	protected static boolean neg = false;

	private static Polygon massRect1;
	private static Polygon massRect2;
	
	private static Polygon rope1;
	private static Polygon rope2;

	private boolean hasDrawn = false;
	public static boolean isPulley = false;

	private static Group group;
	
	private Polygon plane;
	
	private Circle pulleyPlane;

	private ImageView newton;
	
	private static Line line;

	protected static final int tickRate = 60;

	private static final double boxHeight = 100;
	private static final double boxWidth = 100;

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		
		Image background = new Image(Main.class.getResourceAsStream("background.jpg"));
		BackgroundImage myBI = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		
		
		group = new Group();
		group.setStyle("-fx-background-color :#D3D3D3;");
		
		
		
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		
		//grid.setBackground(new Background(myBI));

		newton = new ImageView(new Image(Main.class.getResourceAsStream("Newton.jpg")));
		newton.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

		primaryStage.setTitle("Newton's Laws");
		
		mass1 = new TextField();
		mass1.setPromptText("kg");

		if(isPulley) {
			mass2 = new TextField();
			mass2.setPromptText("kg");
		}
		Button btn = new Button();
		btn.setText("Animate");
		
		Thread t = new Thread(new Animator());
		Thread time = new Thread(new TimeCount());
		btn.setOnAction(new EventHandler<ActionEvent>(){
			
			public void handle(ActionEvent event){
				if(hasDrawn){
					group.getChildren().remove(massRect1);
					group.getChildren().remove(massRect2);
					group.getChildren().remove(line);
					group.getChildren().remove(plane);
					group.getChildren().remove(pulleyPlane);
					group.getChildren().remove(rope1);
					group.getChildren().remove(rope2);
					restart();
				}
				if(startAnimation()){
					if(hasDrawn) {
						if (getMaxBoxY() <= getBotLeftY())
							t.interrupt();

					}
					drawRamp(primaryStage);
					drawBox();
					if(calculateAcc() != 0){

						t.start();
						time.start();
					}

				}
			}
		});
		
		ObservableList<String> options = FXCollections.observableArrayList ("Mass 1", "Weight 1");
		
		final ComboBox<String> massCombo = new ComboBox<>(options);
		massCombo.setValue("Mass 1");
		
		massCombo.setOnAction(new EventHandler<ActionEvent>(){
			
				public void handle(ActionEvent event){
					massIndex = massCombo.getSelectionModel().getSelectedIndex();
					mass1.setPromptText(mUnits[massIndex]);
				}
		});
		ObservableList<String> options1 = FXCollections.observableArrayList("Mass 2", "Weight 2");
		final ComboBox<String> massCombo1 = new ComboBox<>(options1);
		if(isPulley) {

			massCombo1.setValue("Mass 2");

			massCombo1.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent event) {
					massIndex1 = massCombo1.getSelectionModel().getSelectedIndex();
					mass2.setPromptText(mUnits[massIndex1]);
				}
			});
		}
		
		accel = new TextField();
		accel.setPromptText("m/s\u00b2");
		
		final Label tAccel = new Label();
		tAccel.setText("Acceleration of Gravity");
		
		final Label tAngle = new Label();
		tAngle.setText("Angle");
		
		angle = new TextField();
		angle.setPromptText("Degrees");
		
		final Label tLength = new Label();
		tLength.setText("Length of Incline");
		
		length = new TextField();
		length.setPromptText("meters");
		
		ObservableList<String> optionsFriction = FXCollections.observableArrayList ("Fr\u2096", "Fr\u209B");

		final ComboBox<String> frictionCombo = new ComboBox<>(optionsFriction);
		frictionCombo.setValue("Fr\u2096");
		
		frictionCombo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				isKinetic = frictionCombo.getSelectionModel().getSelectedIndex();
			}
		});
		
		ObservableList<String> optionsPulley = FXCollections.observableArrayList ("Pulley", "No Pulley");
		
		final ComboBox<String> pulleyCombo = new ComboBox<>(optionsPulley);
		pulleyCombo.setValue("Pulley");
		
		pulleyCombo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				if(pulleyCombo.getValue().equals("Pulley")){
					isPulley = true;
				} else {
					isPulley = false;
					
				}
			}
		});
		
		ObservableList<String> optionsStart = FXCollections.observableArrayList ("Top", "Middle", "Bottom");
		
		final ComboBox<String> boxCombo = new ComboBox<>(optionsStart);
		boxCombo.setValue("Middle");
		
		boxCombo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				if(boxCombo.getValue().equals("Top")){
					heightIndex = 0;
				} else if(boxCombo.getValue().equals("Middle")){
					heightIndex = 1;
				} else {
					heightIndex = 2;
				}
			}
		});
		
		final Label pulleyField = new Label();
		pulleyField.setText("Use a Pulley?");
		
		ObservableList<String> opIsPulley = FXCollections.observableArrayList ("Yes", "No");
		
		final ComboBox<String> comboPulley = new ComboBox<>(opIsPulley);
		comboPulley.setValue("No");
		
		comboPulley.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event){
				if(comboPulley.getValue().equals("Yes")){
					isPulley = true;
					mass2 = new TextField();
					mass2.setPromptText("kg");
					grid.getChildren().clear();
					grid.add(newton,0,0,2,3);
					grid.add(mass1, 1, 3);
					grid.add(massCombo, 0, 3);
					grid.add(mass2, 1, 4);
					grid.add(massCombo1, 0, 4);
					grid.add(accel, 1, 5);
					grid.add(tAccel, 0, 5);
					grid.add(tAngle, 0, 6);
					grid.add(angle, 1, 6);
					grid.add(tLength, 0, 7);
					grid.add(length, 1, 7);
					grid.add(frictionCombo, 0, 8);
					grid.add(friction, 1, 8);
					//grid.add(pulleyCombo, 0, 9);
					//grid.add(pulley, 1, 9);
					grid.add(btn, 1, 9);
					grid.add(timeField,1,10);
					grid.add(boxCombo, 0, 11);
					grid.add(pulleyField, 0, 12);
					grid.add(comboPulley, 1, 12);
				} else {
					isPulley = false;
					grid.getChildren().clear();
					grid.add(newton,0,0,2,3);
					grid.add(mass1, 1, 3);
					grid.add(massCombo, 0, 3);
					grid.add(accel, 1, 4);
					grid.add(tAccel, 0, 4);
					grid.add(tAngle, 0, 5);
					grid.add(angle, 1, 5);
					grid.add(tLength, 0, 6);
					grid.add(length, 1, 6);
					grid.add(frictionCombo, 0, 7);
					grid.add(friction, 1, 7);
					//grid.add(pulleyCombo, 0, 8);
					//grid.add(pulley, 1, 8);
					grid.add(btn, 1, 9);
					grid.add(timeField,1,10);
					grid.add(pulleyField, 0, 12);
					grid.add(comboPulley, 1, 12);
				}
			}
		});
		
		
		
		friction = new TextField();

		timeField = new Label();

		clear = new Button("Clear");
		clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				restart();
				length.setText("");
				friction.setText("");
				angle.setText("");
				accel.setText("");
				mass1.setText("");
				group.getChildren().remove(massRect1);
				if(isPulley) {
					mass2.setText("");
					group.getChildren().remove(massRect2);
				}
				group.getChildren().remove(plane);
				newton.setImage(new Image(Main.class.getResourceAsStream("Newton.jpg")));
			}
		});
		
		//pulley = new TextField();
		
		
		if(isPulley) {
			grid.add(newton,0,0,2,3);
			grid.add(mass1, 1, 3);
			grid.add(massCombo, 0, 3);
			grid.add(mass2, 1, 4);
			grid.add(massCombo1, 0, 4);
			grid.add(accel, 1, 5);
			grid.add(tAccel, 0, 5);
			grid.add(tAngle, 0, 6);
			grid.add(angle, 1, 6);
			grid.add(tLength, 0, 7);
			grid.add(length, 1, 7);
			grid.add(frictionCombo, 0, 8);
			grid.add(friction, 1, 8);
			//grid.add(pulleyCombo, 0, 9);
			//grid.add(pulley, 1, 9);
			grid.add(btn, 1, 9);
			grid.add(timeField,1,10);
<<<<<<< HEAD
			grid.add(boxCombo, 0, 11);
			grid.add(pulleyField, 0, 12);
			grid.add(comboPulley, 1, 12);
=======
			grid.add(clear,0,9);
>>>>>>> branch 'master' of https://github.com/macdows1/Newtons_Laws.git
		}
		else{
			grid.add(newton,0,0,2,3);
			grid.add(mass1, 1, 3);
			grid.add(massCombo, 0, 3);
			grid.add(accel, 1, 4);
			grid.add(tAccel, 0, 4);
			grid.add(tAngle, 0, 5);
			grid.add(angle, 1, 5);
			grid.add(tLength, 0, 6);
			grid.add(length, 1, 6);
			grid.add(frictionCombo, 0, 7);
			grid.add(friction, 1, 7);
<<<<<<< HEAD
			//grid.add(pulleyCombo, 0, 8);
			//grid.add(pulley, 1, 8);
			grid.add(btn, 1, 9);
			grid.add(timeField,1,10);
			grid.add(pulleyField, 0, 12);
			grid.add(comboPulley, 1, 12);
=======
			grid.add(btn, 1, 8);
			grid.add(timeField,1,9);
			grid.add(clear,0,8);
>>>>>>> branch 'master' of https://github.com/macdows1/Newtons_Laws.git
		}
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			public void handle(WindowEvent e){
				Platform.exit();
				t.interrupt();
				time.interrupt();
				
			}
		});



		group.getChildren().add(grid);
		
		primaryStage.setScene(new Scene(group, 1280,1024));
		primaryStage.show();
		
	}
	
	private boolean startAnimation(){
		
		String temp;
		
		temp = accel.getText();
		if(temp.equalsIgnoreCase("delrio")) {
			newton.setImage(new Image(Main.class.getResourceAsStream("Delrio.jpg")));
			timeField.setText("\"Before they discovered the strong force, they didn't know about the strong force\""+
								"\n\t\t-Delrio");
		}
		else if(!temp.equals("")) {
			mAccelG = Double.parseDouble(temp);
			mAccelG = Math.abs(mAccelG);
			temp = "";
		}
		else
			return false;
		
		temp = mass1.getText();
		if(!temp.equals("")){
			mWeight1 = Double.parseDouble(temp);
			if(massIndex == 0){
				mWeight1 = mWeight1*mAccelG;
			}
			temp = "";
		}
		else
			return false;
		if(isPulley) {
			temp = mass2.getText();
			if (!temp.equals("")) {
				mWeight2 = Double.parseDouble(temp);
				if (massIndex == 0) {
					mWeight2 = mWeight2 * mAccelG;
				}
				temp = "";
			} else
				return false;
		}
		
		temp = friction.getText();
		if(!temp.equals("")){
			if(isKinetic == 0){
				uFrk = Double.parseDouble(temp);
			}
			else if(isKinetic == 1){
				uFrs = Double.parseDouble(temp);
			}
			temp = "";
		}
		else
			return false;
		
		temp = angle.getText();
		if(!temp.equals("")){
			dAngle = Double.parseDouble(temp);
			if(dAngle > 90) {
				return false;
			}
			rAngle = (dAngle*Math.PI)/180;
			temp = "";
		}
		else
			return false;
		
		temp = length.getText();
		if(!temp.equals("")){
			mLength = Double.parseDouble(temp);
			temp = "";
		}
		else
			return false;
		
		//System.out.println(mWeight1 + " " + mWeight2 + " " + uFrk + " " + uFrs + " " + mAccelG + " " + mLength + " " + dAngle + " " + rAngle);
		
		return true;
		
	}
	
	private void drawRamp(Stage stage){
		
		int windowH = (int)stage.getHeight();
		int windowW = (int)stage.getWidth();
		
		Image wood = new Image(Main.class.getResourceAsStream("wood_floor.jpg"));
		
		Image pulley = new Image(Main.class.getResourceAsStream("pulley.jpg"));
		
		botLeftx = windowW/2;
		botLefty = (int)(windowH*.8);
		
		double pHeight;

		plane = new Polygon();
		
		pulleyPlane = new Circle();
		
		if(dAngle < 45){
		
			double mWidth = (int) (mLength*Math.cos(rAngle));
			System.out.println("Width" + mWidth + " Angle" + rAngle);
			pConvert = 600/mWidth;
				
			double mHeight = (int)(mLength*Math.sin(rAngle));
			pHeight = mHeight*pConvert;

			plane.getPoints().addAll(new Double[]{
					botLeftx,botLefty,
					botLeftx+600,botLefty,
					botLeftx+600,botLefty-pHeight
			});

			topRightx = botLeftx+600;
			topRighty = botLefty-pHeight;
			
			pulleyCenterX = topRightx + pulleyRadius;
			pulleyCenterY = topRighty - pulleyRadius;
		}
		else{
			int mHeight = (int)(mLength*Math.sin(rAngle));
			pConvert = (botLefty-200)/mHeight;
			
			double mWidth = (int)(mLength*Math.cos(rAngle));
			double pWidth = mWidth*pConvert;
			
			plane.getPoints().addAll(new Double[]{
					botLeftx+600,200.0,
					botLeftx+600,botLefty,
					(botLeftx+600)-pWidth,botLefty
			});

			topRightx = botLeftx + 600;
			topRighty = 200.0;
			
			pulleyCenterX = topRightx + pulleyRadius;
			pulleyCenterY = topRighty - pulleyRadius;
			
		}
		
		plane.setStroke(Color.BLACK);
		
		plane.setFill(new ImagePattern(wood,0,0,2,2,true));
		
		pulleyPlane.setStroke(Color.BLACK);
		
		pulleyPlane.setFill(new ImagePattern(pulley));
		
		if(isPulley){
			pulleyPlane.setCenterX(pulleyCenterX);
			pulleyPlane.setCenterY(pulleyCenterY);
			pulleyPlane.setRadius(pulleyRadius);
		}
		
		System.out.println(hasDrawn);
		
		if(!hasDrawn){
			group.getChildren().add(plane);
			group.getChildren().add(pulleyPlane);
			hasDrawn = true;
		}
		else{
			group.getChildren().remove(plane);
			group.getChildren().add(plane);
			group.getChildren().remove(pulleyPlane);
			group.getChildren().add(pulleyPlane);
		}
		
		
		
	}
	
	private double calculateAcc(){
		if(!isPulley){
		mAccelH = (mAccelG*Math.sin(rAngle))-(uFrk*mAccelG*Math.cos(rAngle));
		} else {
			mAccelH = ((mWeight1*Math.sin(rAngle))-mWeight2-(uFrk*mWeight1*Math.cos(rAngle)))/((mWeight1+mWeight2)/mAccelG);
		}
		pAccel = mAccelH*pConvert;
		pAccelx = pAccel*Math.cos(rAngle);
		pAccely = pAccel*Math.sin(rAngle);

		
		if(!isPulley){
			if(mAccelH < 0)
				return 0;
		}
		//else
			return mAccelH;
	}

	private void drawBox() {

		double rBotLeftx;
		double rBotLefty;
		
		double rBotLeftx2;
		double rBotLefty2;

<<<<<<< HEAD
		double boxHeight = 100;
		double boxWidth = 100;
		
		double ropeBotLeftx;
		double ropeBotLefty;
		
		double ropeHeight = 5;
		
		box2y = 0;
		
		if(heightIndex == 0){
			box2y = topRighty;
		} else if(heightIndex == 1){
			box2y = topRighty + ((0.5)*(botLefty - topRighty));
		} else {
			box2y = botLefty - 100;
		}
		if((box2y + 100) > botLefty){
			box2y = botLefty + 100;
		}
		
		double ropeStartX = pulleyPlane.getCenterX();
		double ropeStartY = pulleyPlane.getCenterY();
		
		if(!isPulley){
			massRect1 = new Polygon();
			massRect1.getPoints().addAll(new Double[] {
					/*bot Right*/ topRightx, topRighty,
					/*bot left*/  rBotLeftx = topRightx - (boxWidth * Math.cos(rAngle)), rBotLefty = topRighty + (boxWidth * Math.sin(rAngle)),
					/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
					/*top right*/ topRightx - (boxHeight * Math.sin(rAngle)), topRighty - (boxHeight * Math.cos(rAngle))
						}
					);
		

			Image metal = new Image(Main.class.getResourceAsStream("metal-texture.jpg"));
=======
		massRect1 = new Polygon();
		massRect1.getPoints().addAll(new Double[] {
		/*bot Right*/ topRightx, topRighty,
		/*bot left*/  rBotLeftx = topRightx - (boxWidth * Math.cos(rAngle)), rBotLefty = topRighty + (boxWidth * Math.sin(rAngle)),
		/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
		/*top right*/ topRightx - (boxHeight * Math.sin(rAngle)), topRighty - (boxHeight * Math.cos(rAngle))
				}
		);
>>>>>>> branch 'master' of https://github.com/macdows1/Newtons_Laws.git

			massRect1.setFill(new ImagePattern(metal));

			massRect1.setStroke(Color.BLACK);

			group.getChildren().add(massRect1);

			System.out.println(String.valueOf(massRect1.getScaleX()));
			System.out.println(String.valueOf(massRect1.localToScene(massRect1.getLayoutBounds())));
		} else {
			
			massRect1 = new Polygon();
			rope1 = new Polygon();
			if(dAngle < 45){
			massRect1.getPoints().addAll(new Double[] {
					/*bot Right*/ (botLeftx + topRightx)/2, topRighty + (0.5*(botLefty - topRighty)),
					/*bot left*/  rBotLeftx = (botLeftx + topRightx)/2 - (boxWidth * Math.cos(rAngle)), rBotLefty = (topRighty + (0.5*(botLefty - topRighty))) + (boxWidth * Math.sin(rAngle)),
					/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
					/*top right*/ (botLeftx + topRightx)/2 - (boxHeight * Math.sin(rAngle)), (topRighty + (0.5*(botLefty - topRighty))) - (boxHeight * Math.cos(rAngle))
						}
					);
			
			//line = new Line();
			
			//line.startXProperty().bind(pulleyPlane.centerXProperty().add(pulleyPlane.translateXProperty()));
			//line.startYProperty().bind(pulleyPlane.centerYProperty().add(pulleyPlane.translateYProperty()));
			//line.setEndX(massRect1.getLayoutBounds().getMaxX());
			//line.setEndY(massRect1.getLayoutBounds().getMaxY());
			
//			rope1.getPoints().addAll(new Double[] {
//					/*bot Right*/ pulleyCenterX + (ropeHeight/2)*Math.cos(rAngle), pulleyCenterY + (ropeHeight/2)*Math.sin(rAngle),
//					/*bot left*/  ropeBotLeftx = ((((botLeftx + topRightx)/2 - (boxHeight * Math.sin(rAngle))) + ((botLeftx + topRightx)/2))/2) + (ropeHeight/2)*Math.cos(rAngle), ropeBotLefty = ((((topRighty + (0.5*(botLefty - topRighty))) - (boxHeight * Math.cos(rAngle)) + (topRighty + (0.5*(botLefty - topRighty))))/2) + (ropeHeight/2)*Math.sin(rAngle)),
//					/*top left*/  ropeBotLeftx - (ropeHeight/2)*Math.sin(rAngle), ropeBotLefty - ((ropeHeight/2) * Math.cos(rAngle)),
//					/*top right*/ pulleyCenterX - (ropeHeight/2)*Math.cos(rAngle), pulleyCenterY - (ropeHeight/2)*Math.sin(rAngle)
//						}
//					);
			
			}
			else {
				massRect1.getPoints().addAll(new Double[] {
						/*bot Right*/ ((botLeftx + 600) - (((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle)))/2)), topRighty + (0.5*(botLefty - topRighty)),
						/*bot left*/  rBotLeftx = ((botLeftx + 600) - (((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle)))/2)) - (boxWidth * Math.cos(rAngle)), rBotLefty = (topRighty + (0.5*(botLefty - topRighty))) + (boxWidth * Math.sin(rAngle)),
						/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
						/*top right*/ ((botLeftx + 600) - (((int)(mLength*Math.cos(rAngle))*(botLefty-200)/(int)(mLength*Math.sin(rAngle)))/2)) - (boxHeight * Math.sin(rAngle)), (topRighty + (0.5*(botLefty - topRighty))) - (boxHeight * Math.cos(rAngle))
							}
						);
//				rope1.getPoints().addAll(new Double[] {
//						/*bot Right*/ pulleyCenterX + (ropeHeight/2)*Math.cos(rAngle), pulleyCenterY + (ropeHeight/2)*Math.sin(rAngle),
//						/*bot left*/  rBotLeftx, rBotLefty,
//						/*top left*/  rBotLeftx - (boxHeight * Math.sin(rAngle)), rBotLefty - (boxHeight * Math.cos(rAngle)),
//						/*top right*/ pulleyCenterX + 2, pulleyCenterY -2
//							}
//						);
				//line = new Line();
				
				//line.startXProperty().bind(pulleyPlane.centerXProperty().add(pulleyPlane.translateXProperty()));
				//line.startYProperty().bind(pulleyPlane.centerYProperty().add(pulleyPlane.translateYProperty()));
				//line.setEndX(massRect1.getLayoutBounds().getMaxX());
				//line.setEndY(massRect1.getLayoutBounds().getMaxY());
			}
		

			Image metal = new Image(Main.class.getResourceAsStream("metal-texture.jpg"));
			Image rope = new Image(Main.class.getResourceAsStream("rope.jpg"));

			massRect1.setFill(new ImagePattern(metal));

			massRect1.setStroke(Color.BLACK);
			
			//rope1.setFill(new ImagePattern(rope));
			
			//rope1.setStroke(Color.BLACK);

			group.getChildren().add(massRect1);
			//group.getChildren().add(line);
			//group.getChildren().add(rope1);

			System.out.println(String.valueOf(massRect1.getScaleX()));
			System.out.println(String.valueOf(massRect1.localToScene(massRect1.getLayoutBounds())));
		
			massRect2 = new Polygon();
			//rope2 = new Polygon();
			
			massRect2.getPoints().addAll(new Double[] {
				/*bot Right*/ topRightx + 100, box2y + 100,
				/*bot left*/  rBotLeftx2 = topRightx, rBotLefty2 = box2y + 100,
				/*top left*/  topRightx, box2y,
				/*top right*/ topRightx + 100, box2y
			});
			
//			rope2.getPoints().addAll(new Double[] {
//					/*bot Right*/ pulleyCenterX + ropeHeight/2, box2y,
//					/*bot left*/  pulleyCenterX - ropeHeight/2, box2y,
//					/*top left*/  pulleyCenterX - ropeHeight/2, pulleyCenterY,
//					/*top right*/ pulleyCenterX + ropeHeight/2, pulleyCenterY
//				});
		
			massRect2.setFill(new ImagePattern(metal));
		
			massRect2.setStroke(Color.BLACK);
			
			//rope2.setFill(new ImagePattern(rope));
			
			//rope2.setStroke(Color.BLACK);
		
			group.getChildren().add(massRect2);
			//group.getChildren().add(rope2);
		}
		

	}

	public double getMinBoxX(){return massRect1.localToScene(massRect1.getLayoutBounds()).getMinX();}

	public double getMaxBoxX(){return massRect1.localToScene(massRect1.getLayoutBounds()).getMaxX();}

	public static double getMinBoxY(){return massRect1.localToScene(massRect1.getLayoutBounds()).getMinY();}

	public static double getMaxBoxY(){return massRect1.localToScene(massRect1.getLayoutBounds()).getMaxY();}
	
	public static double getMaxBoxY2(){
		return massRect2.localToScene(massRect2.getLayoutBounds()).getMaxY();
	}

	public static double getBotLeftY(){return botLefty;}
	
	public static double getTopRightY(){return topRighty;}

	public static void moveRect1X(double deltaX){
		massRect1.setLayoutX(massRect1.getLayoutX()+deltaX);
	}

	public static void moveRect1Y(double deltaY){
		massRect1.setLayoutY(massRect1.getLayoutY()+deltaY);
	}
	
	public static void moveRect2X(double deltaX){
		massRect2.setLayoutX(massRect2.getLayoutX()+deltaX);
	}

	public static void moveRect2Y(double deltaY){
		massRect2.setLayoutY(massRect2.getLayoutY()+deltaY);
	}

	public static void restart(){
		pVelx = 0;
		pVely = 0;
		pAccelx = 0;
		pAccely = 0;
		pConvert = 0;
		mSecs = 0;
		timeField.setText("");
	}

	public static void showTime(){
		System.out.println(mSecs);
		timeField.setText("Time = " + String.format("%.2f",mSecs/1000.0) + " s");
	}

	public static void showVectors(){

		Line gravityV = new Line();
		double startX = botLeftx+
				((boxWidth/2)*Math.cos(rAngle));

		double startY = botLefty -
				((boxWidth/2)*Math.sin(rAngle));
		gravityV.setStartX(startX);
		gravityV.setEndX(startX);
		gravityV.setStartY(startY);
		gravityV.setEndY(startY+50);
		gravityV.setStroke(Color.RED);
		gravityV.setStrokeWidth(5);

		group.getChildren().add(gravityV);

		System.out.println(gravityV.getStartX() + "\n" + gravityV.getStartY());
	}
	
	

}
