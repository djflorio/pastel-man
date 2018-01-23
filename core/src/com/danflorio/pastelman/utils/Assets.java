package com.danflorio.pastelman.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Asset manager for the game.
 */

public class Assets implements Disposable, AssetErrorListener {

    private static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    public PastelManAssets pastelManAssets;
    public BulletAssets bulletAssets;
    public RobotAssets robotAssets;
    public TiledMap world;

    private AssetManager assMan;

    private Assets() {}

    public void init(AssetManager assMan, String map) {
        this.assMan = assMan;
        assMan.setErrorListener(this);
        assMan.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assMan.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assMan.load(map, TiledMap.class);
        assMan.finishLoading();
        world = assMan.get(map);
        // TODO: Possibly load asynchronously.
        assMan.finishLoading();

        TextureAtlas atlas = assMan.get(Constants.TEXTURE_ATLAS);



        pastelManAssets = new PastelManAssets(atlas);
        bulletAssets = new BulletAssets(atlas);
        robotAssets = new RobotAssets(atlas);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable);
    }



    public class PastelManAssets {

        public final AtlasRegion standingRight;
        public final AtlasRegion standingLeft;
        public final AtlasRegion armRight;
        public final AtlasRegion armLeft;
        public final Animation<TextureRegion> runningRightAnimation;
        public final Animation<TextureRegion> runningLeftAnimation;

        PastelManAssets(TextureAtlas atlas) {

            standingRight = atlas.findRegion(Constants.STANDING_RIGHT);
            standingLeft = atlas.findRegion(Constants.STANDING_LEFT);
            armRight = atlas.findRegion(Constants.ARM_RIGHT);
            armLeft = atlas.findRegion(Constants.ARM_LEFT);

            Array<AtlasRegion> runningRightFrames = new Array<AtlasRegion>();
            runningRightFrames.add(atlas.findRegion(Constants.RUNNING_RIGHT_1));
            runningRightFrames.add(atlas.findRegion(Constants.RUNNING_RIGHT_2));
            runningRightFrames.add(atlas.findRegion(Constants.RUNNING_RIGHT_3));
            runningRightAnimation = new Animation<TextureRegion>(
                    Constants.RUN_LOOP_DURATION, runningRightFrames, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> runningLeftFrames = new Array<AtlasRegion>();
            runningLeftFrames.add(atlas.findRegion(Constants.RUNNING_LEFT_1));
            runningLeftFrames.add(atlas.findRegion(Constants.RUNNING_LEFT_2));
            runningLeftFrames.add(atlas.findRegion(Constants.RUNNING_LEFT_3));
            runningLeftAnimation = new Animation<TextureRegion>(
                    Constants.RUN_LOOP_DURATION, runningLeftFrames, PlayMode.LOOP_PINGPONG);

        }

    }

    public class BulletAssets {

        public final AtlasRegion bullet;

        BulletAssets(TextureAtlas atlas) {
            bullet = atlas.findRegion(Constants.BULLET);
        }

    }

    public class RobotAssets {

        public final Animation<TextureRegion> angryFloatRight;
        public final Animation<TextureRegion> calmFloatRight;
        public final Animation<TextureRegion> angryFloatLeft;
        public final Animation<TextureRegion> calmFloatLeft;

        RobotAssets(TextureAtlas atlas) {

            Array<AtlasRegion> angryFloatLeftFrames = new Array<AtlasRegion>();
            angryFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_4_LEFT));
            angryFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_5_LEFT));
            angryFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_1_LEFT));
            angryFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_2_LEFT));
            angryFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_3_LEFT));
            angryFloatLeft = new Animation<TextureRegion>(
                    Constants.ROBO_BOUNCE_DURATION, angryFloatLeftFrames, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> angryFloatRightFrames = new Array<AtlasRegion>();
            angryFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_4_RIGHT));
            angryFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_5_RIGHT));
            angryFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_1_RIGHT));
            angryFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_2_RIGHT));
            angryFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_ANGRY_3_RIGHT));
            angryFloatRight = new Animation<TextureRegion>(
                    Constants.ROBO_BOUNCE_DURATION, angryFloatRightFrames, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> calmFloatLeftFrames = new Array<AtlasRegion>();
            calmFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_CALM_4_LEFT));
            calmFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_CALM_5_LEFT));
            calmFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_CALM_1_LEFT));
            calmFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_CALM_2_LEFT));
            calmFloatLeftFrames.add(atlas.findRegion(Constants.ROBOT_CALM_3_LEFT));
            calmFloatLeft = new Animation<TextureRegion>(
                    Constants.ROBO_BOUNCE_DURATION, calmFloatLeftFrames, PlayMode.LOOP_PINGPONG);

            Array<AtlasRegion> calmFloatRightFrames = new Array<AtlasRegion>();
            calmFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_CALM_4_RIGHT));
            calmFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_CALM_5_RIGHT));
            calmFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_CALM_1_RIGHT));
            calmFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_CALM_2_RIGHT));
            calmFloatRightFrames.add(atlas.findRegion(Constants.ROBOT_CALM_3_RIGHT));
            calmFloatRight = new Animation<TextureRegion>(
                    Constants.ROBO_BOUNCE_DURATION, calmFloatRightFrames, PlayMode.LOOP_PINGPONG);


        }

    }

    @Override
    public void dispose() {
        assMan.dispose();
    }

}
