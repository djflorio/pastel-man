package com.danflorio.pastelman.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.danflorio.pastelman.utils.Assets;
import com.danflorio.pastelman.Level;
import com.danflorio.pastelman.utils.Constants;

/**
 * The main play screen used to display the actual game play.
 */

public class PlayScreen extends ScreenAdapter {

    private Level level;
    private SpriteBatch batch;
    private ShapeRenderer debug;


    @Override
    public void show() {
        AssetManager assMan = new AssetManager();
        Assets.instance.init(assMan, Constants.LEVELS[0]);
        TiledMap map = Assets.instance.world;
        this.level = new Level(map);
        this.batch = new SpriteBatch();
        this.debug = new ShapeRenderer();
    }

    @Override
    public void resize(int width, int height) {
        level.resize(width, height);
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }

    @Override
    public void render(float delta) {

        level.update(delta);
        level.render(batch, debug);

    }

}
