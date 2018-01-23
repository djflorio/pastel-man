package com.danflorio.pastelman.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.danflorio.pastelman.utils.Assets;
import com.danflorio.pastelman.utils.Constants;
import com.danflorio.pastelman.utils.Enums;

/**
 * Class for all bullets.
 */

public class Bullet {

    private Vector2 velocity;
    private Vector2 position;
    private float angle;
    private TextureRegion texture;

    public Bullet(Vector2 position, float angle, Enums.Direction facing) {
        this.position = new Vector2(position);
        this.angle = angle;
        this.velocity = new Vector2(
                (float) Math.cos(Math.toRadians(angle)) * Constants.BULLET_SPEED,
                (float) Math.sin(Math.toRadians(angle)) * Constants.BULLET_SPEED
        );
        if (facing == Enums.Direction.LEFT) {
            velocity.x *= -1;
            this.angle = 180 - angle;
        }
        this.texture = Assets.instance.bulletAssets.bullet;
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
    }

    public void render(SpriteBatch batch) {
        batch.draw(
                texture,
                position.x,
                position.y,
                texture.getRegionWidth() / 2,
                texture.getRegionHeight() / 2,
                texture.getRegionWidth(),
                texture.getRegionHeight(),
                1,
                1,
                angle);
    }

    public Vector2 getPosition() {
        return position;
    }

}
