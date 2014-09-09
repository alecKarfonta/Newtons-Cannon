package com.alec.newtonsCannon.views;

import java.util.ArrayList;

import com.alec.newtonsCannon.Constants;
import com.alec.newtonsCannon.MyMath;
import com.alec.newtonsCannon.listeners.NewtonsCannonContactListener;
import com.alec.newtonsCannon.models.Assets;
import com.alec.newtonsCannon.models.Cannon;
import com.alec.newtonsCannon.models.CannonBall;
import com.alec.newtonsCannon.models.Earth;
import com.alec.newtonsCannon.models.Moon;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;

public class Play extends AbstractGameScreen {
	private static final String TAG = Play.class.getName();
	
	// world variables
	private DirectedGame game;
	private World world;
	private InputMultiplexer inputPlexer;
	private Box2DDebugRenderer debugRenderer;
	private Stage stage;
	private Table table;
	private Skin skin;
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private ArrayList<Body> destroyQueue;
	private Array<CannonBall> cannonBalls;

	private Sprite background;
	private float backgroundRotation;
	private int zoom = 10;

	// world properties
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8;
	private final int POSITIONITERATONS = 3;
	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	private int right = width / 2 / zoom;
	private int left = -(width / 2 / zoom);
	private int top = height / 2 / zoom;
	private int bottom = -(height / 2 / zoom);

	// solar system stuff
	private float cannonForce = Constants.MIN_CANNON_FORCE; // starting force

	// UI stuff
	private Label forceSliderLabel;
	private Slider forceSlider;

	// world objects
	private Earth earth;
	private Moon moon;
	private Cannon cannon;

	private boolean isDebug = false;

	public Play(DirectedGame game) {
		super(game);
		this.game = game;
		this.skin = Assets.instance.skin; // share the same skin throughout the
	}

	// initialize
	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		cannonBalls = new Array<CannonBall>();
		destroyQueue = new ArrayList<Body>();

		// create each part of the screen
		createWorld();
		createLevel();
		createUI();
		createInputListener();
		
		if (isDebug) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
			Gdx.app.debug(TAG, "Screen Resolution: (" + Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight() + ")");
			//Gdx.app.
		}
	}

	// update and draw
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		// center the camera on the earth
		// camera.position.set(earth.getBody().getPosition(), 0);
		camera.update();

		// add each sprite
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

		backgroundRotation += (delta / 10);
		background.setRotation(backgroundRotation);
		background.draw(spriteBatch);
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATONS);

		earth.render(spriteBatch, delta);
		cannon.render(spriteBatch, delta);
		moon.update(delta);
		moon.render(spriteBatch);

		for (CannonBall cannonBall : cannonBalls) {
			cannonBall.update();
			cannonBall.render(spriteBatch);
		}

		spriteBatch.end();
		if (isDebug) {
			debugRenderer.render(world, camera.combined);
		}

		destroyQueue();
	}

	public void createWorld() {
		// create the world with surface gravity
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new NewtonsCannonContactListener(this));
		debugRenderer = new Box2DDebugRenderer();

		camera = new OrthographicCamera(width, height);

		camera.position.set(0, 0, 0);
	}

	public void createLevel() {
		background = new Sprite(Assets.instance.level.background);
		float backgroundWidth = 2 * Constants.VIEWPORT_WIDTH;
		float backgroundHeight = 2 * Constants.VIEWPORT_HEIGHT;
		background.setBounds(-backgroundWidth / 2, -backgroundHeight / 2 - 50,
				backgroundWidth, backgroundHeight);
		background.setOrigin(backgroundWidth / 2 - 50,
				backgroundHeight / 2 - 50);
		background.setPosition(-backgroundWidth / 2 + 60, -backgroundHeight / 2);
		createEarth();
		moon = new Moon(world);
		createCannon();
	}

	public void createUI() {
		stage = new Stage(width, height, true);
		table = new Table(skin);
		table.setFillParent(true);

		table.add(createForceSlider()).expand().align(Align.center + Align.top);
		
		stage.addActor(table);
	}
	
	public Table createForceSlider () { 
		Table sliderTable = new Table(skin);
		forceSliderLabel = new Label("Force", skin, "medium");

		SliderStyle horizSliderStyle = new SliderStyle();
		horizSliderStyle.background = new SpriteDrawable(new Sprite(
				Assets.instance.ui.sliderBar));
		horizSliderStyle.knob = new SpriteDrawable(new Sprite(
				Assets.instance.ui.slider));
		//horizSliderStyle.knob.setMinHeight(30);
		//horizSliderStyle.knob.setMinWidth(15);
		
		forceSlider = new Slider(Constants.MIN_CANNON_FORCE, Constants.MAX_CANNON_FORCE, .01f, false,
				horizSliderStyle);
		forceSlider.setValue(cannonForce);
		forceSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cannonForce = forceSlider.getValue();
			}

		});
		/**/
		//sliderTable.setFillParent(true);
		sliderTable.row().expand().pad(10, 10, 10, 10);
		sliderTable.add(forceSliderLabel).align(Align.top + Align.center).row();
		sliderTable.add(forceSlider).align(Align.top + Align.center).minWidth(width / 2);
		return sliderTable;
	}

	public void createInputListener() {
		// handle the input
		inputPlexer = new InputMultiplexer(stage,
		// anonymous inner class for screen specific input
				new InputAdapter() {

					// Handle keyboard input
					@Override
					public boolean keyDown(int keycode) {
						switch (keycode) {
						case Keys.SPACE:
							fireCannon();
							break;
						case Keys.UP:
							camera.position.add(0, 10, 0);
							break;
						case Keys.DOWN:
							camera.position.add(0, -10, 0);
							break;
						case Keys.LEFT:
							camera.position.add(-10, 0, 0);
							break;
						case Keys.RIGHT:
							camera.position.add(10, 0, 0);
							break;
						case Keys.ESCAPE:
							((Game) Gdx.app.getApplicationListener())
									.setScreen(new Play(game));
							break;
						}
						return false;
					}

					// zoom
					@Override
					public boolean scrolled(int amount) {
						if (amount == 1) {
							camera.zoom = camera.zoom + (camera.zoom * .05f);
						} else if (amount == -1) {
							camera.zoom = camera.zoom - (camera.zoom * .05f);
						}
						return true;
					}

					// click or touch
					@Override
					public boolean touchDown(int screenX, int screenY,
							int pointer, int button) {
						fireCannon();
						return false;
					}

					@Override
					public boolean touchUp(int x, int y, int pointer, int button) {

						return false;
					}

					@Override
					public boolean touchDragged(int x, int y, int pointer) {

						return false;
					}
				}); // second input adapter for the input multiplexer
	}

	public void createCannon() {
		Vector2 topEdgeOfEarth = MyMath.getRectCoords(Constants.EARTH_RADIUS,
				90);
		cannon = new Cannon(world, topEdgeOfEarth.x - Constants.CANNON_RADIUS,
				topEdgeOfEarth.y);
	}

	public void createEarth() {
		earth = new Earth(world);
	}

	// draw
	public void renderEquations () {
		// TODO
	}
	
	// actions
	public void fireCannon() {
		cannonBalls.add(cannon.fire(world, cannonForce));
	}

	public void collisionEarthCannonBall(CannonBall ball) {
		destroyBody(ball.getBody());
		cannonBalls.removeValue(ball, true);
	}

	public void destroyBody(Body body) {
		if (!destroyQueue.contains(body)) {
			destroyQueue.add(body);
		}
	}

	public void destroyQueue() {
		if (!destroyQueue.isEmpty()) {
			for (Body body : destroyQueue) {
				world.destroyBody(body);
			}
			destroyQueue.clear();
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / zoom;
		camera.viewportHeight = height / zoom;
		stage.setViewport(width, height);
		table.invalidateHierarchy();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		world.dispose();
		debugRenderer.dispose();
		cannon.dispose();
		stage.dispose();
	}

	@Override
	public InputProcessor getInputProcessor() {
		
		return inputPlexer;
	}

}
