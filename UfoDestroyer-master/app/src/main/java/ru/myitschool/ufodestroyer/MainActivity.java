package ru.myitschool.ufodestroyer;

import android.app.Activity;
import android.os.Bundle;

import ru.myitschool.ufodestroyer.model.Game;
import ru.myitschool.ufodestroyer.model.GameImpl;

public class MainActivity extends Activity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new GameImpl();
        setContentView(new GameView(game, this));
    }
}
