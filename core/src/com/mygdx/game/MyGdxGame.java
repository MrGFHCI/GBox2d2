// I got this example from http://programmersweb.blogspot.ca/2012/07/simple-libgdx-box2d-bouncing-ball.html
// I had to update "GL10" to "GL20" - that's it.
// time to use an image rather than a circle.

// I copied this code from: http://www.gamefromscratch.com/post/2014/08/27/LibGDX-Tutorial-13-Physics-with-Box2D-Part-1-A-Basic-Physics-Simulations.aspx
// now, I have to integrate the bouncing that takes place in GBox2d2
// don't know if the camera is helping or not. I will figure that out.
// I am trying to integrate how slim the ball bounces relative to the busy render code for the image.

package com.mygdx.game;import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    World world;
    Body body;
    OrthographicCamera camera;
    Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        // don't know if I need a camera - but why not?
        camera = new OrthographicCamera();
        camera.viewportHeight = 320;
        camera.viewportWidth = 480;
        camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);
        camera.update();

        batch = new SpriteBatch();
        // We will use the default LibGdx logo for this example, but we need a
        //sprite since it's going to move
        img = new Texture("badlogic.jpg");
        sprite = new Sprite(img);

        // Center the sprite in the top/middle of the screen
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);

        // Create a physics world, the heart of the simulation.  The Vector
        //passed in is gravity
        world = new World(new Vector2(0, -50f), true);

        //Ground body should be added after camera defn

        BodyDef groundBodyDef =new BodyDef();
        groundBodyDef.position.set(new Vector2(0, 10));
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox((camera.viewportWidth) * 2, 5.0f);
        groundBody.createFixture(groundBox, 0.0f);

        // Now create a BodyDefinition.  This defines the physics objects type
        //and position in the simulation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine
        //is 1 pixel
        // Set our body to the same position as our sprite
        //bodyDef.position.set(sprite.getX(), sprite.getY());
        //bodyDef.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2);
        bodyDef.position.set(camera.viewportWidth / 2, camera.viewportHeight); // start the image higher than above.

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions
        //as our sprite
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the
        //body
        // you also define it's properties like density, restitution and others
        //we will see shortly
        // If you are wondering, density and area are used to calculate over all
        //mass
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = .3f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = .8f; // by switching this from 1 to a value less than one, but bounce has a decay rate.

        Fixture fixture = body.createFixture(fixtureDef);
        debugRenderer = new Box2DDebugRenderer();

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
    }

    @Override
    public void render() {

        // Advance the world, by the amount of time that has elapsed since the
        //last frame
        // Generally in a real game, dont do this in the render loop, as you are
        //tying the physics
        // update rate to the frame rate, and vice versa
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        // Now update the spritee position accordingly to it's now updated
        //Physics body
        sprite.setPosition(body.getPosition().x, body.getPosition().y);

        // You know the rest...
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY());
        batch.end();
        //debugRenderer.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        // Hey, I actually did some clean up in a code sample!
        img.dispose();
        world.dispose();
    }
}
