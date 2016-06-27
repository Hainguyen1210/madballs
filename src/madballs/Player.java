/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madballs;

/**
 *
 * @author Caval
 */
public class Player {
    public Ball ball;
    
    public void generateBall(Environment environment, double x, double y){
        ball = new Ball(environment, x,y);
    }
}
