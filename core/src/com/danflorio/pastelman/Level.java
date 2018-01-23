package com.danflorio.pastelman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.danflorio.pastelman.entities.Bullet;
import com.danflorio.pastelman.entities.Robot;
import com.danflorio.pastelman.entities.Player;
import com.danflorio.pastelman.utils.ChaseCam;
import com.danflorio.pastelman.utils.Constants;
import com.danflorio.pastelman.utils.Enums;

/**
 * Class that all levels are made from.
 */

public class Level {

    private Viewport viewport;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public OrthographicCamera orthoCam;
    private Player player;
    private DelayedRemovalArray<Robot> robotArray;
    private DelayedRemovalArray<Bullet> bulletsArray;
    public Vector2 playerSpawn;
    public MapObjects groundObjects;
    private ChaseCam chaseCam;
    private boolean debugMode;


    public Level(TiledMap map) {
        this.map = map;

        this.renderer = new OrthogonalTiledMapRenderer(map, 1);
        this.orthoCam = new OrthographicCamera();
        this.viewport = new FitViewport(Constants.WORLD_SIZE_X, Constants.WORLD_SIZE_Y, orthoCam);
        this.debugMode = true;

        init();

    }

    public void init() {

        MapLayer spawnLayer = map.getLayers().get("spawn");
        this.groundObjects = map.getLayers().get("ground").getObjects();
        MapObjects pathEnds = map.getLayers().get("pathEnds").getObjects();
        MapObjects robotObjects = map.getLayers().get("robots").getObjects();

        MapObject playerSpawnObject = spawnLayer.getObjects().get("Player");

        this.playerSpawn = new Vector2(
                playerSpawnObject.getProperties().get("x", Float.class),
                playerSpawnObject.getProperties().get("y", Float.class)
        );

        this.robotArray = new DelayedRemovalArray<Robot>();
        for (MapObject robot : robotObjects) {
            Vector2 robotSpawn = new Vector2(
                    robot.getProperties().get("x", Float.class),
                    robot.getProperties().get("y", Float.class)
            );
            robotArray.add(new Robot(robotSpawn, pathEnds));
        }

        this.bulletsArray = new DelayedRemovalArray<Bullet>();

        this.player = new Player(this);

        this.chaseCam = new ChaseCam();
        orthoCam.update();
        orthoCam.position.y = player.getPosition().y;
        chaseCam.camera = orthoCam;
        chaseCam.map = map;
        chaseCam.init();
        chaseCam.target = player;


    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void update(float delta) {

        orthoCam.update();
        player.update(delta);
        robotArray.begin();
        for (int i = 0; i < robotArray.size; i++) {
            Robot robot = robotArray.get(i);
            robot.update(delta, player.getPosition());
            if (!robot.alive) {
                robotArray.removeIndex(i);
            }

            if (robot.getSight().contains(player.getPosition())) {
                robot.setAngered(true);
            } else {
                robot.setAngered(false);
            }
        }
        robotArray.end();

        bulletsArray.begin();
        for (int i = 0; i < bulletsArray.size; i++) {
            Vector2 playerPos = player.getPosition();
            Bullet bullet = bulletsArray.get(i);
            bullet.update(delta);

            boolean outRight = bullet.getPosition().x > playerPos.x + viewport.getWorldWidth();
            boolean outLeft = bullet.getPosition().x < 0;
            boolean outTop = bullet.getPosition().y > playerPos.y + viewport.getWorldHeight();
            boolean outBottom = bullet.getPosition().y < 0;
            boolean hitGround = checkBulletCollision(bullet);
            boolean hitEnemy = checkEnemyCollision(bullet);

            boolean kill = outRight || outLeft || outTop || outBottom || hitGround || hitEnemy;

            if (kill) {
                bulletsArray.removeIndex(i);
            }
        }
        bulletsArray.end();

        chaseCam.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer debug) {

        viewport.apply();
        batch.setProjectionMatrix(orthoCam.combined);

        Gdx.gl.glClearColor(0,0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(orthoCam);
        //renderer.setBlending(true);
        renderer.render();

        batch.begin();
        player.render(batch);
        for (Robot robot : robotArray) {
            robot.render(batch);
        }
        for (Bullet bullet : bulletsArray) {
            bullet.render(batch);
        }
        batch.end();

        if (debugMode) {
            debug.setProjectionMatrix(orthoCam.combined);
            debug.begin(ShapeRenderer.ShapeType.Line);
            for (Robot robot : robotArray) {
                robot.debugRender(debug);
            }
            player.debugRender(debug);
            debug.end();
        }

    }

    public void spawnBullet() {
        Vector2 bulletSpawn = new Vector2();
        float bulletAngle = player.getArmAngle() + 6;

        bulletSpawn.set(
                player.getPosition().x - Constants.PLAYER_CENTER.x + Constants.PLAYER_SHOULDER_RIGHT.x,
                player.getPosition().y + Constants.PLAYER_SHOULDER_RIGHT.y
        );

        double xOff = Math.cos(Math.toRadians(bulletAngle)) * Constants.PLAYER_ARM_LENGTH;
        double yOff = Math.sin(Math.toRadians(bulletAngle)) * Constants.PLAYER_ARM_LENGTH;

        if (player.getFacing() == Enums.Direction.RIGHT) {
            bulletSpawn.x += xOff;
        } else {
            bulletSpawn.x -= xOff;
        }
        bulletSpawn.y += yOff;

        Bullet bullet = new Bullet(bulletSpawn, player.getArmAngle(), player.getFacing());
        bulletsArray.add(bullet);
    }

    private boolean checkEnemyCollision(Bullet bullet) {

        for (Robot robot : robotArray) {
            if (robot.getBounds().contains(bullet.getPosition())) {
                robot.getHit();
                return true;
            }
        }
        return false;

    }

    private boolean checkBulletCollision(Bullet bullet) {

        for (MapObject ground : groundObjects) {
            float width = ground.getProperties().get("width", Float.class);
            float height = ground.getProperties().get("height", Float.class);
            float bottom = ground.getProperties().get("y", Float.class);
            float left = ground.getProperties().get("x", Float.class);

            Rectangle groundRect = new Rectangle(left, bottom, width, height);

            if (groundRect.contains(bullet.getPosition())) {
                return true;
            }
        }
        return false;

    }

    public DelayedRemovalArray<Robot> getRobotArray() {
        return robotArray;
    }

}
