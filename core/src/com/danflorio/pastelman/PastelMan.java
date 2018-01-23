package com.danflorio.pastelman;

import com.badlogic.gdx.Game;
import com.danflorio.pastelman.screens.PlayScreen;

public class PastelMan extends Game {

	@Override
	public void create() {
		setScreen(new PlayScreen());
	}

}
