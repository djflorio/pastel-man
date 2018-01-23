package com.danflorio.pastelman.utils;

import com.badlogic.gdx.math.Vector2;

/**
 * Constants used throughout the code.
 */

public class Constants {

    // GENERAL

    private static final float WORLD_SIZE = 50;
    public static final float WORLD_SIZE_X = 16 * WORLD_SIZE;
    public static final float WORLD_SIZE_Y = 9 * WORLD_SIZE;
    static final float MAP_TILE_SIZE = 16;
    static final String TEXTURE_ATLAS = "images/pastelman.atlas";
    public static final String[] LEVELS = {"maps/world1.tmx"};

    // PLAYER

    static final String STANDING_RIGHT = "standing-right";
    static final String STANDING_LEFT = "standing-left";
    static final String ARM_RIGHT = "arm-right";
    static final String ARM_LEFT = "arm-left";
    static final String RUNNING_RIGHT_1 = "running-1-right";
    static final String RUNNING_RIGHT_2 = "running-2-right";
    static final String RUNNING_RIGHT_3 = "running-3-right";
    static final String RUNNING_LEFT_1 = "running-1-left";
    static final String RUNNING_LEFT_2 = "running-2-left";
    static final String RUNNING_LEFT_3 = "running-3-left";

    static final float RUN_LOOP_DURATION = 0.20f;
    public static final Vector2 PLAYER_CENTER = new Vector2(11, 25);
    public static final float PLAYER_FEET_WIDTH = 5;
    public static final Vector2 PLAYER_SHOULDER_RIGHT = new Vector2(PLAYER_CENTER.x - 2, 17.5f);
    public static final Vector2 PLAYER_SHOULDER_LEFT = new Vector2(PLAYER_CENTER.x + 2, 17.5f);
    public static final float PLAYER_ARM_LENGTH = 13;
    public static final float PLAYER_HEAD_HEIGHT = 30;
    public static final float PLAYER_GRAVITY = 30;
    public static final float MAX_JUMP_DURATION = 0.05f;
    public static final float JUMP_SPEED = 400;
    public static final float PLAYER_SPEED = 120;

    // BULLETS

    static final String BULLET = "bullet";

    public static final float BULLET_SPEED = 700;

    // ROBOTS

    static final String ROBOT_ANGRY_1_LEFT = "robot-angry-1-left";
    static final String ROBOT_ANGRY_2_LEFT = "robot-angry-2-left";
    static final String ROBOT_ANGRY_3_LEFT = "robot-angry-3-left";
    static final String ROBOT_ANGRY_4_LEFT = "robot-angry-4-left";
    static final String ROBOT_ANGRY_5_LEFT = "robot-angry-5-left";
    static final String ROBOT_ANGRY_1_RIGHT = "robot-angry-1-right";
    static final String ROBOT_ANGRY_2_RIGHT = "robot-angry-2-right";
    static final String ROBOT_ANGRY_3_RIGHT = "robot-angry-3-right";
    static final String ROBOT_ANGRY_4_RIGHT = "robot-angry-4-right";
    static final String ROBOT_ANGRY_5_RIGHT = "robot-angry-5-right";
    static final String ROBOT_CALM_1_LEFT = "robot-calm-1-left";
    static final String ROBOT_CALM_2_LEFT = "robot-calm-2-left";
    static final String ROBOT_CALM_3_LEFT = "robot-calm-3-left";
    static final String ROBOT_CALM_4_LEFT = "robot-calm-4-left";
    static final String ROBOT_CALM_5_LEFT = "robot-calm-5-left";
    static final String ROBOT_CALM_1_RIGHT = "robot-calm-1-right";
    static final String ROBOT_CALM_2_RIGHT = "robot-calm-2-right";
    static final String ROBOT_CALM_3_RIGHT = "robot-calm-3-right";
    static final String ROBOT_CALM_4_RIGHT = "robot-calm-4-right";
    static final String ROBOT_CALM_5_RIGHT = "robot-calm-5-right";

    static final float ROBO_BOUNCE_DURATION = 0.2f;
    public static final float ROBO_SIGHT_RADIUS = 100;

}
