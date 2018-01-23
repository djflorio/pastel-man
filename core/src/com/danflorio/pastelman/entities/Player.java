package com.danflorio.pastelman.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.danflorio.pastelman.Level;
import com.danflorio.pastelman.utils.Assets;
import com.danflorio.pastelman.utils.Constants;
import com.danflorio.pastelman.utils.Enums.JumpState;
import com.danflorio.pastelman.utils.Enums.RunState;
import com.danflorio.pastelman.utils.Enums.Direction;

/**
 * Class for the player.
 */

public class Player {

    private Level level;
    private Vector2 spawnLocation;
    private Vector2 position;
    private Vector2 lastFramePosition;
    private Vector2 velocity;
    private RunState runState;
    private Direction facing;
    private long runStartTime;
    private float armAngle;
    private Vector2 mouseCoords;
    private Vector2 shoulderCoords;
    private JumpState jumpState;
    private long jumpStartTime;
    private MapObjects groundObjects;
    private Rectangle bounds;
    private Rectangle lastBounds;


    public Player(Level level) {
        this.level = level;
        this.spawnLocation = level.playerSpawn;
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.runState = RunState.NOT_RUNNING;
        this.facing = Direction.RIGHT;
        this.armAngle = 0;
        this.mouseCoords = new Vector2();
        this.shoulderCoords = new Vector2();
        this.jumpState = JumpState.FALLING;
        this.lastFramePosition = new Vector2();
        this.groundObjects = level.groundObjects;
        this.bounds = new Rectangle();
        this.lastBounds = new Rectangle();

        spawn();
    }

    private void spawn() {

        position.set(spawnLocation);
        lastFramePosition.set(spawnLocation);

    }

    public void update(float delta) {

        lastFramePosition.set(position);
        velocity.y -= Constants.PLAYER_GRAVITY;
        position.mulAdd(velocity, delta);

        bounds.x = position.x - Constants.PLAYER_FEET_WIDTH;
        bounds.y = position.y;
        bounds.width = Constants.PLAYER_FEET_WIDTH * 2;
        bounds.height = Constants.PLAYER_HEAD_HEIGHT;

        lastBounds.x = lastFramePosition.x - Constants.PLAYER_FEET_WIDTH;
        lastBounds.y = lastFramePosition.y;
        lastBounds.width = Constants.PLAYER_FEET_WIDTH * 2;
        lastBounds.height = Constants.PLAYER_HEAD_HEIGHT;

        checkGroundCollision();
        handleMousePosition();
        handleInput();

        if (checkEnemyCollision()) {
            level.init();
        }

    }

    public void render(SpriteBatch batch) {

        TextureRegion body = Assets.instance.pastelManAssets.standingRight;
        TextureRegion arm;
        Vector2 shoulder = new Vector2();

        if (facing == Direction.RIGHT) {
            arm = Assets.instance.pastelManAssets.armRight;
            shoulder.set(Constants.PLAYER_SHOULDER_RIGHT);
        } else {
            arm = Assets.instance.pastelManAssets.armLeft;
            shoulder.set(Constants.PLAYER_SHOULDER_LEFT);
            armAngle = -armAngle;
        }

        if (runState == RunState.RUNNING) {
            if (facing == Direction.RIGHT) {
                float runTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - runStartTime);
                body = Assets.instance.pastelManAssets.runningRightAnimation.getKeyFrame(runTimeSeconds);
            } else {
                float runTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - runStartTime);
                body = Assets.instance.pastelManAssets.runningLeftAnimation.getKeyFrame(runTimeSeconds);
            }
        } else if (runState == RunState.NOT_RUNNING) {
            if (facing == Direction.RIGHT) {
                body = Assets.instance.pastelManAssets.standingRight;
            } else {
                body = Assets.instance.pastelManAssets.standingLeft;
            }
        }

        batch.draw(body, position.x - Constants.PLAYER_CENTER.x, position.y);

        batch.draw(
                arm,
                position.x - Constants.PLAYER_CENTER.x,
                position.y,
                shoulder.x,
                shoulder.y,
                arm.getRegionWidth(),
                arm.getRegionHeight(),
                1,
                1,
                armAngle
        );

    }

    public void debugRender(ShapeRenderer debug) {
        debug.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    private void startJump() {
        jumpState = JumpState.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        continueJump();
    }

    private void continueJump() {
        if (jumpState == JumpState.JUMPING) {
            float jumpDuration = MathUtils.nanoToSec * (TimeUtils.nanoTime() - jumpStartTime);
            if (jumpDuration < Constants.MAX_JUMP_DURATION) {
                velocity.y = Constants.JUMP_SPEED;
            } else {
                endJump();
            }
        }
    }

    private void endJump() {
        if (jumpState == JumpState.JUMPING) {
            jumpState = JumpState.FALLING;
        }
    }

    private void handleInput() {

        if (Gdx.input.isKeyPressed(Keys.D) && !Gdx.input.isKeyPressed(Keys.A)) {
            if (runState != RunState.RUNNING) {
                runState = RunState.RUNNING;
                runStartTime = TimeUtils.nanoTime();
            }
            velocity.x = Constants.PLAYER_SPEED;
        } else if (Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)) {
            if (runState != RunState.RUNNING) {
                runState = RunState.RUNNING;
                runStartTime = TimeUtils.nanoTime();
            }
            velocity.x = -Constants.PLAYER_SPEED;
        } else {
            runState = RunState.NOT_RUNNING;
        }

        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            switch(jumpState) {
                case GROUNDED:
                    startJump();
                    break;
                case JUMPING:
                    continueJump();
            }
        } else {
            endJump();
        }

        if (Gdx.input.justTouched()) {
            level.spawnBullet();
        }

    }

    private void handleMousePosition() {
        Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        level.orthoCam.unproject(worldCoords);
        mouseCoords.set(worldCoords.x, worldCoords.y);

        if (mouseCoords.x > position.x) {
            facing = Direction.RIGHT;
            shoulderCoords.set(
                    (position.x - Constants.PLAYER_CENTER.x) + Constants.PLAYER_SHOULDER_RIGHT.x,
                    position.y + Constants.PLAYER_SHOULDER_RIGHT.y);
        } else {
            facing = Direction.LEFT;
            shoulderCoords.set(
                    (position.x - Constants.PLAYER_CENTER.x) + Constants.PLAYER_SHOULDER_LEFT.x,
                    position.y + Constants.PLAYER_SHOULDER_LEFT.y);
        }

        float y = mouseCoords.y - shoulderCoords.y;
        float x = mouseCoords.x - shoulderCoords.x;

        if (facing == Direction.RIGHT) {
            armAngle = MathUtils.atan2(y, x) * MathUtils.radiansToDegrees;
        } else {
            armAngle = MathUtils.atan2(y, -x) * MathUtils.radiansToDegrees;
        }
    }

    private void checkGroundCollision() {

        if (lastFramePosition.y >= 0 && position.y < 0) {
            jumpState = JumpState.GROUNDED;
            velocity.y = 0;
            velocity.x = 0;
            position.y = 0;
        }

        for (MapObject object : groundObjects) {
            float width = object.getProperties().get("width", Float.class);
            float height = object.getProperties().get("height", Float.class);
            float bottom = object.getProperties().get("y", Float.class);
            float top = bottom + height;
            float left = object.getProperties().get("x", Float.class);
            float right = left + width;

            if (lastFramePosition.y >= top && position.y < top) {
                float leftFootPos = bounds.x;
                float rightFootPos = bounds.x + bounds.width;
                if (rightFootPos > left && leftFootPos < right) {
                    jumpState = JumpState.GROUNDED;
                    velocity.y = 0;
                    velocity.x = 0;
                    position.y = top;
                }
            }

            float lastHitboxR = lastBounds.x + lastBounds.width;
            float lastHitboxL = lastBounds.x;
            float hitboxR = bounds.x + bounds.width;
            float hitboxL = bounds.x;


            if (lastHitboxR <= left && hitboxR > left) {
                if (position.y < top && position.y + Constants.PLAYER_HEAD_HEIGHT > bottom) {
                    velocity.x = 0;
                    position.x = left - Constants.PLAYER_FEET_WIDTH;
                }
            }
            if (lastHitboxL >= right && hitboxL < right) {
                if (position.y < top && position.y + Constants.PLAYER_HEAD_HEIGHT > bottom) {
                    velocity.x = 0;
                    position.x = right + Constants.PLAYER_FEET_WIDTH;
                }
            }

        }

    }

    private boolean checkEnemyCollision() {

        for (Robot robot : level.getRobotArray()) {

            if (robot.getBounds().overlaps(bounds)) {
                return true;
            }

        }

        return false;

    }

    public Vector2 getPosition() {
        return position;
    }
    public Direction getFacing() { return facing; }
    public float getArmAngle() { return armAngle; }

}
