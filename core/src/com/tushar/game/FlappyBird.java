package com.tushar.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

import javax.xml.soap.Text;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bottomtube;
	Texture toptube;
	Texture gameOver;
    //ShapeRenderer shapeRenderer ;

	Texture[] birds;

	Circle birdircle ;

	int flappyState = 0;
	float birdY = 0;
	float velocity = 0;
	int gamestate = 0;
	float gravity = 1;
	float gap = 400;
	int score = 0;
	int scoringTube = 0;
	float maxTubeOffSet;
	Random randomGenerator;

    float tubeVelocity = 4;
    int numberOfTubes = 4;
    float distanceBetweenTubes;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffSet = new float[numberOfTubes];
    Rectangle[]  topTubeRectangles;
    Rectangle[] bottomTubeRectangles;
    BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("Bg.png");

		birds = new Texture[2];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		birds[0] = new Texture("bird3.png");
		birds[1] = new Texture("bird4.png");
		gameOver = new Texture("gameover.png");

        bottomtube =  new Texture("toppipe.png");
        toptube =  new Texture("bottompipe.png");
        maxTubeOffSet = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
        birdircle = new Circle();
        //shapeRenderer = new ShapeRenderer();
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        gameStart();


	}

	public void gameStart(){
        birdY = Gdx.graphics.getHeight() /2 - birds[0].getHeight() / 2;
        for (int i = 0; i< numberOfTubes; i++){
            tubeOffSet[i] = (randomGenerator.nextFloat() - 0.2f) * (Gdx.graphics.getHeight() - gap - 1200) ;
            tubeX[i] = Gdx.graphics.getWidth()/2-toptube.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

	@Override
	public void render () {


        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gamestate == 1) {

            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
                score++;
                Gdx.app.log("Score",String.valueOf(score));
                if (scoringTube < numberOfTubes - 1)
                {
                    scoringTube++;
                }else
                {
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched()){
                velocity = -20;

            }

            for (int i = 0; i< numberOfTubes; i++) {

                if (tubeX[i] < - toptube.getWidth()){
                    tubeX[i] = numberOfTubes * distanceBetweenTubes;
                    tubeOffSet[i] = (randomGenerator.nextFloat() - 0.2f) * (Gdx.graphics.getHeight() - gap - 1200) ;
                }else {

                    tubeX[i] = tubeX[i] - tubeVelocity;
                }
                batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i]);
                batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i]);

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],toptube.getWidth(),toptube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i],bottomtube.getWidth(),bottomtube.getHeight());
            }
            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY -= velocity;
            }
            else {
                gamestate = 2;
            }

        }else if (gamestate == 0)
        {
            if (Gdx.input.justTouched()){
                gamestate = 1;
            }
        }
        else if (gamestate == 2){
            batch.draw(gameOver,Gdx.graphics.getWidth() /2 - gameOver.getWidth() / 2,Gdx.graphics.getHeight() /2 - gameOver.getHeight() / 2);
            if (Gdx.input.justTouched()){
                gamestate = 1;
                gameStart();
                score = 0;
                scoringTube =0 ;
                velocity = 0;

            }
        }

        if (flappyState == 0) {
            flappyState = 1;
        } else {
            flappyState = 0;
        }

        batch.draw(birds[flappyState], Gdx.graphics.getWidth() / 2 - birds[flappyState].getWidth() / 2, birdY);
        font.draw(batch,String.valueOf(score),100,200);
        batch.end();

        birdircle.set(Gdx.graphics.getWidth() / 2,birdY + birds[flappyState].getHeight() / 2,birds[flappyState].getWidth() / 2);

      //  shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      //  shapeRenderer.setColor(Color.RED);
      //  shapeRenderer.circle(birdircle.x,birdircle.y,birdircle.radius);
        for (int i = 0; i< numberOfTubes; i++) {
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],toptube.getWidth(),toptube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i],bottomtube.getWidth(),bottomtube.getHeight());

            if (Intersector.overlaps(birdircle, topTubeRectangles[i]) || Intersector.overlaps(birdircle,bottomTubeRectangles[i])){
                gamestate = 2;
            }
        }
       // shapeRenderer.end();
    }
	

}
