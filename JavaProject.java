import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;

public class JavaProject extends Application{
   
   //Root component and center GridPane
   BorderPane root = new BorderPane();
   GridPane center = new GridPane();
   
   //ArrayList of GamePanes
   ArrayList <GamePane> balls = new ArrayList <GamePane>();
   
   //Can move variable bank
   ArrayList <GamePane> moveables = new ArrayList <GamePane>();
   
   public void start(Stage stage){
      //Animation Handler
      AnimationHandler ah = new AnimationHandler();
      ah.start();
      
      //Add 16 GamePanes to GridPane
      for (int i = 0; i < 4; i++){
         for (int j = 0; j < 4; j++){
            GamePane toAdd = new GamePane();
            balls.add(toAdd);
            toAdd.setBallNum((i*4)+j);
            center.add(toAdd, j, i);
         }
      }
      
      //Set 0, 2 to inactive on start
      balls.get(8).setVisibility(false);
            
      //Add GridPane to center
      root.setCenter(center);
      root.setAlignment(center, Pos.CENTER);
      
      
      //Set scene
      Scene scene = new Scene(root, 600, 600);
      stage.setScene(scene);
      stage.setTitle("BorderPane");
      stage.show();
      
      //Set focus
      root.requestFocus();
   }
   
   /*GAMEPANE CLASS*/
   public class GamePane extends GridPane{
      
      //Create 4 buttons
      Button top = new Button();
      Button bottom = new Button();
      Button left = new Button();
      Button right = new Button();
      
      //Create canvas for circle
      Canvas canvas = new Canvas();
      
      //Boolean for if ball and buttons are visible (initialize to ball visible, buttons not)
      private boolean ballVisible = true;
      private boolean topVisible;
      private boolean bottomVisible;
      private boolean leftVisible;
      private boolean rightVisible;
      
      //Boolean for if a move can be made
      private boolean canMove;
      
      //Ball number
      private int ballNum;
      
            
      /*Constructor*/
      public GamePane(){
         //Set GamePane size
         setMinHeight(150);
         setMinWidth(150);
         
         //Set button dimenstions
         top.setPrefSize(80, 20);
         bottom.setPrefSize(80, 20);
         left.setPrefSize(20, 80);
         right.setPrefSize(20, 80);
         
         //Add buttons
         add(top, 1, 0);
         add(bottom, 1, 2);
         add(left, 0, 1);
         add(right, 2, 1);
         
         //Add circle
         add(canvas, 1, 1);
         
         //Draw
         draw();
         
         //Button stuff
         ButtonListener bl = new ButtonListener();
         top.setOnAction(bl);
         bottom.setOnAction(bl);
         left.setOnAction(bl);
         right.setOnAction(bl);
      }
      
      /*DRAW METHOD*/
      public void draw(){
         //GraphicsContext, initialize by ClearRect
         GraphicsContext gc = canvas.getGraphicsContext2D();
         gc.clearRect(0, 0, 150, 150);
         
         //If the ball is visible, draw it
         if (ballVisible){
            //Create circle
            canvas.setHeight(80);
            canvas.setWidth(80);
            gc.setFill(Color.BLACK);
            gc.fillOval(0, 0, 80, 80);
         }
         
         //Set button visibility
         top.setVisible(topVisible);
         bottom.setVisible(bottomVisible);
         left.setVisible(leftVisible);
         right.setVisible(rightVisible);
      }
      
      public void setVisibility(boolean visibility){
         //Set visibility
         ballVisible = visibility;
         
         //Redraw
         draw();
      }
      
      public boolean visible(){
         return ballVisible;
      }
      
      public void setBallNum(int ballNum){
         this.ballNum = ballNum;
      }
      
      public void setButtonVisibility(int button, boolean visibility){
         //If ball is visible
         if (ballVisible){
            switch (button){
               case 0: topVisible = visibility;
                  break;
               case 1: bottomVisible = visibility;
                  break;
               case 2: leftVisible = visibility;
                  break;
               case 3: rightVisible = visibility;
                  break;
            }
         //If ball is not visible
         }else{
            topVisible = false;
            bottomVisible = false;
            leftVisible = false;
            rightVisible = false;
         }
      }
      
      /*BUTTON LISTENER*/
      public class ButtonListener implements EventHandler<ActionEvent>{
         public void handle(ActionEvent e){
            //If top botton is pressed
            if (e.getSource() == top){
               //Set current button to be invisible
               setVisibility(false);
               
               //Set "jumped" button to be invisible
               balls.get(ballNum+4).setVisibility(false);
               
               //Set "landing spot" to be visible
               balls.get(ballNum+8).setVisibility(true);
            }
            //If bottom botton is pressed
            if (e.getSource() == bottom){
               //Set current button to be invisible
               setVisibility(false);
               
               //Set "jumped" button to be invisible
               balls.get(ballNum-4).setVisibility(false);
               
               //Set "landing spot" to be visible
               balls.get(ballNum-8).setVisibility(true);
            }
            //If top left is pressed
            if (e.getSource() == left){
               //Set current button to be invisible
               setVisibility(false);
               
               //Set "jumped" button to be invisible
               balls.get(ballNum+1).setVisibility(false);
               
               //Set "landing spot" to be visible
               balls.get(ballNum+2).setVisibility(true);
            }
            //If right botton is pressed
            if (e.getSource() == right){
               //Set current button to be invisible
               setVisibility(false);
               
               //Set "jumped" button to be invisible
               balls.get(ballNum-1).setVisibility(false);
               
               //Set "landing spot" to be visible
               balls.get(ballNum-2).setVisibility(true);
            }
            //Redraw
            for (int i = 0; i < 16; i++){
               draw();
               canMove(i);
            }
         }
      }
   }
   
   public int canMove(int ball){
      //Initialize moves
      int move = 0;
      
      //Ball is in range to move up?
      if (ball-8 >= 0){
         //If ball can move up, space avaliable?
         if (!balls.get(ball-8).visible() && balls.get(ball-4).visible()){
            //If not visible, add a button
            balls.get(ball).setButtonVisibility(1, true);
            if (balls.get(ball).visible())
               move++;
         //If no space avaliable
         }else{
            //Invisible button
            balls.get(ball).setButtonVisibility(1, false);
         }
      //If not in range to move up, don't show button
      }else{
         //Invisible button
         balls.get(ball).setButtonVisibility(1, false);
      }
      
      //Ball is in range to move down?
      if (ball+8 < 16){
         //If ball can move down, space avaliable?
         if (!balls.get(ball+8).visible() && balls.get(ball+4).visible()){
            //If not visible, add a button
            balls.get(ball).setButtonVisibility(0, true);
            if (balls.get(ball).visible())
               move++;
         //If no space avaliable
         }else{
            //Invisible button
            balls.get(ball).setButtonVisibility(0, false);
         }
      //If not in range to move down, don't show button
      }else{
         //Invisible button
         balls.get(ball).setButtonVisibility(0, false);
      }
      
      //Ball is in range to move right and stay on the same row?
      if ((ball+2 < 16) && ((ball%4)+2 < 4) && balls.get(ball+1).visible()){
         //If ball can move right, space avaliable?
         if (!balls.get(ball+2).visible()){
            //If not visible, add a button
            balls.get(ball).setButtonVisibility(2, true);
            if (balls.get(ball).visible())
               move++;
         //If no space avaliable
         }else{
            //Invisible button
            balls.get(ball).setButtonVisibility(2, false);
         }
      //If not in range to move right, don't show button
      }else{
         //Invisible button
         balls.get(ball).setButtonVisibility(2, false);
      }
      
      //Ball is in range to move left and stay on the same row?
      if ((ball-2 >= 0) && ((ball%4)-2 >= 0) && balls.get(ball-1).visible()){
         //If ball can move left, space avaliable?
         if (!balls.get(ball-2).visible()){
            //If not visible, add a button
            balls.get(ball).setButtonVisibility(3, true);
            if (balls.get(ball).visible())
               move++;
         //If no space avaliable
         }else{
            //Invisible button
            balls.get(ball).setButtonVisibility(3, false);
         }
      //If not in range to move left, don't show button
      }else{
         //Invisible button
         balls.get(ball).setButtonVisibility(3, false);
      }
      
      balls.get(ball).draw();
      
      //Return moves
      return move;
   }
   
   public class AnimationHandler extends AnimationTimer{
      public void handle(long currentTimeInNanoSeconds){
         
         //Add buttons with canMove method
         int move = 0;
         int ballsLeft = 0;
         //Redraw
         for (int i = 0; i < 16; i++){
            move+=canMove(i);
            if (balls.get(i).visible()){
               ballsLeft++;
            }
         }
         
         Label top;
         if (ballsLeft == 1){
            top = new Label("YOU WIN!!!");
         }else if (move > 0){
            top = new Label("Balls Left: "+ballsLeft+"\t\tPossible Moves: "+move);
         }else{
            top = new Label("GAME OVER");
         }
                 
         //Add label to top
         root.setTop(top);
         root.setAlignment(top, Pos.CENTER);
         
         
      }
   }
     
   /*MAIN*/
   public static void main(String[] args){
      launch(args);
   }
}