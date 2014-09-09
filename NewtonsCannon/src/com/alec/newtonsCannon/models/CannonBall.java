package com.alec.newtonsCannon.models;

import com.alec.newtonsCannon.Constants;
import com.alec.newtonsCannon.MyMath;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class CannonBall {
	private Body body;
	private Sprite sprite;
	private float radius;
	private Vector2 orbitTarget = new Vector2(0,0);
	
	public CannonBall(World world, Vector2 origin) {
	    BodyDef bodyDef;
		FixtureDef fixtureDef;
		Shape shape;
		bodyDef = new BodyDef();
		bodyDef.position.set(origin);
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.allowSleep = false;
		
		radius = .5f;
		shape = new CircleShape();
		shape.setRadius(radius);
		
		fixtureDef  = new FixtureDef();
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0; // inellastic collisions
		fixtureDef.density = 3.5f;
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.FILTER_CANNONBALL;
		fixtureDef.filter.maskBits = Constants.FILTER_CANNONBALL | Constants.FILTER_EARTH;		
		sprite = new Sprite(Assets.instance.cannon.cannonBall);
		sprite.setSize(radius * 2, radius * 2);
		sprite.setOrigin(radius, radius);
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.setUserData(this);
	}
	
	public void render (SpriteBatch batch) {
		sprite.setPosition(body.getWorldCenter().x - radius, 
				body.getWorldCenter().y - radius);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		sprite.draw(batch);
	}
	
	public void update () {
		Vector2 forceVectorPolar = new Vector2();
		// rho = (G * m1 * m2) / d^2
		forceVectorPolar.x = (float) ((body.getMass() * 20000)		// earth is static so mass is infinite, cant use in equation
				/ Math.pow(body.getPosition().dst(orbitTarget), 2)); // magnitude
		// theta = angle between body and earth
		forceVectorPolar.y = MyMath.getAngleBetween(body.getPosition(),
				orbitTarget); // direction
		body.applyForceToCenter(MyMath.getRectCoords(forceVectorPolar), false);
	}
	
	public Body getBody() {
		return this.body;
	}
	
	public float getRadius() {
		return this.radius;
	}
}
