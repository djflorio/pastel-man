package com.danflorio.pastelman.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.danflorio.pastelman.entities.Player;

/**
 * The camera that chases the player.
 */

public class ChaseCam {

    public Camera camera;
    public Player target;
    public TiledMap map;

    private float mapWidth;
    private float mapHeight;
    private float padding;


    public ChaseCam() {

    }

    public void init() {
        this.mapWidth = map.getProperties().get("width", Integer.class) * Constants.MAP_TILE_SIZE;
        this.mapHeight = map.getProperties().get("height", Integer.class) * Constants.MAP_TILE_SIZE;
        this.padding = 5;
    }

    public void update(float delta) {

        float lerp = 3f;

        if (target.getPosition().x - camera.viewportWidth / 2 <= 0) {
            camera.position.x = camera.viewportWidth / 2;
        } else if (target.getPosition().x + camera.viewportWidth / 2 >= mapWidth) {
            camera.position.x = mapWidth - camera.viewportWidth / 2;
        } else {
            camera.position.x = target.getPosition().x;
        }

        float playerCamPos = target.getPosition().y + Constants.PLAYER_HEAD_HEIGHT;
        boolean playerCheckBot = playerCamPos - (camera.viewportHeight / 2) <= 0;
        boolean cameraCheckBot = camera.position.y - camera.viewportHeight / 2 <= 0;
        boolean playerCheckTop = playerCamPos >= mapHeight - padding - camera.viewportHeight / 2;
        boolean cameraCheckTop = camera.position.y + camera.viewportHeight / 2 >= mapHeight - padding;

        if (playerCheckBot && cameraCheckBot) {
            camera.position.y = camera.viewportHeight / 2;
        } else if (cameraCheckTop && playerCheckTop) {
            camera.position.y = mapHeight - padding - camera.viewportHeight / 2;
        } else {
            camera.position.y += (
                    target.getPosition().y - camera.position.y + Constants.PLAYER_HEAD_HEIGHT
            ) * lerp * delta;
        }

    }

}
