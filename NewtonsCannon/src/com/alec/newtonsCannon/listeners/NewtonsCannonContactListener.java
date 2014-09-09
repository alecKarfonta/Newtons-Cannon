package com.alec.newtonsCannon.listeners;

import com.alec.newtonsCannon.models.CannonBall;
import com.alec.newtonsCannon.models.Earth;
import com.alec.newtonsCannon.views.Play;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class NewtonsCannonContactListener implements ContactListener {

	Play game;

	public NewtonsCannonContactListener(Play newtonsCannon) {
		this.game = newtonsCannon;
	}

	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		if (contact.getFixtureA().getBody().getUserData() != null
				&& contact.getFixtureB().getBody().getUserData() != null) {
			
			// earth - cannon ball
			if (contact.getFixtureA().getBody().getUserData() instanceof Earth
					&& contact.getFixtureB().getBody().getUserData() instanceof CannonBall) {
				game.collisionEarthCannonBall((CannonBall) contact
						.getFixtureB().getBody().getUserData());
			}
			
			
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
