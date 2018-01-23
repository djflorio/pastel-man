package com.danflorio.pastelman.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.danflorio.pastelman.utils.Assets;
import com.danflorio.pastelman.utils.Constants;
import com.danflorio.pastelman.utils.Enums;

/**
 * Class for the Robot enemies.
 */

public class Robot {

    private Vector2 position;
    private Vector2 startPosition;
    private Vector2 velocity;
    private boolean angered;
    private long floatStartTime;
    private MapObjects pathEnds;
    private Rectangle bounds;
    private Circle sight;
    private boolean hitBounds;
    private boolean goingHome;
    private int health;
    public boolean alive;
    private Enums.Direction facing;

    public Robot(Vector2 startPosition, MapObjects pathEnds) {
        this.startPosition = new Vector2(startPosition);
        this.position = new Vector2(startPosition);
        this.velocity = new Vector2(15, 0);
        this.angered = false;
        this.floatStartTime = TimeUtils.nanoTime();
        this.pathEnds = pathEnds;
        this.bounds = new Rectangle();
        this.hitBounds = false;
        this.sight = new Circle();
        this.goingHome = false;
        this.health = 3;
        this.alive = true;
        this.facing = Enums.Direction.LEFT;
        sight.radius = Constants.ROBO_SIGHT_RADIUS;
    }

    public void update(float delta, Vector2 playerPos) {
        bounds.x = position.x;
        bounds.y = position.y;
        bounds.width = 10;
        bounds.height = 24;

        sight.x = position.x;
        sight.y = position.y;

        if (!angered && !goingHome) {
            for (MapObject pathEnd : pathEnds) {
                if (bounds.contains(
                        pathEnd.getProperties().get("x", Float.class),
                        pathEnd.getProperties().get("y", Float.class)) && !hitBounds) {
                    hitBounds = true;
                    velocity.x *= -1;
                } else if (!bounds.contains(
                        pathEnd.getProperties().get("x", Float.class),
                        pathEnd.getProperties().get("y", Float.class))) {
                    hitBounds = false;
                }
            }
        } else if (angered) {
            float vx = playerPos.x - position.x;
            float vy = playerPos.y - position.y;
            float angle = MathUtils.atan2(vy, vx) * MathUtils.radiansToDegrees;
            velocity.set(
                    (float) Math.cos(Math.toRadians(angle)) * 50,
                    (float) Math.sin(Math.toRadians(angle)) * 50
            );
        } else {
            float vx = startPosition.x - position.x;
            float vy = startPosition.y - position.y;
            float angle = MathUtils.atan2(vy, vx) * MathUtils.radiansToDegrees;

            if (Math.abs(vx) < 0.5 && Math.abs(vy) < 0.5) {
                position.set(startPosition);
                velocity.set(15,0);
                goingHome = false;
            } else {
                velocity.set(
                        (float) Math.cos(Math.toRadians(angle)) * 50,
                        (float) Math.sin(Math.toRadians(angle)) * 50
                );
            }
        }

        position.mulAdd(velocity, delta);
        if (velocity.x < 0) {
            facing = Enums.Direction.LEFT;
        }

        if (velocity.x > 0) {
            facing = Enums.Direction.RIGHT;
        }
    }

    public void render(SpriteBatch batch) {
        float floatTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - floatStartTime);
        TextureRegion texture;
        if (!angered) {
            if (facing == Enums.Direction.LEFT) {
                texture = Assets.instance.robotAssets.calmFloatLeft.getKeyFrame(floatTimeSeconds);
            } else {
                texture = Assets.instance.robotAssets.calmFloatRight.getKeyFrame(floatTimeSeconds);
            }
        } else {
            if (facing == Enums.Direction.LEFT) {
                texture = Assets.instance.robotAssets.angryFloatLeft.getKeyFrame(floatTimeSeconds);
            } else {
                texture = Assets.instance.robotAssets.angryFloatRight.getKeyFrame(floatTimeSeconds);
            }
        }
        batch.draw(
                texture,
                position.x,
                position.y,
                texture.getRegionWidth(),
                texture.getRegionHeight());
    }

    public void debugRender(ShapeRenderer debug) {
        debug.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setAngered(boolean a) {
        if (angered && !a) {
            goingHome = true;
        }
        angered = a;
    }

    public Circle getSight() {
        return sight;
    }

    public void getHit() {
        health -= 1;
        if (health <= 0) {
            alive = false;
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

}
