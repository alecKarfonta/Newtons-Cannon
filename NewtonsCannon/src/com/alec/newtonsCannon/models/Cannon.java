package com.alec.newtonsCannon.models;

import com.alec.newtonsCannon.Constants;
import com.alec.newtonsCannon.GamePreferences;
import com.alec.newtonsCannon.MyMath;
import com.alec.newtonsCannon.controllers.AudioManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Cannon {
	public static final String TAG = Cannon.class.getName();
	
	public Body body;
	public Sprite sprite;
	private Vector2 cannonBallOrigin;
	public ParticleEffect cannonFireParticles;
	float radius;

	public Cannon(World world, float x, float y) {

		PolygonShape cannonShape = new PolygonShape();
		float cannonWidth = Constants.EARTH_RADIUS * .25f;
		radius = cannonWidth /2;
		cannonShape.setAsBox(cannonWidth, cannonWidth / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0;
		fixtureDef.restitution = 0;
		fixtureDef.friction = 0;
		fixtureDef.shape = cannonShape;
		fixtureDef.filter.categoryBits = Constants.FILTER_NONE;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(x, y);

		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);

		sprite = new Sprite(Assets.instance.cannon.cannon);
		sprite.setOrigin(0, 0);
		sprite.setSize(cannonWidth * 2, cannonWidth);
		body.setUserData(this);
		
		cannonBallOrigin = new Vector2(body.getPosition().x + radius * 2,
				body.getPosition().y + .8f);
		
		cannonFireParticles = new ParticleEffect();
		cannonFireParticles.load(Gdx.files.internal("particles/cannonFire.pfx"),
				Gdx.files.internal("particles"));
		cannonFireParticles.setPosition(cannonBallOrigin.x, cannonBallOrigin.y);
	}
	
	public CannonBall fire(World world, float force) {
		CannonBall ball = new CannonBall(world, 
				cannonBallOrigin);
		Gdx.app.error(TAG, "force: " + force );
		ball.getBody().setLinearVelocity((float) Math.pow(force, 2.0f), 0);
		cannonFireParticles.start();
		AudioManager.instance.play(Assets.instance.sounds.cannon, 
				MathUtils.clamp(
						MyMath.scaleValue(force, Constants.MIN_CANNON_FORCE, Constants.MAX_CANNON_FORCE),
						.25f,
						1.0f) );
		return ball;
	}

	public void render(SpriteBatch batch, float delta) {
		sprite.setPosition(body.getPosition().x - radius * 2 ,
				body.getPosition().y - radius);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		sprite.draw(batch);
		
		// cannon fire
		if (	!cannonFireParticles.isComplete()) {
			cannonFireParticles.draw(batch, delta);
		}
	}
	
	public void dispose() {
		cannonFireParticles.dispose();
	}
}
