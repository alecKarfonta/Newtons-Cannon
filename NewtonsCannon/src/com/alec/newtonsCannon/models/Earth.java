package com.alec.newtonsCannon.models;

import com.alec.newtonsCannon.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Earth {

	private final String TAG = Earth.class.getName();
	private Sprite sprite;
	

	public Earth(World world) {
		init(world);
	}

	public void init(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(0, 0);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 5.5f;
		fixtureDef.friction = .32f;
		fixtureDef.restitution = 0;
		fixtureDef.filter.categoryBits = Constants.FILTER_EARTH;
		fixtureDef.filter.maskBits = Constants.FILTER_CANNONBALL;

		CircleShape shape = new CircleShape();
		shape.setRadius(Constants.EARTH_RADIUS);
		fixtureDef.shape = shape;

		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);

		sprite = new Sprite(Assets.instance.earth.earth);
		sprite.setSize(Constants.EARTH_RADIUS * 2, Constants.EARTH_RADIUS * 2);
		sprite.setOrigin(Constants.EARTH_RADIUS, Constants.EARTH_RADIUS);
		sprite.setPosition(0- Constants.EARTH_RADIUS, 0- Constants.EARTH_RADIUS);

		body.setUserData(this);
	}


	public void render(SpriteBatch batch, float delta) {
		sprite.draw(batch);
	}
}
